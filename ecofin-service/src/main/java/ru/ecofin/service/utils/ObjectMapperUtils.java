package ru.ecofin.service.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import ru.ecofin.service.config.MainConfig;

@Slf4j
public class ObjectMapperUtils {

  private ObjectMapperUtils() {
    throw new IllegalArgumentException("Utility class");
  }

  @Nullable
  public static String convertObjectToString(@Nullable Object data) {
    ObjectMapper objectMapper = MainConfig.objectMapper();
    try {
      return objectMapper.writeValueAsString(data);
    } catch (Exception e) {
      log.warn("Can't get string value from object. Message: {}", e.getMessage());
      return null;
    }
  }

}
