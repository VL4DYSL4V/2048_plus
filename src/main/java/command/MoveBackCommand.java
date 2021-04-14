package command;

import service.ui.executor.CommandExecutor;
import model.GameModel;

public final class MoveBackCommand implements Command {

    private final GameModel gameModel;
    private final CommandExecutor commandExecutor;

    public MoveBackCommand(GameModel gameModel, CommandExecutor commandExecutor) {
        this.gameModel = gameModel;
        this.commandExecutor = commandExecutor;
    }

    @Override
    public void execute() {
        commandExecutor.execute(() -> {
            synchronized (gameModel) {
                if (gameModel.restore()) {
                    gameModel.setGameIsOver(false);
                }
            }
        });
    }

}
