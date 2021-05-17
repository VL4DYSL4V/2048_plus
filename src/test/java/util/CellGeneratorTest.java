package util;

import entity.Field;
import helper.FieldUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellGeneratorTest {

    @Test
    void setRandomFieldElements() {
        Field field = FieldUtils.getEmptyField();
        assertDoesNotThrow(() -> CellGenerator.setRandomFieldElements(field, 3), "Should be able to insert element to empty field");
        assertEquals(13, field.getAvailableCoordinates().size(), "Wrong amount of slots are available");

        Field fullField = FieldUtils.getFullFiled();
        assertThrows(IllegalArgumentException.class, () -> CellGenerator.setRandomFieldElements(fullField, 1),
                "Should not be able to insert element to full field");
    }

    @Test
    void setRandomFieldElement() {
        Field field = FieldUtils.getEmptyField();
        assertDoesNotThrow(() -> CellGenerator.setRandomFieldElement(field), "Should be able to insert element to empty field");
        Field fullField = FieldUtils.getFullFiled();
        assertThrows(IllegalArgumentException.class, () -> CellGenerator.setRandomFieldElement(fullField),
                "Should not be able to insert element to full field");
    }
}