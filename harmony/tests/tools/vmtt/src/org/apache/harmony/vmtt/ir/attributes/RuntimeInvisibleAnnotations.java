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
public class RuntimeInvisibleAnnotations extends Attribute {

	
	Annotation [] annotations;
	
	short num_annotations; //for testing purpose, should be annotations.length in normal case
	/**
	 * 
	 */
	public RuntimeInvisibleAnnotations() {
		super(3);
	}
	
	public void setAnnotations(Annotation [] annotations) {
		this.annotations = annotations;
	}
	
	public Annotation [] getAnnotations() {
		return annotations;
	}

	public void setNumAnnotations(short num_annotations) {
		this.num_annotations = num_annotations;
	}

	
	public short getNumAnnotations() {
		return num_annotations;
	}
}
