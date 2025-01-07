package ru.ecofin.service.security.impl;

import java.util.ArrayList;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.ecofin.service.dto.request.LoginRequestDto;
import ru.ecofin.service.dto.request.OtpRequestDto;
import ru.ecofin.service.dto.request.RefreshTokenRequest;
import ru.ecofin.service.dto.request.RegistrationRequestDto;
import ru.ecofin.service.dto.response.JwtResponseDto;
import ru.ecofin.service.dto.response.LoginResponseDto;
import ru.ecofin.service.dto.response.UserResponseDto;
import ru.ecofin.service.entity.Otp;
import ru.ecofin.service.entity.User;
import ru.ecofin.service.exception.NotFoundException;
import ru.ecofin.service.exception.ValidationException;
import ru.ecofin.service.repository.UserRepository;
import ru.ecofin.service.security.AuthenticationService;
import ru.ecofin.service.security.JWTService;
import ru.ecofin.service.service.OtpService;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JWTService jwtService;
  private final OtpService otpService;

  @Override
  public UserResponseDto registration(RegistrationRequestDto requestDto) {
    User user = User.builder()
        .phone(requestDto.getPhone())
        .password(passwordEncoder.encode(requestDto.getPassword()))
        .firstName(requestDto.getFirstName())
        .lastName(requestDto.getLastName())
        .middleName(requestDto.getMiddleName())
        .roles(Set.of("USER"))
        .build();
    userRepository.save(user);
    return UserResponseDto.builder()
        .phone(user.getPhone())
        .id(user.getId().toString())
        .build();
  }

  @Override
  public LoginResponseDto login(LoginRequestDto requestDto) {
    User user = userRepository.findUserByPhone(requestDto.getPhone()).orElseThrow(
        () -> new NotFoundException(requestDto.getPhone(), "Invalid username or password"));
    if (!validateCredentials(user, requestDto.getPassword())) {
      throw new ValidationException("Invalid username or password", HttpStatus.UNAUTHORIZED);
    }
    if (!user.isConfirmed()) {
      throw new ValidationException("Used is not confirmed", HttpStatus.BAD_REQUEST);
    }
    String tempToken = jwtService.generateTempToken(user);
    Otp otp = otpService.generateOtp(user);
    return LoginResponseDto.builder()
        .tempToken(tempToken)
        .operationId(otp.getId().toString())
        .expired(otp.getExpiration())
        .build();
  }

  @Override
  @SneakyThrows
  public JwtResponseDto authenticate(OtpRequestDto otpRequestDto) {
    String phone = jwtService.extractPhone(otpRequestDto.getTempToken());
    User user = userRepository.findUserByPhone(phone)
        .orElseThrow(() -> new NotFoundException(phone, "Invalid username or password"));
    boolean isValidOtp = otpService.verifyOtp(otpRequestDto.getOperationId(),
        otpRequestDto.getOtp());
    if (!isValidOtp || !jwtService.tokenIsTemp(otpRequestDto.getTempToken())) {
      throw new ValidationException("Invalid otp or token", HttpStatus.UNAUTHORIZED);
    }

    Authentication authentication = new UsernamePasswordAuthenticationToken(user.getPhone(),
        null, new ArrayList<>());
    SecurityContextHolder.getContext().setAuthentication(authentication);

    String token = jwtService.generateToken(user);
    String refreshToken = jwtService.generateRefreshToken(user);
    return JwtResponseDto.builder()
        .token(token).refreshToken(refreshToken).build();
  }

  @SneakyThrows
  private boolean validateCredentials(User user, String password) {
    return passwordEncoder.matches(password, user.getPassword());
  }

  @Override
  public JwtResponseDto refreshToken(RefreshTokenRequest refreshTokenRequest) {
    String phone = jwtService.extractPhone(refreshTokenRequest.getRefreshToken());
    User user = userRepository.findUserByPhone(phone).orElseThrow(
        () -> new IllegalArgumentException("Invalid token"));
    if (jwtService.isTokenValid(refreshTokenRequest.getRefreshToken(), user)
        && jwtService.tokenIsRefresh(refreshTokenRequest.getRefreshToken())) {
      String token = jwtService.generateToken(user);
      return JwtResponseDto.builder()
          .token(token).refreshToken(refreshTokenRequest.getRefreshToken()).build();
    }
    throw new NotFoundException(null, "Invalid refresh token");
  }
}
