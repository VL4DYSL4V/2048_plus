package view.context;

import view.theme.Theme;

import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@ThreadSafe
public final class RenderingContext implements ThemeHolder {

    private Theme theme;

    private final Lock readLock;
    private final Lock writeLock;

    public RenderingContext() {
        ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);
        this.readLock = readWriteLock.readLock();
        this.writeLock = readWriteLock.writeLock();
    }

    @Override
    public void setTheme(Theme theme) {
        writeLock.lock();
        try {
            this.theme = theme;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public Theme getTheme() {
        readLock.lock();
        try {
            return theme;
        } finally {
            readLock.unlock();
        }
    }

}
