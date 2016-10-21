/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.watcher;

import java.util.Iterator;

import org.punit.method.runner.MethodRunner;

/**
 * Basic class for watchers triggered by internal test events such as a callback
 * or an exception.
 * 
 * Here is a custom watcher design. Register a special
 */
public class CustomWatcher extends AbstractWatcher {

    private static final long serialVersionUID = -497434829953274515L;

    private String _name;

    private String _unit;
    
    transient long _value;
    
    transient long _scale;

    private transient static MethodRunner _methodRunner;

    public CustomWatcher(MethodRunner methodRunner, String name, String unit) {
        this(methodRunner, name, unit, 1);
    }

    public CustomWatcher(MethodRunner methodRunner, String name, String unit,
            long scale) {
        _methodRunner = methodRunner;
        _name = name;
        _methodRunner.addWatcher(this);
        _scale = scale;
        _unit = unit;
    }

    public void start() {
    }

    public void stop() {
    }
    
    public double value() {
    	return ((double)_value)/_scale;
    }

    /**
     * Updates all registered <code>CustomWatcher</code> for this method
     * runner.
     */
    public static synchronized void setResult(final String name,
            final long value, final String unit, final long scale) {
        Iterator<Watcher> iter = _methodRunner.watchers().iterator();
        while (iter.hasNext()) {
            try {
                CustomWatcher watcher = (CustomWatcher) iter.next();
                if (watcher.punitName().equals(name)) {
                    watcher.setValue(value, scale);
                    watcher._unit = unit;
                }
            } catch (ClassCastException cce) {
            }
        }
    }

    /**
     * Updates all registered <code>CustomWatcher</code> for this method
     * runner.
     */
    public static synchronized void setResult(final String name,
            final long value) {
        Iterator<Watcher> iter = _methodRunner.watchers().iterator();
        while (iter.hasNext()) {
            try {
                CustomWatcher watcher = (CustomWatcher) iter.next();
                if (watcher.punitName().equals(name)) {
                    watcher.setValue(value, watcher._scale);
                }
            } catch (ClassCastException cce) {
            }
        }
    }

    public static boolean shouldStop() {
        return _methodRunner.shouldStop();
    }

    /**
     * Updates the value for this runner.
     */
    public void setValue(final long value, final long scale) {
        _value = value;
        _scale = scale;
    }

    public String punitName() {
        return _name;
    }

    public String unit() {
        return _unit;
    }
}
