package dao.preferences;

import preferences.UserPreferences;

public interface PreferencesDAO {

    void saveOrUpdate(UserPreferences userPreferences);

    void updatePreferences(UserPreferences userPreferences);

}
