package service.saver;

import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service("gameSaver")
public final class GameSaver implements PeriodicalSavingService{

    private final Runnable savingTask;
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    private Future<?> savingHolder;
    private final Object myLock = new Object();

    public GameSaver(Runnable savingTask) {
        this.savingTask = savingTask;
    }

    @Override
    public void start() {
        synchronized (myLock) {
            if (savingHolder == null) {
                savingHolder = scheduledExecutorService.scheduleAtFixedRate(savingTask, 10, 10, TimeUnit.SECONDS);
            }
        }
    }

    @Override
    public void stop() {
        synchronized (myLock){
            if(savingHolder != null){
                savingHolder.cancel(false);
                savingHolder = null;
            }
        }
    }
}
