package ru.ecofin.service.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.ecofin.service.dto.kafka.ConfirmationDto;

public interface UserService {

  UserDetailsService getUserDetailsService();

  void confirmationUser(ConfirmationDto confirmationDto);
}
