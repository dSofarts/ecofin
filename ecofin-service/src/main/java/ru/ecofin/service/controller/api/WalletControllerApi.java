package ru.ecofin.service.controller.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.ecofin.service.dto.request.MainRequestDto;
import ru.ecofin.service.dto.request.TransferRequest;
import ru.ecofin.service.dto.request.UserWalletRequestDto;
import ru.ecofin.service.dto.response.FrontResponseDto;
import ru.ecofin.service.dto.response.UserWalletResponseDto;
import ru.ecofin.service.dto.response.WalletsResponseDto;

@Tag(name = "API для управления кошельком")
@RequestMapping(value = "wallet", produces = MediaType.APPLICATION_JSON_VALUE)
public interface WalletControllerApi {

  @PostMapping("/create-wallet")
  ResponseEntity<FrontResponseDto> createWallet(
      @RequestHeader Map<String, String> requestHeader,
      @RequestBody MainRequestDto requestBody);

  @GetMapping("/wallets")
  ResponseEntity<WalletsResponseDto> getWallets(
      @RequestHeader Map<String, String> requestHeader,
      @RequestBody MainRequestDto requestBody);

  @GetMapping("/user-wallet")
  ResponseEntity<UserWalletResponseDto> getUserWallet(
      @RequestHeader Map<String, String> requestHeader,
      @RequestBody UserWalletRequestDto requestBody);

  @PostMapping("/transfer")
  ResponseEntity<FrontResponseDto> transfer(
      @RequestHeader Map<String, String> requestHeader,
      @RequestBody TransferRequest requestBody);
}
