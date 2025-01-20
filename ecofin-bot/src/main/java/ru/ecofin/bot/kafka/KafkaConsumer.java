package ru.ecofin.bot.kafka;

import ru.ecofin.bot.dto.CodeDto;
import ru.ecofin.bot.dto.TransferMessageDto;

public interface KafkaConsumer {

  void consumeSendCode(CodeDto codeDto);

  void consumeSendTransferMessage(TransferMessageDto transferMessageDto);
}
