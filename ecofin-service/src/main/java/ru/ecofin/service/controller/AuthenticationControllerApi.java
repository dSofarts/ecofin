package ru.ecofin.service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.ecofin.service.dto.request.LoginRequestDto;
import ru.ecofin.service.dto.request.OtpRequestDto;
import ru.ecofin.service.dto.request.RefreshTokenRequest;
import ru.ecofin.service.dto.request.RegistrationRequestDto;
import ru.ecofin.service.dto.response.FrontResponseDto;
import ru.ecofin.service.dto.response.JwtResponseDto;
import ru.ecofin.service.dto.response.LoginResponseDto;
import ru.ecofin.service.dto.response.UserResponseDto;

@Tag(name = "API для аутентификации пользователя")
@RequestMapping("/api/v1/auth")
public interface AuthenticationControllerApi {

  @PostMapping("/registration")
  @Operation(summary = "Запрос на регистрацию пользователя",
      description = "Запрос на регистрацию пользователя")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          content = @Content(schema = @Schema(implementation = UserResponseDto.class)),
          description = "Пользователь успешно зарегистрирован"),
      @ApiResponse(
          responseCode = "400",
          content = @Content(schema = @Schema(implementation = FrontResponseDto.class)),
          description = "Ошибка во входящих данных"),
      @ApiResponse(
          responseCode = "401",
          content = @Content(schema = @Schema(implementation = FrontResponseDto.class)),
          description = "Доступ к ресурсу запрещен без авторизации"),
      @ApiResponse(
          responseCode = "403",
          content = @Content(schema = @Schema(implementation = FrontResponseDto.class)),
          description = "Доступ к ресурсу запрещен"),
      @ApiResponse(
          responseCode = "500",
          content = @Content(schema = @Schema(implementation = FrontResponseDto.class)),
          description = "Ошибка сервера"),
  })
  @Parameters({
      @Parameter(
          name = "service-name",
          description = "Название потребителя",
          example = "service",
          required = true,
          in = ParameterIn.HEADER,
          schema = @Schema(format = "String"))
  })
  ResponseEntity<UserResponseDto> registration(
      @RequestHeader Map<String, String> requestHeader,
      @RequestBody RegistrationRequestDto requestBody);

  @PostMapping("/login")
  @Operation(summary = "Запрос на авторизацию пользователя",
      description = "Запрос на авторизацию пользователя")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          content = @Content(schema = @Schema(implementation = LoginResponseDto.class)),
          description = "Пользователь успешно авторизирован"),
      @ApiResponse(
          responseCode = "400",
          content = @Content(schema = @Schema(implementation = FrontResponseDto.class)),
          description = "Ошибка во входящих данных, либо пользователь не найден"),
      @ApiResponse(
          responseCode = "401",
          content = @Content(schema = @Schema(implementation = FrontResponseDto.class)),
          description = "Доступ к ресурсу запрещен без авторизации"),
      @ApiResponse(
          responseCode = "403",
          content = @Content(schema = @Schema(implementation = FrontResponseDto.class)),
          description = "Доступ к ресурсу запрещен"),
      @ApiResponse(
          responseCode = "500",
          content = @Content(schema = @Schema(implementation = FrontResponseDto.class)),
          description = "Ошибка сервера"),
  })
  @Parameters({
      @Parameter(
          name = "service-name",
          description = "Название потребителя",
          example = "service",
          required = true,
          in = ParameterIn.HEADER,
          schema = @Schema(format = "String"))
  })
  ResponseEntity<LoginResponseDto> login(
      @RequestHeader Map<String, String> requestHeader,
      @RequestBody LoginRequestDto requestBody);

  @PostMapping("/authenticate")
  @Operation(summary = "Запрос на авторизацию пользователя",
      description = "Запрос на авторизацию пользователя")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          content = @Content(schema = @Schema(implementation = JwtResponseDto.class)),
          description = "Пользователь успешно аутентифицирован"),
      @ApiResponse(
          responseCode = "400",
          content = @Content(schema = @Schema(implementation = FrontResponseDto.class)),
          description = "Ошибка во входящих данных, либо пользователь не найден"),
      @ApiResponse(
          responseCode = "401",
          content = @Content(schema = @Schema(implementation = FrontResponseDto.class)),
          description = "Доступ к ресурсу запрещен без авторизации"),
      @ApiResponse(
          responseCode = "403",
          content = @Content(schema = @Schema(implementation = FrontResponseDto.class)),
          description = "Доступ к ресурсу запрещен"),
      @ApiResponse(
          responseCode = "500",
          content = @Content(schema = @Schema(implementation = FrontResponseDto.class)),
          description = "Ошибка сервера"),
  })
  @Parameters({
      @Parameter(
          name = "service-name",
          description = "Название потребителя",
          example = "service",
          required = true,
          in = ParameterIn.HEADER,
          schema = @Schema(format = "String"))
  })
  ResponseEntity<JwtResponseDto> authenticate(
      @RequestHeader Map<String, String> requestHeader,
      @RequestBody OtpRequestDto requestBody);

  @PostMapping("/refresh-token")
  @Operation(summary = "Запрос на обновление токена",
      description = "Запрос на обновление токена")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          content = @Content(schema = @Schema(implementation = JwtResponseDto.class)),
          description = "Токен успешно обновлен"),
      @ApiResponse(
          responseCode = "400",
          content = @Content(schema = @Schema(implementation = FrontResponseDto.class)),
          description = "Ошибка во входящих данных, либо пользователь не найден"),
      @ApiResponse(
          responseCode = "401",
          content = @Content(schema = @Schema(implementation = FrontResponseDto.class)),
          description = "Доступ к ресурсу запрещен без авторизации"),
      @ApiResponse(
          responseCode = "403",
          content = @Content(schema = @Schema(implementation = FrontResponseDto.class)),
          description = "Доступ к ресурсу запрещен"),
      @ApiResponse(
          responseCode = "500",
          content = @Content(schema = @Schema(implementation = FrontResponseDto.class)),
          description = "Ошибка сервера"),
  })
  @Parameters({
      @Parameter(
          name = "service-name",
          description = "Название потребителя",
          example = "service",
          required = true,
          in = ParameterIn.HEADER,
          schema = @Schema(format = "String"))
  })
  ResponseEntity<JwtResponseDto> refreshToken(
      @RequestHeader Map<String, String> requestHeader,
      @RequestBody RefreshTokenRequest requestBody);
}
