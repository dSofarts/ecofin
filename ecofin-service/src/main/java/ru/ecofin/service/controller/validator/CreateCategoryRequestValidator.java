package ru.ecofin.service.controller.validator;

import java.math.BigDecimal;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.ecofin.service.dto.request.CreateCategoryRequestDto;

public class CreateCategoryRequestValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return CreateCategoryRequestDto.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    CreateCategoryRequestDto request = (CreateCategoryRequestDto) target;
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "categoryName", "categoryName");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "limit", "limit");

    if (request.getLimit() != null && request.getLimit().compareTo(BigDecimal.ZERO) < 0) {
      errors.rejectValue("limit", "limit");
    }
  }
}
