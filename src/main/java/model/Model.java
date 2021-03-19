package model;

import entity.Field;
import enums.FieldDimension;
import observer.Publisher;
import observer.Subscriber;
import observer.event.EventType;
import util.CellGenerator;

import javax.annotation.concurrent.ThreadSafe;
import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@ThreadSafe
public final class Model implements Externalizable, Publisher {

    private static final int MAX_HISTORY_SIZE = 3;

    private Field field;
    private BigInteger scores;
    private List<Memento> history;
    private boolean gameIsOver = false;

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

    public final class Memento implements Serializable {

        private final BigInteger scores;
        private final Field field;

        private static final long serialVersionUID = 5761738890498234L;

        public Memento(BigInteger scores, Field field) {
            readLock.lock();
            try {
                this.scores = scores;
                this.field = field.copy();
            } finally {
                readLock.unlock();
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Memento memento = (Memento) o;
            return Objects.equals(scores, memento.scores) &&
                    Objects.equals(field, memento.field);
        }

        @Override
        public int hashCode() {
            return Objects.hash(scores, field, gameIsOver);
        }

    }

    private Memento save() {
        readLock.lock();
        try {
            return new Memento(scores, field);
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
                Memento memento = save();
                saveHistory(memento);
                this.field = field.copy();
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

    public void reset() {
        writeLock.lock();
        try {
            scores = BigInteger.ZERO;
            field.reset();
            CellGenerator.setRandomFieldElements(field, 2);
            history.clear();
            gameIsOver = false;
        } finally {
            writeLock.unlock();
        }
        notifySubscribers(EventType.MODEL_CHANGED);
    }

    public boolean restore() {
        writeLock.lock();
        try {
            if (history.isEmpty()) {
                return false;
            }
            int lastIndex = history.size() - 1;
            Memento last = history.remove(lastIndex);
            this.scores = last.scores;
            this.field = last.field;
        } finally {
            writeLock.unlock();
        }
        notifySubscribers(EventType.MODEL_CHANGED);
        return true;
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
        } finally {
            writeLock.unlock();
        }
        notifySubscribers(EventType.MODEL_CHANGED);

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
