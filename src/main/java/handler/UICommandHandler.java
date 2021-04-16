package handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class UICommandHandler implements CommandHandler {

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
