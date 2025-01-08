package ru.ecofin.service.service.impl;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.ecofin.service.dto.kafka.CodeDto;
import ru.ecofin.service.entity.Otp;
import ru.ecofin.service.entity.User;
import ru.ecofin.service.kafka.KafkaProducer;
import ru.ecofin.service.repository.OtpRepository;
import ru.ecofin.service.service.OtpService;
import ru.ecofin.service.utils.EncryptionUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

  private final OtpRepository otpRepository;
  private final KafkaProducer kafkaProducer;
  @Value("${encryption.key}")
  private String secretKey;

  @Override
  @SneakyThrows
  public Otp generateOtp(User user) {
    String otp = String.valueOf((int) (Math.random() * 900000) + 100000);
    LocalDateTime expirationDate = LocalDateTime.now().plusMinutes(5);
    Otp otpEntity = Otp.builder()
        .user(user)
        .otpCode(EncryptionUtils.encrypt(otp, secretKey))
        .expiration(expirationDate)
        .build();
    otpRepository.save(otpEntity);

    kafkaProducer.send(CodeDto.builder()
        .operationId(otpEntity.getId().toString())
        .code(otp)
        .expirationDate(expirationDate)
        .chatId(user.getChatId()).build());
    return otpEntity;
  }

  @Override
  @SneakyThrows
  @Transactional
  public boolean verifyOtp(String operationId, String otp) {
    Optional<Otp> optionalOtp = otpRepository.findOtpById(UUID.fromString(operationId));
    if (optionalOtp.isPresent()) {
      Otp otpEntity = optionalOtp.get();
      if (otpEntity.getExpiration().isAfter(LocalDateTime.now())
          && otp.equals(EncryptionUtils.decrypt(otpEntity.getOtpCode(), secretKey))
          && !otpEntity.isUsed()) {
        otpEntity.setUsed(true);
        return true;
      }
    }
    return false;
  }
}
