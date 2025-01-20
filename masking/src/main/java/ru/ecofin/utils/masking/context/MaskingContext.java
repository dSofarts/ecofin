package ru.ecofin.utils.masking.context;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.ecofin.utils.masking.service.MaskingObjectMapper;

@Configuration
@ComponentScan(basePackages = {"ru.ecofin.utils.masking"})
public class MaskingContext {

  @Bean("maskingObjectMapper")
  public MaskingObjectMapper maskingObjectMapper() {
    MaskingObjectMapper maskingObjectMapper = new MaskingObjectMapper();
    maskingObjectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    maskingObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    maskingObjectMapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
    return maskingObjectMapper;
  }
}
