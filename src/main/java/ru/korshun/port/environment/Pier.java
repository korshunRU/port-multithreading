package ru.korshun.port.environment;

import lombok.Getter;
import ru.korshun.port.environment.options.Type;

import java.util.List;
import java.util.concurrent.*;

@SuppressWarnings("FieldCanBeLocal")
public class Pier {

  private final int uploadSpeedDelay = 20;

  @Getter
  private final Type type;
  private final List<Ship> list;
  private final Port port;

  public Pier(Port port, Type type) {
    this.port = port;
    this.type = type;
    this.list = new CopyOnWriteArrayList<>();
  }

  private boolean isListFull() {
    return list.size() == type.getCount();
  }

  public void addShip(Ship ship) {
    if (!isListFull()) {
      list.add(ship);
      System.out.printf("%-20s > Ship: %s%n", "ADD PORT", ship);
      unloadShip(ship);
    }
  }

  private void deleteShip(Ship ship) {
    if (list.size() > 0) {
      list.remove(ship);
      System.out.printf("%-20s > Ship: %s%n", "DELETE PORT", ship);
      port.deletedShip(ship);
    }
  }

  private void unloadShip(Ship ship) {
    var count = ship.getCapacity().getCapacity();
    while (count > 0) {
      count--;
      // System.out.printf("\rShip %s, capacity: %d", ship, count);
      try {
        TimeUnit.MILLISECONDS.sleep(uploadSpeedDelay);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    deleteShip(ship);
  }

}
