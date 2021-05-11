package dao.preferences;

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

@Repository("preferencesDao")
public final class FileSystemPreferencesDAO implements PreferencesDAO {

    private final ThemeDao themeDao;
    private final Path preferencesRepository;

    @Autowired
    public FileSystemPreferencesDAO(ThemeDao themeDao,
                                    @Qualifier("preferencesRepository") Path preferencesRepository) {
        this.themeDao = themeDao;
        this.preferencesRepository = preferencesRepository;
    }

    @Override
    public void saveOrUpdate(UserPreferences userPreferences) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(preferencesRepository.toString());
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
        try (FileInputStream fileInputStream = new FileInputStream(preferencesRepository.toString());
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
}
