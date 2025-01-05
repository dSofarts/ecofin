package ru.ecofin.service.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class NotFoundException extends RuntimeException {

  private final String objectId;
  private final String message;

  public NotFoundException(String objectId, String message) {
    super();
    this.objectId = objectId;
    this.message = message;
  }
}
