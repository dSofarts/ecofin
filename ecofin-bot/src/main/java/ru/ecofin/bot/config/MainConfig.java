package ru.ecofin.bot.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.ecofin.bot.kafka.KafkaProducer;
import ru.ecofin.bot.service.TelegramBot;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MainConfig {

  private final KafkaProducer kafkaProducer;

  @Bean
  public TelegramBot telegramBot(@Value("${bot.name}") String botName,
      @Value("${bot.token}") String botToken) {
    TelegramBot telegramBot = new TelegramBot(botName, botToken, kafkaProducer);
    try {
      TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
      telegramBotsApi.registerBot(telegramBot);
    } catch (TelegramApiException e) {
      log.error("Exception during registration telegram api: {}", e.getMessage());
    }
    return telegramBot;
  }
}
