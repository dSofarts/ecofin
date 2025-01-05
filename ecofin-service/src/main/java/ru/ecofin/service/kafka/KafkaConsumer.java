package ru.ecofin.service.kafka;

import ru.ecofin.service.dto.kafka.ConfirmationDto;

public interface KafkaConsumer {

  void consume(ConfirmationDto confirmationDto);
}
