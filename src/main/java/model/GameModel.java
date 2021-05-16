package model;

import entity.Field;
import enums.FieldDimension;
import observer.Publisher;
import observer.Subscriber;
import observer.event.ModelEvent;

import javax.annotation.concurrent.ThreadSafe;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

@ThreadSafe
public final class GameModel implements Publisher<ModelEvent> {

    private GameData gameData;
    private final Collection<Subscriber<ModelEvent>> subscribers = new HashSet<>();

    public GameModel() {
        this(FieldDimension.FOUR_AND_FOUR);
    }

    public GameModel(FieldDimension dimension) {
        this.gameData = new GameData(dimension);
    }

    @Override
    public synchronized void subscribe(Subscriber<ModelEvent> subscriber) {
        Objects.requireNonNull(subscriber);
        subscribers.add(subscriber);
    }

    @Override
    public synchronized void unsubscribe(Subscriber<ModelEvent> subscriber) {
        subscribers.remove(subscriber);
    }

    @Override
    public synchronized void notifySubscribers(ModelEvent eventType) {
        for (Subscriber<ModelEvent> subscriber : subscribers) {
            subscriber.reactOnNotification(eventType);
        }
    }

    public synchronized void updateAndSaveHistory(Field field, BigInteger scoresToAdd) {
        gameData.updateAndSaveHistory(field, scoresToAdd);
        notifySubscribers(ModelEvent.GAME_DATA_CHANGED);
    }

    public synchronized void reset() {
        gameData.reset();
        notifySubscribers(ModelEvent.GAME_DATA_CHANGED);
    }

    public synchronized boolean restore() {
        boolean out = gameData.restore();
        notifySubscribers(ModelEvent.GAME_DATA_CHANGED);
        return out;
    }

    public synchronized BigInteger getScores() {
        return gameData.getScores();
    }

    public synchronized Field getField() {
        return gameData.getField();
    }

    public synchronized FieldDimension getFieldDimension() {
        return gameData.getFieldDimension();
    }

    public synchronized boolean gameIsOver() {
        return gameData.gameIsOver();
    }

    public synchronized void setGameIsOver(boolean gameIsOver) {
        gameData.setGameIsOver(gameIsOver);
        notifySubscribers(ModelEvent.GAME_DATA_CHANGED);
    }

    public void setGameData(GameData gameData) {
        Objects.requireNonNull(gameData);
        synchronized(this) {
            GameData prev = this.gameData;
            this.gameData = gameData;
            if (prev.getFieldDimension() != gameData.getFieldDimension()) {
                notifySubscribers(ModelEvent.FIELD_DIMENSION_CHANGED);
            }
            notifySubscribers(ModelEvent.GAME_DATA_CHANGED);
        }
    }

    public synchronized GameData getGameData() {
        return new GameData(gameData);
    }

}
