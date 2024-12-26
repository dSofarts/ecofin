package ru.ecofin.service.security;

import java.util.Map;
import org.springframework.security.core.userdetails.UserDetails;

public interface JWTService {

  String extractUsername(String token);

  String generateToken(UserDetails user);

  String generateRefreshToken(Map<String, Object> extraClaims, UserDetails user);

  boolean isTokenValid(String token, UserDetails userDetails);

  boolean tokenIsNotRefresh(String jwt, UserDetails userDetails);
}
