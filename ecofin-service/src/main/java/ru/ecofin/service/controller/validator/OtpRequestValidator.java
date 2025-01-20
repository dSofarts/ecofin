package ru.ecofin.service.controller.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.ecofin.service.dto.request.OtpRequestDto;

public class OtpRequestValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return OtpRequestDto.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    OtpRequestDto requestDto = (OtpRequestDto) target;

    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "tempToken", "tempToken");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "operationId", "operationId");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "otp", "otp");

    if (requestDto.getOperationId() != null
        && requestDto.getOperationId().length() != 36) {
      errors.reject("operationId", "operationId");
    }
    if (requestDto.getOtp() != null
        && requestDto.getOtp().length() != 6) {
      errors.reject("otp", "otp");
    }
  }
}
