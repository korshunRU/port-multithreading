package ru.korshun.port.handler.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PierNotFoundException extends RuntimeException {
  public PierNotFoundException(String message) {
    super(message);
  }
}
