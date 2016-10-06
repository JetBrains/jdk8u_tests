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

import org.apache.harmony.vmtt.ir.attributes.Attribute;
import org.apache.harmony.vmtt.ir.attributes.Attributes;

/**
 * @author Aleksey V. Golubitsky
 * @version $Revision: 1.6 $
 */

public class Method extends Defined implements Attributes {

	public static final int ACCESS_FLAG		= 0;
	public static final int NAME_INDEX		= 1;
	public static final int DESCRIPTOR_INDEX	= 2;
	public static final int ATTRIBUTES_COUNT	= 3;

	private short access_flag;
	private short name_index;
	private short descriptor_index;
	private short attributes_count;
	private ArrayList attributes = new ArrayList();

	public Method() {
		super(4);
	}
	
	public Method(short af, short ni, short di, short ac) {
		super(4);
		setAccessFlag(af);
		setNameIndex(ni);
		setDescriptorIndex(di);
		setAttributesCount(ac);
	}
	
	public void setAccessFlag(short af) {
		access_flag = af;
		setDefined(ACCESS_FLAG);
	}
	
	public void setNameIndex(short ni) {
		name_index = ni;
		setDefined(NAME_INDEX);
	}
	
	public void setDescriptorIndex(short di) {
		descriptor_index = di;
		setDefined(DESCRIPTOR_INDEX);
	}
	
	public void setAttributesCount(short ac) {
		attributes_count = ac;
		setDefined(ATTRIBUTES_COUNT);
	}
	
	public short getAccessFlag() {
		return access_flag;
	}
	
	public short getNameIndex() {
		return name_index;
	}
	
	public short getDescriptorIndex() {
		return descriptor_index;
	}
	
	public short getAttributesCount() {
		return attributes_count;
	}
	
	public void addAttribute(Attribute attr) {
		attributes.add(attr);
	}
	
	public Attribute attributeAt(int index) {
		return (Attribute) attributes.get(index);
	}
	
	public int getActualAttributesCount() {
		return attributes.size();
	}
	
	public ArrayList getAttributes() {
		return attributes;
	}
}

