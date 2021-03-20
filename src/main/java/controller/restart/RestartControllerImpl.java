package controller.restart;

import dao.model.ModelDao;
import exception.StoreException;
import model.Model;
import org.springframework.stereotype.Controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller("restartController")
public final class RestartControllerImpl implements RestartController{

    private final Model model;
    private final ModelDao modelDao;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Object lock = new Object();

    public RestartControllerImpl(Model model, ModelDao modelDao) {
        this.model = model;
        this.modelDao = modelDao;
    }

    @Override
    public void restart() {
        synchronized (lock){
            executorService.execute(() -> {
                model.reset();
                try {
                    modelDao.save(model);
                }catch (StoreException e){
                    e.printStackTrace();
                }
            });
        }
    }
}
