package command;

import controller.CommandExecutor;

public final class ExitCommand implements Command {

    private final CommandExecutor commandExecutor;

    public ExitCommand(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    @Override
    public void execute() {
        commandExecutor.execute(() -> System.exit(0));
    }

}
