package ru.ecofin.service.aspect;

import org.aspectj.lang.annotation.Pointcut;
import ru.ecofin.service.annotation.LoggingUsed;

public class LoggingPointcut {
  @Pointcut("@annotation(loggingUsed)")
  public void logController(LoggingUsed loggingUsed) {

  }
}
