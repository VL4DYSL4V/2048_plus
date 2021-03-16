package entity;

import enums.FieldDimension;
import util.CellGenerator;

import javax.annotation.concurrent.ThreadSafe;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@ThreadSafe
public final class Model implements Externalizable {

    private Field field;
    private BigInteger scores;
    private List<Memento> history;
    private static final int MAX_HISTORY_SIZE = 3;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    public Model(){
        this(FieldDimension.FOUR_AND_FOUR);
    }

    public Model(FieldDimension dimension){
        this.scores = BigInteger.ZERO;
        this.field = new Field(dimension);
        CellGenerator.setRandomFieldElements(field, 2);
        this.history = new LinkedList<>();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        readLock.lock();
        try{
            out.writeObject(field);
            out.writeObject(scores);
            out.writeObject(history);
        }finally {
            readLock.unlock();
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        writeLock.lock();
        try{
            this.field = (Field) in.readObject();
            this.scores = (BigInteger) in.readObject();
            @SuppressWarnings("unchecked")
            List<Memento> hist = (List<Memento>) in.readObject();
            this.history = hist;
        }finally {
            writeLock.unlock();
        }
    }

    public final class Memento {

        private final BigInteger scores;
        private final Field field;
        private final List<Memento> history;

        public Memento(BigInteger scores, Field field, List<Memento> history) {
            readLock.lock();
            try {
                this.scores = scores;
                this.field = field.copy();
                this.history = new LinkedList<>(history);
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
                    Objects.equals(field, memento.field) &&
                    Objects.equals(history, memento.history);
        }

        @Override
        public int hashCode() {
            return Objects.hash(scores, field, history);
        }

    }

    /** Method is designed to replace the entire state of this object with state of {@param another}
     **/
    public void replaceState(Model another) {
        writeLock.lock();
        try {
            Memento anotherMemento = another.save();
            this.scores = anotherMemento.scores;
            this.field = anotherMemento.field;
            this.history = anotherMemento.history;
        } finally {
            writeLock.unlock();
        }
    }

    private Memento save() {
        readLock.lock();
        try {
            return new Memento(scores, field, history);
        }finally {
            readLock.unlock();
        }
    }

    /**This method is designed to update state of this object and change
     * */
    public void updateAndSaveHistory(Field field, BigInteger scoresToAdd){
        writeLock.lock();
        try{
            if(this.field.getFieldDimension() == field.getFieldDimension()) {
                Memento memento = new Memento(this.scores, this.field, history);
                saveHistory(memento);
                this.field = field;
                this.scores = this.scores.add(scoresToAdd);
            }
        }finally {
            writeLock.unlock();
        }
    }

    private void saveHistory(Memento memento){
        writeLock.lock();
        try {
            if(history.size() == MAX_HISTORY_SIZE){
                history.remove(0);
            }
            history.add(memento);
        }finally {
            writeLock.unlock();
        }
    }

    public void restore(){
        writeLock.lock();
        try{
            if(history.isEmpty()){
                return;
            }
            int lastIndex = history.size() - 1;
            Memento last = history.remove(lastIndex);
            this.scores = last.scores;
            this.field = last.field;
        }finally {
            writeLock.unlock();
        }
    }

    public BigInteger getScores(){
        readLock.lock();
        try{
            return scores;
        }finally {
            readLock.unlock();
        }
    }

    public Field getField(){
        readLock.lock();
        try{
            return field.copy();
        }finally {
            readLock.unlock();
        }
    }

    public FieldDimension getFieldDimension(){
        readLock.lock();
        try{
            return field.getFieldDimension();
        }finally {
            readLock.unlock();
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
        }finally {
            readLock.unlock();
        }
    }
}