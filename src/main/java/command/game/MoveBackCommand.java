package command.game;

import command.Command;
import handler.CommandHandler;
import model.GameModel;

public final class MoveBackCommand implements Command {

    private final GameModel gameModel;
    private final CommandHandler commandHandler;

    public MoveBackCommand(GameModel gameModel, CommandHandler commandHandler) {
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
