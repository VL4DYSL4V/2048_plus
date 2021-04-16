package command.game;

import command.Command;
import handler.CommandHandler;
import model.GameModel;

public final class RestartCommand implements Command {

    private final GameModel gameModel;
    private final Runnable savingTask;
    private final CommandHandler controller;

    public RestartCommand(GameModel gameModel, Runnable savingTask, CommandHandler controller) {
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
