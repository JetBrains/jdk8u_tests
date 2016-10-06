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
package org.apache.harmony.vmtt.ir.constants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Aleksey V. Golubitsky
 * @version $Revision: 1.2 $
 */

public class ConstantRef extends Constant {

	private short class_index;
	private short name_and_type_index;
	
	public ConstantRef(byte t) {
		tag = t;
	}
	
	public ConstantRef(byte t, short ci, short nati) {
		this(t);
		class_index = ci;
		name_and_type_index = nati;
	}

	public String getValue() {
		return "#" + Short.toString(class_index) +
		       " #" + Short.toString(name_and_type_index);
	}
	
	public void setClassIndex(short index) {
		class_index = index;
	}
	
	public void setNameAndTypeIndex(short index) {
		name_and_type_index = index;
	}
	
	public short getClassIndex() {
		return class_index;
	}
	
	public short getNameAndTypeIndex() {
		return name_and_type_index;
	}
	
	protected void writeData(DataOutputStream dos)
	throws IOException {
		dos.writeShort(class_index);
		dos.writeShort(name_and_type_index);
	}
	
	protected void readData(DataInputStream dis)
	throws IOException {
		class_index = dis.readShort();
		name_and_type_index = dis.readShort();
	}
}
