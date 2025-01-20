package ru.ecofin.bot.kafka.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.ecofin.bot.dto.CodeDto;
import ru.ecofin.bot.dto.TransferMessageDto;
import ru.ecofin.bot.kafka.KafkaConsumer;
import ru.ecofin.bot.service.TelegramBot;

@Slf4j
@Service
public class KafkaConsumerImpl implements KafkaConsumer {

  private final TelegramBot telegramBot;

  public KafkaConsumerImpl(TelegramBot telegramBot) {
    this.telegramBot = telegramBot;
  }

  @Override
  @KafkaListener(topics = "${topics.send-code-topic}",
      containerFactory = "codeDtoFactory")
  public void consumeSendCode(CodeDto codeDto) {
    log.info("Successfully consumed code from kafka with operationId: {}",
        codeDto.getOperationId());
    telegramBot.sendConfirmCode(codeDto.getChatId(), codeDto.getCode());
  }

  @Override
  @KafkaListener(topics = "${topics.send-transfer-message-topic}",
      containerFactory = "transferMessageDtoFactory")
  public void consumeSendTransferMessage(TransferMessageDto transferMessageDto) {
    log.info("Successfully consumed transfer message from kafka with operationId: {}",
        transferMessageDto.getOperationId());
    telegramBot.sendTransferMessages(transferMessageDto);
  }
}
