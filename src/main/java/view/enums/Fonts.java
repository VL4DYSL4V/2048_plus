package view.enums;

import java.awt.*;

public enum Fonts {

    STANDARD_FONT(new Font(Font.SANS_SERIF, Font.PLAIN, 18));

    private final Font font;

    Fonts(Font font) {
        this.font = font;
    }

    public Font getFont() {
        return font;
    }
}
