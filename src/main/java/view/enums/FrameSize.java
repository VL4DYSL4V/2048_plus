package view.enums;

import java.awt.*;

public enum FrameSize {

    GAME_FRAME(new Dimension(350, 430)),
    MAIN_FRAME(new Dimension(350, 350));

    private final Dimension dimension;

    FrameSize(Dimension dimension) {
        this.dimension = dimension;
    }

    public Dimension getDimension() {
        return dimension;
    }
}
