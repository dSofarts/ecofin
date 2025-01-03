package ru.ecofin.service.utils.event;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum ServiceEventType implements IEventType {

  REGISTRATION_REQUEST("Запрос на регистрацию пользователя"),
  AUTHENTICATION_REQUEST("Запрос на авторизацию пользователя"),
  UPDATE_TOKEN_REQUEST("Запрос на обновление токена"),
  GET_USER_REQUEST("Запрос на получение данных о пользователе"),
  GET_WALLET_REQUEST("Запрос на получение кошелька"),
  GET_WALLET_INFO_REQUEST("Запрос на получение данных пользователя кошелька");

  private final String description;

  ServiceEventType(String description) {
    this.description = description;
  }

  @Override
  public String getName() {
    return this.name();
  }
}
