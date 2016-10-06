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
package org.apache.harmony.vmtt.ir;

import java.util.ArrayList;
import java.util.Vector;

import org.apache.harmony.vmtt.ir.attributes.Attribute;
import org.apache.harmony.vmtt.ir.attributes.Attributes;
import org.apache.harmony.vmtt.ir.constants.Constant;

/**
 * @author Aleksey V. Golubitsky
 * @version $Revision: 1.14 $
 */

public class ClassFile 
    extends Defined 
    implements Attributes {
	
	public static final int MAGIC				= 0;
	public static final int MINOR_VERSION		= 1;
	public static final int MAJOR_VERSION		= 2;
	public static final int CONSTANT_POOL_COUNT	= 3;
	public static final int ACCESS_FLAGS		= 4;
	public static final int THIS_CLASS			= 5;
	public static final int SUPER_CLASS			= 6;
	public static final int INTERFACES_COUNT	= 7;
	public static final int FIELDS_COUNT		= 8;
	public static final int METHODS_COUNT		= 9;
	public static final int ATTRIBUTES_COUNT	= 10;

	private String className;
	private int magic;
	private short minor_version;
	private short major_version;
	private short constant_pool_count;
	private Vector constant_pool = new Vector();
	private short access_flags;
	private short this_class;
	private short super_class;
	private short interfaces_count;
	private ArrayList interfaces = new ArrayList();
	private short fields_count;
	private ArrayList fields = new ArrayList();
	private short methods_count;
	private ArrayList methods = new ArrayList();
	private short attributes_count;
	private ArrayList attributes = new ArrayList();
	
	/** 
	 * Create new uninitialized ClassFile
	 */
	public ClassFile() {
		super(11);
	}
	
	public void setClassName(String className) {
		this.className = className;
	}
	
	public String getClassName() {
		return className;
	}
	
	/** Sets magic number
	 * @param m the new magic number of this class
	 */
	public void setMagic(int m) {
		magic = m;
		setDefined(MAGIC);
	}
	
	/** Returns magic number of this class
	 * @return this class's magic number
	 */
	public int getMagic() {
		return magic;
	}
	
	/** Sets minor version
	 * @param mv the new minor version of this class 
	 */
	public void setMinorVersion(short mv) {
		minor_version = mv;
		setDefined(MINOR_VERSION);
	}
	
	/** Returns minor version of this class
	 *  @return this class's minor version
	 */
	public short getMinorVersion() {
		return minor_version;
	}
	
	/** Sets new major version
	 *  @param mv the new major version of this class 
	 */
	public void setMajorVersion(short mv) {
		major_version = mv;
		setDefined(MAJOR_VERSION);
	}
	
	/** Returns major version of this class
	 *  @return this class's major version
	 */
	public short getMajorVersion() {
		return major_version;
	}
	
	/** Sets new access flags of this class
	 *  @param af the new access flags of this class
	 */
	public void setAccessFlags(short af) {
		access_flags = af;
		setDefined(ACCESS_FLAGS);
	}
	
	/** Returns access flags of this class
	 *  @return this class's access flags
	 */
	public short getAccessFlags() {
		return access_flags;
	}
	
	/** Sets new this class
	 *  @param clazz the new this class
	 */
	public void setThisClass(short clazz) {
		this_class = clazz;
		setDefined(THIS_CLASS);
	}
	
	/** Returns this class
	 *  @return this class
	 */
	public short getThisClass() {
		return this_class;
	}
	
	/** Sets new super class of this class 
	 *  @param clazz the new super class of this class
	 */
	public void setSuperClass(short clazz) {
		super_class = clazz;
		setDefined(SUPER_CLASS);
	}
	
	/** Returns super class of this class
	 *  @return this class's super class
	 */
	public short getSuperClass() {
		return super_class;
	}
	
	/** Sets new interfaces count of this class
	 *  @param ic the new interfaces count of thic class
	 */
	public void setInterfacesCount(short ic) {
		interfaces_count = ic;
		setDefined(INTERFACES_COUNT);
	}

	/** Returns interfaces count of this class
	 *  @return interfaces count of this class
	 */
	public short getInterfacesCount() {
		return interfaces_count;
	}

	/** Returns actual interfaces count of this class
	 * @return actual interface count of this class
	 */
	public int getActualInterfacesCount() {
		return interfaces.size();
	}
	
	/** Add new interface to this class
	 *  @param index index of the constant pool item, that contains interface info
	 *  @return index in interfaces table of the added interface
	 */
	public short addInterface(short index)
	throws IllegalArgumentException {
	    if (index < 0) {
	        throw new IllegalArgumentException();
	    }
		interfaces.add(new Short(index));
		return (short)(interfaces.size() - 1);
	}

	/** Returns index to the constant pool, what according to interface 
	 * @param i index of an interface in this class
	 * @return index to the constant pool, what according to selected interface
	 */
	public short interfaceAt(int i) {
		return ((Short) interfaces.get(i)).shortValue();
	}
	
	/** Add new constant to this class
	 * @param ñ constant
	 * @return index of added constant in the constant pool
	 * @throws NullPointerException if c argument is null
	 */
	public short addConstant(Constant constant) {
		constant_pool.add(constant);
		return (short) (constant_pool.size() - 1);
	}
	
	/** Returns constant of this class at index i
	 * @param i index of constant in the constant pool
	 * @return constant
	 * @throws ArrayIndexOutOfBoundsException if the i is negative or not 
	 * less than the current size of the constant pool
	 */
	public Constant constantAt(int index)
		throws ArrayIndexOutOfBoundsException {
		return (Constant) constant_pool.elementAt(index);
	}
	
	/** Sets new constant pool count
	 *  @param count constant pool count of this class
	 */
	public void setCPCount(short count) {
		constant_pool_count = count;
		setDefined(CONSTANT_POOL_COUNT);
	}
	
	/** Returns constant pool count of this class
	 *  @return constant pool count
	 */
	public short getCPCount() {
		return constant_pool_count;
	}

	/** Returns actual constant pool count of this class
	 *  @return actual constant pool count
	 */
	public int getActualCPCount() {
		return constant_pool.size();
	}
	
	/** Add new field to this class
	 * @param field field
	 * @return index of added field in the list of fields
	 * @throws NullPointerException if field argument is null
	 */
	public short addField(Field field)
		throws NullPointerException {
	    if (field == null) {
	        throw new NullPointerException();
	    }
		fields.add(field);
		return (short)(fields.size() - 1);
	}
	
	/** Returns field of this class at index i
	 * @param i index of field in this class
	 * @return field
	 * @throws ArrayIndexOutOfBoundsException if the i is negative or not 
	 * less than the current size of the fields list
	 */
	public Field fieldAt(int i)
		throws ArrayIndexOutOfBoundsException {
		return (Field) fields.get(i);
	}

	/** Sets fields count of this class
	 * @param count fileds count
	 */
	public void setFieldsCount(short count) {
		fields_count = count;
		setDefined(FIELDS_COUNT);
	}
	
	/** Returns fields count of this class
	 * @return fields count
	 */
	public short getFieldsCount() {
		return fields_count;
	}

	/** Returns actual fields count of this class
	 * @return actual fields count
	 */
	public int getActualFieldsCount() {
		return fields.size();
	}
	
	/** Add new method to this class
	 * @param method method
	 * @return index of added method in the list of methods
	 * @throws NullPointerException if method argument is null
	 */
	public short addMethod(Method method) 
		throws NullPointerException {
	    if (method == null) {
	        throw new NullPointerException();
	    }
		methods.add(method);
		return (short)(methods.size() - 1);
	}
	
	/** Returns method of this class at index i
	 * @param i index of method in this class
	 * @return method
	 * @throws ArrayIndexOutOfBoundsException if the i is negative or not 
	 * less than the current size of the methods list
	 */
	public Method methodAt(int i)
		throws ArrayIndexOutOfBoundsException {
		return (Method) methods.get(i);
	}
	
	/** Sets methods count of this class
	 * @param count methods count
	 */
	public void setMethodsCount(short count) {
		methods_count = count;
		setDefined(METHODS_COUNT);
	}

	/** Returns methods count of this class
	 * @return methods count
	 */
	public short getMethodsCount() {
		return methods_count;
	}

	/** Returns actual methods count of this class
	 * @return actual methods count
	 */
	public int getActualMethodsCount() {
		return methods.size();
	}
	
	/** Returns attributes count of this class
	 * @return attributes count
	 */
	public short getAttributesCount() {
		return attributes_count;
	}
	
	/** Returns actual attributes count of this class
	 * @return actual attributes count
	 */
	public int getActualAttributesCount() {
		return attributes.size();
	}

	/** Sets attributes count of this class
	 * @param count attributes count of this class
	 */
	public void setAttributesCount(short count) {
		attributes_count = count;
		setDefined(ATTRIBUTES_COUNT);
	}

	/** Add new attribute to this class
	 * @param attr attribute
	 * @return index of added attribute in the list of attributes
	 * @throws NullPointerException if attr argument is null
	 */
	public short addAttribute(Attribute attribute)
		throws NullPointerException {
	    if (attribute == null) {
	        throw new NullPointerException();
	    }
		attributes.add(attribute);
		return (short)(attributes.size() - 1);
	}
	
	/** Returns attribute of this class at index i
	 * @param i index of attribute in this class
	 * @return attribute
	 * @throws ArrayIndexOutOfBoundsException if the i is negative or not 
	 * less than the current size of the attributes list
	 */
	public Attribute attributeAt(int i) 
		throws ArrayIndexOutOfBoundsException {
		return (Attribute) attributes.get(i);
	}

	/** Returns vector of the attributes of this class
	 * @return attributes of this class
	 */
	public ArrayList getAttributes() {
		return attributes;
	}
}
