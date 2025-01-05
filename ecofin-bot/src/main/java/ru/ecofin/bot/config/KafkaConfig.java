package ru.ecofin.bot.config;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.ecofin.bot.dto.CodeDto;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

  private final KafkaProperties kafkaProperties;

  @Bean
  public ProducerFactory<String, String> producerFactory() {
    Map<String, Object> props = kafkaProperties.buildProducerProperties(null);
    return new DefaultKafkaProducerFactory<>(props);
  }

  @Bean
  public KafkaTemplate<String, String> kafkaTemplate(
      ProducerFactory<String, String> producerFactory) {
    return new KafkaTemplate<>(producerFactory);
  }

  @Bean
  public ConsumerFactory<String, CodeDto> consumerFactory() {
    Map<String, Object> props = kafkaProperties.buildConsumerProperties(null);
    return new DefaultKafkaConsumerFactory<>(
        props,
        new StringDeserializer(),
        new JsonDeserializer<>(CodeDto.class));
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, CodeDto> codeDtoFactory(
      ConsumerFactory<String, CodeDto> consumerFactory) {
    ConcurrentKafkaListenerContainerFactory<String, CodeDto> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory);
    return factory;
  }
}
