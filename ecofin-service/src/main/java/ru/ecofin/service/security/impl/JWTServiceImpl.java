package ru.ecofin.service.security.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.ecofin.service.security.JWTService;

@Service
public class JWTServiceImpl implements JWTService {

  private final long EXPIRATION_TIME = 1000 * 60 * 24;
  private final long EXPIRATION_REFRESH_TIME = 604800000;
  private final String REFRESH = "refresh";

  @Override
  public String generateToken(UserDetails user) {
    return Jwts.builder()
        .setSubject(user.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        .signWith(getSignKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  @Override
  public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails user) {
    return Jwts.builder()
        .setClaims(extraClaims)
        .setSubject(user.getUsername())
        .setAudience(REFRESH)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_REFRESH_TIME))
        .signWith(getSignKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  @Override
  public String extractEmail(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaim(token);
    return claimsResolver.apply(claims);
  }

  private Key getSignKey() {
    byte[] keyBytes = Decoders.BASE64.decode("A0D293181A1D0F768D9DD45E129283637658FD1C1DDA362358");
    return Keys.hmacShaKeyFor(keyBytes);
  }

  private Claims extractAllClaim(String token) {
    return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String email = extractEmail(token);
    return email != null && email.equals(userDetails.getUsername()) && !isTokenExpired(token);
  }

  @Override
  public boolean tokenIsNotRefresh(String jwt, UserDetails userDetails) {
    final String audience = extractAudience(jwt);
    return audience == null || !audience.equals(REFRESH);
  }

  private String extractAudience(String jwt) {
    return extractClaim(jwt, Claims::getAudience);
  }

  private boolean isTokenExpired(String token) {
    return extractClaim(token, Claims::getExpiration).before(new Date());
  }
}
