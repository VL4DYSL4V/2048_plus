package dao.model;

import enums.FieldDimension;
import exception.FetchException;
import exception.StoreException;
import model.GameModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;

@Repository("modelDao")
public final class FileSystemGameModelDao implements GameModelDao {

    private final Map<FieldDimension, Path> repositories;

    @Autowired
    public FileSystemGameModelDao(@Qualifier("repositories") Map<FieldDimension, Path> repositories) {
        this.repositories = repositories;
        try {
            createRepositories(repositories.values());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(GameModel gameModel, FieldDimension fieldDimension) throws StoreException {
        Path path = repositories.get(fieldDimension);
        if (path == null) {
            throw new StoreException("No such path");
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(path.toFile());
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(bufferedOutputStream)) {
            gameModel.writeExternal(objectOutputStream);
        } catch (IOException e) {
            throw new StoreException(e);
        }
    }

    @Override
    public GameModel getByDimension(FieldDimension fieldDimension) throws FetchException {
        Path path = repositories.get(fieldDimension);
        if (path == null) {
            throw new FetchException("No such path");
        }
        try (FileInputStream fileInputStream = new FileInputStream(path.toFile());
             BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
             ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream)) {
            GameModel out = new GameModel();
            out.readExternal(objectInputStream);
            return out;
        } catch (EOFException e) {
            return new GameModel(fieldDimension);
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
