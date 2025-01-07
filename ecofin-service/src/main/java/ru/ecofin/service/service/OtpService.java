package ru.ecofin.service.service;

import ru.ecofin.service.entity.Otp;
import ru.ecofin.service.entity.User;

public interface OtpService {

  Otp generateOtp(User user);

  boolean verifyOtp(String operationId, String otp);
}
