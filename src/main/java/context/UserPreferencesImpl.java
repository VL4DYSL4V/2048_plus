package context;

import enums.FieldDimension;
import observer.Publisher;
import observer.Subscriber;
import observer.event.EventType;
import view.theme.Theme;

import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;

public final class UserPreferencesImpl implements UserPreferences, Publisher {

    private Locale locale;
    private Theme theme;
    private FieldDimension fieldDimension;
    private final Collection<Subscriber> subscribers = new HashSet<>();

    public UserPreferencesImpl(Locale locale, Theme theme, FieldDimension fieldDimension) {
        this.locale = locale;
        this.theme = theme;
        this.fieldDimension = fieldDimension;
    }

    @Override
    public synchronized Locale getLocale() {
        return locale;
    }

    @Override
    public synchronized boolean setLocale(Locale locale) {
        if(!Objects.equals(locale, this.locale)) {
            this.locale = locale;
            notifySubscribers(EventType.USER_PREFERENCES_CHANGED);
            return true;
        }
        return false;
    }

    @Override
    public synchronized Theme getTheme() {
        return theme;
    }

    @Override
    public synchronized boolean setTheme(Theme theme) {
        if(!Objects.equals(theme, this.theme)){
            this.theme = theme;
            notifySubscribers(EventType.USER_PREFERENCES_CHANGED);
            return true;
        }
        return false;
    }

    @Override
    public FieldDimension getFieldDimension() {
        return fieldDimension;
    }

    @Override
    public boolean setFieldDimension(FieldDimension fieldDimension) {
        if(! Objects.equals(fieldDimension, this.fieldDimension)){
            this.fieldDimension = fieldDimension;
            notifySubscribers(EventType.USER_PREFERENCES_CHANGED);
            return true;
        }
        return false;
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
