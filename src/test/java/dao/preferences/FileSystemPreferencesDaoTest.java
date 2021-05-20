package dao.preferences;

import dao.RepositoryDirectoryManager;
import dao.theme.ThemeDao;
import enums.FieldDimension;
import exception.FetchException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import preferences.UserPreferences;
import testUtils.LocaleUtils;
import testUtils.PropertyUtils;
import testUtils.ThemeUtils;
import view.theme.Theme;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileSystemPreferencesDaoTest {

    private ThemeDao themeDao;
    private Properties preferencesProperties;
    private RepositoryDirectoryManager repositoryDirectoryManager;
    private FileSystemPreferencesDao fileSystemPreferencesDao;

    @BeforeEach
    void setup(){
        themeDao = spy(TestThemeDao.class);
        preferencesProperties = PropertyUtils.getTestPreferencesProperties();
        repositoryDirectoryManager = new RepositoryDirectoryManager(PropertyUtils.getRepositoryDirectoryProperties());
        fileSystemPreferencesDao = new FileSystemPreferencesDao(themeDao, preferencesProperties, repositoryDirectoryManager);
    }

    @AfterEach
    void cleanUp() throws IOException {
        String directoryName = preferencesProperties.getProperty("preferences-directory-name");
        String fileName = preferencesProperties.getProperty("preferences-file-name");
        Path path = repositoryDirectoryManager.getPathForRepository(directoryName, fileName);
        Files.delete(path);
        Path directoryPath = repositoryDirectoryManager.getRepositoryDirectory().resolve(directoryName);
        Files.delete(directoryPath);
    }

    @Test
    void nullConstructorArgTest(){
        assertThrows(NullPointerException.class, () -> new FileSystemPreferencesDao(null, preferencesProperties, repositoryDirectoryManager));
        assertThrows(NullPointerException.class, () -> new FileSystemPreferencesDao(themeDao, null, repositoryDirectoryManager));
        assertThrows(NullPointerException.class, () -> new FileSystemPreferencesDao(themeDao, preferencesProperties, null));
    }

    @Test
    void saveOrUpdate() throws FetchException {
        Locale locale = LocaleUtils.getDefaultLocale();
        Theme theme = themeDao.loadTheme(null);
        FieldDimension fieldDimension = FieldDimension.FOUR_AND_FOUR;
        UserPreferences userPreferences = new UserPreferences(locale, theme, fieldDimension);

        assertDoesNotThrow(() -> fileSystemPreferencesDao.saveOrUpdate(userPreferences));
        UserPreferences result = fileSystemPreferencesDao.getUserPreferences();
        assertEquals(userPreferences.getLocale(), result.getLocale());
        assertEquals(userPreferences.getFieldDimension(), result.getFieldDimension());
        assertEquals(userPreferences.getTheme(), result.getTheme());
    }

    @Test
    void getUserPreferences() throws FetchException {
        UserPreferences userPreferences = fileSystemPreferencesDao.getUserPreferences();
        assertNotNull(userPreferences);
        verify(themeDao).loadByName(any());
    }

    private static class TestThemeDao implements ThemeDao{

        @Override
        public Theme loadTheme(String location){
            return ThemeUtils.getDefaultTheme();
        }

        @Override
        public Theme loadByName(String name) {
            return ThemeUtils.getDefaultTheme();
        }
    }
}