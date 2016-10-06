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

import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author Aleksey V. Golubitsky
 * @version $Revision: 1.2 $
 */

public abstract class Constant {
	
	protected byte tag;
	
	public byte getTag() {
		return tag;
	}
	
	public void setTag(byte t) {
		tag = t;
	}
	
	public abstract String getValue();

	public String getType() {
		return CPTags.getConstantType(tag);
	}

	public String toString() {
		return getType() + " = " + getValue();
	}
	
	public boolean isGeneral() {
		return false;
	}
	
	public void write(DataOutputStream dos)
		throws IOException {
	
		write(dos, false);
	}

	public void write(DataOutputStream dos, boolean writeTag)
		throws IOException {
		
		if (writeTag) {
			writeTag(dos);
		}
		writeData(dos);
	}
	
	public void read(DataInputStream dis)
		throws IOException {
		
		read(dis, false);
	}

	public void read(DataInputStream dis, boolean readTag)
		throws IOException {
		
		if (readTag) {
			readTag(dis);
		}
		readData(dis);
	}

	protected void writeTag(DataOutputStream dos)
		throws IOException {
	
		dos.write(tag);
	}

	protected void readTag(DataInputStream dis)
		throws IOException {
	
		tag = dis.readByte();
	}

	protected abstract void writeData(DataOutputStream dos)
		throws IOException;
	
	protected abstract void readData(DataInputStream dis)
		throws IOException;
}
