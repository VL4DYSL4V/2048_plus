package command;

import controller.exit.ExitController;

public final class ExitAndSaveCommand implements Command{

    private final ExitController exitController;
    private final Runnable saveTask;

    public ExitAndSaveCommand(ExitController exitController, Runnable saveTask) {
        this.exitController = exitController;
        this.saveTask = saveTask;
    }

    @Override
    public void execute() {
        exitController.exitAndSave(saveTask);
    }

}
