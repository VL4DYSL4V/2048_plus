package command.menu;

import command.Command;
import handler.CommandHandler;

public final class ExitCommand implements Command {

    private final CommandHandler commandHandler;

    public ExitCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void execute() {
        commandHandler.execute(() -> System.exit(0));
        commandHandler.shutdown();
    }

}
