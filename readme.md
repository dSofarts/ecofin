# ECOFIN - проект для ведения личных финансов

В проекте вы можете вести личные финансы, иметь несколько кошельков, переводить средства между ними,
а также переводить средства на кошельки других пользователей. 
Полная функциональность будет дорабатываться

## Сервисы

В качестве frontend части проекта используется сервис на Angular. В качестве backend части 
используется сервис на Java. 
Также имеется сервис для работы Telegram бота, при помощи которого происходит
подтверждение пользователя и двухфакторная аутентификация.

## Запуск

Данные сервисы (кроме frontend части) уже работают на сервере.

Swagger сервиса: http://77.91.65.153:8080/swagger-ui

Хост для запросов: http://77.91.65.153:8080

Телеграм бот для подтверждения аккаунта: https://t.me/EcofinConfirmationBot