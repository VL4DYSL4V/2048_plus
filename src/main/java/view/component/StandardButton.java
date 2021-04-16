package view.component;

import command.Command;
import context.UserPreferences;
import view.enums.Fonts;
import view.theme.Theme;

import javax.swing.*;

public final class StandardButton extends JButton implements StyleVaryingComponent {

    private final UserPreferences userPreferences;

    public StandardButton(UserPreferences userPreferences, Command command) {
        this.userPreferences = userPreferences;
        setFocusable(false);
        addActionListener((e) -> command.execute());
        style();
    }

    private void style() {
        Theme theme = userPreferences.getTheme();
        setBackground(theme.getBackground());
        setForeground(theme.getForeground());
        setFont(Fonts.STANDARD_FONT.getFont());
    }

    @Override
    public void updateStyle() {
        style();
    }
}
