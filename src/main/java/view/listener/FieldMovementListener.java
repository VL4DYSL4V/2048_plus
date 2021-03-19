package view.listener;

import controller.FieldShiftController;
import enums.Direction;
import org.springframework.stereotype.Component;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

@Component
public final class FieldMovementListener extends KeyAdapter {

    private final FieldShiftController fieldShiftController;

    public FieldMovementListener(FieldShiftController fieldShiftController) {
        this.fieldShiftController = fieldShiftController;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        switch (code) {
            case KeyEvent.VK_UP:
                fieldShiftController.shift(Direction.UP);
                break;
            case KeyEvent.VK_DOWN:
                fieldShiftController.shift(Direction.DOWN);
                break;
            case KeyEvent.VK_LEFT:
                fieldShiftController.shift(Direction.LEFT);
                break;
            case KeyEvent.VK_RIGHT:
                fieldShiftController.shift(Direction.RIGHT);
                break;
        }
    }

}
