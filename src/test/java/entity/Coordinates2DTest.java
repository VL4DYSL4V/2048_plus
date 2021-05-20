package entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Coordinates2DTest {

    @Test
    void testWrongCoordinates(){
        assertThrows(IllegalArgumentException.class, () -> new Coordinates2D(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> new Coordinates2D(0, -1));
    }

    @Test
    void testRightCoordinates(){
        assertDoesNotThrow(() -> new Coordinates2D(0, 0));
    }
}