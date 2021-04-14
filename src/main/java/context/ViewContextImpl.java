package context;

import observer.Publisher;
import observer.Subscriber;
import observer.event.EventType;
import view.theme.Theme;

import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;

public final class ViewContextImpl implements ViewContext, Publisher {

    private Locale locale;
    private Theme theme;
    private final Collection<Subscriber> subscribers = new HashSet<>();

    public ViewContextImpl(Locale locale, Theme theme) {
        this.locale = locale;
        this.theme = theme;
    }

    @Override
    public synchronized Locale getCurrentLocale() {
        return locale;
    }

    @Override
    public synchronized void setLocale(Locale locale) {
        this.locale = locale;
        notifySubscribers(EventType.VIEW_CONTEXT_CHANGED);
    }

    @Override
    public synchronized Theme getCurrentTheme() {
        return theme;
    }

    @Override
    public synchronized void setTheme(Theme theme) {
        this.theme = theme;
        notifySubscribers(EventType.VIEW_CONTEXT_CHANGED);
    }

    @Override
    public synchronized void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public synchronized void unsubscribe(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    @Override
    public synchronized void notifySubscribers(EventType eventType) {
        for (Subscriber subscriber: subscribers){
            subscriber.reactOnNotification(eventType);
        }
    }
}
