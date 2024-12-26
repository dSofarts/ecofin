package ru.ecofin.service.security;

import ru.ecofin.service.dto.request.RefreshTokenRequest;
import ru.ecofin.service.dto.request.SigninRequestDto;
import ru.ecofin.service.dto.request.SignupRequestDto;
import ru.ecofin.service.dto.response.JwtResponseDto;
import ru.ecofin.service.entity.User;

public interface AuthenticationService {

  User signup(SignupRequestDto registerRequest);

  JwtResponseDto signin(SigninRequestDto registerRequest);

  JwtResponseDto refreshToken(RefreshTokenRequest refreshTokenRequest);

}
