package command.game;

import entity.Field;
import handler.CommandHandler;
import handler.UICommandHandler;
import model.GameModel;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testUtils.FieldUtils;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class ShiftFieldCommandTest {

    private static final CommandHandler commandHandler = new UICommandHandler();
    private GameModel gameModel;
    private MoveBackCommand moveBackCommand;

    @AfterAll
    static void tearDown(){
        commandHandler.shutdown();
    }

    @BeforeEach
    void setup(){
        gameModel = new GameModel();
        moveBackCommand = new MoveBackCommand(gameModel, commandHandler);
    }

    @Test
    void testImpossibleShift() {
        Field field = FieldUtils.getFieldWhichCannotBeMoved();
        gameModel.updateAndSaveHistory(field, BigInteger.ZERO);

    }

    @Test
    void testUpShift() {

    }
    @Test
    void testLeftShift() {

    }
    @Test
    void testRightShift() {

    }
    @Test
    void testDownShift() {

    }
}