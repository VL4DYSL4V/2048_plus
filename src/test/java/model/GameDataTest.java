package model;

import entity.Coordinates2D;
import entity.Field;
import entity.FieldElement;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class GameDataTest {

    @Test
    void testConsistence() throws IOException, ClassNotFoundException {
        GameData gameData = new GameData();
        Field field = gameData.getField();
        field.reset();

        field.setElement(new FieldElement(new Coordinates2D(1, 1), 10));
        gameData.updateAndSaveHistory(field, BigInteger.ONE);

        byte[] serialized1 = serialize(gameData);
        byte[] serialized2 = serialize(gameData);
        GameData gameData1 = deserialize(serialized1);
        GameData gameData2 = deserialize(serialized2);

        assertEquals(gameData, gameData1);
        assertEquals(gameData, gameData2);
    }

    private byte[] serialize(GameData gameData) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        gameData.writeExternal(objectOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private GameData deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        GameData gameData = new GameData();
        gameData.readExternal(objectInputStream);
        return gameData;
    }

    @Test
    void updateAndSaveHistory() {
        GameData gameData = new GameData();
        Field field = gameData.getField();
        field.reset();

        field.setElement(new FieldElement(new Coordinates2D(1, 1), 10));
        gameData.updateAndSaveHistory(field, BigInteger.ONE);

        assertEquals(field, gameData.getField(), "Field must be updated");
        assertEquals(BigInteger.ONE, gameData.getScores(), "Scores must be updated");
    }

    @Test
    void reset() {
        GameData gameData = new GameData();
        Field field = gameData.getField();
        field.reset();

        field.setElement(new FieldElement(new Coordinates2D(1, 1), 10));
        gameData.updateAndSaveHistory(field, BigInteger.ONE);
        field.setElement(new FieldElement(new Coordinates2D(2, 3), 14));
        gameData.updateAndSaveHistory(field, BigInteger.ONE);
        field.setElement(new FieldElement(new Coordinates2D(0, 1), 2));
        gameData.updateAndSaveHistory(field, BigInteger.ONE);
        field.setElement(new FieldElement(new Coordinates2D(1, 2), 4));
        gameData.updateAndSaveHistory(field, BigInteger.ONE);

        gameData.reset();
        assertEquals(14, gameData.getField().getAvailableCoordinates().size(),
                "Invalid amount of unoccupied slots");
        assertEquals(BigInteger.ZERO, gameData.getScores(), "Invalid scores");
    }

    @Test
    void getField() {
        GameData gameData = new GameData();
        Field field = gameData.getField();
        field.reset();
        gameData.updateAndSaveHistory(field, BigInteger.ZERO);
        field.setElement(new FieldElement(new Coordinates2D(1, 1), 10));
        assertNotEquals(field, gameData.getField(), "Returning field should be copied");
    }

    @Test
    void restore() {
        GameData gameData = new GameData();
        Field field = gameData.getField();
        field.reset();
        gameData.updateAndSaveHistory(field, BigInteger.ZERO);

        field.setElement(new FieldElement(new Coordinates2D(1, 1), 10));
        gameData.updateAndSaveHistory(field, BigInteger.ONE);
        Field expectedAfterThirdReset = field.copy();

        field.setElement(new FieldElement(new Coordinates2D(2, 3), 14));
        gameData.updateAndSaveHistory(field, BigInteger.ONE);
        Field expectedAfterSecondReset = field.copy();

        field.setElement(new FieldElement(new Coordinates2D(0, 1), 2));
        gameData.updateAndSaveHistory(field, BigInteger.ONE);
        Field expectedAfterFirstReset = field.copy();

        field.setElement(new FieldElement(new Coordinates2D(1, 2), 4));
        gameData.updateAndSaveHistory(field, BigInteger.ONE);

        gameData.restore();
        assertEquals(expectedAfterFirstReset, gameData.getField(), "Incorrect field");
        assertEquals(gameData.getScores(), new BigInteger("3"), "Incorrect scores");

        gameData.restore();
        assertEquals(expectedAfterSecondReset, gameData.getField(), "Incorrect field");
        assertEquals(gameData.getScores(), new BigInteger("2"), "Incorrect scores");

        gameData.restore();
        for (int i = 0; i < 2; i++) {
            assertEquals(expectedAfterThirdReset, gameData.getField(), "Incorrect field");
            assertEquals(gameData.getScores(), new BigInteger("1"), "Incorrect scores");
        }

    }
}