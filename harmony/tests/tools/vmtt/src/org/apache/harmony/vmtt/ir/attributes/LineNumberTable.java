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
/*
 * Created on 18.10.2004
 */

package org.apache.harmony.vmtt.ir.attributes;

import java.util.Vector;

import org.apache.harmony.vmtt.ir.Defined;

/**
 * @author agolubit
 */

public class LineNumberTable extends Attribute {
	
	public class line_number extends Defined {

		public static final int START_PC	= 0;
		public static final int LINE_NUMBER	= 1;
		
		private short start_pc;
		private short line_number;
		
		public line_number() {
			super(2);
		}
		
		public line_number(short spc, short ln) {
			super(2);
			setStartPC(spc);
			setLineNumber(ln);
		}
		
		public short getStratPC() {
			return start_pc;
		}
		
		public short getLineNumber() {
			return line_number;
		}
		
		public void setStartPC(short spc) {
			start_pc = spc;
			setDefined(START_PC);
		}
		
		public void setLineNumber(short ln) {
			line_number = ln;
			setDefined(LINE_NUMBER);
		} 
		
		public String toString() {
			String str = Integer.toString(start_pc) + " " +
						 Integer.toString(line_number);
			return str;
		}
	};

	public static final int LINE_NUMBER_TABLE_LENGTH = 2;
	
	private short line_number_table_length;
	private Vector line_number_table = new Vector();
	
	public LineNumberTable() {
		super(3);
	}

	public void setLineNumberTableLength(short lntl) {
		line_number_table_length = lntl;
		setDefined(LINE_NUMBER_TABLE_LENGTH);
	}
	
	public short getLineNumberTableLength() {
		return line_number_table_length;
	}
	
	public void addLineNumber(line_number ln) {
		line_number_table.add(ln);
	}
	
	public line_number lineNumberAt(int index) {
		return (line_number) line_number_table.elementAt(index);
	}
	
	public int getLineNumberTableSize() {
		return line_number_table.size();
	}
}
