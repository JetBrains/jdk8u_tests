/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.type;

/**
 * If a test implements this interface, punit concurrent runner will executes
 * its method concurrently.
 */
public interface Concurrent {
	/**
	 * @return returns the concurrent number for punit concurrent runner.
	 */
	public int concurrentCount();
}
