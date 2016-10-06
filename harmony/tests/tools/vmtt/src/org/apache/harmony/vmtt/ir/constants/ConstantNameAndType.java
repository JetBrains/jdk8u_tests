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

public class ConstantNameAndType extends Constant {

	private short name_index;
	private short descriptor_index;
	
	public ConstantNameAndType() {
		tag = CPTags.CONSTANT_NameAndType;
	}
	
	public ConstantNameAndType(short ni, short di) {
		this();
		name_index = ni;
		descriptor_index = di;
	}

	public String getValue() {
		return "#" + Short.toString(name_index) +
			   " #" + Short.toString(descriptor_index);
	}
	
	public void setNameIndex(short index) {
		name_index = index;
	}
	
	public void setDescriptorIndex(short index) {
		descriptor_index = index;
	}
	
	public short getNameIndex() {
		return name_index;
	}
	
	public short getDescriptorIndex() {
		return descriptor_index;
	}
	
	protected void writeData(DataOutputStream dos)
		throws IOException {
		
		dos.writeShort(name_index);
		dos.writeShort(descriptor_index);
	}
	
	protected void readData(DataInputStream dis)
		throws IOException {
		
		name_index = dis.readShort();
		descriptor_index = dis.readShort();
	}
}
