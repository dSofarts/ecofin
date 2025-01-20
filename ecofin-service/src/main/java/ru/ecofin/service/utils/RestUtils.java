package ru.ecofin.service.utils;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.ecofin.service.dto.response.FrontResponseDto;
import ru.ecofin.service.exception.ValidationException;

@Slf4j
public class RestUtils {

  private RestUtils() {
    throw new IllegalStateException("Utility class");
  }

  public static ResponseEntity<FrontResponseDto> failReturn(String message, HttpStatus status,
      String entityId) {
    FrontResponseDto.FrontResponseDtoBuilder responseDto = FrontResponseDto.builder()
        .status(message)
        .statusCode(String.valueOf(status.value()));
    return ResponseEntity.status(status).body(Objects.isNull(entityId) ?
        responseDto.build() : responseDto.objectId(entityId).build());
  }

  public static ResponseEntity<FrontResponseDto> failReturn(String message, HttpStatus status) {
    return failReturn(message, status, null);
  }

  public static ResponseEntity<FrontResponseDto> failCaptchaReturn(String message) {
    return failReturn(message, HttpStatus.UNPROCESSABLE_ENTITY, null);
  }

  public static HttpStatus findHttpStatus(Object response) {
    if (response instanceof FrontResponseDto) {
      return HttpStatus.valueOf(Integer.parseInt(((FrontResponseDto) response).getStatusCode()));
    }
    return HttpStatus.OK;
  }

  public static void serviceNameCheck(String serviceName) {
    log.error("Not permission for this service-name: {} to execute request", serviceName);
    throw new ValidationException(
        String.format("Not permission for this service-name: %s to execute request",
            serviceName), HttpStatus.FORBIDDEN);
  }

  public static FrontResponseDto buildResponseDto(String message, String statusCode) {
    return buildResponseDto(null, message, statusCode);
  }

  public static FrontResponseDto buildResponseDto(String entityId, String message, String statusCode) {
    return FrontResponseDto.builder()
        .statusCode(statusCode)
        .status(statusCode)
        .objectId(entityId).build();
  }
}
