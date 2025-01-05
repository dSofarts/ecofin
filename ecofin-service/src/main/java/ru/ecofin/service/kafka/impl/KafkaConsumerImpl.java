package ru.ecofin.service.kafka.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.ecofin.service.dto.kafka.ConfirmationDto;
import ru.ecofin.service.kafka.KafkaConsumer;

@Slf4j
@Service
public class KafkaConsumerImpl implements KafkaConsumer {

  @Override
  @KafkaListener(topics = "${topics.confirmation-topic}",
      containerFactory = "confirmationDtoFactory")
  public void consume(ConfirmationDto confirmationDto) {
    log.info("Successfully consumed confirmation from kafka with operationId: {}",
        confirmationDto.getOperationId());
  }
}
