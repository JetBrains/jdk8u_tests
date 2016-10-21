/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.exception;

/**
 * The root exception of all PUnit exception.
 */
public abstract class PUnitException extends RuntimeException {

    public PUnitException() {
    }

    public PUnitException(Throwable e) {
        super(e);
    }

    public PUnitException(String message, Throwable e) {
        super(message, e);
    }
}
