package controller;

import enums.Direction;
import exception.game.EndOfGameException;

public interface FieldShiftController {

    void shift(Direction direction) throws EndOfGameException;

    void cancelShifts();

}
