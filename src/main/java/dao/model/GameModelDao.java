package dao.model;

import model.GameModel;
import enums.FieldDimension;
import exception.FetchException;
import exception.StoreException;

public interface GameModelDao {

    void save(GameModel gameModel, FieldDimension fieldDimension) throws StoreException;

    GameModel getByDimension(FieldDimension fieldDimension) throws FetchException;

}
