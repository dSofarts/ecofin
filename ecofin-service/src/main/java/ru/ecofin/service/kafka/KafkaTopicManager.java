package ru.ecofin.service.kafka;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

@Component
public class KafkaTopicManager {

  @Value("${topics.confirmation-topic}")
  private String confirmationTopicName;

  @Value("${topics.send-code-topic}")
  private String sendCodeTopicName;

  @Bean
  public NewTopic confirmationTopic() {
    return TopicBuilder.name(confirmationTopicName).partitions(3)
        .replicas(2).build();
  }

  @Bean
  public NewTopic sendCodeTopic() {
    return TopicBuilder.name(sendCodeTopicName).partitions(3)
        .replicas(2).build();
  }
}
