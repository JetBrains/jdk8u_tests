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

public class ConstantNum64 extends Constant {
	
	private int high_bytes;
	private int low_bytes;
	
	public ConstantNum64(byte t) {
		tag = t;
	}
	
	public ConstantNum64(byte t, int lb, int hb) {
		this(t);
		high_bytes = hb;
		low_bytes = lb;
	}

	public String getValue() {
		long bits = ((long)high_bytes << 32) + 
		            ((long) low_bytes & 0xFFFFFFFFL);
		if (tag == CPTags.CONSTANT_Long) {
			return Long.toString(bits);
		}
		return Double.toString(Double.longBitsToDouble(bits));
	}
	
	public void setLowBytes(int lb) {
		low_bytes = lb;
	}
	
	public void setHighBytes(int hb) {
		high_bytes = hb;
	}
	
	public int getLowBytes() {
		return low_bytes;
	}
	
	public int getHighBytes() {
		return high_bytes;
	}

	protected void writeData(DataOutputStream dos)
		throws IOException {
		dos.writeInt(high_bytes);
		dos.writeInt(low_bytes);
	}
	
	protected void readData(DataInputStream dis)
		throws IOException {
		high_bytes = dis.readInt();
		low_bytes = dis.readInt();
	}
}
