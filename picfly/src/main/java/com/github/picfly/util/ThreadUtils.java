package com.github.picfly.util;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Utility class for thread management in PicFly.
 */
public class ThreadUtils {
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(
            Math.max(2, Runtime.getRuntime().availableProcessors()));
    private static final Handler MAIN_THREAD_HANDLER = new Handler(Looper.getMainLooper());

    /**
     * Executes a task on a background thread.
     *
     * @param runnable The task to execute
     */
    public static void executeInBackground(Runnable runnable) {
        EXECUTOR_SERVICE.execute(runnable);
    }

    /**
     * Executes a task on the main thread.
     *
     * @param runnable The task to execute
     */
    public static void executeOnMainThread(Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
        } else {
            MAIN_THREAD_HANDLER.post(runnable);
        }
    }

    /**
     * Checks if the current thread is the main thread.
     *
     * @return true if the current thread is the main thread, false otherwise
     */
    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
