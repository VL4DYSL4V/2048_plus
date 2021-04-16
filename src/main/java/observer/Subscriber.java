package observer;

public interface Subscriber <EventType>{

    void reactOnNotification(EventType eventType);

}
