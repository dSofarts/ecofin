package ru.ecofin.service.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.ecofin.service.dto.request.MainRequestDto;
import ru.ecofin.service.dto.response.FrontResponseDto;
import ru.ecofin.service.dto.response.WalletsResponseDto;

@Tag(name = "API для управления кошельком")
@RequestMapping("/api/v1/wallet")
public interface WalletControllerApi {

  @PostMapping("/create-wallet")
  ResponseEntity<FrontResponseDto> createWallet(
      @RequestHeader Map<String, String> requestHeader,
      @RequestBody MainRequestDto requestBody);

  @GetMapping("/wallets")
  ResponseEntity<WalletsResponseDto> getWallets(
      @RequestHeader Map<String, String> requestHeader,
      @RequestBody MainRequestDto requestBody);
}
