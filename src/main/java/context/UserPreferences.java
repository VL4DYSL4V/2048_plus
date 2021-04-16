package context;

import enums.FieldDimension;
import view.theme.Theme;

import java.util.Locale;

public interface UserPreferences {

    Locale getLocale();

    boolean setLocale(Locale locale);

    Theme getTheme();

    boolean setTheme(Theme theme);

    FieldDimension getFieldDimension();

    boolean setFieldDimension(FieldDimension fieldDimension);

}
