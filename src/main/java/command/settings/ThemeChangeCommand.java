package command.settings;

import command.VolatileCommand;
import dao.preferences.PreferencesDao;
import handler.CommandHandler;
import preferences.UserPreferences;
import view.theme.Theme;

import java.util.Objects;

public final class ThemeChangeCommand implements VolatileCommand<Theme> {

    private final CommandHandler commandHandler;
    private final UserPreferences userPreferences;
    private final PreferencesDao preferencesDAO;
    private Theme neuTheme;

    public ThemeChangeCommand(CommandHandler commandHandler, UserPreferences userPreferences, PreferencesDao preferencesDAO) {
        Objects.requireNonNull(commandHandler);
        Objects.requireNonNull(userPreferences);
        Objects.requireNonNull(preferencesDAO);
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
