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
/*
 * Created on 18.10.2004
 */

package org.apache.harmony.vmtt.ir.attributes;

import java.util.Vector;

/**
 * @author agolubit
 */

public class Exceptions extends Attribute {

	public static final int NUMBER_OF_EXCEPTIONS = 2;
	
	private short number_of_exceptions;
	private Vector exception_index_table = new Vector();
	
	public Exceptions() {
		super(3);
	}
	
	public short getNumberOfExceptions() {
		return number_of_exceptions;
	}
	
	public void setNumberOfException(short n) {
		number_of_exceptions = n;
		setDefined(NUMBER_OF_EXCEPTIONS);
	}
	
	public void addException(short exc) {
		exception_index_table.add(new Short(exc));
	}
	
	public short exceptionAt(int index) {
		return ((Short)exception_index_table.elementAt(index)).shortValue();
	}
	
	public int getExceptionTableSize() {
		return exception_index_table.size();
	}
}
