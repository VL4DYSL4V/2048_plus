package entity;

import enums.FieldDimension;

import javax.annotation.concurrent.NotThreadSafe;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NotThreadSafe
public final class Field implements Serializable {

    private final List<List<FieldElement>> fieldElements = new ArrayList<>();
    private final FieldDimension fieldDimension;

    private static final long serialVersionUID = 1726351751L;

    public Field(FieldDimension fieldDimension) {
        Objects.requireNonNull(fieldDimension);
        this.fieldDimension = fieldDimension;
        reset();
    }

    public void reset() {
        fieldElements.clear();
        for (int y = 0; y < fieldDimension.getHeight(); y++) {
            ArrayList<FieldElement> row = new ArrayList<>();
            fieldElements.add(row);
            for (int x = 0; x < fieldDimension.getWidth(); x++) {
                Coordinates2D coordinates2D = new Coordinates2D(x, y);
                row.add(FieldElement.zero(coordinates2D));
            }
        }
    }

    public void setElement(FieldElement fieldElement) {
        Objects.requireNonNull(fieldElement);
        requireCoordinatesWithinBounds(fieldElement.getCoordinates2D());
        int y = fieldElement.getY();
        int x = fieldElement.getX();
        if (fieldElements.get(y).get(x).isEmpty()) {
            fieldElements.get(y).set(x, fieldElement);
        } else {
            throw new IllegalArgumentException("Such element already exists");
        }
    }

    private void requireCoordinatesWithinBounds(Coordinates2D coordinates2D){
        requireRowIndexWithinBounds(coordinates2D.getY());
        requireColumnIndexWithinBounds(coordinates2D.getX());
    }

    public List<Coordinates2D> getAvailableCoordinates() {
        List<Coordinates2D> out = new ArrayList<>();
        for (List<FieldElement> row : fieldElements) {
            for (FieldElement el : row) {
                if (el.isEmpty()) {
                    out.add(el.getCoordinates2D());
                }
            }
        }
        return out;
    }

    public List<FieldElement> getRow(int index) {
        requireRowIndexWithinBounds(index);
        return new ArrayList<>(fieldElements.get(index));
    }

    public void setRow(List<FieldElement> neuRow, int index) {
        Objects.requireNonNull(neuRow);
        requireRowIndexWithinBounds(index);
        List<FieldElement> row = fieldElements.get(index);
        for (int x = 0; x < row.size(); x++) {
            row.set(x, neuRow.get(x));
        }
    }

    public List<FieldElement> getColumn(int index) {
        requireColumnIndexWithinBounds(index);
        List<FieldElement> out = new ArrayList<>();
        for (List<FieldElement> fieldElement : fieldElements) {
            out.add(fieldElement.get(index));
        }
        return out;
    }

    public void setColumn(List<FieldElement> neuColumn, int index) {
        Objects.requireNonNull(neuColumn);
        requireColumnIndexWithinBounds(index);
        for (int y = 0; y < fieldElements.size(); y++) {
            List<FieldElement> row = fieldElements.get(y);
            row.set(index, neuColumn.get(y));
        }
    }

    private void requireRowIndexWithinBounds(int index){
        if(! (index >= fieldDimension.getMinY() && index <= fieldDimension.getMaxY())){
            throw new IllegalArgumentException("Invalid index: " + index);
        }
    }

    private void requireColumnIndexWithinBounds(int index){
        if(! (index >= fieldDimension.getMinX() && index <= fieldDimension.getMaxX())){
            throw new IllegalArgumentException("Invalid index: " + index);
        }
    }

    public Field copy() {
        Field out = new Field(fieldDimension);
        for (int y = 0; y < fieldDimension.getHeight(); y++) {
            out.setRow(getRow(y), y);
        }
        return out;
    }

    public FieldDimension getFieldDimension() {
        return fieldDimension;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return Objects.equals(fieldElements, field.fieldElements) &&
                fieldDimension == field.fieldDimension;

    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldElements, fieldDimension);
    }

    @Override
    public String toString() {
        return "Field{" +
                "fieldElements=" + fieldElements +
                ", fieldDimension=" + fieldDimension +
                '}';

    }
}
