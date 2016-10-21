/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.watcher;

import org.punit.message.Messages;

public class TimeWatcher extends AbstractWatcher {

    private static final long serialVersionUID = -1374581076649027519L;

    private transient long _startTime;
    
    private transient long _value;
    
    private static final int _SCALE = 1000000;
    
    public void start() {
        _startTime = System.nanoTime();
    }

    public void stop() {
        _value = System.nanoTime() - _startTime;
    }
    
    public double value() {
    	return ((double)_value)/_SCALE;
    }

    public String punitName() {
        return Messages.getString("watcher.time"); //$NON-NLS-1$
    }

    public String unit() {
        return Messages.getString("watcher.ms"); //$NON-NLS-1$ 
    }
}
