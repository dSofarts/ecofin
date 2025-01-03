package ru.ecofin.service.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

@Slf4j
public class ObjectMapperUtils {

  private ObjectMapperUtils() {
    throw new IllegalArgumentException("Utility class");
  }

  public static Optional<JsonNode> convertObjectToJsonNode(Object object) {
    try {
      return Optional.of(
          object instanceof String
              ? new ObjectMapper().readTree((String) object)
              : new ObjectMapper().valueToTree(object)
      );
    } catch (JsonProcessingException e) {
      log.error("Error converting string to tree", e);
      return Optional.empty();
    }
  }

  @Nullable
  public static String convertObjectToString(@Nullable Object data) {
    try {
      return new ObjectMapper().writeValueAsString(data);
    } catch (Exception e) {
      log.warn("Can't get string value from object. Message: {}", e.getMessage());
      return null;
    }
  }

}
