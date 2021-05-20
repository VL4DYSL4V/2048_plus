package command.game;

import command.Command;
import handler.CommandHandler;
import model.GameModel;

import java.util.Objects;

public final class RestartCommand implements Command {

    private final GameModel gameModel;
    private final Runnable savingTask;
    private final CommandHandler commandHandler;

    public RestartCommand(GameModel gameModel, Runnable savingTask, CommandHandler commandHandler) {
        Objects.requireNonNull(gameModel);
        Objects.requireNonNull(savingTask);
        Objects.requireNonNull(commandHandler);
        this.gameModel = gameModel;
        this.savingTask = savingTask;
        this.commandHandler = commandHandler;
    }

    @Override
    public void execute() {
        commandHandler.execute(() -> {
            synchronized (gameModel) {
                gameModel.reset();
                savingTask.run();
            }
        });
    }

}
