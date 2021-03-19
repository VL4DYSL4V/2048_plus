package command;

import controller.restart.RestartController;

public final class RestartCommand implements Command {

    private final RestartController restartController;

    public RestartCommand(RestartController restartController) {
        this.restartController = restartController;
    }

    @Override
    public void execute() {
        restartController.restart();
    }

}
