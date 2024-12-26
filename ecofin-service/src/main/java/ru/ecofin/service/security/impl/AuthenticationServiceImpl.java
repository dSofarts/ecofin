package ru.ecofin.service.security.impl;

import java.util.HashMap;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.ecofin.service.dto.request.RefreshTokenRequest;
import ru.ecofin.service.dto.request.SigninRequestDto;
import ru.ecofin.service.dto.request.SignupRequestDto;
import ru.ecofin.service.dto.response.JwtResponseDto;
import ru.ecofin.service.entity.User;
import ru.ecofin.service.repository.UserRepository;
import ru.ecofin.service.security.AuthenticationService;
import ru.ecofin.service.security.JWTService;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JWTService jwtService;

  @Override
  public User signup(SignupRequestDto signupRequestDto) {
    User user = User.builder()
        .username(signupRequestDto.getUsername())
        .password(passwordEncoder.encode(signupRequestDto.getPassword()))
        .roles(Set.of("USER"))
        .build();
    return userRepository.save(user);
  }

  @Override
  public JwtResponseDto signin(SigninRequestDto signinRequestDto) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
        signinRequestDto.getUsername(), signinRequestDto.getPassword()));
    User user = userRepository.findByUsername(signinRequestDto.getUsername()).orElseThrow(
        () -> new IllegalArgumentException("Invalid username or password"));
    String token = jwtService.generateToken(user);
    String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
    return JwtResponseDto.builder()
        .token(token).refreshToken(refreshToken).build();
  }

  @Override
  public JwtResponseDto refreshToken(RefreshTokenRequest refreshTokenRequest) {
    String username = jwtService.extractUsername(refreshTokenRequest.getRefreshToken());
    User user = userRepository.findByUsername(username).orElseThrow(
        () -> new IllegalArgumentException("Invalid token"));
    if (jwtService.isTokenValid(refreshTokenRequest.getRefreshToken(), user)) {
      String token = jwtService.generateToken(user);
      return JwtResponseDto.builder()
          .token(token).refreshToken(refreshTokenRequest.getRefreshToken()).build();
    }
    return null;
  }
}
