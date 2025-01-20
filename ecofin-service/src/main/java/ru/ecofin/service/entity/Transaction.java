package ru.ecofin.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ecofin.service.entity.enums.TransactionType;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  @ManyToOne
  @JoinColumn(name = "wallet_id", nullable = false)
  private Wallet wallet;
  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 10)
  private TransactionType transactionType;
  @Column(nullable = false, precision = 15, scale = 2)
  private BigDecimal amount;
  @Column
  private String description;
  @Column(nullable = false)
  @Builder.Default
  private ZonedDateTime transactionDate = ZonedDateTime.now();
}

