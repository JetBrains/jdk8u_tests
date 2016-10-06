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
package org.apache.harmony.vmtt.disasm;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.StreamTokenizer;

import org.apache.harmony.vmtt.Utils;
import org.apache.harmony.vmtt.ccode.CodeFileFormatException;

/**
 * @author Aleksey V. Golubitsky
 * @version $Revision: 1.3 $
 */

public class SimpleInstruction extends Instruction {

	public SimpleInstruction(int c, String mnem) {
		setOpcode(c);
		setMnemonic(mnem);
	}
	
	public SimpleInstruction(int c, String mnem, Operand[] ops) {
		setOpcode(c);
		setMnemonic(mnem);
		setOperands(ops);
	}

	public int read(DataInputStream stream, int cp) 
		throws IOException, EOFException {

		if (stream == null) {
			throw new NullPointerException();
		}

		int total_size = 0;
		for (int i = 0; i < operands.size(); i++) {
			Operand op = operandAt(i);
			total_size += op.getSize();
			switch (op.getSize()) {
			case 1:
				op.setValue(new Byte(stream.readByte()));
				break;
			case 2:
				op.setValue(new Short(stream.readShort()));
				break;
			case 4:
				op.setValue(new Integer(stream.readInt()));
				break;
			}
		}
		return total_size;
	}
	
	public void parse(StreamTokenizer st, DataOutputStream dos)
		throws IOException, CodeFileFormatException {
	    
	    /* write VM instruction code */
	    dos.writeByte(opcode);

	    /* write instruction operands */
	    for (int i = 0; i < getOperandsCount(); i++) {
	        Operand op = operandAt(i);
	        int op_size = op.getSize();
	        int token = st.nextToken();
	        if (op.getType() == Operand.OT_REF) {
	            if (token == StreamTokenizer.TT_WORD) {
	                String ref_str = st.sval;
	                if (Utils.isCPRef(ref_str)) {
	                    short ref = Utils.cpRefValue(ref_str);
		                writeData(dos, ref & 0xFFFF, op_size);
	                } else {
	                    String msg = Utils.quote(ref_str) +
                                     " is not a constant pool reference";
	                    throw new CodeFileFormatException(st.lineno(), msg);
	                }
	            } else {
	                String msg = "Constant pool reference is expected";
	                throw new CodeFileFormatException(st.lineno(), msg);
	            }
	        } else if (op.getType() == Operand.OT_VAL) {
	            if (token == StreamTokenizer.TT_WORD) {
	                String hex_str = st.sval;
	                if (Utils.isHex(hex_str)) {
	                    int dec = Utils.hexToDec(hex_str);
		                writeData(dos, dec, op_size);
	                } else {
		                String msg = Utils.quote(hex_str) +
		                             " is not a hexadecimal number";
		                throw new CodeFileFormatException(st.lineno(), msg);
	                }
	            } else if (token == StreamTokenizer.TT_NUMBER) {
	                int dec = (int) st.nval;
	                writeData(dos, dec, op_size);
	            } else {
	                String msg = "Decimal or hexadecimal number is expected";
	                throw new CodeFileFormatException(st.lineno(), msg);
	            }
	        }
	    }
	}
	
	private void writeData(DataOutputStream dos, int data, int op_size)
		throws IOException {

	    switch (op_size) {
        case 1: dos.writeByte(data); break;
        case 2: dos.writeShort(data); break;
        case 4: dos.writeInt(data); break;
        }
	}
	
	public String toString() {
		StringBuffer str = new StringBuffer(mnemonic);
		for (int i = 0; i < operands.size(); i++) {
			str.append(" " + operandAt(i).toString());
		}
		for (int i= 0; i < operands.size(); i++) {
			str.append(" " + operandAt(i).operandCommentString());
		}
		return str.toString();
	}
}
