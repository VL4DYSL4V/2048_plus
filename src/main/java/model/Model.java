package model;

import entity.Field;
import enums.FieldDimension;
import observer.Publisher;
import observer.Subscriber;
import observer.event.EventType;
import util.CellGenerator;

import javax.annotation.concurrent.ThreadSafe;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@ThreadSafe
public final class Model implements Externalizable, Publisher {

    private Field field;
    private BigInteger scores;
    private List<Memento> history;
    private boolean gameIsOver = false;
    private static final int MAX_HISTORY_SIZE = 3;
    private Collection<Subscriber> subscribers = new HashSet<>();

    private final Lock readLock;
    private final Lock writeLock;

    public Model() {
        this(FieldDimension.FOUR_AND_FOUR);
    }

    public Model(FieldDimension dimension) {
        ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);
        this.readLock = readWriteLock.readLock();
        this.writeLock = readWriteLock.writeLock();
        this.scores = BigInteger.ZERO;
        this.field = new Field(dimension);
        CellGenerator.setRandomFieldElements(field, 2);
        this.history = new LinkedList<>();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        readLock.lock();
        try {
            out.writeObject(field);
            out.writeObject(scores);
            out.writeObject(history);
            out.writeBoolean(gameIsOver);
            out.writeObject(subscribers);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        writeLock.lock();
        try {
            this.field = (Field) in.readObject();
            this.scores = (BigInteger) in.readObject();
            @SuppressWarnings("unchecked")
            List<Memento> hist = (List<Memento>) in.readObject();
            this.history = hist;
            this.gameIsOver = in.readBoolean();
            @SuppressWarnings("unchecked")
            Collection<Subscriber> subscribers = (Collection<Subscriber>) in.readObject();
            this.subscribers = subscribers;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void subscribe(Subscriber subscriber) {
        writeLock.lock();
        try {
            subscribers.add(subscriber);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void unsubscribe(Subscriber subscriber) {
        writeLock.lock();
        try {
            subscribers.remove(subscriber);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void notifySubscribers(EventType eventType) {
        readLock.lock();
        try {
            for (Subscriber subscriber : subscribers) {
                subscriber.reactOnNotification(eventType);
            }
        } finally {
            readLock.unlock();
        }
    }

    public final class Memento {

        private final BigInteger scores;
        private final Field field;
        private final List<Memento> history;
        private final boolean gameIsOver;

        public Memento(BigInteger scores, Field field, List<Memento> history, boolean gameIsOver) {
            readLock.lock();
            try {
                this.scores = scores;
                this.field = field.copy();
                this.history = new LinkedList<>(history);
                this.gameIsOver = gameIsOver;
            } finally {
                readLock.unlock();
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Memento memento = (Memento) o;
            return gameIsOver == memento.gameIsOver &&
                    Objects.equals(scores, memento.scores) &&
                    Objects.equals(field, memento.field) &&
                    Objects.equals(history, memento.history);
        }

        @Override
        public int hashCode() {
            return Objects.hash(scores, field, history, gameIsOver);
        }
    }

    /**
     * Method is designed to replace the entire state of this object with state of {@param another}
     **/
    public void replaceState(Model another) {
        writeLock.lock();
        try {
            Memento anotherMemento = another.save();
            this.scores = anotherMemento.scores;
            this.field = anotherMemento.field;
            this.history = anotherMemento.history;
            this.gameIsOver = anotherMemento.gameIsOver;
        } finally {
            writeLock.unlock();
        }
        notifySubscribers(EventType.MODEL_CHANGED);
    }

    private Memento save() {
        readLock.lock();
        try {
            return new Memento(scores, field, history, gameIsOver);
        } finally {
            readLock.unlock();
        }
    }

    /**
     * This method is designed to update state of this object and change
     */
    public void updateAndSaveHistory(Field field, BigInteger scoresToAdd) {
        writeLock.lock();
        try {
            if (this.field.getFieldDimension() == field.getFieldDimension()) {
                Memento memento = new Memento(this.scores, this.field, history, gameIsOver);
                saveHistory(memento);
                this.field = field;
                this.scores = this.scores.add(scoresToAdd);
            }
        } finally {
            writeLock.unlock();
        }
        notifySubscribers(EventType.MODEL_CHANGED);
    }

    private void saveHistory(Memento memento) {
        writeLock.lock();
        try {
            if (history.size() == MAX_HISTORY_SIZE) {
                history.remove(0);
            }
            history.add(memento);
        } finally {
            writeLock.unlock();
        }
    }

    public void restore() {
        writeLock.lock();
        try {
            if (history.isEmpty()) {
                return;
            }
            int lastIndex = history.size() - 1;
            Memento last = history.remove(lastIndex);
            this.scores = last.scores;
            this.field = last.field;
            this.gameIsOver = last.gameIsOver;
        } finally {
            writeLock.unlock();
        }
        notifySubscribers(EventType.MODEL_CHANGED);
    }

    public BigInteger getScores() {
        readLock.lock();
        try {
            return scores;
        } finally {
            readLock.unlock();
        }
    }

    public Field getField() {
        readLock.lock();
        try {
            return field.copy();
        } finally {
            readLock.unlock();
        }
    }

    public FieldDimension getFieldDimension() {
        readLock.lock();
        try {
            return field.getFieldDimension();
        } finally {
            readLock.unlock();
        }
    }

    public boolean gameIsOver() {
        readLock.lock();
        try {
            return gameIsOver;
        } finally {
            readLock.unlock();
        }
    }

    public void setGameIsOver(boolean gameIsOver) {
        writeLock.lock();
        try {
            this.gameIsOver = gameIsOver;
            if (gameIsOver) {
                notifySubscribers(EventType.GAME_OVER);
            }
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public String toString() {
        readLock.lock();
        try {
            return "Model{" +
                    "field=" + field +
                    ", scores=" + scores +
                    ", history=" + history +
                    '}';
        } finally {
            readLock.unlock();
        }
    }

}
