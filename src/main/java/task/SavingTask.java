package task;

import dao.model.GameModelDao;
import enums.FieldDimension;
import exception.StoreException;
import model.GameModel;
import model.Model;

public final class SavingTask implements Runnable {

    private final Model model;
    private final GameModelDao gameModelDao;

    public SavingTask(Model model, GameModelDao gameModelDao) {
        this.model = model;
        this.gameModelDao = gameModelDao;
    }

    @Override
    public void run() {
        try {
            GameModel gameModel;
            FieldDimension dimension;
            boolean gameIsOver;
            synchronized (model) {
                gameModel = model.getGameModel();
                dimension = model.getFieldDimension();
                gameIsOver = model.gameIsOver();
            }
            if (gameIsOver) {
                gameModelDao.save(new GameModel(dimension), dimension);
            } else {
                gameModelDao.save(gameModel, dimension);
            }
        } catch (StoreException e) {
            e.printStackTrace();
        }
    }
}
