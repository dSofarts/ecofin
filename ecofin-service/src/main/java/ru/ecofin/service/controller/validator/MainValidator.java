package ru.ecofin.service.controller.validator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import ru.ecofin.service.exception.ValidationException;
import ru.ecofin.service.properties.RequestValidationProperties;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainValidator {

  private final RequestValidationProperties requestValidationProperties;

  public void validateRequest(Object requestBody,
      Class<?> validatorClass, Map<String, String> requestHeaders) {
    requestValidationProperties.validateRequest(requestHeaders);
    validateFields(requestBody, validatorClass);
  }

  public void validateRequest(Map<String, String> requestHeaders) {
    requestValidationProperties.validateRequest(requestHeaders);
  }

  private void validateFields(Object requestBody, Class<?> validatorClass) {
    DataBinder binder = new DataBinder(requestBody);
    try {
      Validator validator = (Validator) validatorClass.getConstructor().newInstance();
      binder.setValidator(validator);
      binder.validate();
      BindingResult bindingResult = binder.getBindingResult();
      if (!CollectionUtils.isEmpty(bindingResult.getAllErrors())) {
        throw new ValidationException(getResultErrorString(bindingResult.getAllErrors()),
            HttpStatus.BAD_REQUEST);
      }
    } catch (ReflectiveOperationException | IllegalArgumentException e) {
      log.error("Developer error, check classes that implements "
          + "org.springframework.validation.Validator");
      throw new ValidationException("Technical error",
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private String getResultErrorString(List<ObjectError> errors) {
    String missingFields = errors.stream()
        .filter(objectError -> objectError instanceof FieldError)
        .map(FieldError.class::cast)
        .filter(fieldError -> fieldError.getField().equals(fieldError.getCode()))
        .map(DefaultMessageSourceResolvable::getCode)
        .collect(Collectors.joining(", "));
    String incorrectLength = errors.stream()
        .filter(objectError -> !(objectError instanceof FieldError))
        .map(DefaultMessageSourceResolvable::getCode)
        .collect(Collectors.joining(", "));
    String otherMessages = errors.stream()
        .filter(objectError -> objectError instanceof FieldError)
        .map(FieldError.class::cast)
        .filter(fieldError -> !fieldError.getField().equals(fieldError.getCode()))
        .map(DefaultMessageSourceResolvable::getCode)
        .collect(Collectors.joining(", "));

    missingFields =
        missingFields.isEmpty() ? "" : "Missing required fields: " + missingFields + "; ";
    incorrectLength =
        incorrectLength.isEmpty() ? "" : "Incorrect fields length: " + incorrectLength + "; ";
    otherMessages = otherMessages.isEmpty() ? "" : otherMessages + "; ";
    String result = missingFields + incorrectLength + otherMessages;
    return result.substring(0, result.length() - 2);
  }
}
