package ru.ecofin.utils.masking.service;

import java.util.List;
import org.springframework.lang.Nullable;

public interface MaskingService {

  String writeAsString(@Nullable List<?> objects);

  String writeAsString(@Nullable Object object);

  String writeAsString(@Nullable String json);

  String writeAsStringWithoutMasking(@Nullable Object object);

  String maskingValue(@Nullable String value);
}
