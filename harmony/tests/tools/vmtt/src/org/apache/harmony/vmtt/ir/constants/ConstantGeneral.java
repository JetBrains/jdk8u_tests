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

public class ConstantGeneral extends Constant {
	
	private byte[] bytes = null;
	
	public ConstantGeneral(byte t) {
		tag = t;
	}

	public ConstantGeneral(byte[] b) {
		if (b == null) {
			throw new NullPointerException();
		}
		if (b.length > 0) {
			tag = b[0];
		}
		bytes = new byte[b.length - 1];
		System.arraycopy(b, 1, bytes, 0, bytes.length);
	}
	
	public byte[] getBytes() {
		return bytes;
	}
	
	public byte byteAt(int index) {
		return bytes[index];
	}
	
	public void setBytes(byte[] b) {
		if (b == null) {
			return;
		}
		if (b.length > 0) {
			tag = b[0];
		}
		bytes = new byte[b.length - 1];
		System.arraycopy(b, 1, bytes, 0, bytes.length);
	}
	
	public int getBytesLength() {
		return bytes.length;
	}

	public String getValue() {
		return null;
	}

	public boolean isGeneral() {
		return true;
	}
	
	protected void writeData(DataOutputStream dos)
		throws IOException {
		
		dos.write(bytes);
	}
	
	protected void readData(DataInputStream dis)
		throws IOException {
		
		dis.read(bytes);
	}
}
