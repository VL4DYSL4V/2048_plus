package command;

import controller.MoveBackController;

public final class MoveBackCommand implements Command {

    private final MoveBackController moveBackController;

    public MoveBackCommand(MoveBackController moveBackController) {
        this.moveBackController = moveBackController;
    }

    @Override
    public void execute() {
        moveBackController.moveBack();
    }

}
