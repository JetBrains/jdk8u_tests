/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.exception;

/**
 * Indicates reflection error.
 */
public class ReflectionException extends PUnitException {

	private static final long serialVersionUID = -5498831354775690972L;

	public ReflectionException(Throwable e) {
		super(e);
	}
}
