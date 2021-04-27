package dao.game;

import enums.FieldDimension;
import exception.FetchException;
import exception.StoreException;
import model.GameData;

public interface GameDataDao {

    void save(GameData gameData, FieldDimension fieldDimension) throws StoreException;

    GameData getByDimension(FieldDimension fieldDimension) throws FetchException;

}
