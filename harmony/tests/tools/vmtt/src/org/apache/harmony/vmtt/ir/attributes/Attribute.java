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

package org.apache.harmony.vmtt.ir.attributes;

import org.apache.harmony.vmtt.ir.Defined;

/**
 * @author Aleksey V. Golubitsky
 * @version $Revision: 1.1 $
 */

public abstract class Attribute extends Defined {

	public static final int ATTRIBUTE_NAME_INDEX = 0;
	public static final int ATTRIBUTE_LENGTH     = 1;
	
	protected short attribute_name_index;
	protected int attribute_length;
	
	protected Attribute() {
	}
	
	protected Attribute(int parameters_count) {
		super(parameters_count);
	}
	
	/** Returns name index of the attribute
	 * @return attribute name index
	*/
	public short getAttributeNameIndex() {
		return attribute_name_index;
	}
	
	/** Returns length of the attribute in bytes
	 * @return attribute length in bytes
	*/
	public int getAttributeLength() {
		return attribute_length;
	}
	
	/** Sets name index of the attribute
	 * @param index attribute name index
	*/
	public void setAttributeNameIndex(short index) {
		attribute_name_index = index;
		setDefined(ATTRIBUTE_NAME_INDEX);
	}
	
	/** Sets length of the attribute in bytes
	 * @param length attribute length in bytes
	*/
	public void setAttributeLength(int length) {
		attribute_length = length;
		setDefined(ATTRIBUTE_LENGTH);
	}
	
	/** Returns true if this attribute is General, otherwise false
	 * @return true if this attribute is General, otherwise false
	*/
	public boolean isGeneral() {
		return false;
	}
}
