package ru.korshun.port.environment;

import ru.korshun.port.environment.options.Type;
import ru.korshun.port.handler.exception.PierNotFoundException;
import ru.korshun.port.handler.notification.NotificationManager;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@SuppressWarnings("FieldCanBeLocal")
public class Port {

  private static volatile Port instance;
  private final List<Pier> pierList;
  private final NotificationManager notificationManager;
  private final ExecutorService executorService;

  private Port(NotificationManager notificationManager, ExecutorService executorService) {
    this.pierList = initPiers();
    this.notificationManager = notificationManager;
    this.executorService = executorService;
  }

  public static Port getInstance(NotificationManager notificationManager,
                                 ExecutorService executorService) {
    var localInstance = instance;
    if (localInstance == null) {
      synchronized (Port.class) {
        localInstance = instance;
        if (localInstance == null) {
          instance = localInstance = new Port(notificationManager, executorService);
        }
      }
    }
    return localInstance;
  }

  private List<Pier> initPiers() {
    return Arrays.stream(Type.values())
        .map(item -> new Pier(this, item))
        .collect(Collectors.toList());
  }

  public void addShip(Ship ship) throws PierNotFoundException {
    Runnable task = () -> {
      var pier = getPierByType(ship.getType()).orElseThrow(PierNotFoundException::new);
      pier.addShip(ship);
    };
    executorService.execute(task);
  }

  public void deletedShip(Ship ship) {
    Runnable task = () -> notificationManager.notifyByType(ship.getType());
    executorService.execute(task);
  }

  private Optional<Pier> getPierByType(Type type) {
    return pierList.stream()
        .filter(item -> item.getType() == type)
        .findFirst();
  }

}
