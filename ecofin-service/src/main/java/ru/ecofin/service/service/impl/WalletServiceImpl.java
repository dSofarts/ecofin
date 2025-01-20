package ru.ecofin.service.service.impl;

import jakarta.transaction.Transactional;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.ecofin.service.constant.Constants;
import ru.ecofin.service.dto.kafka.TransferMessageDto;
import ru.ecofin.service.dto.request.TransferRequest;
import ru.ecofin.service.dto.request.UserWalletRequestDto;
import ru.ecofin.service.dto.response.FrontResponseDto;
import ru.ecofin.service.dto.response.UserWalletResponseDto;
import ru.ecofin.service.dto.response.WalletsResponseDto;
import ru.ecofin.service.entity.Transfer;
import ru.ecofin.service.entity.User;
import ru.ecofin.service.entity.Wallet;
import ru.ecofin.service.exception.NotFoundException;
import ru.ecofin.service.exception.ValidationException;
import ru.ecofin.service.kafka.KafkaProducer;
import ru.ecofin.service.repository.TransferRepository;
import ru.ecofin.service.repository.UserRepository;
import ru.ecofin.service.repository.WalletRepository;
import ru.ecofin.service.service.WalletService;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

  private final WalletRepository walletRepository;
  private final UserRepository userRepository;
  private final TransferRepository transferRepository;
  private final KafkaProducer kafkaProducer;

  @Override
  @Transactional
  public String createWallet(String userId) {
    User user = userRepository.findById(UUID.fromString(userId)).orElseThrow(
        () -> new NotFoundException(userId, "User not found"));
    boolean isPrimary = user.getWallets().stream().noneMatch(Wallet::isPrimary);
    Wallet wallet = Wallet.builder()
        .isPrimary(isPrimary)
        .user(user)
        .build();
    walletRepository.save(wallet);
    return wallet.getId().toString();
  }

  @Override
  public WalletsResponseDto findWallet(String userId) {
    User user = userRepository.findById(UUID.fromString(userId)).orElseThrow(
        () -> new NotFoundException(userId, "User not found"));
    List<WalletsResponseDto.Wallet> wallets = new ArrayList<>();
    user.getWallets().stream().filter(Wallet::isOpen).forEach(
        wallet -> wallets.add(WalletsResponseDto.Wallet.builder()
            .id(wallet.getId().toString())
            .balance(wallet.getBalance())
            .isPrimary(wallet.isPrimary())
            .walletName(wallet.getWalletName())
            .build())
    );
    return WalletsResponseDto.builder()
        .userId(String.valueOf(user.getId()))
        .userPhone(user.getPhone())
        .wallets(wallets).build();
  }

  @Override
  public UserWalletResponseDto getUserWallet(UserWalletRequestDto requestDto) {
    User user;
    Wallet wallet;
    if (requestDto.getPhone() != null) {
      user = userRepository.findUserByPhone(requestDto.getPhone()).orElseThrow(
          () -> new NotFoundException(requestDto.getPhone(), "User not found"));
      wallet = user.getWallets().stream().filter(Wallet::isOpen).filter(Wallet::isPrimary)
          .findFirst()
          .orElseThrow(() -> new NotFoundException(requestDto.getPhone(), "Wallets not found"));
    } else {
      wallet = walletRepository.findById(UUID.fromString(requestDto.getWalletId())).orElseThrow(
          () -> new NotFoundException(requestDto.getWalletId(), "Wallet not found"));
      user = wallet.getUser();
    }

    return UserWalletResponseDto.builder()
        .walletId(wallet.getId().toString())
        .fullName(getFullName(user)).build();
  }

  @Override
  @Transactional
  public FrontResponseDto transfer(TransferRequest transferRequest) {
    Wallet senderWallet = walletRepository.findById(
        UUID.fromString(transferRequest.getWalletFromId())).orElseThrow(
        () -> new NotFoundException(transferRequest.getWalletFromId(), "Wallet not found"));
    Wallet receiverWallet = walletRepository.findById(
        UUID.fromString(transferRequest.getWalletToId())).orElseThrow(
        () -> new NotFoundException(transferRequest.getWalletFromId(), "Wallet not found"));
    if (senderWallet.getBalance().compareTo(transferRequest.getAmount()) < 0) {
      throw new ValidationException("There are insufficient funds in the account",
          HttpStatus.BAD_REQUEST);
    }

    senderWallet.setBalance(senderWallet.getBalance().subtract(transferRequest.getAmount()));
    receiverWallet.setBalance(receiverWallet.getBalance().add(transferRequest.getAmount()));

    Transfer transfer = Transfer.builder()
        .senderWallet(senderWallet)
        .receiverWallet(receiverWallet)
        .amount(transferRequest.getAmount())
        .description(transferRequest.getDescription())
        .build();
    transferRepository.save(transfer);

    if (!senderWallet.getUser().getId().equals(receiverWallet.getUser().getId())) {
      DecimalFormatSymbols symbols = new DecimalFormatSymbols();
      symbols.setDecimalSeparator(',');
      symbols.setGroupingSeparator(' ');
      DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", symbols);

      kafkaProducer.sendTransferMessage(TransferMessageDto.builder()
          .operationId(UUID.randomUUID().toString())
          .senderFullName(getFullName(senderWallet.getUser()))
          .receiverChatId(receiverWallet.getUser().getChatId())
          .amount(decimalFormat.format(transferRequest.getAmount()))
          .build());
    }

    return FrontResponseDto.builder()
        .objectId(transferRequest.getWalletFromId())
        .statusCode("200")
        .status("Successful translation to wallet: %s".formatted(transferRequest.getWalletToId()))
        .build();
  }

  private String getFullName(User user) {
    return Constants.FULL_NAME_TEMPLATE.formatted(
        user.getFirstName(),
        user.getMiddleName() == null ? "" : " " + user.getMiddleName(),
        user.getLastName().charAt(0)
    );
  }
}
