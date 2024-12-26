package ru.ecofin.service.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtResponseDto {

  private String token;
  private String refreshToken;
}
