package context;

import view.theme.Theme;

import java.util.Locale;

public interface ViewContext {

    Locale getCurrentLocale();

    void setLocale(Locale locale);

    Theme getCurrentTheme();

    void setTheme(Theme theme);

}
