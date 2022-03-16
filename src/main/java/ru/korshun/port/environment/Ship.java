package ru.korshun.port.environment;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import ru.korshun.port.environment.options.Capacity;
import ru.korshun.port.environment.options.Speed;
import ru.korshun.port.environment.options.Type;

@Builder
@Getter
@ToString
public class Ship {

  private final Long id;
  private final Type type;
  private final Speed speed;
  private final Capacity capacity;
}
