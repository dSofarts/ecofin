package ru.ecofin.service.service.impl;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.ecofin.service.dto.response.WalletsResponseDto;
import ru.ecofin.service.entity.User;
import ru.ecofin.service.entity.Wallet;
import ru.ecofin.service.exception.NotFoundException;
import ru.ecofin.service.repository.UserRepository;
import ru.ecofin.service.repository.WalletRepository;
import ru.ecofin.service.service.WalletService;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

  private final WalletRepository walletRepository;
  private final UserRepository userRepository;

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
}
