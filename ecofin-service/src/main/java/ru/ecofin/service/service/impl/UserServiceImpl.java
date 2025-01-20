package ru.ecofin.service.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.ecofin.service.dto.kafka.ConfirmationDto;
import ru.ecofin.service.dto.response.UserResponseDto;
import ru.ecofin.service.dto.response.UserResponseDto.UserInfo;
import ru.ecofin.service.entity.User;
import ru.ecofin.service.exception.NotFoundException;
import ru.ecofin.service.repository.UserRepository;
import ru.ecofin.service.security.JWTService;
import ru.ecofin.service.service.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final JWTService jwtService;
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
    User user = getUserByPhone(confirmationDto.getPhone());
    user.setConfirmed(true);
    user.setChatId(confirmationDto.getChatId());
    log.info("Successfully confirmed user: {}, operationId: {}", user.getId(),
        confirmationDto.getOperationId());
  }

  @Override
  @SneakyThrows
  public UserResponseDto getUserByToken(String token) {
    String phone = jwtService.extractPhone(token);
    User user = getUserByPhone(phone);
    return UserResponseDto.builder()
        .id(user.getId().toString())
        .phone(user.getPhone())
        .userInfo(UserInfo.builder()
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .middleName(user.getMiddleName())
            .birthDate(user.getBirthdate())
            .build())
        .build();
  }

  @Override
  public User getUserByPhone(String phone) {
    return userRepository.findUserByPhone(phone).orElseThrow(
        () -> new NotFoundException(phone, "User not found with phone"));
  }
}
