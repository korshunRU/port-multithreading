package ru.korshun.port.environment;

import lombok.Getter;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings("FieldCanBeLocal")
public class Tunnel {

  private final int maxShipsInside = 5;
  private final int shipSpeedDelay = 70;
  private final int tunnelLength = 500;

  private static volatile Tunnel instance;
  private final Port port;
  private final Queue<Ship> buffer;
  private final List<Ship> tunnel;
  private final ExecutorService executorService;
  @Getter
  private final AtomicLong id = new AtomicLong();

  private Tunnel(Port port, ExecutorService executorService) {
    this.port = port;
    this.buffer = new LinkedList<>();
    this.tunnel = new CopyOnWriteArrayList<>();
    this.executorService = executorService;
  }

  public static Tunnel getInstance(Port port, ExecutorService executorService) {
    var localInstance = instance;
    if (localInstance == null) {
      synchronized (Tunnel.class) {
        localInstance = instance;
        if (localInstance == null) {
          instance = localInstance = new Tunnel(port, executorService);
        }
      }
    }
    return localInstance;
  }

  public void addShip(Ship ship) {
    if (isTunnelFull()) {
      buffer.add(ship);
      System.out.printf("%-20s > Ship: %s%n", "ADD TUNNEL BUFFER", ship);
      return;
    }
    tunnel.add(ship);
    System.out.printf("%-20s > Ship: %s%n", "ADD TUNNEL", ship);
    move(ship);
  }

  private void deleteShip(Ship ship) {
    Runnable task = () -> {
      tunnel.remove(ship);
      System.out.printf("%-20s > Ship: %s%n", "DELETE TUNNEL", ship);
      port.addShip(ship);
      if (isBufferNotEmpty()) {
        addShip(buffer.poll());
      }
    };
    executorService.execute(task);
  }

  private void move(Ship ship) {
    Runnable task = () -> {
      var start = 0;
      while (start < tunnelLength) {
        start += ship.getSpeed().getSpeed();
        try {
          TimeUnit.MILLISECONDS.sleep(shipSpeedDelay);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      deleteShip(ship);
    };
    executorService.execute(task);
  }

  private boolean isTunnelFull() {
    return tunnel.size() == maxShipsInside;
  }

  private boolean isBufferNotEmpty() {
    return buffer.size() > 0;
  }

}
