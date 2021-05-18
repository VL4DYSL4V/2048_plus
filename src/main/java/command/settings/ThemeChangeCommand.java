package command.settings;

import command.VolatileCommand;
import dao.preferences.PreferencesDAO;
import handler.CommandHandler;
import preferences.UserPreferences;
import view.theme.Theme;

import java.util.Objects;

public final class ThemeChangeCommand implements VolatileCommand<Theme> {

    private final CommandHandler commandHandler;
    private final UserPreferences userPreferences;
    private final PreferencesDAO preferencesDAO;
    private Theme neuTheme;

    public ThemeChangeCommand(CommandHandler commandHandler, UserPreferences userPreferences, PreferencesDAO preferencesDAO) {
        this.commandHandler = commandHandler;
        this.userPreferences = userPreferences;
        this.preferencesDAO = preferencesDAO;
    }

    @Override
    public void setParam(Theme param) {
        Objects.requireNonNull(param);
        this.neuTheme = param;
    }

    @Override
    public void execute() {
        commandHandler.execute(() -> {
            if (userPreferences.setTheme(neuTheme)) {
                preferencesDAO.saveOrUpdate(userPreferences);
            }
        });
    }
}
