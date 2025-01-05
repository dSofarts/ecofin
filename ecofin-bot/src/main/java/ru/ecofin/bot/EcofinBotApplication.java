package ru.ecofin.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
@EnableConfigurationProperties
public class EcofinBotApplication {
  public static void main(String[] args) {
    SpringApplication.run(EcofinBotApplication.class, args);
  }
}
