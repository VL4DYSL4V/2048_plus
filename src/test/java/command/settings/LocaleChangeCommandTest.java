package command.settings;

import dao.preferences.PreferencesDAO;
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
    private PreferencesDAO preferencesDAO;
    private LocaleChangeCommand localeChangeCommand;

    @BeforeEach
    void init() {
        userPreferences = spy(UserPreferencesUtils.getDefaultUserPreferences());
        commandHandler = spy(new ThisThreadCommandHandler());
        preferencesDAO = mock(PreferencesDAO.class);
        localeChangeCommand = new LocaleChangeCommand(commandHandler, userPreferences, preferencesDAO);
    }

    @Test
    void nullConstructorArgTest() {
        assertThrows(NullPointerException.class,
                () -> new LocaleChangeCommand(null, userPreferences, preferencesDAO));
        assertThrows(NullPointerException.class,
                () -> new LocaleChangeCommand(commandHandler, null, preferencesDAO));
        assertThrows(NullPointerException.class,
                () -> new LocaleChangeCommand(commandHandler, userPreferences, null));
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