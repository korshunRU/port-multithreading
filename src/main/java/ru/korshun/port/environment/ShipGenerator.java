package ru.korshun.port.environment;

import ru.korshun.port.environment.options.Capacity;
import ru.korshun.port.environment.options.Speed;
import ru.korshun.port.environment.options.Type;
import ru.korshun.port.handler.notification.NotificationListener;
import ru.korshun.port.handler.notification.NotificationManager;

import java.util.Random;

/**
 * This class generates a new ship objects.
 *
 * When a free pier appears in the {@link ru.korshun.port.environment.Port} this
 * generator receive a notification from port (by the update method) and generates
 * a new {@link ru.korshun.port.environment.Ship} object of the same
 * {@link ru.korshun.port.environment.options.Type}.
 */
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
    for (int x = 0; x < type.getCount(); x++)
      tunnel.addShip(generateShip());
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
