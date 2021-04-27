package command.settings;

import command.VolatileCommand;
import dao.preferences.PreferencesDAO;
import handler.CommandHandler;
import preferences.UserPreferences;
import view.theme.Theme;

public final class ThemeChangeCommand implements VolatileCommand<Theme> {

    private Theme neuTheme;
    private final CommandHandler commandHandler;
    private final UserPreferences userPreferences;
    private final PreferencesDAO preferencesDAO;
    private final Object lock = new Object();

    public ThemeChangeCommand(CommandHandler commandHandler, UserPreferences userPreferences, PreferencesDAO preferencesDAO) {
        this.commandHandler = commandHandler;
        this.userPreferences = userPreferences;
        this.preferencesDAO = preferencesDAO;
    }

    @Override
    public void setParam(Theme param) {
        synchronized (lock){
            this.neuTheme = param;
        }
    }

    @Override
    public void execute() {
        commandHandler.execute(() -> {
            synchronized (lock) {
                if (neuTheme != null) {
                    if(userPreferences.setTheme(neuTheme)){
                        preferencesDAO.saveOrUpdate(userPreferences);
                    }
                }
            }
        });
    }
}
