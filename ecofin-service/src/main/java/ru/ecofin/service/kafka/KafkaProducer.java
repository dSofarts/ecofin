package ru.ecofin.service.kafka;

import ru.ecofin.service.dto.kafka.CodeDto;

public interface KafkaProducer {

  void send(CodeDto confirmationDto);
}
