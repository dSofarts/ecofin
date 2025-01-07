package ru.ecofin.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transfer {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  @ManyToOne
  @JoinColumn(name = "sender_wallet_id", nullable = false)
  private Wallet senderWallet;
  @ManyToOne
  @JoinColumn(name = "receiver_wallet_id", nullable = false)
  private Wallet receiverWallet;
  @Column(nullable = false, precision = 15, scale = 2)
  private BigDecimal amount;
  @Column(nullable = false)
  @Builder.Default
  private LocalDateTime transferDate = LocalDateTime.now();
  private String description;
}
