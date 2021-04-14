package command;

import service.ui.executor.CommandExecutor;

public class GameFrameToMenuCommand implements Command{

    private final CommandExecutor commandExecutor;

    public GameFrameToMenuCommand(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    @Override
    public void execute() {

    }
}
