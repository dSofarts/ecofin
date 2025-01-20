package ru.ecofin.utils.masking.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.ecofin.utils.masking.properties.MaskingProperties;
import ru.ecofin.utils.masking.service.MaskingObjectMapper;
import ru.ecofin.utils.masking.service.MaskingService;

@Slf4j
@Service
public class MaskingServiceImpl implements MaskingService {

  private final MaskingObjectMapper objectMapper;
  private final MaskingProperties maskingProperties;

  @Autowired
  public MaskingServiceImpl(MaskingObjectMapper objectMapper,
      MaskingProperties maskingProperties) {
    this.objectMapper = objectMapper;
    this.maskingProperties = maskingProperties;
  }

  @Override
  public String writeAsString(List<?> objects) {
    String masked = objects.stream()
        .map(this::writeAsString)
        .collect(Collectors.joining(","));
    return String.format("[%s]", masked);
  }

  @Override
  public String writeAsString(@Nullable Object object) {
    try {
      String str = objectMapper.writeValueAsString(object);
      return writeAsString(str);
    } catch (Exception e) {
      log.warn("Can't get string value from object. Message: {}", e.getMessage());
    }
    return null;
  }

  @Override
  public String writeAsString(@Nullable String json) {
    try {
      Object obj = new JSONParser().parse(json);
      if (obj instanceof JSONObject) {
        String maskedJson = maskJsonObject((JSONObject) obj);
        if (json != null) {
          return maskedJson;
        }
      } else if (obj instanceof JSONArray) {
        JSONArray array = (JSONArray) obj;
        return maskJsonArray(array);
      }
    } catch (Exception e) {
      log.warn("Error while parsing json. Message: {}", e.getMessage());
    }
    return null;
  }

  private String maskJsonArray(JSONArray jsonArray) {
    StringBuilder sb = new StringBuilder();
    for (Object o : jsonArray) {
      String json = maskJsonObject((JSONObject) o);
      if (json != null) {
        sb.append(json);
        sb.append(",");
      }
    }
    return "[" + (sb.length() > 0 ?
        sb.substring(0, sb.length() - 1) : sb) + "]";
  }

  private String maskJsonObject(JSONObject jsonObject) {
    JSONObject objRes = maskingMap(jsonObject);
    if (objRes != null) {
      return objRes.toJSONString();
    }
    return null;
  }

  @Nullable
  @SuppressWarnings("unchecked")
  private JSONObject maskingMap(@Nullable JSONObject input) {
    return maskingMap(input, maskingProperties.getMaxNestedDepth());
  }

  @Nullable
  @SuppressWarnings("unchecked")
  private JSONObject maskingMap(@Nullable JSONObject input, int countRemainingDepth) {
    JSONObject result = new JSONObject();
    if (input == null) {
      return null;
    }
    for (Object key : input.keySet()) {
      Object value = input.get(key);
      if (value instanceof String) {
        if ((maskingProperties.isShowBooleanFields() && valueIsBoolean((String) value))
            || (key instanceof String && isNotMasking((String) key))) {
          result.put(key, value);
        } else {
          if (key instanceof String &&
              maskingProperties.getSecretFields().contains((String) key)) {
            result.put(key, maskPassword((String) value));
            continue;
          }
          result.put(key, maskingValue((String) value));
        }
        continue;
      }
      if (value instanceof Long) {
        if (key instanceof String && isNotMasking((String) key)) {
          result.put(key, value);
        } else {
          result.put(key, maskingValue(Long.toString((Long) value)));
        }
        continue;
      }
      if (value instanceof Double) {
        if (key instanceof String && isNotMasking((String) key)) {
          result.put(key, value);
        } else {
          result.put(key, maskingValue(Double.toString((Double) value)));
        }
        continue;
      }
      if (value instanceof Boolean) {
        if (maskingProperties.isShowBooleanFields() ||
            (key instanceof String && isNotMasking((String) key))) {
          result.put(key, Boolean.toString((Boolean) value));
        } else {
          result.put(key, maskingProperties.getMaskSymbol());
        }
        continue;
      }
      if (value instanceof JSONObject) {
        if (countRemainingDepth == 0) {
          result.put(key, "{" + String.valueOf(maskingProperties.getMaskSymbol()).repeat(5) + "}");
          continue;
        }
        result.put(key, maskingMap((JSONObject) value, --countRemainingDepth));
      }
      if (value instanceof JSONArray) {
        if (countRemainingDepth == 0) {
          result.put(key, "[" + String.valueOf(maskingProperties.getMaskSymbol()).repeat(5) + "]");
          continue;
        }
        result.put(key, maskingList((JSONArray) value, --countRemainingDepth));
        continue;
      }
      if (value == null) {
        result.put(key, null);
        continue;
      }
      result.put(key, String.valueOf(maskingProperties.getMaskSymbol()).repeat(7));
    }
    return result;
  }

  @Nullable
  @SuppressWarnings("unchecked")
  private Object maskingList(JSONArray inputList) {
    return maskingList(inputList, maskingProperties.getMaxNestedDepth());
  }

  @Nullable
  @SuppressWarnings("unchecked")
  private Object maskingList(JSONArray inputList, int depth) {
    if (inputList == null) {
      return null;
    }
    JSONArray resultList = new JSONArray();
    for (Object value : inputList) {
      if (value instanceof String) {
        resultList.add(maskingValue((String) value));
        continue;
      }
      if (value instanceof Long) {
        resultList.add(Long.toString((Long) value));
        continue;
      }
      if (value instanceof Double) {
        resultList.add(Double.toString((Double) value));
        continue;
      }
      if (value instanceof Boolean) {
        resultList.add(Boolean.toString((Boolean) value));
        continue;
      }
      if (value instanceof JSONArray) {
        if (depth == 0) {
          resultList.add("[" + String.valueOf(maskingProperties.getMaskSymbol()).repeat(5) + "]");
          continue;
        }
        resultList.add(maskingList((JSONArray) value, --depth));
        continue;
      }
      if (value instanceof JSONObject) {
        if (depth == 0) {
          resultList.add("{" + String.valueOf(maskingProperties.getMaskSymbol()).repeat(5) + "}");
          continue;
        }
        resultList.add(maskingMap((JSONObject) value, --depth));
        continue;
      }
      resultList.add(String.valueOf(maskingProperties.getMaskSymbol()).repeat(5));
    }
    return resultList;
  }

  @NonNull
  private boolean valueIsBoolean(@Nullable String value) {
    if (value == null) {
      return false;
    }
    return value.equalsIgnoreCase("true")
        || value.equalsIgnoreCase("false");
  }

  @NonNull
  private boolean isNotMasking(@Nullable String key) {
    if (key == null) {
      return false;
    }
    for (String notMaskingKey : maskingProperties.getNotMaskingKeys()) {
      if (notMaskingKey.equalsIgnoreCase(key)) {
        return true;
      }
    }
    return false;
  }

  @Override
  @Nullable
  public String writeAsStringWithoutMasking(@Nullable Object object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      return null;
    }
  }

  @Override
  @Nullable
  public String maskingValue(@Nullable String value) {
    if (value == null) {
      return null;
    }
    if (value.isEmpty()) {
      return "";
    }
    if (value.length() <= 4) {
      return String.valueOf(maskingProperties.getMaskSymbol()).repeat(value.length());
    }
    if (value.length() <= 7) {
      return value.charAt(0) + String.valueOf(maskingProperties.getMaskSymbol()).repeat(4) +
          value.charAt(value.length() - 1);
    }
    return value.substring(0, 2) + String.valueOf(maskingProperties.getMaskSymbol()).repeat(4) +
        value.substring(value.length() - 2);
  }

  @Nullable
  private String maskPassword(@Nullable String value) {
    if (value == null) {
      return null;
    }
    return String.valueOf(maskingProperties.getMaskSymbol()).repeat(5);
  }
}
