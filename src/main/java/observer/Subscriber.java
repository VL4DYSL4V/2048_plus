package observer;

import observer.event.EventType;

public interface Subscriber {

    void reactOnNotification(EventType eventType);

}
