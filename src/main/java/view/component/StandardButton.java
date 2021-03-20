package view.component;

import command.Command;
import view.context.ThemeHolder;
import view.enums.Fonts;
import view.theme.Theme;

import javax.swing.*;

public final class StandardButton extends JButton {

    private ThemeHolder themeHolder;

    public StandardButton(String text, ThemeHolder themeHolder, Command command) {
        this.themeHolder = themeHolder;
        setText(text);
        setFocusable(false);
        addActionListener((e) -> command.execute());
        style(themeHolder.getTheme());
    }

    private void style(Theme theme){
        setBackground(theme.getBackground());
        setForeground(theme.getForeground());
        setFont(Fonts.STANDARD_FONT.getFont());
    }

}
