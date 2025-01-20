package ru.ecofin.service.kafka;

import ru.ecofin.service.dto.kafka.CodeDto;
import ru.ecofin.service.dto.kafka.TransferMessageDto;

public interface KafkaProducer {

  void sendCode(CodeDto confirmationDto);

  void sendTransferMessage(TransferMessageDto transferMessageDto);
}
