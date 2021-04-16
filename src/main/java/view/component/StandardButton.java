package view.component;

import command.Command;
import context.UserPreferences;
import view.enums.Fonts;
import view.theme.Theme;

import javax.swing.*;

public final class StandardButton extends JButton implements ThemeVaryingComponent {

    private final UserPreferences userPreferences;

    public StandardButton(UserPreferences userPreferences, Command command) {
        this.userPreferences = userPreferences;
        setFocusable(false);
        addActionListener((e) -> command.execute());
        style();
    }

    private void style() {
        updateTheme();
        setFont(Fonts.STANDARD_FONT.getFont());
    }

    private void updateTheme(){
        Theme theme = userPreferences.getTheme();
        setBackground(theme.getBackground());
        setForeground(theme.getForeground());
    }

    @Override
    public void applyNewTheme() {
        updateTheme();
    }
}
