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

/**
 * @author Aleksey V. Golubitsky
 * @version $Revision: 1.2 $
 */

public class CPTags {

	public static final byte CONSTANT_Utf8					= 1;
	public static final byte CONSTANT_Integer				= 3;
	public static final byte CONSTANT_Float					= 4;
	public static final byte CONSTANT_Long					= 5;
	public static final byte CONSTANT_Double				= 6;
	public static final byte CONSTANT_Class					= 7;
	public static final byte CONSTANT_String				= 8;
	public static final byte CONSTANT_Fieldref				= 9;
	public static final byte CONSTANT_Methodref				= 10;
	public static final byte CONSTANT_InterfaceMethodref	= 11;
	public static final byte CONSTANT_NameAndType			= 12;
	
	private static byte[] tags = {CONSTANT_Utf8, CONSTANT_Integer,
								  CONSTANT_Float, CONSTANT_Long,
								  CONSTANT_Double, CONSTANT_Class,
								  CONSTANT_String, CONSTANT_Fieldref,
								  CONSTANT_Methodref,
								  CONSTANT_InterfaceMethodref,
								  CONSTANT_NameAndType};

	private static String[] types = {"UTF8", "int", "float",
									 "long", "double", "Class",
									 "String", "Field", "Method",
									 "InterfaceMethod", "NameAndType"};

	public static String getConstantType(byte tag) {
		for (int i = 0; i < tags.length; i++) {
			if (tag == tags[i]) {
				return types[i];
			}
		}
		return null;
	}

	public static byte getConstantTag(String str) {
		for (int i = 0; i < types.length; i++) {
			if (str.equals(types[i])) {
				return tags[i];
			}
		}
		return -1;
	}

	public static boolean isConstantTag(String str) {
		if (getConstantTag(str) == -1) {
			return false;
		}
		return true;
	}
}
