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
 * Created on 05.06.2006
 *
 */
package org.apache.harmony.vmtt.ir.attributes;

/**
 * @author aesin
 *
 */
public class ElementValue {

	public static abstract class value {
	}
	
	public static class const_value extends value {
		private short const_value_index;
		
		public const_value(short const_value_index) {
			this.const_value_index = const_value_index;
		}
		
		public short getConstValueIndex() {
			return const_value_index;
		}
		
		public void setConstValueIndex(short val) {
			const_value_index = val;
		}
	}
	
	public static class enum_value extends value {
		private short type_name_index;
		private short const_name_index;
		
		public enum_value(short type_name_index, short const_name_index) {
			this.type_name_index = type_name_index;
			this.const_name_index = const_name_index;
		}
		
		public short getTypeNameIndex() {
			return type_name_index;
		}
		
		public void setTypeNameIndex(short type_name_index) {
			this.type_name_index = type_name_index;
		}
		
		public short getConstNameIndex() {
			return const_name_index;
		}
		
		public void setConstNameIndex(short const_name_index) {
			this.const_name_index = const_name_index;
		}

	}
	
	public static class class_value extends value {
		private short class_info_index;
		
		public class_value(short class_info_index) {
			this.class_info_index = class_info_index;
		}
		public short getClassInfoIndex() {
			return class_info_index;
		}
		
		public void setClassInfoIndex(short val) {
			class_info_index = val;
		}
	}
	
	public static class annotation_value extends value {
		private Annotation annotation;
		
		public annotation_value(Annotation annotation) {
			this.annotation = annotation;
		}
		
		public Annotation getAnnotation() {
			return annotation;
		}
		public void setValue(Annotation annotation) {
			this.annotation = annotation;
		}
	}
	
	public static class array_value extends value {
		private int array_length = 0;
		private ElementValue [] array_value;
		public array_value(ElementValue [] array_value) {
			this.array_value = array_value;
			this.array_length = array_value.length; 
		}
		public array_value(ElementValue [] array_value, int array_length) {
			this.array_value = array_value;
			this.array_length = array_length;
		}
		public int getArrayLength() {
			return array_length;
		}
		
		public void setArrayLength(int array_length) {
			this.array_length = array_length;
		}
		
		public ElementValue [] getArrayValue() {
			return array_value;
		}
		
		public void setArrayValue(ElementValue [] val) {
			array_value = val;
		}
	}
	/**
	 * tag
	 */
	private byte tag;
/**
 * val
 */
	private value val;
	public void setTag(byte tag) {
		this.tag = tag;
	}
	
	public byte getTag() {
		return tag;
	}
	
	public void setValue(value val) {
		this.val= val;
	}
	
	public value getValue() {
		return val;
	}
	
	/**
	 */
	public ElementValue() {
	}
	
	

}
