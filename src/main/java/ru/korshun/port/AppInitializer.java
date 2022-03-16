package ru.korshun.port;

import ru.korshun.port.environment.Port;
import ru.korshun.port.environment.ShipGenerator;
import ru.korshun.port.environment.Tunnel;
import ru.korshun.port.environment.options.Type;
import ru.korshun.port.handler.notification.NotificationManager;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("FieldCanBeLocal")
public class AppInitializer {

  private static volatile AppInitializer instance;
  private final Port port;
  private final Tunnel tunnel;
  private final NotificationManager notificationManager;
  private final ExecutorService executorService;

  private AppInitializer() {
    this.notificationManager = NotificationManager.getInstance(Type.values());
    this.executorService = Executors.newCachedThreadPool();
    this.port = Port.getInstance(notificationManager, executorService);
    this.tunnel = Tunnel.getInstance(port, executorService);
    crateShipGenerators();
  }

  public static AppInitializer getInstance() {
    var localInstance = instance;
    if (localInstance == null) {
      synchronized (AppInitializer.class) {
        localInstance = instance;
        if (localInstance == null) {
          instance = localInstance = new AppInitializer();
        }
      }
    }
    return localInstance;
  }

  private void crateShipGenerators() {
    Arrays.stream(Type.values())
        .forEach(item -> ShipGenerator.create(tunnel, item, notificationManager));
  }

  public void run() {
    notificationManager.notifyByStart();
  }

}
