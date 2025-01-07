package ru.ecofin.service.controller;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.ecofin.service.annotation.LoggingUsed;
import ru.ecofin.service.dto.request.MainRequestDto;
import ru.ecofin.service.dto.response.FrontResponseDto;
import ru.ecofin.service.dto.response.WalletsResponseDto;
import ru.ecofin.service.service.WalletService;
import ru.ecofin.service.utils.event.ServiceEventType;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WalletController implements WalletControllerApi {

  private final WalletService walletService;

  @Override
  @LoggingUsed(eventType = ServiceEventType.REGISTRATION_REQUEST,
      endpoint = "/create-wallet")
  public ResponseEntity<FrontResponseDto> createWallet(
      @RequestHeader Map<String, String> requestHeader,
      @RequestBody MainRequestDto requestBody) {
    return ResponseEntity.ok(FrontResponseDto.builder()
        .objectId(walletService.createWallet(requestBody.getUserId()))
        .status("Wallet successfully created")
        .statusCode("200").build());
  }

  @Override
  @LoggingUsed(eventType = ServiceEventType.REGISTRATION_REQUEST,
      endpoint = "/wallets")
  public ResponseEntity<WalletsResponseDto> getWallets(
      @RequestHeader Map<String, String> requestHeader,
      @RequestBody MainRequestDto requestBody) {
    return ResponseEntity.ok(walletService.findWallet(requestBody.getUserId()));
  }
}
