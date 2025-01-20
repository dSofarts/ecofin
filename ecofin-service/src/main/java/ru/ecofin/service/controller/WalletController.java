package ru.ecofin.service.controller;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.ecofin.service.annotation.LoggingUsed;
import ru.ecofin.service.controller.api.WalletControllerApi;
import ru.ecofin.service.controller.validator.MainRequestValidator;
import ru.ecofin.service.controller.validator.MainValidator;
import ru.ecofin.service.controller.validator.TransferRequestValidator;
import ru.ecofin.service.controller.validator.UserWalletRequestValidator;
import ru.ecofin.service.dto.request.MainRequestDto;
import ru.ecofin.service.dto.request.TransferRequest;
import ru.ecofin.service.dto.request.UserWalletRequestDto;
import ru.ecofin.service.dto.response.FrontResponseDto;
import ru.ecofin.service.dto.response.UserWalletResponseDto;
import ru.ecofin.service.dto.response.WalletsResponseDto;
import ru.ecofin.service.service.WalletService;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class WalletController implements WalletControllerApi {

  private final WalletService walletService;
  private final MainValidator mainValidator;

  @Override
  @SneakyThrows
  @LoggingUsed(endpoint = "POST /wallet/create-wallet")
  public ResponseEntity<FrontResponseDto> createWallet(
      @RequestHeader Map<String, String> requestHeader,
      @RequestBody MainRequestDto requestBody) {
    mainValidator.validateRequest(requestBody, MainRequestValidator.class, requestHeader);
    return ResponseEntity.ok(FrontResponseDto.builder()
        .objectId(walletService.createWallet(requestBody.getUserId()))
        .status("Wallet successfully created")
        .statusCode("200").build());
  }

  @Override
  @SneakyThrows
  @LoggingUsed(endpoint = "GET /wallet/wallets")
  public ResponseEntity<WalletsResponseDto> getWallets(
      @RequestHeader Map<String, String> requestHeader,
      @RequestBody MainRequestDto requestBody) {
    mainValidator.validateRequest(requestBody, MainRequestValidator.class, requestHeader);
    return ResponseEntity.ok(walletService.findWallet(requestBody.getUserId()));
  }

  @Override
  @SneakyThrows
  @LoggingUsed(endpoint = "GET /wallet/user-wallet")
  public ResponseEntity<UserWalletResponseDto> getUserWallet(
      @RequestHeader Map<String, String> requestHeader,
      @RequestBody UserWalletRequestDto requestBody) {
    mainValidator.validateRequest(requestBody, UserWalletRequestValidator.class, requestHeader);
    return ResponseEntity.ok(walletService.getUserWallet(requestBody));
  }

  @Override
  @SneakyThrows
  @LoggingUsed(endpoint = "POST /wallet/transfer")
  public ResponseEntity<FrontResponseDto> transfer(
      @RequestHeader Map<String, String> requestHeader,
      @RequestBody TransferRequest requestBody) {
    mainValidator.validateRequest(requestBody, TransferRequestValidator.class, requestHeader);
    return ResponseEntity.ok(walletService.transfer(requestBody));
  }
}
