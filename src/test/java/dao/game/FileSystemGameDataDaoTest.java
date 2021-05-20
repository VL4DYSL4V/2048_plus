package dao.game;

import dao.RepositoryDirectoryManager;
import enums.FieldDimension;
import exception.FetchException;
import exception.StoreException;
import model.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import testUtils.PropertyUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class FileSystemGameDataDaoTest {

    private Properties savedGamesProperties;
    private RepositoryDirectoryManager repositoryDirectoryManager;
    private FileSystemGameDataDao fileSystemGameDataDao;

    @BeforeEach
    void init() {
        savedGamesProperties = PropertyUtils.getTestSavedGamesProperties();
        Properties repositoryDirectoryProperties = PropertyUtils.repositoryDirectoryProperties();
        repositoryDirectoryManager = new RepositoryDirectoryManager(repositoryDirectoryProperties);
        fileSystemGameDataDao = new FileSystemGameDataDao(savedGamesProperties, repositoryDirectoryManager);
    }

    @AfterEach
    void cleanup() throws IOException {
        String directoryName = savedGamesProperties.getProperty("saved-games-directory-name");
        for (String propertyName : savedGamesProperties.stringPropertyNames()) {
            if (propertyName.startsWith("file.")) {
                String value = savedGamesProperties.getProperty(propertyName);
                Path path = repositoryDirectoryManager.getPathForRepository(directoryName, value);
                Files.delete(path);
            }
        }
        Path directoryPath = repositoryDirectoryManager.getRepositoryDirectory().resolve(directoryName);
        Files.delete(directoryPath);
    }

    @Test
    void nullConstructorArgTest() {
        assertThrows(NullPointerException.class, () -> new FileSystemGameDataDao(null, repositoryDirectoryManager));
        assertThrows(NullPointerException.class, () -> new FileSystemGameDataDao(new Properties(), null));
    }

    @Nested
    class GetByDimensionTest {

        @Test
        void nullArgumentTest() {
            assertThrows(NullPointerException.class, () -> fileSystemGameDataDao.getByDimension(null));
        }

        @Test
        void testWithNotExistingPath() {
            Properties properties = new Properties();
            FileSystemGameDataDao fileSystemGameDataDao = new FileSystemGameDataDao(properties, repositoryDirectoryManager);
            assertThrows(FetchException.class, () -> fileSystemGameDataDao.getByDimension(FieldDimension.FOUR_AND_FOUR));
        }

        @Test
        void getByDimensionWithExpectedEOFException() throws FetchException {
            FieldDimension fieldDimension = FieldDimension.FIVE_AND_FIVE;
            assertDoesNotThrow(() -> fileSystemGameDataDao.getByDimension(fieldDimension));
            GameData result = fileSystemGameDataDao.getByDimension(fieldDimension);
            assertNotNull(result);
        }
    }

    @Nested
    class SaveTest {

        @Test
        void nullArgumentTest() {
            assertThrows(NullPointerException.class, () -> fileSystemGameDataDao.save(null, FieldDimension.FOUR_AND_FOUR));
            assertThrows(NullPointerException.class, () -> fileSystemGameDataDao.save(new GameData(), null));
        }

        @Test
        void testWithNotExistingPath() {
            Properties properties = new Properties();
            FileSystemGameDataDao fileSystemGameDataDao = new FileSystemGameDataDao(properties, repositoryDirectoryManager);
            assertThrows(StoreException.class, () -> fileSystemGameDataDao.save(new GameData(), FieldDimension.FOUR_AND_FOUR));
        }

        @Test
        void save() throws FetchException {
            GameData gameData = new GameData();
            FieldDimension fieldDimension = FieldDimension.FOUR_AND_FOUR;
            assertDoesNotThrow(() -> fileSystemGameDataDao.save(gameData, fieldDimension));
            GameData stored = fileSystemGameDataDao.getByDimension(fieldDimension);
            assertEquals(gameData, stored);
        }
    }
}