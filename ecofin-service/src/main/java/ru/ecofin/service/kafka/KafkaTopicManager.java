package ru.ecofin.service.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Component;

@Component
public class KafkaTopicManager {

  @Value("${topics.confirmation-topic}")
  private String confirmationTopicName;

  @Value("${topics.send-code-topic}")
  private String sendCodeTopicName;

  @Value("${topics.send-transfer-message-topic}")
  private String sendTransferMessageTopic;

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

  @Bean
  public NewTopic sendTransferMessageTopic() {
    return TopicBuilder.name(sendTransferMessageTopic).partitions(3)
        .replicas(2).build();
  }
}
