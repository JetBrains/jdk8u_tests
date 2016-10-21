/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.type;

/**
 * If a test implements this interface, PUnit concurrent runner executes
 * the method repeatedly until timeout.
 */
public interface Loop {
    /**
     * @return returns the time to loop
     */
    public long toWork();
}
