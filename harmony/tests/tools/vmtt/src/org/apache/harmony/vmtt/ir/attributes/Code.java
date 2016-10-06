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
package org.apache.harmony.vmtt.ir.attributes;

import java.util.ArrayList;

import org.apache.harmony.vmtt.ir.Defined;

/**
 * @author Aleksey V. Golubitsky
 * @version $Revision: 1.2 $
 */

public class Code 
    extends Attribute 
    implements Attributes {
	
	public class exception extends Defined {
		
		public static final int START_PC	= 0;
		public static final int END_PC		= 1;
		public static final int HANDLER_PC	= 2;
		public static final int CATCH_TYPE	= 3;
		
		private short start_pc;
		private short end_pc;
		private short handler_pc;
		private short catch_type;
		
		/** Creates new uninitialized excption */
		public exception() {
			super(4);
		}
		
		/** Creates new exception
		 * @param spc start PC
		 * @param epc end PC
		 * @param hpc handler PC
		 * @param ct catch type
		 */
		public exception(short spc, short epc, short hpc, short ct) {
			this();
			setStartPC(spc);
			setEndPC(epc);
			setHandlerPC(hpc);
			setCatchType(ct);
		}

		/** Sets start PC
		 * @param spc start PCs
		 */
		public void setStartPC(short spc) {
			start_pc = spc;
			setDefined(START_PC);
		}
		
		/** Returns start PC
		 * @return start PC
		 */
		public short getStartPC() {
			return start_pc;
		}
		
		/** Sets end PC
		 * @param epc end PC
		 */
		public void setEndPC(short epc) {
			end_pc = epc;
			setDefined(END_PC);
		}
		
		/** Returns end PC
		 * @return end PC
		 */
		public short getEndPC() {
			return end_pc;
		}

		/** Sets handler PC
		 * @param hpc handler PC
		 */
		public void setHandlerPC(short hpc) {
			handler_pc = hpc;
			setDefined(HANDLER_PC);
		}
		
		/** Returns handler PC
		 * @return handler PC
		 */
		public short getHandlerPC() {
			return handler_pc;
		}

		/** Sets catch type
		 * @param ct catch type
		 */
		public void setCatchType(short ct) {
			catch_type = ct;
			setDefined(CATCH_TYPE);
		}
		
		/** Returns catch type
		 * @return catch type
		 */
		public short getCatchType() {
			return catch_type;
		}

		/** Returns a string representation of this exception. The string
		 *  representation contains fields of this exceptions separated by
		 *  the single space
		 * @return a string representation of this exception
		 */
		public String toString() {
			String str = Integer.toString(start_pc) + " " +
						 Integer.toString(end_pc) + " " +
						 Integer.toString(handler_pc) + " " +
						 Integer.toString(catch_type);
			return str;
		}
	};

	public static final int MAX_STACK				= 2;
	public static final int MAX_LOCALS				= 3;
	public static final int CODE_LENGTH				= 4;
	public static final int EXCEPTION_TABLE_LENGTH	= 5;
	public static final int ATTRIBUTES_COUNT		= 6;
	
	private short max_stack;
	private short max_locals;
	private int code_length;
	private ArrayList code = new ArrayList();
	private short exception_table_length;
	private ArrayList exception_table = new ArrayList();
	private short attributes_count;
	private ArrayList attributes = new ArrayList();
	
	/**
	 * Creates new uninitialized code attribute
	 */
	public Code() {
		super(7);
	}

	/** Returns max_stack value of this code attribute
	 * @return max_stack value 
	 */
	public short getMaxStack() {
		return max_stack;
	}
	
	/** Sets max_stack of this code attribute
	 * @param ms new max_stack value
	 */
	public void setMaxStack(short ms) {
		max_stack = ms;
		setDefined(MAX_STACK);
	}

	/** Returns max_locals value of this code attribute
	 * @return max_locals value
	 */
	public short getMaxLocals() {
		return max_locals;
	}
	
	/** Sets max_locals value of this code attribute
	 * @param ml max_locals value
	 */
	public void setMaxLocals(short ml) {
		max_locals = ml;
		setDefined(MAX_LOCALS);
	}

	/** Returns length of the exception table of this code attribute
	 * @return length of the exception table
	 */
	public short getExceptionTableLength() {
		return exception_table_length;
	}

	/** Sets exception table length of this code attribute
	 * @param length new exception table length
	 */
	public void setExceptionTableLength(short length) {
		exception_table_length = length;
		setDefined(EXCEPTION_TABLE_LENGTH);
	}

	/** Returns actual length of the exception table of this code attribute
	 * @return actual length of the exception table
	 */
	public int getActualExceptionTableLength() {
		return exception_table.size();
	}

	
	/** Add new exception to this code attribute
	 * @param e exception
	 * @throws NullPointerException if e argument is null
	 */
	public void addException(exception e)
		throws NullPointerException {
	    if (e == null) {
	        throw new NullPointerException();
	    }
		exception_table.add(e);
	}

	/** Rerturns exception at index i of this code attribute 
	 * @param i index of exception in the exception table of this code attribute
	 * @return exception
	 * @throws ArrayIndexOutOfBoundsException if the i is negative or not 
	 * less than the current size of the exception table
	 */
	public exception exceptionAt(int i) 
		throws ArrayIndexOutOfBoundsException {
		return (exception) exception_table.get(i);
	}
	
	/** Returns length of code of this code attribute
	 * @return length of code
	 */
	public int getCodeLength() {
		return code_length;
	}

	/** Returns actual length of code of this code attribute
	 * @return actual length of code
	 */
	public int getActualCodeLength() {
		return code.size();
	}
	
	/** Sets length of code of this code attribute
	 * @param length new length of code
	 */
	public void setCodeLength(int length) {
		code_length = length;
		setDefined(CODE_LENGTH);
	}

	/** Rerturns byte value of code at index i of this code attribute 
	 * @param i index of code in the code table of this code attribute
	 * @return byte value of code
	 * @throws ArrayIndexOutOfBoundsException if the i is negative or not 
	 * less than the current size of the code table
	 */
	public byte codeAt(int i) 
		throws ArrayIndexOutOfBoundsException {
		return ((Byte) code.get(i)).byteValue();
	}

	/** Returns code table of this code attribute as byte array
	 * @return code represented as byte array
	 */
	public byte[] getCodeAsByteArray() {
		byte[] bytes = new byte[code.size()];
		for (int i = 0; i < code.size(); i++) {
			bytes[i] = codeAt(i);
		}
		return bytes;
	}
	
	/** Add new code (instruction or instruction parameter) to this code attribute
	 * @param b code (instruction or instruction parameter)
	 */
	public void addCode(byte b) {
		code.add(new Byte(b));
	}

	/** Sets code of code attribute
	 * @param bytes code represented as byte array
	 * @throws NullPointerException if bytes argument is null
	 */
	public void setCode(byte[] bytes)
		throws NullPointerException {
		if (bytes == null) {
			throw new NullPointerException();
		}
		code.clear();
		for (int i = 0; i < bytes.length; i++) {
			addCode(bytes[i]);
		}
	}
	
	/** Sets count of attributes of this code attribute
	 * @param count count of attributes
	 */
	public void setAttributesCount(short count) {
		attributes_count = count;
		setDefined(ATTRIBUTES_COUNT);
	}
	
	/** Returns count of attributes of this code attribute
	 * @return count of attributes
	 */
	public short getAttributesCount() {
		return attributes_count;
	}
	
	/** Returns actual count of attributes of this code attribute
	 * @return actual count of attributes
	 */
	public int getActualAttributesCount() {
		return attributes.size();
	}

	/** Add new attribute to this code attribute
	 * @param attr attribute
	 * @throws NullPointerException if attr argument is null
	 */
	public void addAttribute(Attribute attr)
		throws NullPointerException {
	    if (attr == null) {
	        throw new NullPointerException();
	    }
		attributes.add(attr);
	}
	
	/** Rerturns attribute of code at index i of this code attribute 
	 * @param i index of attribute in the attributes table of this code attribute
	 * @return attribute
	 * @throws ArrayIndexOutOfBoundsException if the i is negative or not 
	 * less than the current size of the attributes table
	 */
	public Attribute attributeAt(int i)
		throws ArrayIndexOutOfBoundsException {
		return (Attribute) attributes.get(i);
	}

	/** Returns attributes 
	 * @return attributes 
	 */
	public ArrayList getAttributes() {
		return attributes;
	}
}
