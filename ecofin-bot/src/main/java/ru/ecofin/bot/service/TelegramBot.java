package ru.ecofin.bot.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.ecofin.bot.dto.ConfirmationDto;
import ru.ecofin.bot.kafka.KafkaProducer;

@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

  private final String botName;
  private final KafkaProducer kafkaProducer;
  private final Set<String> confirmedChatId;

  public TelegramBot(String botName, String botToken, KafkaProducer kafkaProducer) {
    super(botToken);
    this.botName = botName;
    this.kafkaProducer = kafkaProducer;
    confirmedChatId = new HashSet<>();
  }

  @Override
  public void onUpdateReceived(Update update) {
    if (update.hasMessage() && update.getMessage().hasContact() &&
        !confirmedChatId.contains(update.getMessage().getChatId().toString())) {
      handleContact(update);
    } else if (update.hasMessage()) {
      handleText(update);
    }
  }

  private void handleText(Update update) {
    SendMessage message = new SendMessage();
    String chatId = update.getMessage().getChatId().toString();

    message.setText("Вы уже подтвердили ваш аккаунт");
    if (!confirmedChatId.contains(chatId)) {
      KeyboardButton contactButton = new KeyboardButton("Отправить контакт");
      contactButton.setRequestContact(true);

      KeyboardRow row = new KeyboardRow();
      row.add(contactButton);

      List<KeyboardRow> keyboard = new ArrayList<>();
      keyboard.add(row);

      ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
      markup.setKeyboard(keyboard);
      markup.setResizeKeyboard(true);
      message.setReplyMarkup(markup);
      message.setText("Пожалуйста, отправьте ваш контакт для подтверждения номера телефона.");
    }

    message.setChatId(chatId);
    sendMessage(message);
  }

  private void handleContact(Update update) {
    Contact contact = update.getMessage().getContact();
    String phoneNumber = contact.getPhoneNumber();
    String chatId = update.getMessage().getChatId().toString();
    String operationId = UUID.randomUUID().toString();
    ConfirmationDto confirmationDto = ConfirmationDto.builder()
        .phone(phoneNumber)
        .chatId(chatId)
        .operationId(operationId)
        .build();

    log.info("Confirmation request received, operationId: {}", operationId);
    SendMessage message = new SendMessage();
    message.setChatId(chatId);

    if (Objects.equals(contact.getUserId(), update.getMessage().getFrom().getId())) {
      log.info("Contact confirmed, sending a message to Kafka, operationId: {}", operationId);
      message.setText("Спасибо! Ваш номер телефона успешно подтвержден");
      ReplyKeyboardRemove keyboardRemove = new ReplyKeyboardRemove();
      keyboardRemove.setRemoveKeyboard(true);
      message.setReplyMarkup(keyboardRemove);

      kafkaProducer.send(confirmationDto);
      confirmedChatId.add(chatId);
      sendMessage(message);
      return;
    }

    log.info("Contact not confirmed, operationId: {}", operationId);
    message.setText("Вы можете отправить только свой контакт!");
    sendMessage(message);
  }

  public void sendConfirmCode(String chatId, String code) {
    SendMessage message = new SendMessage();
    message.setChatId(chatId);
    message.setText(("Для входа в аккаунт введите код: `%s`\n"
        + "Не сообщайте данный код никому, его спрашивают только мошенники")
        .formatted(code));
    message.setParseMode("MarkdownV2");
    sendMessage(message);
  }

  @Override
  public String getBotUsername() {
    return botName;
  }

  private void sendMessage(SendMessage message) {
    try {
      execute(message);
    } catch (TelegramApiException e) {
      log.error("An error occurred while sending a message: {}", e.getMessage());
    }
  }
}
