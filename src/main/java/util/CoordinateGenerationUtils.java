package util;

import entity.Coordinates2D;
import enums.FieldDimension;

import java.util.Collection;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public final class CoordinateGenerationUtils {

    private CoordinateGenerationUtils() {
    }

    public static Coordinates2D simpleCoordinatesCalculation(int maxDesiredX, int maxDesiredY) {
        Random random = ThreadLocalRandom.current();
        return new Coordinates2D(random.nextInt(maxDesiredX + 1),
                random.nextInt(maxDesiredY + 1));
    }

    public static Coordinates2D advancedCoordinatesCalculation(Collection<Coordinates2D> redundantCoords, FieldDimension fieldDimension) {
        Coordinates2D coordinates;
        int chance = ThreadLocalRandom.current().nextInt(4);
        switch (chance) {
            case 1:
                coordinates = generateLeftBottomCorner(fieldDimension);
                while (redundantCoords.contains(coordinates)) {
                    coordinates = walkRightAndUp(coordinates, fieldDimension.getMinX(), fieldDimension);
                    if (coordinates.getX() == fieldDimension.getMaxX() && coordinates.getY() == fieldDimension.getMinY()) {
                        return coordinates;
                    }
                }
                break;
            case 2:
                coordinates = generateRightUpperCorner(fieldDimension);
                while (redundantCoords.contains(coordinates)) {
                    coordinates = walkLeftAndDown(coordinates, fieldDimension.getMaxX(), fieldDimension);
                    if (coordinates.getX() == fieldDimension.getMinX() && coordinates.getY() == fieldDimension.getMaxY()) {
                        return coordinates;
                    }
                }
                break;
            case 3:
                coordinates = generateRightBottomCorner(fieldDimension);
                while (redundantCoords.contains(coordinates)) {
                    coordinates = walkLeftAndUp(coordinates, fieldDimension.getMaxX(), fieldDimension);
                    if (coordinates.getX() == fieldDimension.getMinX() && coordinates.getY() == fieldDimension.getMinY()) {
                        return coordinates;
                    }
                }
                break;
            default:
                coordinates = generateLeftUpperCorner(fieldDimension);
                while (redundantCoords.contains(coordinates)) {
                    coordinates = walkRightAndDown(coordinates, fieldDimension.getMinX(), fieldDimension);
                    if (coordinates.getX() == fieldDimension.getMaxX() && coordinates.getY() == fieldDimension.getMaxY()) {
                        return coordinates;
                    }
                }
                break;
        }
        return coordinates;
    }

    public static Coordinates2D walkRightAndDown(Coordinates2D from, int where2StartNewRowX, FieldDimension fieldDimension) {
        if (from.getX() < fieldDimension.getMaxX()) {
            return walkRight(from);
        } else if (from.getY() < fieldDimension.getMaxY()) {
            return new Coordinates2D(where2StartNewRowX, from.getY() + 1);
        } else {
            throw new IllegalStateException(from.toString());
        }
    }

    public static Coordinates2D walkLeftAndDown(Coordinates2D from, int where2StartNewRowX, FieldDimension fieldDimension) {
        if (from.getX() > fieldDimension.getMinX()) {
            return walkLeft(from);
        } else if (from.getY() < fieldDimension.getMaxY()) {
            return new Coordinates2D(where2StartNewRowX, from.getY() + 1);
        } else {
            throw new IllegalStateException(from.toString());
        }
    }

    public static Coordinates2D walkLeftAndUp(Coordinates2D from, int where2StartNewRowX, FieldDimension fieldDimension) {
        if (from.getX() > fieldDimension.getMinX()) {
            return walkLeft(from);
        } else if (from.getY() > fieldDimension.getMinY()) {
            return new Coordinates2D(where2StartNewRowX, from.getY() - 1);
        } else {
            throw new IllegalStateException(from.toString());
        }
    }

    public static Coordinates2D walkRightAndUp(Coordinates2D from, int where2StartNewRowX, FieldDimension fieldDimension) {
        if (from.getX() < fieldDimension.getMaxX()) {
            return walkRight(from);
        } else if (from.getY() > fieldDimension.getMinY()) {
            return new Coordinates2D(where2StartNewRowX, from.getY() - 1);
        } else {
            throw new IllegalStateException(from.toString());
        }
    }

    public static Coordinates2D walkRight(Coordinates2D from) {
        return new Coordinates2D(from.getX() + 1, from.getY());
    }

    public static Coordinates2D walkLeft(Coordinates2D from) {
        return new Coordinates2D(from.getX() - 1, from.getY());
    }

    public static Coordinates2D walkUp(Coordinates2D from) {
        return new Coordinates2D(from.getX(), from.getY() - 1);
    }

    public static Coordinates2D walkDown(Coordinates2D from) {
        return new Coordinates2D(from.getX(), from.getY() + 1);
    }

    public static Coordinates2D generateLeftUpperCorner(FieldDimension fieldDimension) {
        return new Coordinates2D(fieldDimension.getMinX(), fieldDimension.getMinY());
    }

    public static Coordinates2D generateRightBottomCorner(FieldDimension fieldDimension) {
        return new Coordinates2D(fieldDimension.getMaxX(), fieldDimension.getMaxY());
    }

    public static Coordinates2D generateRightUpperCorner(FieldDimension fieldDimension) {
        return new Coordinates2D(fieldDimension.getMaxX(), fieldDimension.getMinY());
    }

    public static Coordinates2D generateLeftBottomCorner(FieldDimension fieldDimension) {
        return new Coordinates2D(fieldDimension.getMinX(), fieldDimension.getMaxY());
    }

}
