package entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FieldElementTest {

    @Test
    void testConstructor(){
        assertThrows(NullPointerException.class, () -> new FieldElement(null, 0));
    }

    @Test
    void zero() {
        FieldElement fieldElement = FieldElement.zero(new Coordinates2D(0, 0));
        assertEquals(0, fieldElement.getValue());
    }

    @Test
    void isEmpty() {
        FieldElement fieldElement = new FieldElement(new Coordinates2D(0, 0), 0);
        assertTrue(fieldElement.isEmpty());

        fieldElement = new FieldElement(new Coordinates2D(0, 0), 1);
        assertFalse(fieldElement.isEmpty());
    }

}