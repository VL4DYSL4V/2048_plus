package controller.restart;

import dao.model.GameModelDao;
import enums.FieldDimension;
import exception.StoreException;
import model.GameModel;
import model.Model;
import org.springframework.stereotype.Controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller("restartController")
public final class RestartControllerImpl implements RestartController {

    private final Model model;
    private final GameModelDao gameModelDao;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Object lock = new Object();

    public RestartControllerImpl(Model model, GameModelDao gameModelDao) {
        this.model = model;
        this.gameModelDao = gameModelDao;
    }

    @Override
    public void restart() {
        synchronized (lock) {
            executorService.execute(() -> {
                GameModel gameModel;
                FieldDimension dimension;
                synchronized (model) {
                    model.reset();
                    gameModel = model.getGameModel();
                    dimension = model.getFieldDimension();
                }
                try {
                    gameModelDao.save(gameModel, dimension);
                } catch (StoreException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
