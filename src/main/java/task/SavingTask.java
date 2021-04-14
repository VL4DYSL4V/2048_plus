package task;

import dao.model.GameDataDao;
import enums.FieldDimension;
import exception.StoreException;
import model.GameData;
import model.GameModel;

public final class SavingTask implements Runnable {

    private final GameModel gameModel;
    private final GameDataDao gameDataDao;

    public SavingTask(GameModel gameModel, GameDataDao gameDataDao) {
        this.gameModel = gameModel;
        this.gameDataDao = gameDataDao;
    }

    @Override
    public void run() {
        try {
            GameData gameData;
            FieldDimension dimension;
            boolean gameIsOver;
            synchronized (gameModel) {
                gameData = gameModel.getGameData();
                dimension = gameModel.getFieldDimension();
                gameIsOver = gameModel.gameIsOver();
            }
            if (gameIsOver) {
                gameDataDao.save(new GameData(dimension), dimension);
            } else {
                gameDataDao.save(gameData, dimension);
            }
        } catch (StoreException e) {
            e.printStackTrace();
        }
    }
}
