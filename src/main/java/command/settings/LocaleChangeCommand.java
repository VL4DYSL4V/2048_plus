package command.settings;

import command.VolatileCommand;
import dao.preferences.PreferencesDAO;
import handler.CommandHandler;
import preferences.UserPreferences;

import java.util.Locale;
import java.util.Objects;

public final class LocaleChangeCommand implements VolatileCommand<Locale> {

    private final CommandHandler commandHandler;
    private final UserPreferences userPreferences;
    private final PreferencesDAO preferencesDAO;
    private volatile Locale neuLocale;

    public LocaleChangeCommand(CommandHandler commandHandler, UserPreferences userPreferences, PreferencesDAO preferencesDAO) {
        this.commandHandler = commandHandler;
        this.userPreferences = userPreferences;
        this.preferencesDAO = preferencesDAO;
    }

    @Override
    public void setParam(Locale param) {
        Objects.requireNonNull(param);
        this.neuLocale = param;
    }

    @Override
    public void execute() {
        commandHandler.execute(() -> {
            if (userPreferences.setLocale(neuLocale)) {
                preferencesDAO.saveOrUpdate(userPreferences);
            }
        });
    }
}
