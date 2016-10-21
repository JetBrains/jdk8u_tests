/* (C) Copyright 2007 */

package org.punit.watcher;

import org.punit.method.runner.MethodRunner;

/**
 * The class accumulates the maximum reported value.
 */
public class MaximumWatcher extends CustomWatcher {

    private static final long serialVersionUID = 383142832201569299L;

    public MaximumWatcher(MethodRunner methodRunner, String name, String unit) {
        super(methodRunner, name, unit);
    }

    public MaximumWatcher(MethodRunner methodRunner, String name, String unit, long scale) {
        super(methodRunner, name, unit, scale);
    }

    public void start() {
        _value = 0;
    }

    /**
     * Updates the value for this runner.
     */
    public void setValue(final long value, final long scale) {
        long dv = value - _value;
        long ds = scale - _scale;
        if (dv * _scale > _value * ds) {
            _value = value;
            _scale = scale;
        }
    }
}
