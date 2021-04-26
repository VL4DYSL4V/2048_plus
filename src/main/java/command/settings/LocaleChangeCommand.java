package command.settings;

import command.VolatileCommand;
import handler.CommandHandler;
import preferences.UserPreferences;

import java.util.Locale;

public final class LocaleChangeCommand implements VolatileCommand<Locale> {

    private Locale neuLocale;
    private final CommandHandler commandHandler;
    private final UserPreferences userPreferences;
    private final Object lock = new Object();

    public LocaleChangeCommand(CommandHandler commandHandler, UserPreferences userPreferences) {
        this.commandHandler = commandHandler;
        this.userPreferences = userPreferences;
    }

    @Override
    public void setParam(Locale param) {
        synchronized (lock) {
            this.neuLocale = param;
        }
    }

    @Override
    public void execute() {
        commandHandler.execute(() -> {
            synchronized (lock) {
                if (neuLocale != null) {
                    userPreferences.setLocale(neuLocale);
                }
            }
        });
    }
}
