package command.game;

import entity.Coordinates2D;
import entity.Field;
import entity.FieldElement;
import handler.CommandHandler;
import model.GameModel;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import testUtils.FieldUtils;

import java.math.BigInteger;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestartCommandTest {

    private static final CommandHandler commandHandler = spy(new ThisThreadCommandHandler());
    private final Runnable savingTask = Mockito.mock(Runnable.class);
    private GameModel gameModel;
    private RestartCommand restartCommand;

    @AfterAll
    static void tearDown() {
        commandHandler.shutdown();
    }

    @BeforeEach
    void setup() {
        gameModel = new GameModel();
        restartCommand = new RestartCommand(gameModel, savingTask, commandHandler);
    }

    @Test
    void execute() {
        Field field = FieldUtils.getFieldWithData();
        BigInteger scores = new BigInteger("24");
        gameModel.updateAndSaveHistory(field, scores);
        gameModel.setGameIsOver(true);
        restartCommand.execute();

        verify(commandHandler).execute(any());

        field = gameModel.getField();

        assertEquals(BigInteger.ZERO, gameModel.getScores());
        assertFalse(gameModel.gameIsOver());
        Collection<Coordinates2D> availableCoordinates = field.getAvailableCoordinates();
        assertEquals(field.getFieldDimension().getHeight() * field.getFieldDimension().getWidth() - 2,
                availableCoordinates.size());
        for (int i = field.getFieldDimension().getMinY(); i <= field.getFieldDimension().getMaxY(); i++) {
            for (FieldElement e : field.getRow(i)) {
                assertTrue(e.isEmpty() || e.getValue() == 1 || e.getValue() == 2);
            }
        }

        verify(savingTask, times(1)).run();
    }
}