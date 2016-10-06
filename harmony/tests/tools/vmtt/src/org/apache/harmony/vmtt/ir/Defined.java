/*
    Copyright 2005-2006 The Apache Software Foundation or its licensors, as applicable

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

    See the License for the specific language governing permissions and
    limitations under the License.
*/
package org.apache.harmony.vmtt.ir;

import java.util.Arrays;

/**
 * @author Aleksey V. Golubitsky
 * @version $Revision: 1.4 $
 */

public abstract class Defined {

	private boolean[] defined;
	private boolean[] autodef;
	
	public Defined() {}

	/** Creates new instance of the Defined class
	 * @param size specifies a number of definable parameters, it should be > 0
	 * @throws IllegalArgumentException - if size < 0
	 */
	public Defined(int size)
	throws IllegalArgumentException {
	    if (size < 0) {
	        throw new IllegalArgumentException();
	    }
		defined = new boolean[size];
		autodef = new boolean[size];
		resetAll();
	}
	
	/** Reset defined and autodef flags
	 * @throws IllegalStateException - if defined or autodef flags is non-initialized 
	 * */
	public void resetAll()
	throws IllegalStateException {
		resetDefinedAll();
		resetAutodefAll();
	}

	/** Return <b>true</b> if specified <i>defined</i> flag is defined,
	 *  <b>false</b> if flag is not defined.
	 * @throws IllegalArgumentException - if flag is out of indices of the
	 *                                    defined array
	 * @throws IllegalStateException - if defined flags is non-initialized
	 * */
	public boolean isDefined(int flag)
	throws IllegalArgumentException, IllegalStateException {
		checkState(flag, defined);
		return defined[flag];
	}
	
	/** Set specified defined flag
	 * @throws IllegalArgumentException - if flag is out of indices of the
	 *                                    defined array
	 * @throws IllegalStateException - if defined flags is non-initialized
	 * */
	public void setDefined(int flag)
	throws IllegalArgumentException, IllegalStateException {
		checkState(flag, defined);
		defined[flag] = true;
	}

	/** Reset specified defined flag
	 * @throws IllegalArgumentException - if flag is out of indices of the
	 *                                    defined array
	 * @throws IllegalStateException - if defined flags is non-initialized
	 * */
	public void resetDefined(int flag)
	throws IllegalArgumentException, IllegalStateException {
		checkState(flag, defined);
		defined[flag] = false;
	}

	/** Reset all defined flags
	 * @throws IllegalStateException - if defined flags is non-initialized 
	 * */
	public void resetDefinedAll()
	throws IllegalStateException {
	    checkState(0, defined);
	    Arrays.fill(defined, false);
	}
	
	/** Return <b>true</b> if specified autodef flag is defined, <b>false</b> if
	 *  flag is not defined.
	 * @throws IllegalArgumentException - if flag is out of indices of the
	 *                                    autodef array
	 * @throws IllegalStateException - if autodef flags is non-initialized
	 * */
	public boolean isAutodef(int flag)
	throws IllegalArgumentException, IllegalStateException {
	    checkState(flag, autodef);
	    return autodef[flag];
	}
	
	/** Set specified autodef flag
	 * @throws IllegalArgumentException - if flag is out of indices of the
	 *                                    autodef array
	 * @throws IllegalStateException - if autodef flags is non-initialized
	 * */
	public void setAutodef(int flag)
	throws IllegalArgumentException, IllegalStateException {
	    checkState(flag, autodef);
	    autodef[flag] = true;
	}
	
	/** Reset specified autodef flag
	 * @throws IllegalArgumentException - if flag is out of indices of the
	 *                                    autodef array
	 * @throws IllegalStateException - if autodef flags is non-initialized
	 * */
	public void resetAutodef(int flag)
	throws IllegalArgumentException, IllegalStateException {
	    checkState(flag, autodef);
	    autodef[flag] = false;
	}
	
	/** Reset all autodef flags
	 * @throws IllegalStateException - if autodef flags is non-initialized 
	 * */
	public void resetAutodefAll()
	throws IllegalStateException {
	    checkState(0, autodef);
	    Arrays.fill(autodef, false);
	}
	
	/** Check specified flag and boolean array for accuracy
	 * @param flag flag for checking
	 * @param arr boolean array for checking
	 * @throws IllegalArgumentException - if flag is out of indeces 
	 *                                    of the specified array
	 * @throws IllegalStateException - if arr is null
	 */
	private void checkState(int flag, boolean[] arr)
	throws IllegalArgumentException, IllegalStateException {
	    if (arr == null) {
	        throw new IllegalStateException();
	    }
	    if (flag < 0 || flag >= arr.length) {
	        throw new IllegalArgumentException();
	    }
	}	
}
