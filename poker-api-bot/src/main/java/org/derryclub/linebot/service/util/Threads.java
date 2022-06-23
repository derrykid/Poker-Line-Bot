package org.derryclub.linebot.service.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class Threads {

    private static Threads instance;
    private final ExecutorService executor;

    private Threads() {
        executor = Executors.newCachedThreadPool();
    }

    public static ExecutorService getExecutor() {
        if (instance == null) {
            instance = new Threads();
        }
        return instance.executor;
    }

}
