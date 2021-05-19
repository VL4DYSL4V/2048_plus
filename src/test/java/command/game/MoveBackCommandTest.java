package command.game;

import entity.Field;
import enums.Direction;
import handler.CommandHandler;
import mock.ThisThreadCommandHandler;
import model.GameModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testUtils.FieldUtils;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class MoveBackCommandTest {

    private CommandHandler commandHandler;
    private GameModel gameModel;
    private ShiftFieldCommand shiftFieldCommand;
    private MoveBackCommand moveBackCommand;

    @AfterEach
    void tearDown() {
        commandHandler.shutdown();
    }

    @BeforeEach
    void setup() {
        commandHandler = spy(new ThisThreadCommandHandler());
        gameModel = new GameModel();
        shiftFieldCommand = new ShiftFieldCommand(commandHandler, gameModel);
        moveBackCommand = new MoveBackCommand(gameModel, commandHandler);
    }

    @Test
    void moveBack() {
        Field field = FieldUtils.getFieldForDownShift();
        Field copy = field.copy();
        gameModel.updateAndSaveHistory(field, BigInteger.ZERO);

        shiftFieldCommand.setParam(Direction.DOWN);
        shiftFieldCommand.execute();

        verify(commandHandler).execute(any());
        assertEquals(new BigInteger("16"), gameModel.getScores());

        moveBackCommand.execute();

        assertEquals(copy, gameModel.getField());
        assertEquals(BigInteger.ZERO, gameModel.getScores());
    }

    @Test
    void nullConstructorArgTest(){
        assertThrows(NullPointerException.class, () -> new MoveBackCommand(null, commandHandler));
        assertThrows(NullPointerException.class, () -> new MoveBackCommand(gameModel, null));
    }
}