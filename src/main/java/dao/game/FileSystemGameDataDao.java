package dao.game;

import enums.FieldDimension;
import exception.FetchException;
import exception.StoreException;
import model.GameData;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;

public final class FileSystemGameDataDao implements GameDataDao {

    private final Map<FieldDimension, Path> repositories;

    public FileSystemGameDataDao(Map<FieldDimension, Path> repositories) {
        this.repositories = repositories;
        try {
            createRepositories(repositories.values());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(GameData gameData, FieldDimension fieldDimension) throws StoreException {
        Path path = repositories.get(fieldDimension);
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
        Path path = repositories.get(fieldDimension);
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

    private void createRepositories(Collection<Path> values) throws IOException {
        for (Path p : values) {
            if (!Files.exists(p)) {
                Files.createFile(p);
            }
        }
    }
}
