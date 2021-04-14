package model;

import entity.Field;
import enums.FieldDimension;
import observer.Publisher;
import observer.Subscriber;
import observer.event.EventType;

import javax.annotation.concurrent.ThreadSafe;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashSet;

@ThreadSafe
public final class GameModel implements Publisher {

    private GameData gameData;
    private Collection<Subscriber> subscribers = new HashSet<>();

    public GameModel() {
        this(FieldDimension.FOUR_AND_FOUR);
    }

    public GameModel(FieldDimension dimension) {
        this.gameData = new GameData(dimension);
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
        for (Subscriber subscriber : subscribers) {
            subscriber.reactOnNotification(eventType);
        }
    }

    public synchronized void updateAndSaveHistory(Field field, BigInteger scoresToAdd) {
        gameData.updateAndSaveHistory(field, scoresToAdd);
        notifySubscribers(EventType.GAME_DATA_CHANGED);
    }

    public synchronized void reset() {
        gameData.reset();
        notifySubscribers(EventType.GAME_DATA_CHANGED);
    }

    public synchronized boolean restore() {
        boolean out = gameData.restore();
        notifySubscribers(EventType.GAME_DATA_CHANGED);
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
        notifySubscribers(EventType.GAME_DATA_CHANGED);
    }

    public synchronized void setGameData(GameData gameData) {
        this.gameData = gameData;
        notifySubscribers(EventType.GAME_DATA_CHANGED);
    }

    public synchronized GameData getGameData() {
        return new GameData(gameData);
    }

}
