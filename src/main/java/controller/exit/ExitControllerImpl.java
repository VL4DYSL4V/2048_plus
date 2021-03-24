package controller.exit;

import org.springframework.stereotype.Controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller("exitController")
public final class ExitControllerImpl implements ExitController {

    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Object lock = new Object();

    @Override
    public void exitAndSave(Runnable savingTask) {
        synchronized (lock) {
            executorService.submit(() -> {
                savingTask.run();
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
