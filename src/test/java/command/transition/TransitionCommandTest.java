package command.transition;

import handler.CommandHandler;
import mock.ThisThreadCommandHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransitionCommandTest {

    private JFrame from;
    private JFrame to;
    private CommandHandler commandHandler;

    @BeforeEach
    void init(){
        from = mock(JFrame.class);
        to = mock(JFrame.class);
        commandHandler = spy(new ThisThreadCommandHandler());
    }

    @Test
    void nullConstructorArgTest() {
        assertThrows(NullPointerException.class,
                () -> new TransitionCommand(null, to, commandHandler));
        assertThrows(NullPointerException.class,
                () -> new TransitionCommand(from, null, commandHandler));
        assertThrows(NullPointerException.class,
                () -> new TransitionCommand(from, to, null));
        assertDoesNotThrow(() -> new TransitionCommand(from, to, commandHandler, null));
    }

    @Test
    void executeInCommandHandler() {
        new TransitionCommand(from, to, commandHandler).execute();
        verify(commandHandler).execute(any());
    }

    void verifyChangingVisibility(TransitionCommand transitionCommand) throws InterruptedException {
        transitionCommand.execute();
        verify(commandHandler).execute(any());
        SwingUtilities.invokeLater(() -> {
            synchronized (TransitionCommandTest.this){
                TransitionCommandTest.this.notifyAll();
            }
        });
        synchronized (this){
            wait(5_000);
        }
        verify(from).setVisible(false);
        verify(to).setVisible(true);
    }

    @Test
    void testChangingVisibility() throws InterruptedException {
        TransitionCommand transitionCommand = new TransitionCommand(from, to, commandHandler);
        verifyChangingVisibility(transitionCommand);
    }

    @Test
    void testRunningRunnable() throws InterruptedException {
        Runnable runnable = mock(Runnable.class);
        TransitionCommand transitionCommand = new TransitionCommand(from, to, commandHandler, runnable);
        verifyChangingVisibility(transitionCommand);
        verify(runnable).run();
    }
}