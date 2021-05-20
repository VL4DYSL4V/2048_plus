package dao.preferences;

import preferences.UserPreferences;

public interface PreferencesDao {

    void saveOrUpdate(UserPreferences userPreferences);

    UserPreferences getUserPreferences();

}
