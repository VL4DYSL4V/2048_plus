package command.game;

import command.Command;
import handler.CommandHandler;
import model.GameModel;

import java.util.Objects;

public final class RestartCommand implements Command {

    private final GameModel gameModel;
    private final Runnable savingTask;
    private final CommandHandler controller;

    public RestartCommand(GameModel gameModel, Runnable savingTask, CommandHandler controller) {
        Objects.requireNonNull(gameModel);
        Objects.requireNonNull(savingTask);
        Objects.requireNonNull(controller);
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
