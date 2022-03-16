package ru.korshun.port.environment;

import ru.korshun.port.environment.options.Capacity;
import ru.korshun.port.environment.options.Speed;
import ru.korshun.port.environment.options.Type;
import ru.korshun.port.handler.exception.PierNotFoundException;
import ru.korshun.port.handler.notification.NotificationListener;
import ru.korshun.port.handler.notification.NotificationManager;

import java.util.Random;

@SuppressWarnings("FieldCanBeLocal")
public class ShipGenerator implements NotificationListener {

  private final Tunnel tunnel;
  private final Type type;

  private ShipGenerator(
      Tunnel tunnel,
      Type type,
      NotificationManager notificationManager) {
    this.tunnel = tunnel;
    this.type = type;
    notificationManager.subscribe(type, this);
  }

  @Override
  public void update() {
    tunnel.addShip(generateShip());
  }

  @Override
  public void start() {
    for (int x = 0; x < type.getCount(); x++) {
      try {
        tunnel.addShip(generateShip());
      } catch (PierNotFoundException e) {
        throw new PierNotFoundException("Error creating Ship");
      }
    }
  }

  public static void create(Tunnel tunnel, Type type,
                            NotificationManager notificationManager) {
    new ShipGenerator(tunnel, type, notificationManager);
  }

  private Ship generateShip() {
    return Ship.builder()
        .id(tunnel.getId().incrementAndGet())
        .capacity(getRandomType(Capacity.values()))
        .speed(getRandomType(Speed.values()))
        .type(type)
        .build();
  }

  private <T> T getRandomType(T[] values) {
    var random = new Random();
    var randomId = random.nextInt((values.length - 1) + 1) + 1;
    return values[randomId - 1];
  }

}
