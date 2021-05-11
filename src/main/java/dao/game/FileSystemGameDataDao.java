package dao.game;

import dao.RepositoryDirectoryManager;
import enums.FieldDimension;
import exception.FetchException;
import exception.StoreException;
import model.GameData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;

@Repository("gameDataDao")
public final class FileSystemGameDataDao implements GameDataDao {

    private final Map<FieldDimension, Path> fieldDimensionToPathMap = new EnumMap<>(FieldDimension.class);
    private final Properties savedGamesProperties;
    private final RepositoryDirectoryManager repositoryDirectoryManager;

    @Autowired
    public FileSystemGameDataDao(Properties savedGamesProperties,
                                 RepositoryDirectoryManager repositoryDirectoryManager) {
        this.savedGamesProperties = savedGamesProperties;
        this.repositoryDirectoryManager = repositoryDirectoryManager;
        createRepositoriesAndSetupDimensionMap();
    }

    @Override
    public void save(GameData gameData, FieldDimension fieldDimension) throws StoreException {
        Path path = fieldDimensionToPathMap.get(fieldDimension);
        if (path == null) {
            throw new StoreException("No such path");
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(path.toFile());
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(bufferedOutputStream)) {
            gameData.writeExternal(objectOutputStream);
        } catch (IOException e) {
            throw new StoreException(e);
        }
    }

    @Override
    public GameData getByDimension(FieldDimension fieldDimension) throws FetchException {
        Path path = fieldDimensionToPathMap.get(fieldDimension);
        if (path == null) {
            throw new FetchException("No such path");
        }
        try (FileInputStream fileInputStream = new FileInputStream(path.toFile());
             BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
             ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream)) {
            GameData out = new GameData();
            out.readExternal(objectInputStream);
            return out;
        } catch (EOFException e) {
            return new GameData(fieldDimension);
        } catch (IOException | ClassNotFoundException e) {
            throw new FetchException(e);
        }
    }

    private Path getSavedGamePath(String fileName) {
        String directoryName = savedGamesProperties.getProperty("saved-games-directory-name");
        return repositoryDirectoryManager.getPathForRepository(directoryName, fileName);
    }

    private void createRepositoriesAndSetupDimensionMap() {
        String directoryName = savedGamesProperties.getProperty("saved-games-directory-name");
        for (String key : savedGamesProperties.stringPropertyNames()) {
            if (key.startsWith("file.")) {
                String[] nameAndDimension = key.split("\\.");
                if (nameAndDimension.length == 2) {
                    String name = savedGamesProperties.getProperty(key);
                    repositoryDirectoryManager.createFile(directoryName, name);

                    String dimension = nameAndDimension[1].replace("-", "_").toUpperCase();
                    Path savedGamePath = getSavedGamePath(name);
                    fieldDimensionToPathMap.put(FieldDimension.valueOf(dimension), savedGamePath);
                }
            }
        }
    }
}
