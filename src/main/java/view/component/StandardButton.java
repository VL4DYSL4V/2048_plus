package view.component;

import view.enums.Fonts;
import view.theme.Theme;

import javax.swing.*;

public final class StandardButton extends JButton {

    private Theme theme;

    public StandardButton(String text, Theme theme) {
        this.theme = theme;
        setText(text);
        setBackground(theme.getBackground());
        setForeground(theme.getForeground());
        setFont(Fonts.STANDARD_FONT.getFont());
        setFocusable(false);
    }

}
