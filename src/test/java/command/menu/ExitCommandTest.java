package command.menu;

import mock.ThisThreadCommandHandler;
import handler.CommandHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void execute() {
        exitCommand.execute();
        verify(commandHandler).execute(any());
        verify(commandHandler).shutdown();
    }
}