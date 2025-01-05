package ru.ecofin.bot.kafka.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.ecofin.bot.dto.ConfirmationDto;
import ru.ecofin.bot.kafka.KafkaProducer;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducerImpl implements KafkaProducer {

  @Value("${topics.confirmation-topic}")
  private String topic;

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;

  @Override
  public void send(ConfirmationDto confirmationDto) {
    try {
      log.info("Send confirmation to kafka with operationId: {}", confirmationDto.getOperationId());
      kafkaTemplate.send(topic, objectMapper.writeValueAsString(confirmationDto));
    } catch (Exception e) {
      log.warn("Warning while send message to kafka: {}", confirmationDto);
    }
  }
}
