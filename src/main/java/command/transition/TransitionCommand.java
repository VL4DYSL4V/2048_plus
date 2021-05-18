package command.transition;

import command.Command;
import handler.CommandHandler;

import javax.swing.*;
import java.util.Objects;

public final class TransitionCommand implements Command {

    private final JFrame from;
    private final JFrame to;
    private final CommandHandler commandHandler;
    private final Runnable afterTransition;

    public TransitionCommand(JFrame from, JFrame to, CommandHandler commandHandler) {
        this(from, to, commandHandler, null);
    }

    public TransitionCommand(JFrame from, JFrame to, CommandHandler commandHandler, Runnable afterTransition) {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);
        Objects.requireNonNull(commandHandler);
        this.from = from;
        this.to = to;
        this.commandHandler = commandHandler;
        this.afterTransition = afterTransition;
    }

    @Override
    public void execute() {
        commandHandler.execute(() -> {
            SwingUtilities.invokeLater(() -> {
                from.setVisible(false);
                to.setVisible(true);
            });
            if (afterTransition != null) {
                afterTransition.run();
            }
        });
    }
}
