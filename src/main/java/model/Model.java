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
public final class Model implements Publisher {

    private GameModel gameModel;
    private Collection<Subscriber> subscribers = new HashSet<>();

    public Model() {
        this(FieldDimension.FOUR_AND_FOUR);
    }

    public Model(FieldDimension dimension) {
        this.gameModel = new GameModel(dimension);
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
        gameModel.updateAndSaveHistory(field, scoresToAdd);
        notifySubscribers(EventType.MODEL_CHANGED);
    }

    public synchronized void reset() {
        gameModel.reset();
        notifySubscribers(EventType.MODEL_CHANGED);
    }

    public synchronized boolean restore() {
        boolean out = gameModel.restore();
        notifySubscribers(EventType.MODEL_CHANGED);
        return out;
    }

    public synchronized BigInteger getScores() {
        return gameModel.getScores();
    }

    public synchronized Field getField() {
        return gameModel.getField();
    }

    public synchronized FieldDimension getFieldDimension() {
        return gameModel.getFieldDimension();
    }

    public synchronized boolean gameIsOver() {
        return gameModel.gameIsOver();
    }

    public synchronized void setGameIsOver(boolean gameIsOver) {
        gameModel.setGameIsOver(gameIsOver);
        notifySubscribers(EventType.MODEL_CHANGED);
    }

    public synchronized void setGameModel(GameModel gameModel) {
        this.gameModel = gameModel;
        notifySubscribers(EventType.MODEL_CHANGED);
    }

    public synchronized GameModel getGameModel() {
        return new GameModel(gameModel);
    }

}
