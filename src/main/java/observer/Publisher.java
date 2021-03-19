package observer;

import observer.event.EventType;

public interface Publisher {

    void subscribe(Subscriber subscriber);

    void unsubscribe(Subscriber subscriber);

    void notifySubscribers(EventType eventType);

}
