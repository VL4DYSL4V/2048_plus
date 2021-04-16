package context;

import view.theme.Theme;

import java.util.Locale;

public interface UserPreferences {

    Locale getLocale();

    void setLocale(Locale locale);

    Theme getTheme();

    void setTheme(Theme theme);

}
