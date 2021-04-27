package view.util;

import java.awt.*;

public final class ScreenUtils {

    private ScreenUtils() {
    }

    public static int getScreenWidth() {
        return Toolkit.getDefaultToolkit().getScreenSize().width;
    }

    public static int getScreenHeight() {
        return Toolkit.getDefaultToolkit().getScreenSize().height;
    }
}
