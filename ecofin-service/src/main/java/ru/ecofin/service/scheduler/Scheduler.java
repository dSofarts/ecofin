package ru.ecofin.service.scheduler;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.ecofin.service.repository.OtpRepository;

@Component
@RequiredArgsConstructor
public class Scheduler {

  private final OtpRepository otpRepository;

  @Transactional
  @Scheduled(fixedRate = 3600000) // Запуск раз в час
  public void cleanupExpiredOtps() {
    otpRepository.deleteAllByExpirationBefore(LocalDateTime.now());
  }
}
