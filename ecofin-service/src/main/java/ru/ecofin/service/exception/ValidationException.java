package ru.ecofin.service.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ValidationException extends RuntimeException {

  private HttpStatus status;

  public ValidationException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }
}
