/*
   Licensed to the Apache Software Foundation (ASF) under one
   or more contributor license agreements.  See the NOTICE file
   distributed with this work for additional information
   regarding copyright ownership.  The ASF licenses this file
   to you under the Apache License, Version 2.0 (the
   "License"); you may not use this file except in compliance
   with the License.  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing,
   software distributed under the License is distributed on an
   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
   KIND, either express or implied.  See the License for the
   specific language governing permissions and limitations
   under the License.    
*/
package org.apache.harmony.vmtt.disasm;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.EOFException;
import java.io.StreamTokenizer;
import java.util.Vector;

import org.apache.harmony.vmtt.Utils;
import org.apache.harmony.vmtt.ccode.CodeFileFormatException;
import org.apache.harmony.vmtt.ccode.DefaultCodeFileParser;

/**
 * @author Aleksey V. Golubitsky
 * @version $Revision: 1.3 $
 */

public class TableSwitchInstruction extends Instruction {
	
	public TableSwitchInstruction() {
		setOpcode(170);
		setMnemonic("tableswitch");	
	}
	
	public int read(DataInputStream stream, int cp)
		throws IOException, EOFException {
		
		if (stream == null) {
			throw new NullPointerException();
		}

		int total_size = 0;
		cp = (4 - (cp % 4)) - 1;
		for (int c = 0; c < cp; c++, total_size++) {
			stream.readByte();
		}

		int _default = stream.readInt();
		addOperand(new Operand(Operand.OT_VAL, new Integer(_default)));
		int _low = stream.readInt();
		addOperand(new Operand(Operand.OT_VAL, new Integer(_low)));
		int _high = stream.readInt();
		addOperand(new Operand(Operand.OT_VAL, new Integer(_high)));
		total_size += 12;

		int casescount = _high - _low + 1;
		for (int i = 0; i < casescount; i++) {
			int offset = stream.readInt();
			addOperand(new Operand(Operand.OT_VAL, new Integer(offset)));
			total_size += 4;
		}
		return total_size;
	}

	public void parse(StreamTokenizer st, DataOutputStream dos)
		throws IOException, CodeFileFormatException {

	    int token = st.nextToken();
	    if (token == DefaultCodeFileParser.CC_LEFT_CBRACKET) {
	        Vector matches = new Vector();
	        Vector offsets = new Vector();
	        int _default = 0;
	        while ((token = st.nextToken()) != DefaultCodeFileParser.CC_RIGHT_CBRACKET) {
	            if (token == StreamTokenizer.TT_EOL) {
	            } else if (token == StreamTokenizer.TT_NUMBER) {
	                matches.add(new Integer((int) st.nval));
	                offsets.add(new Integer(parseOffset(st)));
	            } else if (token == StreamTokenizer.TT_WORD) {
	                String str = st.sval;
	                if (Utils.isHex(str)) {
	                    matches.add(new Integer(Utils.hexToDec(str)));
	                    offsets.add(new Integer(parseOffset(st)));
	                } else if (str.toLowerCase().equals("default")) {
	                    _default = parseOffset(st);
	                } else {
	                    String msg = "Syntax error in tablewitch instruction";
	                    throw new CodeFileFormatException(st.lineno(), msg);
	                }
	            } else {
	                String msg = "Syntax error in tableswitch instruction";
	                throw new CodeFileFormatException(st.lineno(), msg);
	            }
	        }

	        /* write VM instruction code */
	        dos.writeByte(opcode);

	        /* write padding */
	        writePadding(dos);

	        /* write default */
	        dos.writeInt(_default);
	        
	        int _low = Integer.MAX_VALUE;
	        int _high = Integer.MIN_VALUE;
	        for (int i = 0; i < matches.size(); i++) {
	            int val = ((Integer) matches.elementAt(i)).intValue();
	            _low = (val < _low) ? val : _low;
	            _high = (val > _high) ? val: _high;
	        }

	        /* write low */
	        dos.writeInt(_low);

	        /* write high */
	        dos.writeInt(_high);

	        /* write offsets */
	        for (int i = 0; i < offsets.size(); i++) {
	            dos.writeInt(((Integer) offsets.elementAt(i)).intValue());
	        }
	    } else {
	        String msg = Utils.quote(DefaultCodeFileParser.CC_LEFT_CBRACKET) + 
                         " is expected after the " + Utils.quote(mnemonic);
	        throw new CodeFileFormatException(st.lineno(), msg);
	    }
	}

	private int parseOffset(StreamTokenizer st)
		throws IOException, CodeFileFormatException {
    
	    int token = st.nextToken();
	    if (token == DefaultCodeFileParser.CC_COLON) {
	        token = st.nextToken();
	        if (token == StreamTokenizer.TT_NUMBER) {
	            return (int) st.nval;
	        } else if (token == StreamTokenizer.TT_WORD) {
	            String hex_str = st.sval;
	            if (Utils.isHex(hex_str)) {
	                return Utils.hexToDec(hex_str);
	            } else {
	                String msg = Utils.quote(hex_str) +
                                 " is not a hexadecimal number";
	                throw new CodeFileFormatException(st.lineno(), msg);
	            }
	        } else {
	            String msg = "Decimal or hexadecimal number is expected";
	            throw new CodeFileFormatException(st.lineno(), msg);
	        }
	    } else {
	        String msg = Utils.quote(DefaultCodeFileParser.CC_COLON) +
                         " is expected after the match";
	        throw new CodeFileFormatException(st.lineno(), msg);
	    }
	} 

	private void writePadding(DataOutputStream dos)
		throws IOException {

	    int pad_size = - dos.size() & 3;
	    byte[] padding = new byte[pad_size];
	    dos.write(padding, 0, padding.length);
	}

	public String toString() {
		StringBuffer str = new StringBuffer(mnemonic + " {\n");
		int _default	= operandAt(0).getValue().intValue();
		int _low		= operandAt(1).getValue().intValue();
		int _high		= operandAt(2).getValue().intValue();
		int casescount	= _high - _low + 1;
		for (int i = 0; i < casescount; i++) {
			int offset = operandAt(3 + i).getValue().intValue();
			str.append(_low + i + ": ");
			str.append(offset + "\n");
		}
		str.append("default: " + _default + "\n");
		return str.append("}").toString();
	}
}
