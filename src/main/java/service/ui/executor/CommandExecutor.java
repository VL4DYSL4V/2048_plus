package service.ui.executor;

public interface CommandExecutor {

    void execute(Runnable runnable);

    void shutdown();

}
