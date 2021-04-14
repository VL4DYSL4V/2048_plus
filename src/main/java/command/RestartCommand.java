package command;

import service.ui.executor.CommandExecutor;
import model.GameModel;

public final class RestartCommand implements Command {

    private final GameModel gameModel;
    private final Runnable savingTask;
    private final CommandExecutor controller;

    public RestartCommand(GameModel gameModel, Runnable savingTask, CommandExecutor controller) {
        this.gameModel = gameModel;
        this.savingTask = savingTask;
        this.controller = controller;
    }

    @Override
    public void execute() {
        controller.execute(() -> {
            synchronized (gameModel) {
                gameModel.reset();
                savingTask.run();
            }
        });
    }

}
