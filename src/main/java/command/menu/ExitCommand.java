package command.menu;

import command.Command;
import handler.CommandHandler;

import java.util.Objects;

public final class ExitCommand implements Command {

    private final CommandHandler commandHandler;

    public ExitCommand(CommandHandler commandHandler) {
        Objects.requireNonNull(commandHandler);
        this.commandHandler = commandHandler;
    }

    @Override
    public void execute() {
        commandHandler.execute(() -> System.exit(0));
        commandHandler.shutdown();
    }

}
