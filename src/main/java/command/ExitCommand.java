package command;

import service.ui.executor.CommandExecutor;

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
