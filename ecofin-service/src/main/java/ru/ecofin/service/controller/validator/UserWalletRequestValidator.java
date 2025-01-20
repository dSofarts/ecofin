package ru.ecofin.service.controller.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.ecofin.service.dto.request.UserWalletRequestDto;

public class UserWalletRequestValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return UserWalletRequestDto.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    UserWalletRequestDto userWalletRequestDto = (UserWalletRequestDto) target;
    if (userWalletRequestDto.getWalletId() == null && userWalletRequestDto.getPhone() == null) {
      ValidationUtils.rejectIfEmptyOrWhitespace(errors, "walletId", "walletId");
      ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "phone");
    }
    if (userWalletRequestDto.getWalletId() != null
        && userWalletRequestDto.getWalletId().length() != 36) {
      errors.reject("walletId", "walletId");
    }
    if (userWalletRequestDto.getPhone() != null && (userWalletRequestDto.getPhone().length() != 11
        || !userWalletRequestDto.getPhone().startsWith("7"))) {
      errors.reject("phone", "phone");
    }
  }
}
