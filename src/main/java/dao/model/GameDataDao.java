package dao.model;

import model.GameData;
import enums.FieldDimension;
import exception.FetchException;
import exception.StoreException;

public interface GameDataDao {

    void save(GameData gameData, FieldDimension fieldDimension) throws StoreException;

    GameData getByDimension(FieldDimension fieldDimension) throws FetchException;

}
