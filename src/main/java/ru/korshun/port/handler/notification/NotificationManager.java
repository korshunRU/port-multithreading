package ru.korshun.port.handler.notification;

import ru.korshun.port.environment.options.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationManager {

  private static volatile NotificationManager instance;

  private final Map<Type, List<NotificationListener>> observersList;

  private NotificationManager(Type[] types) {
    this.observersList = new HashMap<>();
    for (Type type : types) {
      this.observersList.put(type, new ArrayList<>());
    }
  }

  public static NotificationManager getInstance(Type[] types) {
    var localInstance = instance;
    if (localInstance == null) {
      synchronized (NotificationManager.class) {
        localInstance = instance;
        if (localInstance == null) {
          instance = localInstance = new NotificationManager(types);
        }
      }
    }
    return localInstance;
  }

  public void subscribe(Type type, NotificationListener listener) {
    var observers = observersList.get(type);
    observers.add(listener);
  }

  public void unsubscribe(Type type, NotificationListener listener) {
    var observers = observersList.get(type);
    observers.remove(listener);
  }

  public void notifyByType(Type type) {
    var observers = observersList.get(type);
    for (var observer : observers) {
      observer.update();
    }
  }

  public void notifyByStart() {
    observersList.forEach((key, value) -> value.forEach(NotificationListener::start));
  }

}
