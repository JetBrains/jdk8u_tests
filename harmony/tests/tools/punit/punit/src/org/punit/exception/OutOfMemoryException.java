/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.exception;

/**
 * Indicates framework memory problem.
 */
public class OutOfMemoryException extends PUnitException {

    private static final long serialVersionUID = -2694385721230707311L;

    public OutOfMemoryException(OutOfMemoryError e) {
        super("Framework didn't release allocated memory", e);
    }
}
