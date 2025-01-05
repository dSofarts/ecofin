package ru.ecofin.service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.ecofin.service.repository.UserRepository;
import ru.ecofin.service.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Override
  public UserDetailsService getUserDetailsService() {
    return email -> userRepository.findUserByEmail(email).orElseThrow(
        () -> new UsernameNotFoundException("User not found with email: " + email));
  }
}
