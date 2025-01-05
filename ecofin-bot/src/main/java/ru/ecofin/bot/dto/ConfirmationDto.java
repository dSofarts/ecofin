package ru.ecofin.bot.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmationDto {

  private String operationId;
  private String phone;
  private String chatId;
  private LocalDateTime confirmationTime;
}
