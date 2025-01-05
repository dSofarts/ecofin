package ru.ecofin.service.kafka.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.ecofin.service.dto.kafka.CodeDto;
import ru.ecofin.service.kafka.KafkaProducer;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducerImpl implements KafkaProducer {

  @Value("${topics.send-code-topic}")
  private String topic;

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;

  @Override
  public void send(CodeDto codeDto) {
    try {
      log.info("Send code to kafka with operationId: {}", codeDto.getOperationId());
      kafkaTemplate.send(topic, objectMapper.writeValueAsString(codeDto));
    } catch (Exception e) {
      log.warn("Warning while send message to kafka: {}", codeDto);
    }
  }
}
