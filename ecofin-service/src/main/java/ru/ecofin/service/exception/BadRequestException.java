package ru.ecofin.service.exception;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class BadRequestException extends RuntimeException {

  public BadRequestException(String message) {
    super(message);
  }
}
