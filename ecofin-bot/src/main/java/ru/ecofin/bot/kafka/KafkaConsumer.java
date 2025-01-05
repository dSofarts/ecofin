package ru.ecofin.bot.kafka;

import ru.ecofin.bot.dto.CodeDto;

public interface KafkaConsumer {

  void consume(CodeDto codeDto);
}
