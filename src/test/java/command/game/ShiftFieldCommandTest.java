package command.game;

import entity.Coordinates2D;
import entity.Field;
import entity.FieldElement;
import enums.Direction;
import enums.FieldDimension;
import handler.CommandHandler;
import mock.ThisThreadCommandHandler;
import model.GameModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import testUtils.FieldUtils;

import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShiftFieldCommandTest {

    private CommandHandler commandHandler;
    private GameModel gameModel;
    private ShiftFieldCommand shiftFieldCommand;

    @AfterEach
    void tearDown() {
        commandHandler.shutdown();
    }

    @BeforeEach
    void setup() {
        commandHandler = spy(new ThisThreadCommandHandler());
        gameModel = new GameModel();
        shiftFieldCommand = new ShiftFieldCommand(commandHandler, gameModel);
    }

    @Test
    void testSetNullAsParam(){
        assertThrows(NullPointerException.class, () -> shiftFieldCommand.setParam(null));
    }

    @Test
    void testImpossibleShift() {
        Field field = FieldUtils.getFieldWhichCannotBeMoved();
        Field beforeShift = field.copy();
        gameModel.updateAndSaveHistory(field, BigInteger.ZERO);
        for (Direction direction : Direction.values()) {
            shiftFieldCommand.setParam(direction);
            shiftFieldCommand.execute();

            assertEquals(beforeShift, gameModel.getField());
            assertEquals(BigInteger.ZERO, gameModel.getScores());
            assertTrue(gameModel.gameIsOver());
            gameModel.setGameIsOver(false);
        }
        verify(commandHandler, times(Direction.values().length)).execute(any());
    }

    @Test
    void nullConstructorArgTest() {
        assertThrows(NullPointerException.class, () -> new ShiftFieldCommand(null, gameModel));
        assertThrows(NullPointerException.class, () -> new ShiftFieldCommand(commandHandler, null));
    }

    private void conductTest(Field expected, Field field, BigInteger expectedScores) {
        List<Coordinates2D> expectedAvailable = expected.getAvailableCoordinates();
        List<Coordinates2D> actualAvailable = field.getAvailableCoordinates();
        expectedAvailable.removeAll(actualAvailable);
        assertEquals(1, expectedAvailable.size());
        assertEquals(expectedScores, gameModel.getScores());
        assertFalse(gameModel.gameIsOver());
    }

    void performShifting(Field field, Direction direction) {
        gameModel.updateAndSaveHistory(field, BigInteger.ZERO);
        shiftFieldCommand.setParam(direction);
        shiftFieldCommand.execute();
        verify(commandHandler).execute(any());
    }

    @Nested
    class TestRightShift {

        private Field getFieldWithTwosOnLastTwoColumns(FieldDimension fieldDimension) {
            Field expected = FieldUtils.getEmptyField();
            for (int y = fieldDimension.getMinY(); y <= fieldDimension.getMaxY(); y++) {
                for (int x = 2; x <= fieldDimension.getMaxX(); x++) {
                    Coordinates2D coordinates2D = new Coordinates2D(x, y);
                    FieldElement fieldElement = new FieldElement(coordinates2D, 2);
                    expected.setElement(fieldElement);
                }
            }
            return expected;
        }

        private void requireLastTwoColumnsConsistOfTwos(Field field) {
            for (int x = 2; x <= field.getFieldDimension().getMaxX(); x++) {
                List<FieldElement> column = field.getColumn(x);
                for (FieldElement fieldElement : column) {
                    assertEquals(2, fieldElement.getValue());
                }
            }
        }

        @Test
        void testLeftShift() {
            Field field = FieldUtils.getFieldForRightShift();
            performShifting(field, Direction.RIGHT);
            Field expected = getFieldWithTwosOnLastTwoColumns(field.getFieldDimension());
            field = gameModel.getField();
            requireLastTwoColumnsConsistOfTwos(field);
            conductTest(expected, field, new BigInteger("16"));
        }

        @Test
        void testShiftRightWithAllSameElements() {
            Field field = FieldUtils.getFieldWithSameElements(1);
            Field expected = getFieldWithTwosOnLastTwoColumns(field.getFieldDimension());
            performShifting(field, Direction.RIGHT);
            field = gameModel.getField();
            requireLastTwoColumnsConsistOfTwos(field);
            conductTest(expected, field, new BigInteger("32"));
        }
    }

    @Nested
    class TestLeftShift {

        private Field getFieldWithTwosOnFirstTwoColumns(FieldDimension fieldDimension) {
            Field expected = FieldUtils.getEmptyField();
            for (int y = fieldDimension.getMinY(); y <= fieldDimension.getMaxY(); y++) {
                for (int x = fieldDimension.getMinX(); x <= 1; x++) {
                    Coordinates2D coordinates2D = new Coordinates2D(x, y);
                    FieldElement fieldElement = new FieldElement(coordinates2D, 2);
                    expected.setElement(fieldElement);
                }
            }
            return expected;
        }

        private void requireFirstTwoColumnsConsistOfTwos(Field field) {
            for (int x = field.getFieldDimension().getMinX(); x <= 1; x++) {
                List<FieldElement> column = field.getColumn(x);
                for (FieldElement fieldElement : column) {
                    assertEquals(2, fieldElement.getValue());
                }
            }
        }

        @Test
        void testLeftShift() {
            Field field = FieldUtils.getFieldForLeftShift();
            performShifting(field, Direction.LEFT);
            Field expected = getFieldWithTwosOnFirstTwoColumns(field.getFieldDimension());
            field = gameModel.getField();
            requireFirstTwoColumnsConsistOfTwos(field);
            conductTest(expected, field, new BigInteger("16"));
        }

        @Test
        void testShiftLeftWithAllSameElements() {
            Field field = FieldUtils.getFieldWithSameElements(1);
            Field expected = getFieldWithTwosOnFirstTwoColumns(field.getFieldDimension());
            performShifting(field, Direction.LEFT);
            field = gameModel.getField();
            requireFirstTwoColumnsConsistOfTwos(field);
            conductTest(expected, field, new BigInteger("32"));
        }
    }

    @Nested
    class TestDownShift {

        private Field getFieldWithTwosOnLastTwoRows(FieldDimension fieldDimension) {
            Field expected = FieldUtils.getEmptyField();
            for (int y = 2; y <= fieldDimension.getMaxY(); y++) {
                for (int x = fieldDimension.getMinX(); x <= fieldDimension.getMaxX(); x++) {
                    Coordinates2D coordinates2D = new Coordinates2D(x, y);
                    FieldElement fieldElement = new FieldElement(coordinates2D, 2);
                    expected.setElement(fieldElement);
                }
            }
            return expected;
        }

        private void requireLastTwoRowsConsistOfTwos(Field field) {
            for (int y = 2; y <= field.getFieldDimension().getMaxY(); y++) {
                List<FieldElement> row = field.getRow(y);
                for (FieldElement fieldElement : row) {
                    assertEquals(2, fieldElement.getValue());
                }
            }
        }

        @Test
        void testDownShift() {
            Field field = FieldUtils.getFieldForDownShift();
            performShifting(field, Direction.DOWN);
            Field expected = getFieldWithTwosOnLastTwoRows(field.getFieldDimension());
            field = gameModel.getField();
            requireLastTwoRowsConsistOfTwos(field);
            conductTest(expected, field, new BigInteger("16"));
        }

        @Test
        void testShiftDownWithAllSameElements() {
            Field field = FieldUtils.getFieldWithSameElements(1);
            Field expected = getFieldWithTwosOnLastTwoRows(field.getFieldDimension());
            performShifting(field, Direction.DOWN);
            field = gameModel.getField();
            requireLastTwoRowsConsistOfTwos(field);
            conductTest(expected, field, new BigInteger("32"));
        }
    }

    @Nested
    class TestUpShift {

        private Field getFieldWithTwosOnFirstTwoRows(FieldDimension fieldDimension) {
            Field expected = FieldUtils.getEmptyField();
            for (int y = 0; y < 2; y++) {
                for (int x = fieldDimension.getMinX(); x <= fieldDimension.getMaxX(); x++) {
                    Coordinates2D coordinates2D = new Coordinates2D(x, y);
                    FieldElement fieldElement = new FieldElement(coordinates2D, 2);
                    expected.setElement(fieldElement);
                }
            }
            return expected;
        }

        private void requireFirstTwoRowsConsistOfTwos(Field field) {
            for (int y = 0; y < 2; y++) {
                List<FieldElement> row = field.getRow(y);
                for (FieldElement fieldElement : row) {
                    assertEquals(2, fieldElement.getValue());
                }
            }
        }

        @Test
        void testUpShift() {
            Field field = FieldUtils.getFieldForUpShift();
            performShifting(field, Direction.UP);
            Field expected = getFieldWithTwosOnFirstTwoRows(field.getFieldDimension());
            field = gameModel.getField();
            requireFirstTwoRowsConsistOfTwos(field);
            conductTest(expected, field, new BigInteger("16"));
        }

        @Test
        void testShiftUpWithAllSameElements() {
            Field field = FieldUtils.getFieldWithSameElements(1);
            Field expected = getFieldWithTwosOnFirstTwoRows(field.getFieldDimension());
            performShifting(field, Direction.UP);
            field = gameModel.getField();
            requireFirstTwoRowsConsistOfTwos(field);
            conductTest(expected, field, new BigInteger("32"));
        }
    }
}