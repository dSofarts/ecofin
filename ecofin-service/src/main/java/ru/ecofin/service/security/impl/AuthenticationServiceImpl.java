package ru.ecofin.service.security.impl;

import java.util.HashMap;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.ecofin.service.dto.request.LoginRequestDto;
import ru.ecofin.service.dto.request.RefreshTokenRequest;
import ru.ecofin.service.dto.request.RegistrationRequestDto;
import ru.ecofin.service.dto.response.JwtResponseDto;
import ru.ecofin.service.dto.response.UserResponseDto;
import ru.ecofin.service.entity.User;
import ru.ecofin.service.exception.NotFoundException;
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
  public UserResponseDto registration(RegistrationRequestDto requestDto) {
    User user = User.builder()
        .email(requestDto.getEmail())
        .password(passwordEncoder.encode(requestDto.getPassword()))
        .firstName(requestDto.getFirstName())
        .roles(Set.of("USER"))
        .build();
    userRepository.save(user);
    return UserResponseDto.builder()
        .email(user.getUsername())
        .id(user.getId().toString())
        .build();
  }

  @Override
  public JwtResponseDto login(LoginRequestDto requestDto) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
        requestDto.getEmail(), requestDto.getPassword()));
    User user = userRepository.findUserByEmail(requestDto.getEmail()).orElseThrow(
        () -> new NotFoundException(requestDto.getEmail(), "Invalid username or password"));
    String token = jwtService.generateToken(user);
    String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
    return JwtResponseDto.builder()
        .token(token).refreshToken(refreshToken).build();
  }

  @Override
  public JwtResponseDto refreshToken(RefreshTokenRequest refreshTokenRequest) {
    String email = jwtService.extractEmail(refreshTokenRequest.getRefreshToken());
    User user = userRepository.findUserByEmail(email).orElseThrow(
        () -> new IllegalArgumentException("Invalid token"));
    if (jwtService.isTokenValid(refreshTokenRequest.getRefreshToken(), user)) {
      String token = jwtService.generateToken(user);
      return JwtResponseDto.builder()
          .token(token).refreshToken(refreshTokenRequest.getRefreshToken()).build();
    }
    throw new NotFoundException(null, "Invalid refresh token");
  }
}
