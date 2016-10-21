/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.runner;

import java.util.concurrent.*;
import org.punit.exception.*;

public class ExecutorPoolImpl implements ExecutorPool {

    private static final long serialVersionUID = 642472113631919417L;

    private int _threadCount;

    private transient ExecutorService _pool;

    public ExecutorPoolImpl(int count) {
        if (count < 1) {
            throw new IllegalArgumentException();
        }
        _threadCount = count;
    }

    public void execute(Runnable task) {
        _pool.execute(task);
    }

    public void join() {
        try {
            _pool.shutdown();
            _pool.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
    }
    
    public void start() {
        _pool = Executors.newFixedThreadPool(_threadCount);
    }

}
