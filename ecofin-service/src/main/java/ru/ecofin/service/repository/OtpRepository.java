package ru.ecofin.service.repository;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.ecofin.service.entity.Otp;

public interface OtpRepository extends JpaRepository<Otp, UUID> {
  Optional<Otp> findOtpById(UUID uuid);
  void deleteAllByExpirationBefore(ZonedDateTime time);
}
