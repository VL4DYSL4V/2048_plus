package controller;

public interface CommandExecutor {

    void execute(Runnable runnable);

    void shutdown();

}
