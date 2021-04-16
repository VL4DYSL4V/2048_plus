package observer;

public interface Publisher<EventType> {

    void subscribe(Subscriber<EventType> subscriber);

    void unsubscribe(Subscriber<EventType> subscriber);

    void notifySubscribers(EventType eventType);

}
