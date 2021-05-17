package dao;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import testUtils.PropertyUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class RepositoryDirectoryManagerTest {

    private static Properties repositoryDirectoryProperties;
    private RepositoryDirectoryManager repositoryDirectoryManager;

    @BeforeAll
    static void initDirectory() {
        repositoryDirectoryProperties = PropertyUtils.loadByLocation("repository/repository_directory.properties");
    }

    @BeforeEach
    void initManager() {
        this.repositoryDirectoryManager = new RepositoryDirectoryManager(repositoryDirectoryProperties);
    }

    abstract class OperationSystemTest{

        protected final String directoryName = repositoryDirectoryProperties.getProperty("repository-directory-name");
        protected final String userHome = System.getProperty("user.home");

        @Test
        @DisplayName("Repository directory")
        void getRepositoryDirectoryWindows() {
            Path expected = getRepositoryRoot();
            Path path = repositoryDirectoryManager.getRepositoryDirectory();
            assertEquals(expected, path, "Repository directory path is not correct");
        }

        @Test
        void createFile() {
            Path root = getRepositoryRoot();
            assertDoesNotThrow(() -> {
                Path tempPath = Files.createTempFile(root, null, null);
                Files.delete(tempPath);
            });
        }

        @Test
        void getPathForRepository() {
            Path root = getRepositoryRoot();
            String directory = "test/directory";
            String fileName = "testFile.txt";
            Path expected = root.resolve(directory).resolve(fileName);
            Path actual = repositoryDirectoryManager.getPathForRepository(directory, fileName);
            assertEquals(expected, actual, "Wrong path for repository");
        }

        protected abstract Path getRepositoryRoot();

    }

    @Nested
    @EnabledOnOs(OS.WINDOWS)
    class WindowsTest extends OperationSystemTest{

        @Override
        protected Path getRepositoryRoot(){
            String expectedStringPath = String.format("%s\\AppData\\Local\\%s", userHome, directoryName);
            return Paths.get(expectedStringPath);
        }

    }

    @Nested
    @EnabledOnOs(OS.MAC)
    class MacOsTest extends OperationSystemTest{

        @Override
        protected Path getRepositoryRoot() {
            System.out.println(repositoryDirectoryManager.getRepositoryDirectory());
            return null;
        }

    }
}