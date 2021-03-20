package controller.exit;

import dao.model.ModelDao;
import exception.StoreException;
import model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller("exitController")
public final class ExitControllerImpl implements ExitController {

    private final Model model;
    private final ModelDao modelDao;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Object lock = new Object();

    @Autowired
    public ExitControllerImpl(Model model, ModelDao modelDao) {
        this.model = model;
        this.modelDao = modelDao;
    }

    @Override
    public void exitAndSave() {
        synchronized (lock) {
            executorService.submit(() -> {
                try {
                    if (!model.gameIsOver()) {
                        modelDao.save(model);
                    } else {
                        modelDao.save(new Model(model.getFieldDimension()));
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
