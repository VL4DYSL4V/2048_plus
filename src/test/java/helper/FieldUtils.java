package helper;

import entity.Coordinates2D;
import entity.Field;
import entity.FieldElement;
import enums.FieldDimension;

import java.util.concurrent.ThreadLocalRandom;

public class FieldUtils {

    private static final FieldDimension FIELD_DIMENSION = FieldDimension.FOUR_AND_FOUR;

    public static Field getEmptyField() {
        return new Field(FIELD_DIMENSION);
    }

    // 1 5 0 0
    // 2 0 0 0
    // 0 0 1 0
    // 0 0 3 2
    public static Field getFieldWithData() {
        Field field = getEmptyField();
        FieldElement fieldElement = new FieldElement(new Coordinates2D(0, 0), 1);
        field.setElement(fieldElement);
        fieldElement = new FieldElement(new Coordinates2D(0, 1), 2);
        field.setElement(fieldElement);
        fieldElement = new FieldElement(new Coordinates2D(2, 3), 3);
        field.setElement(fieldElement);
        fieldElement = new FieldElement(new Coordinates2D(1, 0), 5);
        field.setElement(fieldElement);
        fieldElement = new FieldElement(new Coordinates2D(2, 2), 1);
        field.setElement(fieldElement);
        fieldElement = new FieldElement(new Coordinates2D(3, 3), 2);
        field.setElement(fieldElement);
        return field;
    }

    public static Field getFullFiled() {
        Field field = getEmptyField();
        for (int i = FIELD_DIMENSION.getMinY(); i <= FIELD_DIMENSION.getMaxY(); i++) {
            for (int j = FIELD_DIMENSION.getMinX(); j <= FIELD_DIMENSION.getMaxX(); j++) {
                field.setElement(new FieldElement(new Coordinates2D(j, i), ThreadLocalRandom.current().nextInt(15) + 1));
            }
        }
        return field;
    }
}
