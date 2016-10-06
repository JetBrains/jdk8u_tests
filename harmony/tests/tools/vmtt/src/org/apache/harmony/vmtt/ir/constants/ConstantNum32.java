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

public class ConstantNum32 extends Constant {
	
	private int bytes;
	
	public ConstantNum32(byte t) {
		tag = t;
	}

	public ConstantNum32(byte t, int b) {
		this(t);
		bytes = b;
	}

	public String getValue() {
		if (tag == CPTags.CONSTANT_Integer) {
			return Integer.toString(bytes);
		}
		return Float.toString(Float.intBitsToFloat(bytes));
	}
	
	public void setBytes(int b) {
		bytes = b;
	}
	
	public int getBytes() {
		return bytes;
	}

	protected void writeData(DataOutputStream dos)
		throws IOException {
		
		dos.writeInt(bytes);
	}
	
	protected void readData(DataInputStream dis)
		throws IOException {
		
		bytes = dis.readInt();
	}
}
