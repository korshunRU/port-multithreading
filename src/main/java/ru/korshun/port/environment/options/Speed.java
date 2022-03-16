package ru.korshun.port.environment.options;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Speed {

  LOW(2),
  MEDIUM(4),
  HIGH(7);

  private final int speed;

}