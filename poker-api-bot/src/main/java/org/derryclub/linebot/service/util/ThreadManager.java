package org.derryclub.linebot.service.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public final class ThreadManager {

    private static ThreadManager instance;
    private final ExecutorService executor;
    private final ScheduledExecutorService scheduledExecutorService;

    private ThreadManager() {
        executor = Executors.newCachedThreadPool();
        scheduledExecutorService = Executors.newScheduledThreadPool(8);
    }

    public static ThreadManager getManager() {
        if (instance == null) {
            instance = new ThreadManager();
        }
        return instance;
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public ScheduledExecutorService getScheduledExecutorService() {
        if (instance == null) {
            instance = new ThreadManager();
        }
        return scheduledExecutorService;
    }

}
