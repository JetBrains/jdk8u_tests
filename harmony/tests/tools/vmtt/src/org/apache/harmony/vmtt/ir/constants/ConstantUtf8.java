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

public class ConstantUtf8 extends Constant {

	private short length = 0;
	private byte[] bytes = null;
	
	public ConstantUtf8() {
		tag = CPTags.CONSTANT_Utf8;
	}
	
	public ConstantUtf8(byte[] bs) {
	    this();
		length = (short) bs.length;
		bytes = new byte[length];
		System.arraycopy(bs, 0, bytes, 0, length);
	}
	
	public String getValue() {
	    if (bytes == null) {
	        throw new NullPointerException();
	    }
		StringBuffer sb = new StringBuffer();
		char[] arr = new String(bytes).toCharArray();
		for (int i = 0; i < arr.length; i++) {
			String str;
			switch (arr[i]) {
			case '\0': str = "\\0"; break;
			case '\r': str = "\\r"; break;
			case '\t': str = "\\t"; break;
			case '\f': str = "\\f"; break;
			case '\n': str = "\\n"; break;
			case '\b': str = "\\b"; break;
			case '\"': str = "\\\""; break;
			case '\\': str = "\\\\"; break;
			default: str = Character.toString(arr[i]); break;
			}
			sb.append(str);
		}
		return ('"' + sb.toString() + '"');
	}
	
	public void setLength(short len) {
		length = len;
	}
	
	public void setBytes(byte[] b) {
		bytes = new byte[b.length];
		System.arraycopy(b, 0, bytes, 0, b.length);
	}
	
	public void setBytes(String str) {
		setBytes(str.getBytes());
	}

	public short getLength() {
		return length;
	}
	
	public byte[] getBytes() {
		return bytes;
	}

	
	protected void writeData(DataOutputStream dos)
		throws IOException {
		
		dos.writeShort(length);
		dos.write(bytes);
	}
	
	protected void readData(DataInputStream dis)
		throws IOException {
		
		length = dis.readShort();
		bytes = new byte[length];
		dis.read(bytes);
	}
}
