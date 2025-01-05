package ru.ecofin.bot.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeDto {

  private String operationId;
  private String code;
  private String chatId;
  private LocalDateTime expirationDate;
}
