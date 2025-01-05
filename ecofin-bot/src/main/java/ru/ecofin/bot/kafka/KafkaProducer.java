package ru.ecofin.bot.kafka;

import ru.ecofin.bot.dto.ConfirmationDto;

public interface KafkaProducer {

  void send(ConfirmationDto confirmationDto);
}
