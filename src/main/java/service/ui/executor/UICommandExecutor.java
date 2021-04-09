package service.ui.executor;

import org.springframework.stereotype.Controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller("uiCommandExecutor")
public final class UICommandExecutor implements CommandExecutor {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public void execute(Runnable runnable) {
        executorService.execute(runnable);
    }

    @Override
    public void shutdown() {
        executorService.shutdown();
    }


}
