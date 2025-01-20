package ru.ecofin.service.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.ecofin.service.dto.kafka.ConfirmationDto;
import ru.ecofin.service.dto.response.UserResponseDto;
import ru.ecofin.service.entity.User;

public interface UserService {

  UserDetailsService getUserDetailsService();

  void confirmationUser(ConfirmationDto confirmationDto);

  UserResponseDto getUserByToken(String token);

  User getUserByPhone(String phone);
}
