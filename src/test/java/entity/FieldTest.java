package entity;

import enums.FieldDimension;
import testUtils.FieldUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class FieldTest {

    @Test
    void reset() {
        Field zero = FieldUtils.getEmptyField();
        Field withData = FieldUtils.getFieldWithData();

        withData.reset();
        assertEquals(zero, withData, "Field must be empty");
    }

    @Test
    void getAvailableCoordinates() {
        Field field = FieldUtils.getFieldWithData();
        Collection<Coordinates2D> availableCoordinates = new HashSet<>(field.getAvailableCoordinates());
        // 1 5 0 0
        // 2 0 0 0
        // 0 0 1 0
        // 0 0 3 2
        Set<Coordinates2D> expectedCoordinates = new HashSet<Coordinates2D>() {{
            add(new Coordinates2D(0, 2));
            add(new Coordinates2D(0, 3));
            add(new Coordinates2D(1, 1));
            add(new Coordinates2D(1, 2));
            add(new Coordinates2D(1, 3));
            add(new Coordinates2D(2, 0));
            add(new Coordinates2D(2, 1));
            add(new Coordinates2D(3, 0));
            add(new Coordinates2D(3, 1));
            add(new Coordinates2D(3, 2));
        }};
        assertEquals(expectedCoordinates, availableCoordinates, "Returning invalid available coordinates");
    }

    @Test
    void copy() {
        Field field = FieldUtils.getFieldWithData();
        Field copy = field.copy();
        assertNotSame(field, copy, "Method must return different object");
        assertEquals(field, copy, "Method returns not a copy");
    }

    @Nested
    class SetElement {

        @Test
        void setNull() {
            Field field = FieldUtils.getEmptyField();
            assertThrows(NullPointerException.class, () -> field.setElement(null),
                    "Shouldn't be able to set null");
        }

        @Test
        void setNormally() {
            Field field = FieldUtils.getEmptyField();
            Coordinates2D coordinates2D = new Coordinates2D(1, 3);
            field.setElement(new FieldElement(coordinates2D, 1));

            assertNotEquals(field, FieldUtils.getEmptyField(), "Field must not be empty after insertion");

            Collection<Coordinates2D> availableCoordinates = field.getAvailableCoordinates();
            FieldDimension fieldDimension = field.getFieldDimension();

            assertEquals(fieldDimension.getHeight() * fieldDimension.getWidth() - 1, availableCoordinates.size(),
                    "Only 1 slot must be unavailable");
            assertFalse(availableCoordinates.contains(coordinates2D),
                    "Slot must be unavailable after inserting element");
        }

        @Test
        void setToUnavailableSlot() {
            Field field = FieldUtils.getFieldWithData();
            Coordinates2D coordinates2D = new Coordinates2D(2, 3);
            assertThrows(IllegalArgumentException.class,
                    () -> field.setElement(new FieldElement(coordinates2D, 1)),
                    "Shouldn't be able to insert element to unavailable slot");
        }


    }

    @Nested
    class RowInteraction {

        @Test
        void getRowWithInvalidIndex() {
            Field field = FieldUtils.getFieldWithData();
            assertThrows(IllegalArgumentException.class, () -> field.getRow(-1));
        }

        @Test
        void testReturningCopy() {
            Field field = FieldUtils.getFieldWithData();
            List<FieldElement> row = field.getRow(0);
            row.set(3, new FieldElement(new Coordinates2D(3, 0), 17));
            assertNotEquals(field.getRow(0), row, "Row getter must return copy of actual row");
        }

        @Test
        void setNullRow() {
            Field field = FieldUtils.getFieldWithData();
            assertThrows(NullPointerException.class, () -> field.setRow(null, 0));
        }

        @Test
        void setRowNormally() {
            Field field = FieldUtils.getFieldWithData();
            List<FieldElement> row = field.getRow(0);
            row.set(3, new FieldElement(new Coordinates2D(3, 0), 17));
            field.setRow(row, 0);

            List<FieldElement> changedRow = new ArrayList<>(row);
            changedRow.set(0, new FieldElement(new Coordinates2D(0, 0), 9));
            assertNotEquals(changedRow, field.getRow(0),
                    "Row must be copied after setting");
        }
    }

    @Nested
    class ColumnInteraction {

        @Test
        void getColumnWithInvalidIndex() {
            Field field = FieldUtils.getFieldWithData();
            assertThrows(IllegalArgumentException.class, () -> field.getColumn(-1));
        }

        @Test
        void testReturningCopy() {
            Field field = FieldUtils.getFieldWithData();
            List<FieldElement> column = field.getColumn(0);
            column.set(3, new FieldElement(new Coordinates2D(0, 3), 17));
            assertNotEquals(field.getColumn(0), column, "Column getter must return copy of actual column");
        }

        @Test
        void setNullColumn() {
            Field field = FieldUtils.getFieldWithData();
            assertThrows(NullPointerException.class, () -> field.setColumn(null, 0));
        }

        @Test
        void setColumnNormally() {
            Field field = FieldUtils.getFieldWithData();
            List<FieldElement> column = field.getColumn(0);
            column.set(3, new FieldElement(new Coordinates2D(0,3), 17));
            field.setColumn(column, 0);

            List<FieldElement> changedColumn = new ArrayList<>(column);
            changedColumn.set(0, new FieldElement(new Coordinates2D(0, 0), 9));
            assertNotEquals(changedColumn, field.getColumn(0),
                    "Column must be copied after setting");
        }
    }
}