package ru.ecofin.service.config;

import static ru.ecofin.service.utils.RestUtils.failReturn;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.ecofin.service.constant.Constants;
import ru.ecofin.service.security.JWTService;
import ru.ecofin.service.service.UserService;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JWTService jwtService;
  private final UserService userService;
  private final ObjectMapper objectMapper;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    final String authHeader = request.getHeader(Constants.AUTHORIZATION_HEADER);
    final String jwt;
    final String phone;

    if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith("Bearer ") ||
        request.getRequestURI().contains("refresh-token")) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      jwt = authHeader.substring(7);
      phone = jwtService.extractPhone(jwt);
      if (StringUtils.isNotEmpty(phone)
          && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = userService.getUserDetailsService().loadUserByUsername(phone);
        if (jwtService.isTokenValid(jwt, userDetails) && jwtService.tokenIsReal(jwt)) {
          SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
          UsernamePasswordAuthenticationToken token =
              new UsernamePasswordAuthenticationToken(userDetails, null,
                  userDetails.getAuthorities());
          token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          securityContext.setAuthentication(token);
          SecurityContextHolder.setContext(securityContext);
        }
      }
      filterChain.doFilter(request, response);
    } catch (Exception exception) {
      response.setContentType("application/json");
      response.setStatus(HttpStatus.FORBIDDEN.value());
      response.getWriter().write(objectMapper.writeValueAsString(failReturn(exception.getMessage(),
          HttpStatus.FORBIDDEN).getBody()));
    }
  }
}
