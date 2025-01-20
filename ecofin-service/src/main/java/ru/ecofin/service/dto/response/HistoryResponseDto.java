package ru.ecofin.service.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class HistoryResponseDto {

  private String userId;
  private String wallet;
  private List<Transaction> incomeTransactions;
  private BigDecimal sumIncome;
  private List<Transaction> expenseTransactions;
  private BigDecimal sumExpense;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Transaction {

    private String transactionId;
    private String category;
    private BigDecimal amount;
    private ZonedDateTime transactionDate;
  }
}
