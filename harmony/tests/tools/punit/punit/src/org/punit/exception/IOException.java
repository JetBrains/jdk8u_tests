/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.exception;

/**
 * Indicates an IO error.
 */
public class IOException extends PUnitException {

    private static final long serialVersionUID = 7098221975109413569L;

    public IOException(Throwable e) {
        super(e);
    }
}
