package handler;

public interface CommandHandler {

    void execute(Runnable runnable);

    void shutdown();

}
