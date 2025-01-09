package ru.ecofin.service.utils.time;

import com.fasterxml.jackson.databind.util.StdConverter;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateToStringConverter extends StdConverter<ZonedDateTime, String> {

  @Override
  public String convert(ZonedDateTime value) {
    return value.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
  }
}
