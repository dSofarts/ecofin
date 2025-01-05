package ru.ecofin.service.controller;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.ecofin.service.annotation.LoggingUsed;
import ru.ecofin.service.dto.kafka.CodeDto;
import ru.ecofin.service.dto.request.LoginRequestDto;
import ru.ecofin.service.dto.request.RefreshTokenRequest;
import ru.ecofin.service.dto.request.RegistrationRequestDto;
import ru.ecofin.service.dto.response.JwtResponseDto;
import ru.ecofin.service.dto.response.UserResponseDto;
import ru.ecofin.service.kafka.KafkaProducer;
import ru.ecofin.service.security.AuthenticationService;
import ru.ecofin.service.utils.event.ServiceEventType;

@RestController
@RequiredArgsConstructor
public class AuthenticationController implements AuthenticationControllerApi {

  private final AuthenticationService authenticationService;
  private final KafkaProducer kafkaProducer;

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
  public ResponseEntity<JwtResponseDto> login(
      @RequestHeader Map<String, String> requestHeader,
      @RequestBody LoginRequestDto requestBody) {
    return ResponseEntity.ok(authenticationService.login(requestBody));
  }

  @Override
  public ResponseEntity<JwtResponseDto> refreshToken(Map<String, String> requestHeader,
      RefreshTokenRequest requestBody) {
    return ResponseEntity.ok(authenticationService.refreshToken(requestBody));
  }

  @GetMapping("/send")
  public ResponseEntity<?> sendToKafka() {
    CodeDto cd = CodeDto.builder()
        .code("123456")
        .chatId("977397441")
        .expirationDate(LocalDateTime.now())
        .operationId(UUID.randomUUID().toString()).build();
    kafkaProducer.send(cd);
    return ResponseEntity.ok(cd);
  }
}
