package ru.ecofin.service.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.ecofin.service.security.JWTService;
import ru.ecofin.service.service.UserService;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JWTService jwtService;
  private final UserService userService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    final String username;

    if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    jwt = authHeader.substring(7);
    username = jwtService.extractEmail(jwt);
    if (StringUtils.isNotEmpty(username)
        && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = userService.getUserDetailsService().loadUserByUsername(username);
      if (jwtService.isTokenValid(jwt, userDetails)
          && jwtService.tokenIsNotRefresh(jwt, userDetails)) {
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
  }
}
