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
 * Created on 15.06.2006
 */
package org.apache.harmony.vmtt.ir.attributes;

/**
 * @author aesin
 */
public class RuntimeVisibleParameterAnnotations extends Attribute {

	RuntimeVisibleAnnotations [] annotations;
	
	byte num_parameters; //for testing purpose, should be annotations.length in normal case
	/**
	 * 
	 */
	public RuntimeVisibleParameterAnnotations() {
		super(3);
	}
	
	public void setAnnotations(RuntimeVisibleAnnotations [] annotations) {
		this.annotations = annotations;
	}
	
	public RuntimeVisibleAnnotations [] getAnnotations() {
		return annotations;
	}

	public void setNumParameters(byte num_parameters) {
		this.num_parameters = num_parameters;
	}

	
	public byte getNumParameters() {
		return num_parameters;
	}
}
