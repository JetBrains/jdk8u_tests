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

public class InnerClasses extends Attribute {
	
	public class inner_class extends Defined {
		
		public static final int INNER_CLASS_INFO_INDEX		= 0;
		public static final int OUTER_CLASS_INFO_INDEX		= 1;
		public static final int INNER_NAME_INDEX			= 2;
		public static final int INNER_CLASS_ACCESS_FLAGS	= 3;

		private short inner_class_info_index;
		private short outer_class_info_index;
		private short inner_name_index;
		private short inner_class_access_flags;
		
		public inner_class() {
			super(4);
		}
		
		public inner_class(short icii, short ocii, short ini, short icaf) {
			super(4);
			setInnerClassInfoIndex(icii);
			setOuterClassInfoIndex(ocii);
			setInnerNameIndex(ini);
			setInnerClassAccessFlags(icaf);
		}
		
		public short getInnerClassInfoIndex() {
			return inner_class_info_index;
		}
		
		public short getOuterClassInfoIndex() {
			return outer_class_info_index;
		}
		
		public short getInnerNameIndex() {
			return inner_name_index;
		}
		
		public short getInnerClassAccessFlags() {
			return inner_class_access_flags;
		}
		
		public void setInnerClassInfoIndex(short icii) {
			inner_class_info_index = icii;
			setDefined(INNER_CLASS_INFO_INDEX);
		}
		
		public void setOuterClassInfoIndex(short ocii) {
			outer_class_info_index = ocii;
			setDefined(OUTER_CLASS_INFO_INDEX);
		}
		
		public void setInnerNameIndex(short ini) {
			inner_name_index = ini;
			setDefined(INNER_NAME_INDEX); 
		}
		
		public void setInnerClassAccessFlags(short flags) {
			inner_class_access_flags = flags;
			setDefined(INNER_CLASS_ACCESS_FLAGS);
		}
	};

	public static final int NUMBER_OF_CLASSES = 2; 
	
	private short number_of_classes;
	private Vector classes = new Vector();
	
	public InnerClasses() {
		super(3);
	}

	public short getNumberOfClasses() {
		return number_of_classes;
	}

	public void setNumberOfClasses(short noc) {
		number_of_classes = noc;
		setDefined(NUMBER_OF_CLASSES);	
	}

	public void addClass(inner_class ic) {
		classes.add(ic);
	}

	public inner_class classAt(int index) {
		return (inner_class) classes.elementAt(index);
	}

	public int getClassesSize() {
		return classes.size();
	}
}
