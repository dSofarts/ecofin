package ru.ecofin.service.security;

import java.util.Map;
import org.springframework.security.core.userdetails.UserDetails;
import ru.ecofin.service.entity.User;

public interface JWTService {

  String extractPhone(String token);

  String generateToken(UserDetails user);

  String generateRefreshToken(UserDetails user);

  boolean isTokenValid(String token, UserDetails userDetails);

  boolean tokenIsReal(String jwt);

  boolean tokenIsRefresh(String jwt);

  boolean tokenIsTemp(String jwt);

  String generateTempToken(User user);

  String getPhoneFromHeader(Map<String, String> requestHeader);
}
