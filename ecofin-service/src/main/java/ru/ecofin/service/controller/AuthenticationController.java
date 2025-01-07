package ru.ecofin.service.controller;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.ecofin.service.annotation.LoggingUsed;
import ru.ecofin.service.dto.request.LoginRequestDto;
import ru.ecofin.service.dto.request.OtpRequestDto;
import ru.ecofin.service.dto.request.RefreshTokenRequest;
import ru.ecofin.service.dto.request.RegistrationRequestDto;
import ru.ecofin.service.dto.response.JwtResponseDto;
import ru.ecofin.service.dto.response.LoginResponseDto;
import ru.ecofin.service.dto.response.UserResponseDto;
import ru.ecofin.service.security.AuthenticationService;
import ru.ecofin.service.utils.event.ServiceEventType;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthenticationController implements AuthenticationControllerApi {

  private final AuthenticationService authenticationService;

  @Override
  @LoggingUsed(eventType = ServiceEventType.REGISTRATION_REQUEST,
      endpoint = "/registration")
  public ResponseEntity<UserResponseDto> registration(
      @RequestHeader Map<String, String> requestHeader,
      @RequestBody RegistrationRequestDto requestBody) {
    return ResponseEntity.ok(authenticationService.registration(requestBody));
  }

  @Override
  @LoggingUsed(eventType = ServiceEventType.AUTHENTICATION_REQUEST,
      endpoint = "/login")
  public ResponseEntity<LoginResponseDto> login(
      @RequestHeader Map<String, String> requestHeader,
      @RequestBody LoginRequestDto requestBody) {
    return ResponseEntity.ok(authenticationService.login(requestBody));
  }

  @Override
  @LoggingUsed(eventType = ServiceEventType.AUTHENTICATION_REQUEST,
      endpoint = "/authenticate")
  public ResponseEntity<JwtResponseDto> authenticate(
      @RequestHeader Map<String, String> requestHeader,
      @RequestBody OtpRequestDto requestBody) {
    return ResponseEntity.ok(authenticationService.authenticate(requestBody));
  }

  @Override
  @LoggingUsed(eventType = ServiceEventType.AUTHENTICATION_REQUEST,
      endpoint = "/refresh-token")
  public ResponseEntity<JwtResponseDto> refreshToken(
      @RequestHeader Map<String, String> requestHeader,
      @RequestBody RefreshTokenRequest requestBody) {
    return ResponseEntity.ok(authenticationService.refreshToken(requestBody));
  }
}
