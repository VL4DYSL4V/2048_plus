package view.component;

import command.Command;
import view.StyleVaryingComponent;
import view.enums.Fonts;
import view.theme.Theme;

import javax.swing.*;

public final class StandardButton extends JButton implements StyleVaryingComponent {

    public StandardButton(String text, Theme theme, Command command) {
        setText(text);
        setFocusable(false);
        addActionListener((e) -> command.execute());
        style(theme);
    }

    private void style(Theme theme) {
        setBackground(theme.getBackground());
        setForeground(theme.getForeground());
        setFont(Fonts.STANDARD_FONT.getFont());
    }

    @Override
    public void update(Theme neuTheme) {
        SwingUtilities.invokeLater(() ->{
            style(neuTheme);
            repaint();
        });
    }
}
