/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.watcher;

import org.punit.message.Messages;
import org.punit.util.MemoryUtil;
import org.punit.util.ThreadUtil;

public class MemoryWatcher extends AbstractWatcher {

    private static final long serialVersionUID = -8555763191477212847L;
    
    private transient long _startUsedMemory;
    
    private transient boolean _stop;
    
    private transient long _value;

    public void start() {
        _stop = false;
        MemoryUtil.clear();
        _startUsedMemory = MemoryUtil.usedMemory();
        _value = 0; 
        new MemoryWatcherThread("memory watcher thread").start(); //$NON-NLS-1$
    }

    public void stop() {
        _stop = true;
        monitorMemory();
    }
    
    public double value() {
    	return _value;
    }

    private void monitorMemory() {
        long usedMemory = MemoryUtil.usedMemory() - _startUsedMemory;
        if (usedMemory > _value) {
            _value = usedMemory;
        }
    }

    private class MemoryWatcherThread extends Thread {
        public MemoryWatcherThread(String threadName) {
            super(threadName);
        }

        public void run() {
            while (!_stop) {
                monitorMemory();
                ThreadUtil.sleepIgnoreInterruption(10);
            }
        }
    }

    public String punitName() {
        return Messages.getString("watcher.memory"); //$NON-NLS-1$
    }

    public String unit() {
        return Messages.getString("watcher.bytes"); //$NON-NLS-1$
    }
}
