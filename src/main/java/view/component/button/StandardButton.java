package view.component.button;

import command.Command;
import preferences.UserPreferences;
import view.component.ThemeVaryingComponent;
import view.util.ThemeUtils;

import javax.swing.*;

public final class StandardButton extends JButton implements ThemeVaryingComponent {

    private final UserPreferences userPreferences;

    public StandardButton(UserPreferences userPreferences, Command command) {
        this.userPreferences = userPreferences;
        setFocusable(false);
        addActionListener((e) -> command.execute());
        applyNewTheme();
    }

    @Override
    public void applyNewTheme() {
        ThemeUtils.style(this, userPreferences.getTheme());
    }
}
