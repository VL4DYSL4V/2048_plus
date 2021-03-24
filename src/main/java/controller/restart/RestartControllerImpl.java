package controller.restart;

import dao.model.GameModelDao;
import model.Model;
import org.springframework.stereotype.Controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller("restartController")
public final class RestartControllerImpl implements RestartController {

    private final Model model;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Object lock = new Object();
    private final Runnable savingTask;

    public RestartControllerImpl(Model model, Runnable savingTask) {
        this.model = model;
        this.savingTask = savingTask;
    }

    @Override
    public void restart() {
        synchronized (lock) {
            executorService.execute(() -> {
                model.reset();
                savingTask.run();
            });
        }
    }
}
