package util;

import entity.Coordinates2D;
import entity.Field;
import entity.FieldElement;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class CellGenerator {

    private CellGenerator() {

    }

    public static void setRandomFieldElements(Field field, int amount) {
        for (int i = 0; i < amount; i++) {
            setRandomFieldElement(field);
        }
    }

    public static void setRandomFieldElement(Field field) {
        List<Coordinates2D> availableCoordinates2DS = field.getAvailableCoordinates();
        int randomIndex = ThreadLocalRandom.current().nextInt(availableCoordinates2DS.size());
        Coordinates2D coordinates2D = availableCoordinates2DS.get(randomIndex);
        field.setElement(new FieldElement(coordinates2D, calculateValue()));
    }

    private static int calculateValue() {
        int chance = ThreadLocalRandom.current().nextInt(100);
        return (chance < 20) ? 2 : 1;
    }
}
