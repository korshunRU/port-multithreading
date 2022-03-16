package ru.korshun.port.environment.options;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Type {

  CARGO(4),
  MILITARY(3),
  PASSENGER(3);

  private final int count;
}