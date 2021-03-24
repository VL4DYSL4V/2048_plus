package controller.moveBack;

import model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller("moveBackController")
public final class MoveBackControllerImpl implements MoveBackController {

    private final Model model;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Object lock = new Object();

    @Autowired
    public MoveBackControllerImpl(Model model) {
        this.model = model;
    }

    @Override
    public void moveBack() {
        synchronized (lock) {
            executorService.execute(() -> {
                synchronized (model) {
                    if (model.restore()) {
                        model.setGameIsOver(false);
                    }
                }
            });
        }
    }

}
