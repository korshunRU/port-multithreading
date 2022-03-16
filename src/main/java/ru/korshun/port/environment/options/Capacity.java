package ru.korshun.port.environment.options;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Capacity {

  LOW(100),
  MEDIUM(250),
  HIGH(400);

  private final int capacity;



}