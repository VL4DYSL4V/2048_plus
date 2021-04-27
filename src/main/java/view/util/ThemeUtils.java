package view.util;

import view.theme.Theme;

import java.awt.*;

public final class ThemeUtils {

    private ThemeUtils() {
    }

    public static void style(Component component, Theme theme) {
        component.setForeground(theme.getForeground());
        component.setBackground(theme.getBackground());
        component.setFont(theme.getFont());
    }

}
