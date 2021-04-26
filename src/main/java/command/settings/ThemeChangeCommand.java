package command.settings;

import command.VolatileCommand;
import handler.CommandHandler;
import preferences.UserPreferences;
import view.theme.Theme;

public final class ThemeChangeCommand implements VolatileCommand<Theme> {

    private Theme neuTheme;
    private final CommandHandler commandHandler;
    private final UserPreferences userPreferences;
    private final Object lock = new Object();

    public ThemeChangeCommand(CommandHandler commandHandler, UserPreferences userPreferences) {
        this.commandHandler = commandHandler;
        this.userPreferences = userPreferences;
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
                    userPreferences.setTheme(neuTheme);
                }
            }
        });
    }
}
