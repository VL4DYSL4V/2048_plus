package command.game;

import entity.Field;
import enums.Direction;
import handler.CommandHandler;
import model.GameModel;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testUtils.FieldUtils;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MoveBackCommandTest {

    private static final CommandHandler commandHandler = new ThisThreadCommandHandler();
    private GameModel gameModel;
    private ShiftFieldCommand shiftFieldCommand;
    private MoveBackCommand moveBackCommand;

    @AfterAll
    static void tearDown() {
        commandHandler.shutdown();
    }

    @BeforeEach
    void setup() {
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

        assertEquals(new BigInteger("16"), gameModel.getScores());

        moveBackCommand.execute();

        assertEquals(copy, gameModel.getField());
        assertEquals(BigInteger.ZERO, gameModel.getScores());

    }

    private static class ThisThreadCommandHandler implements CommandHandler {

        private boolean isShutdown = false;

        @Override
        public void execute(Runnable runnable) {
            if (!isShutdown) {
                runnable.run();
            }
        }

        @Override
        public void shutdown() {
            isShutdown = true;
        }
    }
}