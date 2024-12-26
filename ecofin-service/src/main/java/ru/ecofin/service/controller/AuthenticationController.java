package ru.ecofin.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ecofin.service.dto.request.RefreshTokenRequest;
import ru.ecofin.service.dto.request.SigninRequestDto;
import ru.ecofin.service.dto.request.SignupRequestDto;
import ru.ecofin.service.dto.response.JwtResponseDto;
import ru.ecofin.service.entity.User;
import ru.ecofin.service.security.AuthenticationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/")
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  @PostMapping("/signup")
  public ResponseEntity<User> signup(@RequestBody SignupRequestDto requestDto) {
    return ResponseEntity.ok(authenticationService.signup(requestDto));
  }

  @PostMapping("/signin")
  public ResponseEntity<JwtResponseDto> signin(@RequestBody SigninRequestDto requestDto) {
    return ResponseEntity.ok(authenticationService.signin(requestDto));
  }

  @PostMapping("/refresh")
  public ResponseEntity<JwtResponseDto> refresh(@RequestBody RefreshTokenRequest requestDto) {
    return ResponseEntity.ok(authenticationService.refreshToken(requestDto));
  }
}
