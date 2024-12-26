package ru.ecofin.service.dto.request;

import lombok.Data;

@Data
public class SigninRequestDto {
  private String username;
  private String password;
}
