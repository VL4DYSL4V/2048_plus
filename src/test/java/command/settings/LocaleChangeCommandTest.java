package command.settings;

import dao.preferences.PreferencesDao;
import handler.CommandHandler;
import mock.ThisThreadCommandHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import preferences.UserPreferences;
import testUtils.UserPreferencesUtils;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class LocaleChangeCommandTest {

    private CommandHandler commandHandler;
    private UserPreferences userPreferences;
    private PreferencesDao preferencesDAO;
    private LocaleChangeCommand localeChangeCommand;

    @BeforeEach
    void init() {
        userPreferences = spy(UserPreferencesUtils.getDefaultUserPreferences());
        commandHandler = spy(new ThisThreadCommandHandler());
        preferencesDAO = mock(PreferencesDao.class);
        localeChangeCommand = new LocaleChangeCommand(userPreferences, preferencesDAO, commandHandler);
    }

    @Test
    void nullConstructorArgTest() {
        assertThrows(NullPointerException.class,
                () -> new LocaleChangeCommand(userPreferences, preferencesDAO, null));
        assertThrows(NullPointerException.class,
                () -> new LocaleChangeCommand(null, preferencesDAO, commandHandler));
        assertThrows(NullPointerException.class,
                () -> new LocaleChangeCommand(userPreferences, null, commandHandler));
    }

    @Test
    void testNullAsParam() {
        assertThrows(NullPointerException.class, () -> localeChangeCommand.setParam(null));
    }

    @Test
    void executeInCommandHandler() {
        localeChangeCommand.execute();
        verify(commandHandler).execute(any());
    }

    @Test
    void setNewLocale() {
        Locale locale = Locale.GERMAN;
        localeChangeCommand.setParam(locale);
        localeChangeCommand.execute();
        verify(userPreferences).setLocale(locale);
        verify(preferencesDAO).saveOrUpdate(userPreferences);
    }

    @Test
    void setSameLocale() {
        Locale locale = userPreferences.getLocale();
        localeChangeCommand.setParam(locale);
        localeChangeCommand.execute();
        verify(userPreferences).setLocale(locale);
        verify(preferencesDAO, times(0)).saveOrUpdate(userPreferences);
    }
}