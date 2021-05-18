package command.game;

import command.Command;
import handler.CommandHandler;
import model.GameModel;

import java.util.Objects;

public final class MoveBackCommand implements Command {

    private final GameModel gameModel;
    private final CommandHandler commandHandler;

    public MoveBackCommand(GameModel gameModel, CommandHandler commandHandler) {
        Objects.requireNonNull(gameModel);
        Objects.requireNonNull(commandHandler);
        this.gameModel = gameModel;
        this.commandHandler = commandHandler;
    }

    @Override
    public void execute() {
        commandHandler.execute(() -> {
            synchronized (gameModel) {
                if (gameModel.restore()) {
                    gameModel.setGameIsOver(false);
                }
            }
        });
    }

}
