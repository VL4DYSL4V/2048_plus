package view.listener;

import command.ShiftFieldCommand;
import enums.Direction;
import org.springframework.stereotype.Component;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public final class FieldMovementListener extends KeyAdapter {

    private final ShiftFieldCommand shiftFieldCommand;

    public FieldMovementListener(ShiftFieldCommand shiftFieldCommand) {
        this.shiftFieldCommand = shiftFieldCommand;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        switch (code) {
            case KeyEvent.VK_UP:
                shiftFieldCommand.setDirection(Direction.UP);
                break;
            case KeyEvent.VK_DOWN:
                shiftFieldCommand.setDirection(Direction.DOWN);
                break;
            case KeyEvent.VK_LEFT:
                shiftFieldCommand.setDirection(Direction.LEFT);
                break;
            case KeyEvent.VK_RIGHT:
                shiftFieldCommand.setDirection(Direction.RIGHT);
                break;
        }
        shiftFieldCommand.execute();
    }

}
