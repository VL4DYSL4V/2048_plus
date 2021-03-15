package util;

import entity.Coordinates2D;
import entity.Field;
import entity.FieldElement;
import enums.FieldDimension;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

public final class CellGenerator {

    private CellGenerator(){

    }

    public static void setRandomFieldElements(Field field, int amount){
        for(int i = 0; i < amount; i++){
            setRandomFieldElement(field);
        }
    }

    public static void setRandomFieldElement(Field field){
        field.setElement(generateFieldElement(field.getFieldDimension(), field.unavailableCoordinates()));
    }

    public static FieldElement generateFieldElement(FieldDimension fieldDimension, Collection<Coordinates2D> unavailableElements){
        Coordinates2D coordinates2D = calculateCoordinates(unavailableElements, fieldDimension);
        int value = calculateValue();
        return new FieldElement(coordinates2D, value);
    }


    public static Collection<FieldElement> generateFieldElements(int amount, FieldDimension fieldDimension, Collection<Coordinates2D> unavailableCoordinates){
        Collection<Coordinates2D> unavailableCopy = new LinkedList<>(unavailableCoordinates);
        Collection<FieldElement> out = new HashSet<>();
        while(out.size() != amount) {
            FieldElement fieldElement = generateFieldElement(fieldDimension, unavailableCopy);
            if(! out.contains(fieldElement)
                    && ! unavailableCoordinates.contains(fieldElement.getCoordinates2D())) {
                out.add(fieldElement);
                unavailableCopy.add(fieldElement.getCoordinates2D());
            }
        }
        return out;
    }

    private static Coordinates2D calculateCoordinates(Collection<Coordinates2D> unavailableElements, FieldDimension fieldDimension) {
        Coordinates2D coordinates2D;
        for (int i = 0; i < 5; i++) {
            coordinates2D = CoordinateGenerationUtils
                    .simpleCoordinatesCalculation(fieldDimension.getMaxX(), fieldDimension.getMaxY());
            boolean appropriate = true;
            for (Coordinates2D coordinates : unavailableElements) {
                if (coordinates.equals(coordinates2D)) {
                    appropriate = false;
                    break;
                }
            }
            if (appropriate) {
                return coordinates2D;
            }
        }
        return CoordinateGenerationUtils.advancedCoordinatesCalculation(unavailableElements, fieldDimension);
    }

    private static int calculateValue() {
        int chance = ThreadLocalRandom.current().nextInt(100);
        return (chance < 20) ? 2 : 1;
    }
}
