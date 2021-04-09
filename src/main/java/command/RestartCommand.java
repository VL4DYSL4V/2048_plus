package command;

import service.ui.executor.CommandExecutor;
import model.Model;

public final class RestartCommand implements Command {

    private final Model model;
    private final Runnable savingTask;
    private final CommandExecutor controller;

    public RestartCommand(Model model, Runnable savingTask, CommandExecutor controller) {
        this.model = model;
        this.savingTask = savingTask;
        this.controller = controller;
    }

    @Override
    public void execute() {
        controller.execute(() -> {
            synchronized (model) {
                model.reset();
                savingTask.run();
            }
        });
    }

}
