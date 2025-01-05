package ru.ecofin.service.security;

import ru.ecofin.service.dto.request.LoginRequestDto;
import ru.ecofin.service.dto.request.RefreshTokenRequest;
import ru.ecofin.service.dto.request.RegistrationRequestDto;
import ru.ecofin.service.dto.response.JwtResponseDto;
import ru.ecofin.service.dto.response.UserResponseDto;

public interface AuthenticationService {

  UserResponseDto registration(RegistrationRequestDto registerRequest);

  JwtResponseDto login(LoginRequestDto registerRequest);

  JwtResponseDto refreshToken(RefreshTokenRequest refreshTokenRequest);

}
