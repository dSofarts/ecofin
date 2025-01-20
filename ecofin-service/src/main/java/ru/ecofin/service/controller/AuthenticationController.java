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
import ru.ecofin.service.controller.api.AuthenticationControllerApi;
import ru.ecofin.service.controller.validator.LoginRequestValidator;
import ru.ecofin.service.controller.validator.MainValidator;
import ru.ecofin.service.controller.validator.OtpRequestValidator;
import ru.ecofin.service.controller.validator.RefreshTokenRequestValidator;
import ru.ecofin.service.controller.validator.RegistrationRequestValidator;
import ru.ecofin.service.dto.request.LoginRequestDto;
import ru.ecofin.service.dto.request.OtpRequestDto;
import ru.ecofin.service.dto.request.RefreshTokenRequest;
import ru.ecofin.service.dto.request.RegistrationRequestDto;
import ru.ecofin.service.dto.response.JwtResponseDto;
import ru.ecofin.service.dto.response.LoginResponseDto;
import ru.ecofin.service.dto.response.UserResponseDto;
import ru.ecofin.service.security.AuthenticationService;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class AuthenticationController implements AuthenticationControllerApi {

  private final AuthenticationService authenticationService;
  private final MainValidator mainValidator;

  @Override
  @SneakyThrows
  @LoggingUsed(endpoint = "POST /auth/registration")
  public ResponseEntity<UserResponseDto> registration(
      @RequestHeader Map<String, String> requestHeader,
      @RequestBody RegistrationRequestDto requestBody) {
    mainValidator.validateRequest(requestBody, RegistrationRequestValidator.class, requestHeader);
    return ResponseEntity.ok(authenticationService.registration(requestBody));
  }

  @Override
  @SneakyThrows
  @LoggingUsed(endpoint = "POST /auth/login")
  public ResponseEntity<LoginResponseDto> login(
      @RequestHeader Map<String, String> requestHeader,
      @RequestBody LoginRequestDto requestBody) {
    mainValidator.validateRequest(requestBody, LoginRequestValidator.class, requestHeader);
    return ResponseEntity.ok(authenticationService.login(requestBody));
  }

  @Override
  @SneakyThrows
  @LoggingUsed(endpoint = "POST /auth/authenticate")
  public ResponseEntity<JwtResponseDto> authenticate(
      @RequestHeader Map<String, String> requestHeader,
      @RequestBody OtpRequestDto requestBody) {
    mainValidator.validateRequest(requestBody, OtpRequestValidator.class, requestHeader);
    return ResponseEntity.ok(authenticationService.authenticate(requestBody));
  }

  @Override
  @SneakyThrows
  @LoggingUsed(endpoint = "POST /auth/refresh-token")
  public ResponseEntity<JwtResponseDto> refreshToken(
      @RequestHeader Map<String, String> requestHeader,
      @RequestBody RefreshTokenRequest requestBody) {
    mainValidator.validateRequest(requestBody, RefreshTokenRequestValidator.class, requestHeader);
    return ResponseEntity.ok(authenticationService.refreshToken(requestBody));
  }
}
