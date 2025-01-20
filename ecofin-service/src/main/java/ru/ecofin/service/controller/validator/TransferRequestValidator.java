package ru.ecofin.service.controller.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.ecofin.service.dto.request.TransferRequest;

public class TransferRequestValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return TransferRequest.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    TransferRequest transferRequest = (TransferRequest) target;
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "walletFromId", "walletFromId");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "walletToId", "walletToId");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "amount", "amount");

    if (transferRequest.getWalletFromId() != null
        && transferRequest.getWalletFromId().length() != 36) {
      errors.reject("walletFromId", "walletFromId");
    }

    if (transferRequest.getWalletToId() != null
        && transferRequest.getWalletToId().length() != 36) {
      errors.reject("walletToId", "walletToId");
    }
  }
}
