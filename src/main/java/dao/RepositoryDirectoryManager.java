package dao;

import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

@Component
public final class RepositoryDirectoryManager {

    private final Properties repositoryDirectoryProperties;

    @Autowired
    public RepositoryDirectoryManager(Properties repositoryDirectoryProperties) {
        this.repositoryDirectoryProperties = repositoryDirectoryProperties;
        Path repositoryDirectory = getRepositoryDirectory();
        createDirectoriesIfNotExists(repositoryDirectory);
    }

    private void createSubDirectories(String name){
        Path subDirectoryPath = getRepositoryDirectory().resolve(name);
        createDirectoriesIfNotExists(subDirectoryPath);
    }

    private void createFile(String name){
        Path filePath = getRepositoryDirectory().resolve(name);
        createFileIfNotExists(filePath);
    }

    public void createFile(String parentDirectory, String name){
        createSubDirectories(parentDirectory);
        Path path = Paths.get(parentDirectory).resolve(Paths.get(name));
        createFile(path.toString());
    }

    public Path getPathForRepository(String directoryName, String fileName){
        Path path = Paths.get(directoryName).resolve(Paths.get(fileName));
        return getRepositoryDirectory().resolve(path);
    }

    public Path getRepositoryDirectory() {
        AppDirs appDirs = AppDirsFactory.getInstance();
        String repositoryDirectoryName = repositoryDirectoryProperties.getProperty("repository-directory-name");
        String dataDirectory = appDirs.getUserDataDir(repositoryDirectoryName, null, null);
        return Paths.get(dataDirectory);
    }

    private void createDirectoriesIfNotExists(Path directoryPath) {
        if (Files.notExists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void createFileIfNotExists(Path filePath){
        if (Files.notExists(filePath)) {
            try {
                Files.createFile(filePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
