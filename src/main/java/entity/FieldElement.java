package entity;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.Objects;

@Immutable
public final class FieldElement implements Serializable {

    private final Coordinates2D coordinates2D;
    private final int value;

    private static final long serialVersionUID = 3986465429742953863L;

    public FieldElement(Coordinates2D coordinates2D, int value) {
        this.coordinates2D = coordinates2D;
        this.value = value;
    }

    public static FieldElement zero(Coordinates2D coordinates2D) {
        return new FieldElement(coordinates2D, 0);
    }

    public Coordinates2D getCoordinates2D() {
        return coordinates2D;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "FieldElement{" +
                "coordinates2D=" + coordinates2D +
                ", value=" + value + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldElement that = (FieldElement) o;
        return value == that.value &&
                Objects.equals(coordinates2D, that.coordinates2D);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinates2D, value);
    }
}
