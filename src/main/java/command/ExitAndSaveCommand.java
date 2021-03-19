package command;

import controller.exit.ExitController;

public final class ExitAndSaveCommand implements Command{

    private final ExitController exitController;

    public ExitAndSaveCommand(ExitController exitController) {
        this.exitController = exitController;
    }

    @Override
    public void execute() {
        exitController.exitAndSave();
    }

}
