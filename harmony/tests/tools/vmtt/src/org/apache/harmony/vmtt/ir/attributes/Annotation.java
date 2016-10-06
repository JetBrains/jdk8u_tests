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

/**
 * @author aesin
s
 */
public class Annotation {

	
	ElementValuePair [] elementValuePairs;
	
	short type_index;
	
	short num_element_values; //for testing purpose, should be equal to elementValues.length in normal case
	/**
	 * @param parameters_count
	 */
	public Annotation() {
	}
			
	public void setElementValuePairs(ElementValuePair [] elementValuePairs) {
		this.elementValuePairs = elementValuePairs;
	}
	
	public ElementValuePair [] getElementValuePairs() {
		return elementValuePairs;
	}
	
	public void setTypeIndex(short type_index) {
		this.type_index = type_index;
	}
	
	public short getTypeIndex() {
		return type_index;
	}
	
	
	public void setNumElementValues(short num_element_values) {
		this.num_element_values = num_element_values;
	}
	
	public short getNumElementValues() {
		return num_element_values;
	}

}
