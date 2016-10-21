/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.method.runner;

import java.util.concurrent.*;

import org.punit.exception.ConcurrentException;

public class ConcurrentMethodRunner extends AbstractMethodRunner {

    private static final long serialVersionUID = 6423244395038097893L;

    private ConcurrentException _concurrentException = new ConcurrentException();

    private int _concurrentCount;

    private transient TestMethodThread[] _threads;
    
    private transient CyclicBarrier _barrier;

    public ConcurrentMethodRunner(int concurrentCount) {
        _concurrentCount = concurrentCount;
    }

    protected void runImpl() throws Throwable {
        startThreads();
        joinThreads();
        if (_concurrentException.size() > 0) {
            throw _concurrentException;
        }
    }

    private void startThreads() {
        int count = _convention.getConcurrentCount(_testInstance, _method);
        int threadCount = count > 0 ? count : _concurrentCount;
        _threads = new TestMethodThread[threadCount];
        _barrier = new CyclicBarrier(threadCount);
        for (int i = 0; i < _threads.length; ++i) {
            _threads[i] = new TestMethodThread(this,
                    "punit concurrent method runner"); //$NON-NLS-1$
        }
        for (int i = 0; i < _threads.length; ++i) {
            _threads[i].start();
        }
    }

    private void joinThreads() {
        for (int i = 0; i < _threads.length; ++i) {
            try {
                _threads[i].join();
            } catch (InterruptedException e) {
                _concurrentException.add(e);
            }
        }
    }

    protected String getCheckMethodName() {
        return "check_" + _method.getName(); //$NON-NLS-1$
    }

    private class TestMethodThread extends Thread {
        ConcurrentMethodRunner _runner;

        public TestMethodThread(ConcurrentMethodRunner runner, String threadName) {
            super(threadName);
            _runner = runner;
        }

        public void run() {
            try {
            	_barrier.await();
                _runner.runMethod();
            } catch (Throwable t) {
                _concurrentException.add(t);
            }
        }
    }
}
