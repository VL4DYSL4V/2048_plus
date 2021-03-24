package controller.exit;

import dao.model.GameModelDao;
import enums.FieldDimension;
import exception.StoreException;
import model.GameModel;
import model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller("exitController")
public final class ExitControllerImpl implements ExitController {

    private final Model model;
    private final GameModelDao gameModelDao;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Object lock = new Object();

    @Autowired
    public ExitControllerImpl(Model model, GameModelDao gameModelDao) {
        this.model = model;
        this.gameModelDao = gameModelDao;
    }

    @Override
    public void exitAndSave() {
        synchronized (lock) {
            executorService.submit(() -> {
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
                System.exit(0);
            });
        }
    }

    @Override
    public void exit() {
        synchronized (lock) {
            executorService.submit(() -> System.exit(0));
        }
    }
}
