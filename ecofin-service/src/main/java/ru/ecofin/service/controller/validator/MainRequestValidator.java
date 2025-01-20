package ru.ecofin.service.controller.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.ecofin.service.dto.request.MainRequestDto;

public class MainRequestValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return MainRequestDto.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    MainRequestDto request = (MainRequestDto) target;
    ValidationUtils.rejectIfEmpty(errors, "userId", "userId");
    if (request.getUserId() != null || request.getUserId().length() != 36) {
      errors.reject("userId", "userId");
    }
  }
}
