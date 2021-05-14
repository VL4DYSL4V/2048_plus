package view.listener;

import command.VolatileCommand;
import enums.Direction;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public final class FieldMovementListener extends KeyAdapter {

    private final VolatileCommand<Direction> shiftFieldCommand;

    public FieldMovementListener(VolatileCommand<Direction> shiftFieldCommand) {
        this.shiftFieldCommand = shiftFieldCommand;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        switch (code) {
            case KeyEvent.VK_UP:
                shiftFieldCommand.setParam(Direction.UP);
                shiftFieldCommand.execute();
                break;
            case KeyEvent.VK_DOWN:
                shiftFieldCommand.setParam(Direction.DOWN);
                shiftFieldCommand.execute();
                break;
            case KeyEvent.VK_LEFT:
                shiftFieldCommand.setParam(Direction.LEFT);
                shiftFieldCommand.execute();
                break;
            case KeyEvent.VK_RIGHT:
                shiftFieldCommand.setParam(Direction.RIGHT);
                shiftFieldCommand.execute();
                break;
        }
    }

}
