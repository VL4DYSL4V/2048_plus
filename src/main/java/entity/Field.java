package entity;

import enums.FieldDimension;

import javax.annotation.concurrent.ThreadSafe;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@ThreadSafe
@SuppressWarnings("Duplicates") // It says, there is a duplicate code in setColumn and
// setRow methods, because of list iteration and locking-unlocking
public final class Field implements Serializable {

    private final List<List<FieldElement>> fieldElements = new ArrayList<>();
    private final FieldDimension fieldDimension;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    private static final long serialVersionUID = 1726351751L;

    private Field(FieldDimension fieldDimension) {
        this.fieldDimension = fieldDimension;
    }

    public static Field fromDimension(FieldDimension fieldDimension) {
        Field field = new Field(fieldDimension);
        field.reset();
        return field;
    }

    public void reset() {
        writeLock.lock();
        try{
            fieldElements.clear();
            for (int y = 0; y < fieldDimension.getHeight(); y++) {
                ArrayList<FieldElement> row = new ArrayList<>();
                fieldElements.add(row);
                for (int x = 0; x < fieldDimension.getWidth(); x++) {
                    Coordinates2D coordinates2D = new Coordinates2D(x, y);
                    row.add(FieldElement.zero(coordinates2D));
                }
            }
        }finally {
            writeLock.unlock();
        }
    }

    public void setElement(FieldElement fieldElement) {
        writeLock.lock();
        try {
            int y = fieldElement.getCoordinates2D().getY();
            int x = fieldElement.getCoordinates2D().getX();
            if (fieldElements.get(y).get(x).getValue() == 0) {
                fieldElements.get(y).set(x, fieldElement);
            } else {
                throw new IllegalArgumentException("Such element already exists");
            }
        }finally {
            writeLock.unlock();
        }
    }

    public List<Coordinates2D> unavailableCoordinates() {
        List<Coordinates2D> out = new ArrayList<>();
        readLock.lock();
        try {
            for (List<FieldElement> row : fieldElements) {
                for (FieldElement el : row) {
                    if (el.getValue() != 0) {
                        out.add(el.getCoordinates2D());
                    }
                }
            }
        }finally {
            readLock.unlock();
        }
        return out;
    }

    public List<FieldElement> getRow(int index) {
        readLock.lock();
        try{
            return new ArrayList<>(fieldElements.get(index));
        }finally {
            readLock.unlock();
        }
    }

    public void setRow(List<FieldElement> neuRow, int index) {
        writeLock.lock();
        try {
            List<FieldElement> row = fieldElements.get(index);
            for (int x = 0; x < row.size(); x++) {
                row.set(x, neuRow.get(x));
            }
        }finally {
            writeLock.unlock();
        }
    }

    public List<FieldElement> getColumn(int index) {
        List<FieldElement> out = new ArrayList<>();
        readLock.lock();
        try{
            for (List<FieldElement> fieldElement : fieldElements) {
                out.add(fieldElement.get(index));
            }
            return out;
        }finally {
            readLock.unlock();
        }
    }

    public void setColumn(List<FieldElement> neuColumn, int index) {
        writeLock.lock();
        try {
            for (int y = 0; y < fieldElements.size(); y++) {
                List<FieldElement> row = fieldElements.get(y);
                row.set(index, neuColumn.get(y));
            }
        }finally {
            writeLock.unlock();
        }
    }

    public Field copy() {
        Field out = new Field(fieldDimension);
        readLock.lock();
        try {
            for (int y = 0; y < fieldDimension.getHeight(); y++) {
                out.setRow(getRow(y), y);
            }
        }finally {
            readLock.unlock();
        }
        return out;
    }

    public FieldDimension getFieldDimension() {
        return fieldDimension;
    }

    @Override
    public String toString() {
        readLock.lock();
        try {
            return "Field{" +
                    "fieldElements=" + fieldElements +
                    ", fieldDimension=" + fieldDimension +
                    '}';
        }finally {
            readLock.unlock();
        }
    }
}
