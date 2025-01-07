package ru.ecofin.service.security.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.ecofin.service.entity.User;
import ru.ecofin.service.security.JWTService;

@Service
public class JWTServiceImpl implements JWTService {

  private final long EXPIRATION_TIME = 1000 * 60 * 24;
  private final long EXPIRATION_REFRESH_TIME = 604800000;
  private final long EXPIRATION_TEMP_TIME = 10 * 60 * 1000;
  private final String REFRESH = "refresh";
  private final String REAL = "real";
  private final String TEMP = "temp";

  @Override
  public String generateToken(UserDetails user) {
    return Jwts.builder()
        .setClaims(createClaim(REAL))
        .setSubject(user.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        .signWith(getSignKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  @Override
  public String generateRefreshToken(UserDetails user) {
    return Jwts.builder()
        .setClaims(createClaim(REFRESH))
        .setSubject(user.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_REFRESH_TIME))
        .signWith(getSignKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  @Override
  public String generateTempToken(User user) {
    return Jwts.builder()
        .setClaims(createClaim(TEMP))
        .setSubject(user.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TEMP_TIME))
        .signWith(getSignKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  @Override
  public String extractPhone(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  @Override
  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String phone = extractPhone(token);
    return phone != null && phone.equals(userDetails.getUsername()) && !isTokenExpired(token);
  }

  @Override
  public boolean tokenIsReal(String jwt) {
    final String type = extractType(jwt);
    return type != null && type.equals(REAL);
  }

  @Override
  public boolean tokenIsRefresh(String jwt) {
    final String type = extractType(jwt);
    return type != null && type.equals(REFRESH);
  }

  @Override
  public boolean tokenIsTemp(String jwt) {
    final String type = extractType(jwt);
    return type != null && type.equals(TEMP);
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

  private String extractType(String jwt) {
    return extractClaim(jwt, claims -> claims.get("type", String.class));
  }

  private boolean isTokenExpired(String token) {
    return extractClaim(token, Claims::getExpiration).before(new Date());
  }

  private Map<String, Object> createClaim(String type) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("type", type);
    return claims;
  }
}
