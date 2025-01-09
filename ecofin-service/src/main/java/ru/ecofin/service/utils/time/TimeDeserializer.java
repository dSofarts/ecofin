package ru.ecofin.service.utils.time;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeDeserializer extends StdDeserializer<ZonedDateTime> {

  protected TimeDeserializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public ZonedDateTime deserialize(JsonParser jsonParser,
      DeserializationContext deserializationContext) throws IOException {
    TextNode node = jsonParser.getCodec().readTree(jsonParser);
    String asTextDate = node.asText();
    return ZonedDateTime.parse(asTextDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
  }
}
