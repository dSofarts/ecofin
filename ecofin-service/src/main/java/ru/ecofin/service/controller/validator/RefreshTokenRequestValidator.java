package ru.ecofin.service.controller.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.ecofin.service.dto.request.RefreshTokenRequest;

public class RefreshTokenRequestValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return RefreshTokenRequest.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "refreshToken", "refreshToken");
  }
}
