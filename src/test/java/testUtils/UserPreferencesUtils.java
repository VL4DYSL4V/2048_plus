package testUtils;

import enums.FieldDimension;
import preferences.UserPreferences;

public final class UserPreferencesUtils {

    private static final FieldDimension FIELD_DIMENSION = FieldDimension.FOUR_AND_FOUR;

    private UserPreferencesUtils(){

    }

    public static UserPreferences getDefaultUserPreferences(){
        return new UserPreferences(
                LocaleUtils.getDefaultLocale(),
                ThemeUtils.getDefaultTheme(),
                FIELD_DIMENSION);
    }

}
