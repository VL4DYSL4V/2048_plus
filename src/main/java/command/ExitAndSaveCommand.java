package command;

import service.ui.executor.CommandExecutor;

public final class ExitAndSaveCommand implements Command {

    private final CommandExecutor commandExecutor;
    private final Runnable saveTask;

    public ExitAndSaveCommand(CommandExecutor commandExecutor, Runnable saveTask) {
        this.commandExecutor = commandExecutor;
        this.saveTask = saveTask;
    }

    @Override
    public void execute() {
        commandExecutor.execute(() -> {
            saveTask.run();
            System.exit(0);
        });
    }

}
