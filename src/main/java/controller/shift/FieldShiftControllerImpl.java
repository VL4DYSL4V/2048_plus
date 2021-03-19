package controller.shift;

import entity.Coordinates2D;
import entity.Field;
import entity.FieldElement;
import enums.Direction;
import model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import util.CellGenerator;
import util.PowerOfTwoHolder;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller("fieldShiftController")
public final class FieldShiftControllerImpl implements FieldShiftController {

    private final Model model;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Object lock = new Object();

    @Autowired
    public FieldShiftControllerImpl(Model model) {
        this.model = model;
    }

    @Override
    public void shift(Direction direction) {
        synchronized (lock) {
            if (model.gameIsOver()) {
                return;
            }
            executorService.execute(() -> {
                Field field = model.getField();
                Field copy = field.copy();
                BigInteger scores = shiftField(direction, field);
                if (Objects.equals(field, copy)) {
                    if (checkIfEnd(field)) {
                        model.setGameIsOver(true);
                        cancelShifts();
                    }
                } else {
                    CellGenerator.setRandomFieldElement(field);
                    model.updateAndSaveHistory(field, scores);
                }
            });
        }
    }

    @Override
    public void cancelShifts() {
        synchronized (lock) {
            executorService.shutdownNow();
            executorService = Executors.newSingleThreadExecutor();
        }
    }

    private boolean checkIfEnd(Field field) {
        Field copy = field.copy();
        for (Direction direction : Direction.values()) {
            shiftField(direction, copy);
            if (!Objects.equals(field, copy)) {
                return false;
            }
        }
        return true;
    }

    private BigInteger shiftField(Direction direction, Field field) {
        switch (direction) {
            case UP:
            case DOWN:
                return verticalShift(direction, field);
            case LEFT:
            case RIGHT:
            default:
                return horizontalShift(direction, field);
        }
    }

    private BigInteger verticalShift(Direction direction, Field field) {
        BigInteger out = BigInteger.ZERO;
        for (int x = 0; x < field.getFieldDimension().getWidth(); x++) {
            List<FieldElement> column = field.getColumn(x);
            removeZerosAndShiftCoordinates(column, direction, field);
            out = out.add(mergeSameValues(column, direction, field));
            refillColumnWithZeros(x, direction, column, field);
            field.setColumn(column, x);
        }
        return out;
    }

    private BigInteger horizontalShift(Direction direction, Field field) {
        BigInteger out = BigInteger.ZERO;
        for (int y = 0; y < field.getFieldDimension().getHeight(); y++) {
            List<FieldElement> row = field.getRow(y);
            removeZerosAndShiftCoordinates(row, direction, field);
            out = out.add(mergeSameValues(row, direction, field));
            refillRowWithZeros(y, direction, row, field);
            field.setRow(row, y);
        }
        return out;
    }

    private BigInteger mergeSameValues(List<FieldElement> rowOrColumnWithoutZeros, Direction direction, Field field) {
        BigInteger sumOfMerged = BigInteger.ZERO;
        switch (direction) {
            case UP:
            case LEFT:
                for (int i = 1; i < rowOrColumnWithoutZeros.size(); i++) {
                    FieldElement prev = rowOrColumnWithoutZeros.get(i - 1);
                    FieldElement curr = rowOrColumnWithoutZeros.get(i);
                    if (curr.getValue() == prev.getValue()) {
                        int neuValue = prev.getValue() + 1;
                        rowOrColumnWithoutZeros.set(i - 1, new FieldElement(prev.getCoordinates2D(), neuValue));
                        rowOrColumnWithoutZeros.set(i, FieldElement.zero(curr.getCoordinates2D()));
                        sumOfMerged = sumOfMerged.add(PowerOfTwoHolder.get(neuValue));
                    }
                }
                break;
            case DOWN:
            case RIGHT:
                for (int i = rowOrColumnWithoutZeros.size() - 2; i >= 0; i--) {
                    FieldElement prev = rowOrColumnWithoutZeros.get(i + 1);
                    FieldElement curr = rowOrColumnWithoutZeros.get(i);
                    if (curr.getValue() == prev.getValue()) {
                        int neuValue = prev.getValue() + 1;
                        rowOrColumnWithoutZeros.set(i + 1, new FieldElement(curr.getCoordinates2D(), neuValue));
                        rowOrColumnWithoutZeros.set(i, FieldElement.zero(curr.getCoordinates2D()));
                        sumOfMerged = sumOfMerged.add(PowerOfTwoHolder.get(neuValue));
                    }
                }
                break;
        }
        removeZerosAndShiftCoordinates(rowOrColumnWithoutZeros, direction, field);
        return sumOfMerged;
    }

    private void refillColumnWithZeros(int x, Direction direction, List<FieldElement> column, Field field) {
        if (direction == Direction.UP) {
            for (int y = column.size(); column.size() < field.getFieldDimension().getHeight(); y++) {
                Coordinates2D coordinates2D = new Coordinates2D(x, y);
                column.add(FieldElement.zero(coordinates2D));
            }
        } else {
            int iterAmount = field.getFieldDimension().getHeight() - column.size();
            for (int y = 0; y < iterAmount; y++) {
                Coordinates2D coordinates2D = new Coordinates2D(x, y);
                column.add(y, FieldElement.zero(coordinates2D));
            }
        }
    }

    private void refillRowWithZeros(int y, Direction direction, List<FieldElement> row, Field field) {
        if (direction == Direction.LEFT) {
            for (int x = row.size(); row.size() < field.getFieldDimension().getWidth(); x++) {
                Coordinates2D coordinates2D = new Coordinates2D(x, y);
                row.add(FieldElement.zero(coordinates2D));
            }
        } else {
            int iterAmount = field.getFieldDimension().getWidth() - row.size();
            for (int x = 0; x < iterAmount; x++) {
                Coordinates2D coordinates2D = new Coordinates2D(x, y);
                row.add(x, FieldElement.zero(coordinates2D));
            }
        }
    }

    private void removeZerosAndShiftCoordinates(List<FieldElement> columnOrRow, Direction direction, Field field) {
        columnOrRow.removeIf(element -> element.getValue() == 0);
        switch (direction) {
            case UP:
                for (int y = 0; y < columnOrRow.size(); y++) {
                    FieldElement e = columnOrRow.get(y);
                    if (e.getCoordinates2D().getY() != y) {
                        columnOrRow.set(y, new FieldElement(new Coordinates2D(e.getCoordinates2D().getX(), y), e.getValue()));
                    }
                }
                break;
            case DOWN:
                for (int y = 0; y < columnOrRow.size(); y++) {
                    FieldElement e = columnOrRow.get(y);
                    int expectedY = field.getFieldDimension().getMaxY() - (columnOrRow.size() - (y + 1));
                    if (e.getCoordinates2D().getY() != expectedY) {
                        columnOrRow.set(y, new FieldElement(new Coordinates2D(e.getCoordinates2D().getX(), expectedY), e.getValue()));
                    }
                }
                break;
            case LEFT:
                for (int x = 0; x < columnOrRow.size(); x++) {
                    FieldElement e = columnOrRow.get(x);
                    if (e.getCoordinates2D().getX() != x) {
                        columnOrRow.set(x, new FieldElement(new Coordinates2D(x, e.getCoordinates2D().getY()), e.getValue()));
                    }
                }
                break;
            case RIGHT:
                for (int x = 0; x < columnOrRow.size(); x++) {
                    FieldElement e = columnOrRow.get(x);
                    int expectedX = field.getFieldDimension().getMaxX() - (columnOrRow.size() - (x + 1));
                    if (e.getCoordinates2D().getX() != expectedX) {
                        columnOrRow.set(x, new FieldElement(new Coordinates2D(expectedX, e.getCoordinates2D().getY()), e.getValue()));
                    }
                }
                break;
        }
    }
}
