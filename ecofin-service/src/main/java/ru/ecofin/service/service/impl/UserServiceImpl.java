package ru.ecofin.service.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.ecofin.service.dto.kafka.ConfirmationDto;
import ru.ecofin.service.entity.User;
import ru.ecofin.service.repository.UserRepository;
import ru.ecofin.service.service.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Override
  public UserDetailsService getUserDetailsService() {
    return phone -> userRepository.findUserByPhone(phone).orElseThrow(
        () -> new UsernameNotFoundException("User not found with phone: " + phone));
  }

  @Override
  @SneakyThrows
  @Transactional
  public void confirmationUser(ConfirmationDto confirmationDto){
    User user = userRepository.findUserByPhone(confirmationDto.getPhone()).orElseThrow(
        () -> new UsernameNotFoundException("User not found with phone: " + confirmationDto.getPhone()));
    user.setConfirmed(true);
    user.setChatId(confirmationDto.getChatId());
    log.info("Successfully confirmed user: {}, operationId: {}", user.getId(),
        confirmationDto.getOperationId());
  }
}
