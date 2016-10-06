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
import java.util.Vector;

import org.apache.harmony.vmtt.ccode.CodeFileFormatException;

/**
 * @author Aleksey V. Golubitsky
 * @version $Revision: 1.2 $
 */

public abstract class Instruction {

	protected int opcode = -1;
	protected String mnemonic = null;
	protected Vector operands = new Vector();

	public int getOpcode() {
		return opcode;
	}

	public String getMnemonic() {
		return mnemonic;
	}

	public Operand operandAt(int i)
		throws ArrayIndexOutOfBoundsException {
		return (Operand) operands.elementAt(i);
	}

	public int getOperandsCount() {
		return operands.size();
	}

	public void setOpcode(int c) {
		opcode = c;
	}

	public void setMnemonic(String m) {
		mnemonic = m;
	}

	public void addOperand(Operand op) {
		operands.add(op);
	}

	public void setOperands(Operand[] ops) {
		if (ops == null) {
			throw new NullPointerException();
		}
		operands.clear();
		for (int i = 0; i < ops.length; i++) {
			operands.add(ops[i]);
		}
	}

	public abstract int read(DataInputStream stream, int cp)
		throws IOException, EOFException;

	public abstract void parse(StreamTokenizer st, DataOutputStream dos)
	    throws IOException, CodeFileFormatException;

    public abstract String toString();

    public String operandCommentString() {
    	return "";
    }
}
