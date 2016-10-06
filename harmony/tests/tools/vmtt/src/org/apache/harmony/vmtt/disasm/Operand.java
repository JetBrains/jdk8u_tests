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

import org.apache.harmony.vmtt.Utils;
import org.apache.harmony.vmtt.cdecode.DefaultCodeFileGenerator;

/**
 * @author Aleksey V. Golubitsky
 * @version $Revision: 1.2 $
 */

public class Operand {

	public static final int OT_REF	= 0;
	public static final int OT_VAL	= 1;

	private int operand_type = -1;
	private Number operand_value = null;

	public Operand(int type, Number val) {
		setType(type);
		setValue(val);
	}
	
	public void setType(int type) {
		operand_type = type;
	}
	
	public int getType() {
		return operand_type;
	}
	
	public int getSize() {
		if (operand_value instanceof Byte) {
			return 1;
		} else if (operand_value instanceof Short) {
			return 2;
		} else if (operand_value instanceof Integer) {
			return 4;
		}
		return -1;
	}
	
	public void setValue(Number opval) {
		operand_value = opval;
	}
	
	public Number getValue() {
		return operand_value;
	}
	
	public String toString() {
		String str = operand_value.toString(); 
		if (operand_type == OT_REF) {
		    if (getSize() == 1) {
		        return "#" + (operand_value.byteValue() & 0xFF);
		    }
		    //return "#" + str;
		    return "#" + str; 
		}
		return str; 
	}
	
	public String operandCommentString() {
		String str = operand_value.toString(); 
		if (operand_type == OT_REF) {
		    if (getSize() == 1) {
		        return "";
		    }
		    //return comment
		    return "  // " + Utils.refToValue(DefaultCodeFileGenerator.classFile, "#" + str); 
		}
		return ""; 
	}
}
