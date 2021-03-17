package view.enums;

import java.awt.*;

public enum FrameSize {

    GAME_FRAME(new Dimension(350, 500));

    private final Dimension dimension;

    FrameSize(Dimension dimension) {
        this.dimension = dimension;
    }

    public Dimension getDimension() {
        return dimension;
    }
}
