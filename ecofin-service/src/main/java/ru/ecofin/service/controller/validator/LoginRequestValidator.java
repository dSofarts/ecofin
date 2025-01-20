package ru.ecofin.service.controller.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.ecofin.service.dto.request.LoginRequestDto;

public class LoginRequestValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return LoginRequestDto.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    LoginRequestDto request = (LoginRequestDto) target;

    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "phone");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password");

    if (request.getPhone() != null && (request.getPhone().length() != 11
        || !request.getPhone().startsWith("7"))) {
      errors.reject("phone", "phone");
    }
  }
}
