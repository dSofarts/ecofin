package ru.ecofin.service.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferMessageDto {

  private String operationId;
  private String senderFullName;
  private String receiverChatId;
  private String amount;
}
