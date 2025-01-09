package ru.ecofin.bot;

import java.util.TimeZone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.kafka.annotation.EnableKafka;

@Slf4j
@EnableKafka
@SpringBootApplication
@EnableConfigurationProperties
public class EcofinBotApplication {
  public static void main(String[] args) {
    TimeZone.setDefault(TimeZone.getTimeZone("Europe/Moscow"));
    SpringApplication.run(EcofinBotApplication.class, args);
    log.info("""
        
        :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        ::               ECOFIN BOT SERVICE STARTED                    ::
        :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::""");
  }
}
