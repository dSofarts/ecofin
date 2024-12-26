package ru.ecofin.service.service;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {

  UserDetailsService getUserDetailsService();
}
