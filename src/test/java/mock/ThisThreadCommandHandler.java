package mock;

import handler.CommandHandler;

public class ThisThreadCommandHandler implements CommandHandler {

    private boolean isShutdown = false;

    @Override
    public void execute(Runnable runnable) {
        if (!isShutdown) {
            runnable.run();
        }
    }

    @Override
    public void shutdown() {
        isShutdown = true;
    }

}
