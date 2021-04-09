package command;

import controller.CommandExecutor;
import model.Model;

public final class MoveBackCommand implements Command {

    private final Model model;
    private final CommandExecutor commandExecutor;

    public MoveBackCommand(Model model, CommandExecutor commandExecutor) {
        this.model = model;
        this.commandExecutor = commandExecutor;
    }

    @Override
    public void execute() {
        commandExecutor.execute(() -> {
            synchronized (model) {
                if (model.restore()) {
                    model.setGameIsOver(false);
                }
            }
        });
    }

}
