package ru.ecofin.service.kafka.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.ecofin.service.dto.kafka.ConfirmationDto;
import ru.ecofin.service.kafka.KafkaConsumer;
import ru.ecofin.service.service.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerImpl implements KafkaConsumer {

  private final UserService userService;

  @Override
  @KafkaListener(topics = "${topics.confirmation-topic}",
      containerFactory = "confirmationDtoFactory")
  public void consume(ConfirmationDto confirmationDto) {
    try {
      log.info("Successfully consumed confirmation from kafka with operationId: {}",
          confirmationDto.getOperationId());
      userService.confirmationUser(confirmationDto);
    } catch (Exception e) {
      log.warn("Failed to confirm user with operationId: {}, {}", confirmationDto.getOperationId(),
          e.getMessage());
    }
  }
}
