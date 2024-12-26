package ru.ecofin.service.dto.request;

import lombok.Data;

@Data
public class SignupRequestDto {
  private String username;
  private String password;
}
