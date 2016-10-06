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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.harmony.vmtt.Utils;
import org.apache.harmony.vmtt.ir.attributes.*;
import org.apache.harmony.vmtt.ir.constants.*;

/**
 * @author Aleksey V. Golubitsky
 * @version $Revision: 1.15 $
 */

public class DefaultClassFileParser 
    extends ClassFileParser {
	
	public void parse()
	    throws ClassFileFormatException {
		parseMagic();
		parseMinorVersion();
		parseMajorVersion();
		parseConstantPoolCount();
		parseConstantPool();
		parseAccessFlags();
		parseThisClass();
		parseSuperClass();
		parseInterfacesCount();
		parseInterfaces();
		parseFieldsCount();
		parseFields();
		parseMethodsCount();
		parseMethods();
		parseAttributesCount();
		parseAttributes(classFile.getAttributesCount(),
						classFile.getAttributes());
	}
	
	protected void parseMagic() 
	    throws ClassFileFormatException {
		try {
			int magic = dis.readInt();
			classFile.setMagic(magic);
		} catch (IOException ioe) {
			throw new ClassFileFormatException("magic is expected (4 bytes)");
		}
	}

	protected void parseMinorVersion() 
		throws ClassFileFormatException {
		try {
			short mv = dis.readShort();
			classFile.setMinorVersion(mv);
		} catch (IOException ioe) {
			throw new ClassFileFormatException("minor_version is expected (2 bytes)");
		}
	}

	protected void parseMajorVersion() 
		throws ClassFileFormatException {
		
		try {
			short mv = dis.readShort();
			classFile.setMajorVersion(mv);
		} catch (IOException ioe) {
			throw new ClassFileFormatException("major_version is expected (2 bytes)");
		}
	}

	protected void parseConstantPoolCount() 
		throws ClassFileFormatException {
		
		try {
			short c = dis.readShort();
			classFile.setCPCount(c);
		} catch (IOException ioe) {
			throw new ClassFileFormatException("constant_pool_count is expected (2 bytes)");
		}
	}
	
	protected void parseConstantPool()
		throws ClassFileFormatException {

		Constant cp = null;
		int i = 0;
		byte tag = 0;

		try {
			for (; i < classFile.getCPCount() - 1; i++) {
				tag = dis.readByte();
				boolean extra = false;
				switch (tag) {
				case CONSTANT_Class:
					cp = new ConstantClass();
					break;
				
				case CONSTANT_Integer:
				case CONSTANT_Float:
					cp = new ConstantNum32(tag);
					break;
				case CONSTANT_Long:
				case CONSTANT_Double:
					cp = new ConstantNum64(tag);
					extra = true;
					break;
				case CONSTANT_InterfaceMethodref:
				case CONSTANT_Fieldref:
				case CONSTANT_Methodref:
					cp = new ConstantRef(tag);
					break;
				case CONSTANT_NameAndType:
					cp = new ConstantNameAndType();
					break;
				case CONSTANT_String:
					cp = new ConstantString();
					break;
				case CONSTANT_Utf8:
					cp = new ConstantUtf8();
					break;
				default:
					throw new ClassFileFormatException("Illegal constant tag " + Utils.quote(tag));
				}
				cp.read(dis);
				classFile.addConstant(cp);
				if (extra) {
					classFile.addConstant(null);
					i++;
				}
			}
		} catch (IOException ioe) {
			String msg = "Error occurs while parse constant pool. Constant #" + i + " of " + getConstantType(tag) + " type.";
			throw new ClassFileFormatException(msg);
		}
	}
	
	protected void parseAccessFlags()
		throws ClassFileFormatException {
		
		try {
			classFile.setAccessFlags(dis.readShort());
		} catch (IOException ioe) {
			throw new ClassFileFormatException("access_flags is expected (2 bytes)");
		}
	}
	
	protected void parseThisClass()
		throws ClassFileFormatException {
	
		try {
			classFile.setThisClass(dis.readShort());
		} catch (IOException ioe) {
			throw new ClassFileFormatException("this_class is expected (2 bytes)");
		}
	}
	
	protected void parseSuperClass()
		throws ClassFileFormatException {
	
		try {
			classFile.setSuperClass(dis.readShort());
		} catch (IOException ioe) {
			throw new ClassFileFormatException("super_class is expected (2 bytes)");
		}
	}
	
	protected void parseInterfacesCount()
		throws ClassFileFormatException {
	
		try {
			classFile.setInterfacesCount(dis.readShort());
		} catch (IOException ioe) {
			throw new ClassFileFormatException("interfaces_count is expected (2 bytes)");
		}
	}
	
	protected void parseInterfaces()
		throws ClassFileFormatException {

		int i = 0;
		try {
			for (; i < classFile.getInterfacesCount(); i++) {
				classFile.addInterface(dis.readShort());
			}
		} catch (IOException ioe) {
			String msg = "Error occurs while parse interfaces. Interface #" + i;
			throw new ClassFileFormatException(msg);
		}
	}
	
	protected void parseFieldsCount()
		throws ClassFileFormatException {
	
		try {
			classFile.setFieldsCount(dis.readShort());
		} catch (IOException ioe) {
			throw new ClassFileFormatException("fields_count is expected (2 bytes)");
		}
	}
	
	protected void parseFields()
		throws ClassFileFormatException {
		
		int i = 0;
		try {
			for (; i < classFile.getFieldsCount(); i++) {
				Field field = new Field();
				field.setAccessFlag(dis.readShort());
				field.setNameIndex(dis.readShort());
				field.setDescriptorIndex(dis.readShort());
				field.setAttributesCount(dis.readShort());
				parseAttributes(field.getAttributesCount(), field.getAttributes());
				classFile.addField(field);
			}
		} catch (IOException ioe) {
			String msg = "Error occurs while parse fields. Field #" + i;
			throw new ClassFileFormatException(msg);
		}
	}
	
	protected void parseMethodsCount()
		throws ClassFileFormatException {
	
		try {
			classFile.setMethodsCount(dis.readShort());
		} catch (IOException ioe) {
			throw new ClassFileFormatException("methods_count is expected (2 bytes)");
		}
	}
	
	protected void parseMethods()
		throws ClassFileFormatException {

		int i = 0;
		try {
			for (; i < classFile.getMethodsCount(); i++) {
				Method method = new Method();
				method.setAccessFlag(dis.readShort());
				method.setNameIndex(dis.readShort());
				method.setDescriptorIndex(dis.readShort());
				method.setAttributesCount(dis.readShort());
				parseAttributes(method.getAttributesCount(), method.getAttributes());
				classFile.addMethod(method);
			}
		} catch (IOException ioe) {
			String msg = "Error occurs while parse methods. Method #" + i;
			throw new ClassFileFormatException(msg);
		}
	}
	
	protected void parseAttributesCount()
		throws ClassFileFormatException {
	
		try {
			classFile.setAttributesCount(dis.readShort());
		} catch (IOException ioe) {
			throw new ClassFileFormatException("attributes_count is expected (2 bytes)");
		}
	}
	
	protected void parseAttributes(int ac, ArrayList attributes)
		throws ClassFileFormatException {
		int i = 0;
		try {
			for (; i < ac; i++) {
                Attribute attr = null;
                Constant ci = null;
				short nameIndex = dis.readShort();
				int attrLen = dis.readInt();
                try {
                    ci = classFile.constantAt(nameIndex - 1);
                } catch (ArrayIndexOutOfBoundsException e) {
                }
				if (ci == null) {
                    attr = parseAttrGeneral(nameIndex, attrLen);
				} else {
				    if (ci.getTag() == CONSTANT_Utf8) {
				        ConstantUtf8 utf8i = (ConstantUtf8) ci;
				        String name = new String(utf8i.getBytes());
				        if (name.equals("ConstantValue")) {
				            attr = parseAttrConstantValue();
				        } else if (name.equals("Code")) {
				            attr = parseAttrCode();
				        } else if (name.equals("Exceptions")) {
				            attr = parseAttrExceptions();
				        } else if (name.equals("InnerClasses")){
				            attr = parseAttrInnerClasses();
				        } else if (name.equals("Synthetic")) {
				            attr = parseAttrSynthetic();
				        } else if (name.equals("SourceFile")) {
				            attr = parseAttrSourceFile();
				        } else if (name.equals("LineNumberTable")) {
				            attr = parseAttrLineNumberTable();
				        } else if (name.equals("LocalVariableTable")) {
				            attr = parseAttrLocalVariableTable();
				        } else if (name.equals("LocalVariableTypeTable")) {
				            attr = parseAttrLocalVariableTypeTable();
				        } else if (name.equals("Deprecated")) {
				            attr = parseAttrDeprecated();
				        } else if (name.equals("Signature")) {
                            attr = parseAttrSignature();
                        } else  if (name.equals("EnclosingMethod")) {
                            attr = parseAttrEnclosingMethod();
                        } else  if (name.equals("SourceDebugExtension")) {
                            attr = parseAttrSourceDebugExtension(attrLen);
                        } else  if (name.equals("AnnotationDefault")) {
                            attr = parseAttrAnnotationDefault();
					    } else  if (name.equals("RuntimeVisibleAnnotations")) {
	                        attr = parseAttrRuntimeVisibleAnnotations();
					    } else  if (name.equals("RuntimeInvisibleAnnotations")) {
	                        attr = parseAttrRuntimeInvisibleAnnotations();
					    } else  if (name.equals("RuntimeVisibleParameterAnnotations")) {
	                        attr = parseAttrRuntimeVisibleParameterAnnotations();
					    } else  if (name.equals("RuntimeInvisibleParameterAnnotations")) {
	                        attr = parseAttrRuntimeInvisibleParameterAnnotations();
	                    } 
                        else {
				            attr = parseAttrGeneral(nameIndex, attrLen);
				        }
				    }
				    if (!attr.isGeneral()) {
				        attr.setAttributeNameIndex(nameIndex);
				        attr.setAttributeLength(attrLen);
				    }
                }
				attributes.add(attr);
			}
		} catch (IOException ioe) {
			String msg = "Error occurse while parse attributes. Attribute #" + i;
			throw new ClassFileFormatException(msg);
		}
	}
    
    /** Parses attribute General
     * @param nameIndex - attribute_name_index of the General attribute
     * @param attrLen - attribute_length of the General attribute 
     * @return new attribute General 
     * @throws IOException - if any I/O errors occurs
     */
    protected Attribute parseAttrGeneral(int nameIndex, int attrLen)
        throws IOException {
        General attr = new General();
        ByteArrayOutputStream baos = new ByteArrayOutputStream(6 + attrLen);
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeShort(nameIndex);
        dos.writeInt(attrLen);
        byte[] b = new byte[attrLen];
        dis.read(b);
        dos.write(b);
        dos.close();
        attr.setBytes(baos.toByteArray());
        return attr;
    }
    
    /** Parses attribute Signature
     * @return new attribute Signature
     * @throws IOException - if any I/O errors occurs
     */
    protected Signature parseAttrSignature()
        throws IOException {
        Signature attr = new Signature();
        attr.setSignatureIndex(dis.readShort());
        return attr;
    }
    
	/** Parses attribute ConstantValue
	 * @return new attribute ConstantValue
     * @throws IOException - if any I/O errors occurs
	 */
	protected ConstantValue parseAttrConstantValue()
		throws IOException {
		ConstantValue attr = new ConstantValue();
		attr.setConstantValueIndex(dis.readShort());
		return attr;
	}
    
    /** Parses attribute EnclosingMethod
     * @return new attribute EnclosingMethod
     * @throws IOException - if any I/O errors occurs
     */
    protected EnclosingMethod parseAttrEnclosingMethod()
        throws IOException {
        EnclosingMethod attr = new EnclosingMethod();
        attr.setClassIndex(dis.readShort());
        attr.setMethodIndex(dis.readShort());
        return attr;
    }
	
	protected Attribute parseAttrCode()
		throws IOException, ClassFileFormatException {
		Code cd_attr = new Code();
		cd_attr.setMaxStack(dis.readShort());
		cd_attr.setMaxLocals(dis.readShort());
		cd_attr.setCodeLength(dis.readInt());
		byte[] bytes = new byte[cd_attr.getCodeLength()];
		dis.read(bytes);
		cd_attr.setCode(bytes);
		cd_attr.setExceptionTableLength(dis.readShort());
		for (int i = 0; i < cd_attr.getExceptionTableLength(); i++) {
			Code.exception ex = cd_attr.new exception();
			ex.setStartPC(dis.readShort());
			ex.setEndPC(dis.readShort());
			ex.setHandlerPC(dis.readShort());
			ex.setCatchType(dis.readShort());
			cd_attr.addException(ex);
		}
		cd_attr.setAttributesCount(dis.readShort());
		parseAttributes(cd_attr.getAttributesCount(), cd_attr.getAttributes());
		return cd_attr;
	}
		
	protected Attribute parseAttrExceptions()
		throws IOException {
		Exceptions ex_attr = new Exceptions();
		short num = dis.readShort();
		ex_attr.setNumberOfException(num);
		for (int i = 0; i < num; i++) {
			ex_attr.addException(dis.readShort());
		}
		return ex_attr;
	}
	
	protected Attribute parseAttrInnerClasses()
		throws IOException {
		InnerClasses ic_attr = new InnerClasses();
		short noc = dis.readShort();
		ic_attr.setNumberOfClasses(noc);
		for (int i = 0; i < noc; i++) {
			InnerClasses.inner_class ic = ic_attr.new inner_class();
			ic.setInnerClassInfoIndex(dis.readShort());
			ic.setOuterClassInfoIndex(dis.readShort());
			ic.setInnerNameIndex(dis.readShort());
			ic.setInnerClassAccessFlags(dis.readShort());
			ic_attr.addClass(ic);
		}
		return ic_attr;
	}
	
	protected Attribute parseAttrSynthetic()
		throws IOException {
		Synthetic si_attr = new Synthetic();
		return si_attr;
	}
	
	protected Attribute parseAttrSourceFile()
		throws IOException {
		SourceFile sf_attr = new SourceFile();
		sf_attr.setSourceFileIndex(dis.readShort());
		return sf_attr;
	}
	
	protected Attribute parseAttrLineNumberTable()
		throws IOException {
	    LineNumberTable lnt_attr = new LineNumberTable();
		short lntl = dis.readShort();
		lnt_attr.setLineNumberTableLength(lntl); 
		for (int i = 0; i < lntl; i++) {
			LineNumberTable.line_number ln = lnt_attr.new line_number();
			ln.setStartPC(dis.readShort());
			ln.setLineNumber(dis.readShort());
			lnt_attr.addLineNumber(ln);
		}
		return lnt_attr;
	}

	protected Attribute parseAttrLocalVariableTable()
		throws IOException {
		LocalVariableTable lvt_attr = new LocalVariableTable();
		lvt_attr.setLocalVariableTableLength(dis.readShort());
		for (int i = 0; i < lvt_attr.getLocalVariableTableLength(); i++) {
			LocalVariableTable.local_variable lv = lvt_attr.new local_variable();
			lv.setStartPC(dis.readShort());
			lv.setLength(dis.readShort());
			lv.setNameIndex(dis.readShort());
			lv.setDescriptorIndex(dis.readShort());
			lv.setIndex(dis.readShort());
			lvt_attr.addLocalVariable(lv);
		}
		return lvt_attr;
	}
	
	protected Attribute parseAttrLocalVariableTypeTable()
	throws IOException {
		LocalVariableTypeTable lvtt_attr = new LocalVariableTypeTable();
		lvtt_attr.setLocalVariableTypeTableLength(dis.readShort());
		for (int i = 0; i < lvtt_attr.getLocalVariableTypeTableLength(); i++) {
			LocalVariableTypeTable.local_variable lv = lvtt_attr.new local_variable();
			lv.setStartPC(dis.readShort());
			lv.setLength(dis.readShort());
			lv.setNameIndex(dis.readShort());
			lv.setSignatureIndex(dis.readShort());
			lv.setIndex(dis.readShort());
			lvtt_attr.addLocalVariable(lv);
		}
		return lvtt_attr;
	}
	
	protected Attribute parseAttrDeprecated()
		throws IOException {
		DeprecatedAttr dp_attr = new DeprecatedAttr();
		return dp_attr;
	}
	
	protected Attribute parseAttrSourceDebugExtension(int attrLen)
		throws IOException {
		SourceDebugExtension sde_attr = new SourceDebugExtension();
		byte [] debug_extension = new byte [attrLen];
		dis.readFully(debug_extension);
		sde_attr.setDebugExtension(debug_extension);
		return sde_attr;
	}
	
	
	protected ElementValue parseAttrElementValue() 
		throws IOException {	
		ElementValue ev = new ElementValue();
		byte tag = dis.readByte();
		ev.setTag(tag);
		
		ElementValue.value val = null; 
		switch(tag) {
		case 'B': 
		case 'C': 
		case 'D': 
		case 'F': 
		case 'I': 
		case 'J': 
		case 'S': 
		case 'Z':
		case 's': 
			val = new ElementValue.const_value(dis.readShort());
			break;
		
		case 'e':
			val = new ElementValue.enum_value(dis.readShort(), dis.readShort());
			break;

		case 'c':
			val = new ElementValue.class_value(dis.readShort());
			break;
		case '@':
			val = new ElementValue.annotation_value(parseAnnotation());
			break;
		case '[':
			int num = dis.readShort();
			ElementValue [] array_val = new ElementValue[num];
			for(int i = 0; i < num; ++i) {
				array_val[i] = parseAttrElementValue();
			}
			val = new ElementValue.array_value(array_val);
			break;
		}
		ev.setValue(val);
		return ev;
	}
	
	protected ElementValuePair parseAttrElementValuePair() 
	throws IOException {
		ElementValuePair evp = new ElementValuePair();
		short element_name_index = dis.readShort();
		evp.setElementNameIndex(element_name_index);
		evp.setElementValue(parseAttrElementValue());
		return evp;
	}
	
	protected Attribute parseAttrAnnotationDefault() 
		throws IOException {
		AnnotationDefault ad_attr = new AnnotationDefault();
		ad_attr.setElementValue( parseAttrElementValue() );
		return ad_attr;
	}
	
	protected Attribute parseAttrRuntimeVisibleAnnotations() 
	throws IOException {
		RuntimeVisibleAnnotations rva_attr = new RuntimeVisibleAnnotations();
		rva_attr.setAnnotations(parseAnnotations());
		rva_attr.setNumAnnotations((short)rva_attr.getAnnotations().length);
		return rva_attr;
	}
	
	protected Attribute parseAttrRuntimeInvisibleAnnotations() 
	throws IOException {
		RuntimeInvisibleAnnotations ria_attr = new RuntimeInvisibleAnnotations();
		ria_attr.setAnnotations(parseAnnotations());
		ria_attr.setNumAnnotations((short)ria_attr.getAnnotations().length);
		return ria_attr;
	}
	
	protected Attribute parseAttrRuntimeVisibleParameterAnnotations() 
	throws IOException {
		RuntimeVisibleParameterAnnotations rvpa_attr = new RuntimeVisibleParameterAnnotations();
		byte num_parameters = dis.readByte();
		rvpa_attr.setNumParameters(num_parameters);
		
		int len = num_parameters;
		if(len < 0) {
			len += 256;
		}
		
		RuntimeVisibleAnnotations [] rvas = new RuntimeVisibleAnnotations[len];
		for(int i = 0; i < rvas.length; ++i) {
			rvas[i] = (RuntimeVisibleAnnotations) parseAttrRuntimeVisibleAnnotations();
		}
		
		rvpa_attr.setAnnotations(rvas);
		return rvpa_attr;
	}
	
	protected Attribute parseAttrRuntimeInvisibleParameterAnnotations() 
	throws IOException {
		RuntimeInvisibleParameterAnnotations ripa_attr = new RuntimeInvisibleParameterAnnotations();
		byte num_parameters = dis.readByte();
		ripa_attr.setNumParameters(num_parameters);
		
		int len = num_parameters;
		if(len < 0) {
			len += 256;
		}
		
		RuntimeInvisibleAnnotations [] rvas = new RuntimeInvisibleAnnotations[len];
		for(int i = 0; i < rvas.length; ++i) {
			rvas[i] = (RuntimeInvisibleAnnotations) parseAttrRuntimeInvisibleAnnotations();
		}
		
		ripa_attr.setAnnotations(rvas);
		return ripa_attr;
	}
	
	Annotation [] parseAnnotations() 
		throws IOException {
		short num_annotations = dis.readShort();
		Annotation [] annotations = new Annotation[num_annotations];
		for(int i = 0; i < annotations.length; ++i) {
			annotations[i] = parseAnnotation(); 
		}
		return annotations;
	}
	
	Annotation parseAnnotation() 
		throws IOException {
		Annotation annotation = new Annotation();
		short type_index = dis.readShort();
		annotation.setTypeIndex(type_index);
		short num_element_value_pairs = dis.readShort();
		ElementValuePair [] evps = new ElementValuePair[num_element_value_pairs];
		annotation.setNumElementValues(num_element_value_pairs);
		for(int i = 0; i < evps.length; ++i) {
			evps[i] =  parseAttrElementValuePair();
		}
		annotation.setElementValuePairs(evps);
		return annotation;
	}
}
