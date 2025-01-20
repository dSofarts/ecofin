package ru.ecofin.service.controller;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.ecofin.service.annotation.LoggingUsed;
import ru.ecofin.service.constant.Constants;
import ru.ecofin.service.controller.api.UserControllerApi;
import ru.ecofin.service.controller.validator.MainValidator;
import ru.ecofin.service.dto.response.UserResponseDto;
import ru.ecofin.service.service.UserService;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class UserController implements UserControllerApi {

  private final MainValidator mainValidator;
  private final UserService userService;

  @Override
  @SneakyThrows
  @LoggingUsed(endpoint = "GET /users/user")
  public ResponseEntity<UserResponseDto> getUser(
      @RequestHeader Map<String, String> requestHeader) {
    log.warn(requestHeader.toString());
    String token = requestHeader.get(Constants.AUTHORIZATION_HEADER).substring(7);
    mainValidator.validateRequest(requestHeader);
    return ResponseEntity.ok(userService.getUserByToken(token));
  }
}
