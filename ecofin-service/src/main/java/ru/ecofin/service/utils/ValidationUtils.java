package ru.ecofin.service.utils;

import static ru.ecofin.service.constant.Constants.SERVICE_NAME;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidationUtils {

  private ValidationUtils() {
    throw new IllegalStateException("Utility class");
  }


  public static boolean isValidRequestHeader(Map<String, String> requestHeaders) {
    return requestHeaders.containsKey(SERVICE_NAME)
        && !requestHeaders.get(SERVICE_NAME).isEmpty();
  }

  public static boolean isValidStringRegExp(String phone, String regExp) {
    Pattern compiledPattern = Pattern.compile(regExp);
    Matcher matcher = compiledPattern.matcher(phone);
    return matcher.matches();
  }
}
