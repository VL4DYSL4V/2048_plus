package dao.preferences;

import dao.RepositoryDirectoryManager;
import dao.theme.ThemeDao;
import enums.FieldDimension;
import exception.FetchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import preferences.UserPreferences;

import java.io.*;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Properties;

@Repository("preferencesDao")
public final class FileSystemPreferencesDao implements PreferencesDao {

    private final ThemeDao themeDao;
    private final Properties preferencesProperties;
    private final RepositoryDirectoryManager repositoryDirectoryManager;

    @Autowired
    public FileSystemPreferencesDao(ThemeDao themeDao,
                                    @Qualifier("preferencesProperties") Properties preferencesProperties,
                                    RepositoryDirectoryManager repositoryDirectoryManager) {
        this.themeDao = themeDao;
        this.preferencesProperties = preferencesProperties;
        this.repositoryDirectoryManager = repositoryDirectoryManager;
        setupStorage();
    }

    @Override
    public void saveOrUpdate(UserPreferences userPreferences) {
        String preferencesPath = getPreferencesPath().toString();
        try (FileOutputStream fileOutputStream = new FileOutputStream(preferencesPath);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(bufferedOutputStream)) {
            objectOutputStream.writeUTF(userPreferences.getLocale().getLanguage());
            objectOutputStream.writeUTF(userPreferences.getTheme().getName());
            objectOutputStream.writeObject(userPreferences.getFieldDimension());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserPreferences getUserPreferences() {
        String preferencesPath = getPreferencesPath().toString();
        try (FileInputStream fileInputStream = new FileInputStream(preferencesPath);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
             ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream)) {
            String country = objectInputStream.readUTF();
            String themeName = objectInputStream.readUTF();
            FieldDimension dimension = (FieldDimension) objectInputStream.readObject();
            return new UserPreferences(Locale.forLanguageTag(country), themeDao.loadByName(themeName), dimension);
        } catch (FetchException e) {
            throw new RuntimeException(e);
        } catch (IOException | ClassNotFoundException e) {
            try {
                return new UserPreferences(Locale.forLanguageTag("en"),
                        themeDao.loadByName("Dark"), FieldDimension.FOUR_AND_FOUR);
            } catch (FetchException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void setupStorage(){
        String directoryName = preferencesProperties.getProperty("preferences-directory-name");
        String fileName = preferencesProperties.getProperty("preferences-file-name");
        repositoryDirectoryManager.createFile(directoryName, fileName);
    }

    private Path getPreferencesPath(){
        String directoryName = preferencesProperties.getProperty("preferences-directory-name");
        String fileName = preferencesProperties.getProperty("preferences-file-name");
        return repositoryDirectoryManager.getPathForRepository(directoryName, fileName);
    }

}
