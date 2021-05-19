package testUtils;

import entity.Coordinates2D;
import entity.Field;
import entity.FieldElement;
import enums.FieldDimension;

import java.util.concurrent.ThreadLocalRandom;

public final class FieldUtils {

    private static final FieldDimension FIELD_DIMENSION = FieldDimension.FOUR_AND_FOUR;

    private FieldUtils(){}

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

    //1 2 1 2
    //2 1 2 1
    //1 2 1 2
    //2 1 2 1
    public static Field getFieldWhichCannotBeMoved(){
        Field field = getEmptyField();
        for (int i = FIELD_DIMENSION.getMinY(); i <= FIELD_DIMENSION.getMaxY(); i++) {
            for (int j = FIELD_DIMENSION.getMinX(); j <= FIELD_DIMENSION.getMaxX(); j++) {
                Coordinates2D coordinates2D = new Coordinates2D(j, i);
                FieldElement fieldElement;
                if(i % 2 == 0 && j % 2 == 0 || i % 2 == 1 && j % 2 == 1){
                    fieldElement = new FieldElement(coordinates2D, 1);
                }else{
                    fieldElement = new FieldElement(coordinates2D, 2);
                }
                field.setElement(fieldElement);
            }
        }
        return field;
    }

    //x x x x
    //x x x x
    //x x x x
    //x x x x
    public static Field getFieldWithSameElements(int element) {
        Field field = getEmptyField();
        for (int i = FIELD_DIMENSION.getMinY(); i <= FIELD_DIMENSION.getMaxY(); i++) {
            for (int j = FIELD_DIMENSION.getMinX(); j <= FIELD_DIMENSION.getMaxX(); j++) {
                field.setElement(new FieldElement(new Coordinates2D(j, i), element));
            }
        }
        return field;
    }

    //1 1 2 0
    //1 0 1 2
    //0 1 1 2
    //1 1 0 2
    public static Field getFieldForRightShift(){
        Field field = getEmptyField();
        field.setElement(new FieldElement(new Coordinates2D(0, 0), 1));
        field.setElement(new FieldElement(new Coordinates2D(1, 0), 1));
        field.setElement(new FieldElement(new Coordinates2D(2, 0), 2));

        field.setElement(new FieldElement(new Coordinates2D(0, 1), 1));
        field.setElement(new FieldElement(new Coordinates2D(2, 1), 1));
        field.setElement(new FieldElement(new Coordinates2D(3, 1), 2));

        field.setElement(new FieldElement(new Coordinates2D(1, 2), 1));
        field.setElement(new FieldElement(new Coordinates2D(2, 2), 1));
        field.setElement(new FieldElement(new Coordinates2D(3, 2), 2));

        field.setElement(new FieldElement(new Coordinates2D(0, 3), 1));
        field.setElement(new FieldElement(new Coordinates2D(1, 3), 1));
        field.setElement(new FieldElement(new Coordinates2D(3, 3), 2));

        return field;
    }

    //0 2 1 1
    //2 1 0 1
    //2 1 1 0
    //2 0 1 1
    public static Field getFieldForLeftShift(){
        Field field = getEmptyField();
        field.setElement(new FieldElement(new Coordinates2D(1, 0), 2));
        field.setElement(new FieldElement(new Coordinates2D(2, 0), 1));
        field.setElement(new FieldElement(new Coordinates2D(3, 0), 1));

        field.setElement(new FieldElement(new Coordinates2D(0, 1), 2));
        field.setElement(new FieldElement(new Coordinates2D(1, 1), 1));
        field.setElement(new FieldElement(new Coordinates2D(3, 1), 1));

        field.setElement(new FieldElement(new Coordinates2D(0, 2), 2));
        field.setElement(new FieldElement(new Coordinates2D(1, 2), 1));
        field.setElement(new FieldElement(new Coordinates2D(2, 2), 1));

        field.setElement(new FieldElement(new Coordinates2D(0, 3), 2));
        field.setElement(new FieldElement(new Coordinates2D(2, 3), 1));
        field.setElement(new FieldElement(new Coordinates2D(3, 3), 1));

        return field;
    }

    //1 1 0 1
    //1 0 1 1
    //2 1 1 0
    //0 2 2 2
    public static Field getFieldForDownShift(){
        Field field = getEmptyField();
        field.setElement(new FieldElement(new Coordinates2D(0, 0), 1));
        field.setElement(new FieldElement(new Coordinates2D(1, 0), 1));
        field.setElement(new FieldElement(new Coordinates2D(3, 0), 1));

        field.setElement(new FieldElement(new Coordinates2D(0, 1), 1));
        field.setElement(new FieldElement(new Coordinates2D(2, 1), 1));
        field.setElement(new FieldElement(new Coordinates2D(3, 1), 1));

        field.setElement(new FieldElement(new Coordinates2D(0, 2), 2));
        field.setElement(new FieldElement(new Coordinates2D(1, 2), 1));
        field.setElement(new FieldElement(new Coordinates2D(2, 2), 1));

        field.setElement(new FieldElement(new Coordinates2D(1, 3), 2));
        field.setElement(new FieldElement(new Coordinates2D(2, 3), 2));
        field.setElement(new FieldElement(new Coordinates2D(3, 3), 2));

        return field;
    }

    //0 2 2 2
    //2 1 1 0
    //1 0 1 1
    //1 1 0 1
    public static Field getFieldForUpShift(){
        Field field = getEmptyField();
        field.setElement(new FieldElement(new Coordinates2D(1, 0), 2));
        field.setElement(new FieldElement(new Coordinates2D(2, 0), 2));
        field.setElement(new FieldElement(new Coordinates2D(3, 0), 2));

        field.setElement(new FieldElement(new Coordinates2D(0, 1), 2));
        field.setElement(new FieldElement(new Coordinates2D(1, 1), 1));
        field.setElement(new FieldElement(new Coordinates2D(2, 1), 1));

        field.setElement(new FieldElement(new Coordinates2D(0, 2), 1));
        field.setElement(new FieldElement(new Coordinates2D(2, 2), 1));
        field.setElement(new FieldElement(new Coordinates2D(3, 2), 1));

        field.setElement(new FieldElement(new Coordinates2D(0, 3), 1));
        field.setElement(new FieldElement(new Coordinates2D(1, 3), 1));
        field.setElement(new FieldElement(new Coordinates2D(3, 3), 1));

        return field;
    }
}
