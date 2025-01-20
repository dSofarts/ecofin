package ru.ecofin.service.controller.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.ecofin.service.dto.response.UserResponseDto;

@Tag(name = "API для управления пользователем")
@RequestMapping(value = "users", produces = MediaType.APPLICATION_JSON_VALUE)
public interface UserControllerApi {

  @GetMapping("/user")
  ResponseEntity<UserResponseDto> getUser(
      @RequestHeader Map<String, String> requestHeader);
}
