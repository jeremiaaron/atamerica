package com.example.atamerica.taskhandler;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TaskRunner {

    private static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(5, 128, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    private final Executor executor = Executors.newSingleThreadExecutor(); // change according to your requirements
    private final Handler handler = new Handler(Looper.getMainLooper());

    public interface Callback<R> {
        void onComplete(R result);
    }

    /**
     * executeAsyncSingle()
     * use this task runner for a single pool async runner
     * singe-thread is better for WRITE
     *
     * @param <R> generic type
     * @param callable takes input
     * @param callback return output R
     */
    public <R> void executeAsyncSingle(Callable<R> callable, Callback<R> callback) {
        executor.execute(() -> {
            try {
                final R result = callable.call();
                handler.post(() -> callback.onComplete(result));
            }
            catch (Exception e) {
                Log.e("ERROR", "Error running task.");
                e.printStackTrace();
            }
        });
    }

    /**
     * executeAsyncPool()
     * use this task runner for a multi-threaded pool async runner
     * multi-thread is better for READ
     *
     * @param <R> generic type
     * @param callable takes input
     * @param callback return output R
     */
    public <R> void executeAsyncPool(Callable<R> callable, Callback<R> callback) {
        THREAD_POOL_EXECUTOR.execute(() -> {
            try {
                final R result = callable.call();
                handler.post(() -> callback.onComplete(result));
            }
            catch (Exception e) {
                Log.e("ERROR", "Error running task.");
                e.printStackTrace();
            }
        });
    }
}
