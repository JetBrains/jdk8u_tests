/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.type;

/**
 * punit uses this name in the logger and result file.
 * 
 */
public interface Name {

    /**
     * @return returns the meaningful name of this object.
     */
    public String punitName();
}
