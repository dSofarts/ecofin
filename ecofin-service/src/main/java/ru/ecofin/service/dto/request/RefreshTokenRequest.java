package ru.ecofin.service.dto.request;

import lombok.Data;

@Data
public class RefreshTokenRequest {
  private String refreshToken;
}
