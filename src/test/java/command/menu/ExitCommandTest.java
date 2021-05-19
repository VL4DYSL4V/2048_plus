package command.menu;

import handler.CommandHandler;
import mock.ThisThreadCommandHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ExitCommandTest {

    private ExitCommand exitCommand;
    private CommandHandler commandHandler;

    @BeforeEach
    void setup() {
        commandHandler = mock(ThisThreadCommandHandler.class);
        exitCommand = new ExitCommand(commandHandler);
    }

    @Test
    void nullConstructorArgTest() {
        assertThrows(NullPointerException.class, () -> new ExitCommand(null));
    }

    @Test
    void execute() {
        exitCommand.execute();
        verify(commandHandler).execute(any());
        verify(commandHandler).shutdown();
    }
}