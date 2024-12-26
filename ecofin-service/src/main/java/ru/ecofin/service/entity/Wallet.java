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
public class Wallet {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;
  @Column(nullable = false)
  private String walletName;
  @Column(nullable = false)
  @Builder.Default
  private BigDecimal balance = BigDecimal.ZERO;
  @Column(nullable = false)
  @Builder.Default
  private LocalDateTime created = LocalDateTime.now();
  @Column(nullable = false)
  @Builder.Default
  private boolean isOpen = true;
}
