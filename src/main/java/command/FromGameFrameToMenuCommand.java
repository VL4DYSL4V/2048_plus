package command;

import service.ui.executor.CommandExecutor;

public class FromGameFrameToMenuCommand implements Command{

    private final CommandExecutor commandExecutor;

    public FromGameFrameToMenuCommand(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    @Override
    public void execute() {

    }
}
