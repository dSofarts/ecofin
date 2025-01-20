package ru.ecofin.service.controller.validator;

import java.math.BigDecimal;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.ecofin.service.dto.request.CreateTransactionRequestDto;

public class CreateTransactionRequestValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return CreateTransactionRequestDto.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    CreateTransactionRequestDto request = (CreateTransactionRequestDto) target;

    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "walletId", "walletId");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "categoryId", "categoryId");
    ValidationUtils.rejectIfEmpty(errors, "transactionType", "transactionType");
    ValidationUtils.rejectIfEmpty(errors, "amount", "amount");

    if (request.getWalletId() != null
        && request.getWalletId().length() != 36) {
      errors.reject("walletId", "walletId");
    }

    if (request.getCategoryId() != null
        && request.getCategoryId().length() != 36) {
      errors.reject("categoryId", "categoryId");
    }

    if (request.getAmount() != null && request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
      errors.reject("amount", "amount");
    }
  }
}
