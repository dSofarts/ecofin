package ru.ecofin.service.controller.validator;

import java.math.BigDecimal;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.ecofin.service.dto.request.UpdateCategoryRequestDto;

public class UpdateCategoryRequestValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return UpdateCategoryRequestDto.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    UpdateCategoryRequestDto request = (UpdateCategoryRequestDto) target;

    if (request.getCategoryId() != null && request.getCategoryId().length() != 36) {
      errors.reject("categoryId", "categoryId");
    }
    if (request.getLimit() != null && request.getLimit().compareTo(BigDecimal.ZERO) < 0) {
      errors.reject("limit", "limit");
    }
  }
}
