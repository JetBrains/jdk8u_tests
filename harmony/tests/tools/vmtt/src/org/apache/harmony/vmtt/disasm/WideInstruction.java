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
 * @version $Revision: 1.2 $
 */

public class WideInstruction 
    extends Instruction {

	public WideInstruction() {
		setOpcode(196);
		setMnemonic("wide");
	}
	
	public int read(DataInputStream stream, int cp)
		throws IOException, EOFException {
		
		if (stream == null) {
			throw new NullPointerException();
		}
		
		int total_size = 0;
		byte b = stream.readByte();
		addOperand(new Operand(Operand.OT_VAL, new Byte(b)));
		total_size++;
		
		if ((b & 0xFF) == 132) {
			short _index = stream.readShort();
			addOperand(new Operand(Operand.OT_VAL, new Short(_index)));
			short _const = stream.readShort();
			addOperand(new Operand(Operand.OT_VAL, new Short(_const)));
			total_size += 4;
		} else if (b == 21 || b == 22 || b == 23 || b == 24 || b == 25 ||
				   b == 54 || b == 55 || b == 56 || b == 57 || b == 58 ||
				   (b & 0xFF)== 169) {
			short _index = stream.readShort();
			addOperand(new Operand(Operand.OT_VAL, new Short(_index)));
			total_size += 2;
		}
		
		return total_size;
	}

	public void parse(StreamTokenizer st, DataOutputStream dos)
		throws IOException, CodeFileFormatException {
	    
	    dos.writeByte(opcode);
	    
	    int token = st.nextToken();
	    if (token == StreamTokenizer.TT_WORD) {
	        String instr = st.sval.toLowerCase();
	        if (instr.equals("iinc")) {
	            /* write instruction opcode */
	            dos.writeByte(InstructionsTable.getCode(instr));
	            /* write index */
	            dos.writeShort(parseNumberValue(st));
	            /* write const */
	            dos.writeShort(parseNumberValue(st));
	        } else if (instr.equals("iload") || instr.equals("fload") ||
	                   instr.equals("lload") || instr.equals("dload") ||
	                   instr.equals("aload") || instr.equals("istore") ||
	                   instr.equals("fstore") || instr.equals("lstore") ||
	                   instr.equals("dstore") || instr.equals("astore") ||
	                   instr.equals("ret")) {
	            /* write instruction opcode */
	            dos.writeByte(InstructionsTable.getCode(instr));
	            /* write index */
	            dos.writeShort(parseNumberValue(st));
	        } else {
	            String msg = "Unexpected VM instruction";
	            throw new CodeFileFormatException(st.lineno(), msg);
	        }
	    } else {
	        String msg = "VM instruction is expected after the" +
	                     Utils.quote(mnemonic);
	        throw new CodeFileFormatException(st.lineno(), msg);
	    }
	}
	
	private int parseNumberValue(StreamTokenizer st)
		throws IOException, CodeFileFormatException {
	    
	    int token = st.nextToken();
	    
	    if (token == StreamTokenizer.TT_NUMBER) {
	        return (int) st.nval;
	    } else if (token == StreamTokenizer.TT_WORD) {
	        String hex_str = st.sval;
	        if (Utils.isHex(hex_str)) {
	            int dec = Utils.hexToDec(hex_str);
	            return dec;
	        } else {
	            String msg = Utils.quote(hex_str) +
	                         " is not a hexadecimal number";
	            throw new CodeFileFormatException(st.lineno(), msg);
	        }
	    } else {
	        String msg = "Decimal or hexadecimal number is expected";
	        throw new CodeFileFormatException(st.lineno(), msg);
	    }
	}

	public String toString() {
		StringBuffer str = new StringBuffer(mnemonic + " ");
		byte b = operandAt(0).getValue().byteValue();
		str.append(InstructionsTable.getInstruction(b & 0xFF).getMnemonic() + " ");
		if ((b & 0xFF) == 132) {
			str.append(operandAt(1).getValue().intValue() + " ");
			str.append(operandAt(2).getValue().intValue());
		} else if (b == 21 || b == 22 || b == 23 || b == 24 || b == 25 ||
				   b == 54 || b == 55 || b == 56 || b == 57 || b == 58 ||
				   (b & 0xFF)== 169) {
			str.append(operandAt(1).getValue().intValue());
		}
		return str.toString();
	}
}
