package command;

import controller.exit.ExitController;

public final class ExitCommand implements Command{

    private final ExitController exitController;

    public ExitCommand(ExitController exitController) {
        this.exitController = exitController;
    }

    @Override
    public void execute() {
        exitController.exit();
    }

}
