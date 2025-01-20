package ru.ecofin.service.kafka.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.ecofin.service.dto.kafka.CodeDto;
import ru.ecofin.service.dto.kafka.TransferMessageDto;
import ru.ecofin.service.kafka.KafkaProducer;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducerImpl implements KafkaProducer {

  @Value("${topics.send-code-topic}")
  private String sendCodeTopic;
  @Value("${topics.send-transfer-message-topic}")
  private String sendTransferMessageTopic;

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;

  @Override
  public void sendCode(CodeDto codeDto) {
    try {
      log.info("Send code to kafka with operationId: {}", codeDto.getOperationId());
      kafkaTemplate.send(sendCodeTopic, objectMapper.writeValueAsString(codeDto));
    } catch (Exception e) {
      log.warn("Warning while send message to kafka: {}", codeDto);
    }
  }

  @Override
  public void sendTransferMessage(TransferMessageDto transferMessageDto) {
    try {
      log.info("Send transfer message to kafka with operationId: {}",
          transferMessageDto.getOperationId());
      kafkaTemplate.send(sendTransferMessageTopic,
          objectMapper.writeValueAsString(transferMessageDto));
    } catch (Exception e) {
      log.warn("Warning while send transfer message to kafka: {}", transferMessageDto);
    }
  }
}
