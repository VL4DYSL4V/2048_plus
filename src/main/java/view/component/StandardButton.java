package view.component;

import command.Command;
import view.enums.Fonts;
import view.theme.Theme;

import javax.swing.*;

public final class StandardButton extends JButton {

    private Theme theme;

    public StandardButton(String text, Theme theme, Command command) {
        this.theme = theme;
        setText(text);
        setBackground(theme.getBackground());
        setForeground(theme.getForeground());
        setFont(Fonts.STANDARD_FONT.getFont());
        setFocusable(false);
        addActionListener((e) -> command.execute());
    }

}
