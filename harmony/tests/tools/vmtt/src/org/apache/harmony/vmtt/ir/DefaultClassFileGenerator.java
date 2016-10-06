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

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.harmony.vmtt.ir.attributes.*;
import org.apache.harmony.vmtt.ir.constants.*;
import org.apache.harmony.vmtt.VMTT;

/**
 * @author Aleksey V. Golubitsky
 * @version $Revision: 1.13 $
 */

public class DefaultClassFileGenerator
    extends ClassFileGenerator {
	
    public DefaultClassFileGenerator() {
        super();
    }
    
    public DefaultClassFileGenerator(ClassFile cf, File outputFile)
    throws NullPointerException, IOException {
        super(cf, outputFile);
    }
    
	public void generate()
		throws IOException, NullPointerException {
		writeMagic(dos);
		writeMinorVersion(dos);
		writeMajorVersion(dos);
		writeConstantPoolCount(dos);
		writeConstantPool(dos);
		writeAccessFlags(dos);
		writeThisClass(dos);
		writeSuperClass(dos);
		writeInterfacesCount(dos);
		writeInterfaces(dos);
		writeFieldsCount(dos);
		writeFields(dos);
		writeMethodsCount(dos);
		writeMethods(dos);
		if (classFile.isDefined(ClassFile.ATTRIBUTES_COUNT)) {
			writeAttributesCount(classFile.getAttributesCount(), dos);
		}
		writeAttributes(classFile.getAttributes(), dos);
	}
	
	protected void writeMagic(DataOutputStream dos)
		throws IOException {

		if (classFile.isDefined(ClassFile.MAGIC)) {
			dos.writeInt(classFile.getMagic());
		}
	}
	
	protected void writeMinorVersion(DataOutputStream dos)
		throws IOException {

		if(VMTT.minorVersion != null) {
			short minv = VMTT.minorVersion.shortValue();
			dos.writeShort(minv);
		}
		else if (classFile.isDefined(ClassFile.MINOR_VERSION)) {
			dos.writeShort(classFile.getMinorVersion());
		}
	}

	protected void writeMajorVersion(DataOutputStream dos)
		throws IOException {
		
		if(VMTT.majorVersion != null) {
			short majv = VMTT.majorVersion.shortValue();
			dos.writeShort(majv);
		}
		else if (classFile.isDefined(ClassFile.MAJOR_VERSION)) {
			dos.writeShort(classFile.getMajorVersion());
		}
	}

	protected void writeConstantPoolCount(DataOutputStream dos)
		throws IOException {
		
		if (classFile.isDefined(ClassFile.CONSTANT_POOL_COUNT)) {
			dos.writeShort(classFile.getCPCount());
		}
	}
	
	protected void writeConstantPool(DataOutputStream dos)
		throws IOException {

		for (int i = 0; i < classFile.getActualCPCount(); i++) {
			Constant cp = classFile.constantAt(i);
			if (cp != null) {
				cp.write(dos, true);
			}
		}
	}

	protected void writeAccessFlags(DataOutputStream dos)
		throws IOException {
		
		if (classFile.isDefined(ClassFile.ACCESS_FLAGS)) {
			dos.writeShort(classFile.getAccessFlags());
		}
	}

	protected void writeThisClass(DataOutputStream dos)
		throws IOException {
		
		if (classFile.isDefined(ClassFile.THIS_CLASS)) {
			dos.writeShort(classFile.getThisClass());
		}
	}

	protected void writeSuperClass(DataOutputStream dos)
		throws IOException {
		
		if (classFile.isDefined(ClassFile.SUPER_CLASS)) {
			dos.writeShort(classFile.getSuperClass());
		}
	}

	protected void writeInterfacesCount(DataOutputStream dos)
		throws IOException {
		
		if (classFile.isDefined(ClassFile.INTERFACES_COUNT)) {
			dos.writeShort(classFile.getInterfacesCount());
		}
	}
	
	protected void writeInterfaces(DataOutputStream dos)
		throws IOException {

		for (int i =0; i < classFile.getActualInterfacesCount(); i++) {
			dos.writeShort(classFile.interfaceAt(i));
		}
	}
	
	protected void writeFieldsCount(DataOutputStream dos)
		throws IOException {
		
		if (classFile.isDefined(ClassFile.FIELDS_COUNT)) {
			dos.writeShort(classFile.getFieldsCount());
		}
	}

	protected void writeFields(DataOutputStream dos)
		throws IOException {

		for (int i = 0; i < classFile.getActualFieldsCount(); i++) {
			Field fi = classFile.fieldAt(i);
			if (fi.isDefined(Field.ACCESS_FLAG)) {
				dos.writeShort(fi.getAccessFlag());
			}
			if (fi.isDefined(Field.NAME_INDEX)) {
				dos.writeShort(fi.getNameIndex());
			}
			if (fi.isDefined(Field.DESCRIPTOR_INDEX)) {
				dos.writeShort(fi.getDescriptorIndex());
			}
			if (fi.isDefined(Field.ATTRIBUTES_COUNT)) {
				writeAttributesCount(fi.getAttributesCount(), dos);
			}
			writeAttributes(fi.getAttributes(), dos);
		}
	}
	
	protected void writeMethodsCount(DataOutputStream dos)
		throws IOException {
		
		if (classFile.isDefined(ClassFile.METHODS_COUNT)) {
			dos.writeShort(classFile.getMethodsCount());
		}
	}
	
	protected void writeMethods(DataOutputStream dos)
		throws IOException {

		for (int i = 0; i < classFile.getActualMethodsCount(); i++) {
			Method mi = classFile.methodAt(i);
			if (mi.isDefined(Method.ACCESS_FLAG)) {
				dos.writeShort(mi.getAccessFlag());
			}
			if (mi.isDefined(Method.NAME_INDEX)) {
				dos.writeShort(mi.getNameIndex());
			}
			if (mi.isDefined(Method.DESCRIPTOR_INDEX)) {
				dos.writeShort(mi.getDescriptorIndex());
			}
			if (mi.isDefined(Method.ATTRIBUTES_COUNT)) {
				writeAttributesCount(mi.getAttributesCount(), dos);
			}
			writeAttributes(mi.getAttributes(), dos);
		}
	}

	protected void writeAttributesCount(short ac, DataOutputStream dos)
		throws IOException {

		dos.writeShort(ac);
	}

	protected void writeAttributes(ArrayList attributes, DataOutputStream dos)
		throws IOException {

		for (int i = 0; i < attributes.size(); i++) {
			Attribute attr = (Attribute) attributes.get(i);
			
			if (attr.isGeneral()) {
				writeAttrGeneral((General) attr, dos);
			} else {
				if (attr.isDefined(Attribute.ATTRIBUTE_NAME_INDEX)) {
					dos.writeShort(attr.getAttributeNameIndex());
				}
				if (attr.isDefined(Attribute.ATTRIBUTE_LENGTH)) {
					dos.writeInt(attr.getAttributeLength());
				}
				if (attr instanceof ConstantValue) {
					writeAttrConstantValue((ConstantValue) attr, dos);
				} else if (attr instanceof Code) {
					writeAttr_Code((Code) attr, dos);
				} else if (attr instanceof Exceptions) {
					writeAttr_Exceptions((Exceptions) attr, dos);
				} else if (attr instanceof InnerClasses) {
					writeAttr_InnerClasses((InnerClasses) attr, dos);
				} else if (attr instanceof Synthetic) {
					writeAttr_Synthetic((Synthetic) attr, dos);
				} else if (attr instanceof SourceFile) {
					writeAttr_SourceFile((SourceFile) attr, dos);
				} else if (attr instanceof LineNumberTable) {
					writeAttr_LNT((LineNumberTable) attr, dos);
				} else if (attr instanceof LocalVariableTable) {
					writeAttr_LVT((LocalVariableTable) attr, dos);
				} else if (attr instanceof LocalVariableTypeTable) {
					writeAttr_LVTT((LocalVariableTypeTable) attr, dos);
				} else if (attr instanceof Signature) {
				    writeAttrSignature((Signature) attr, dos); 
                } else if (attr instanceof EnclosingMethod) {
                    writeAttrEnclosingMethod((EnclosingMethod) attr, dos);
                } else if (attr instanceof SourceDebugExtension) {
                	writeAttr_SourceDebugExtension((SourceDebugExtension) attr, dos);
                } else if (attr instanceof AnnotationDefault) {
                    writeAttrAnnotationDefault((AnnotationDefault) attr, dos);
                } else if (attr instanceof RuntimeVisibleAnnotations) {
                    writeAttrRuntimeVisibleAnnotations((RuntimeVisibleAnnotations) attr, dos);
                } else if (attr instanceof RuntimeInvisibleAnnotations) {
                    writeAttrRuntimeInvisibleAnnotations((RuntimeInvisibleAnnotations) attr, dos);
                } else if (attr instanceof RuntimeVisibleParameterAnnotations) {
                    writeAttrRuntimeVisibleParameterAnnotations((RuntimeVisibleParameterAnnotations) attr, dos);
                } else if (attr instanceof RuntimeInvisibleParameterAnnotations) {
                    writeAttrRuntimeInvisibleParameterAnnotations((RuntimeInvisibleParameterAnnotations) attr, dos);
                }
			}
		}
	}
	
	 protected void writeAttrAnnotationDefault(AnnotationDefault attr,
	 		DataOutputStream dos) throws IOException {
	 	writeElementValue(attr.getElementValue(), dos);
	}
	 
	 protected void writeAttrRuntimeVisibleAnnotations(RuntimeVisibleAnnotations attr,
	 		DataOutputStream dos) throws IOException {
	 	
	 	Annotation [] annotations = attr.getAnnotations();
	 	short num_annotations = attr.getNumAnnotations(); //should be annotations.length in normal case
	 	dos.writeShort(num_annotations);
	 	if(annotations == null) return;
	 	for(int i = 0; i < annotations.length; ++i) {
	 		writeAnnotation(annotations[i], dos);
	 	}
	}
	 
	 protected void writeAttrRuntimeInvisibleAnnotations(RuntimeInvisibleAnnotations attr,
	 		DataOutputStream dos) throws IOException {
	 	
	 	Annotation [] annotations = attr.getAnnotations();
	 	short num_annotations = attr.getNumAnnotations(); //should be annotations.length in normal case
	 	dos.writeShort(num_annotations);
	 	if(annotations == null) return;
	 	for(int i = 0; i < annotations.length; ++i) {
	 		writeAnnotation(annotations[i], dos);
	 	}
	}
	 
	 protected void writeAttrRuntimeVisibleParameterAnnotations(RuntimeVisibleParameterAnnotations attr,
	 DataOutputStream dos)	throws IOException {
	 	byte num_parameters = attr.getNumParameters();
	 	dos.writeByte(num_parameters);
	 	RuntimeVisibleAnnotations [] rvas = attr.getAnnotations();
	 	if(rvas == null) return;
	 	for(int i = 0; i < rvas.length; ++i) {
	 		RuntimeVisibleAnnotations rva = rvas[i];
	 		if(rva == null) {
	 			rva = new RuntimeVisibleAnnotations();
	 			rva.setNumAnnotations((short)0);
	 		}
	 		writeAttrRuntimeVisibleAnnotations(rva, dos);
	 	}
	 }
	 
	 protected void writeAttrRuntimeInvisibleParameterAnnotations(RuntimeInvisibleParameterAnnotations attr,
	 	 DataOutputStream dos)	throws IOException {
	 	 byte num_parameters = attr.getNumParameters();
	 	 dos.writeByte(num_parameters);
	 	 RuntimeInvisibleAnnotations [] rias = attr.getAnnotations();
	 	 if(rias == null) return;
	 	 for(int i = 0; i < rias.length; ++i) {
	 	 	RuntimeInvisibleAnnotations ria = rias[i];
	 	 	if(ria == null) {
	 	 		ria = new RuntimeInvisibleAnnotations();
	 	 		ria.setNumAnnotations((short)0);
	 	 	}
	 	 	writeAttrRuntimeInvisibleAnnotations(ria, dos);
	 	 }
	 }
	 
	 protected void writeAnnotation(Annotation annotation,
	 		DataOutputStream dos) throws IOException {
	 	dos.writeShort(annotation.getTypeIndex());
 		ElementValuePair [] evps= annotation.getElementValuePairs();
 		short num_element_value_pairs = annotation.getNumElementValues();//should be evs.length in normal case
 		dos.writeShort(num_element_value_pairs);
 		for(int i = 0; i< evps.length; ++i) {
 			writeElementValuePair(evps[i], dos);
 		}
	 }
	 
	 protected void writeElementValuePair(ElementValuePair elementValuePair,
	 		DataOutputStream dos) throws IOException {
	 	
	 		short element_name_index = elementValuePair.getElementNameIndex();
	 		dos.writeShort(element_name_index);
	 		writeElementValue(elementValuePair.getElementValue(), dos);
	 }
	 
	 protected void writeElementValue(ElementValue elementValue,
	 		DataOutputStream dos) throws IOException {
		byte tag = elementValue.getTag();//FIXME: byte should be unsigned
		dos.writeByte(tag);
		
		ElementValue.value value= elementValue.getValue();
		if(value instanceof ElementValue.const_value) {
			short const_value_index = ((ElementValue.const_value)value).getConstValueIndex();
			dos.writeShort(const_value_index);
		}
		else if(value instanceof ElementValue.enum_value) {
			short type_name_index = ((ElementValue.enum_value)value).getTypeNameIndex();
			short const_name_index = ((ElementValue.enum_value)value).getConstNameIndex();
			dos.writeShort(type_name_index);
			dos.writeShort(const_name_index);
		}
		else if(value instanceof ElementValue.class_value) {
			short class_info_index = ((ElementValue.class_value)value).getClassInfoIndex();
			dos.writeShort(class_info_index);
		}
		else if(value instanceof ElementValue.annotation_value) {
			Annotation annotation =  ((ElementValue.annotation_value)value).getAnnotation();
			writeAnnotation(annotation, dos);
		}
		else if(value instanceof ElementValue.array_value) {
			ElementValue [] innerElementValue = ((ElementValue.array_value)value).getArrayValue();
			dos.writeShort(((ElementValue.array_value)value).getArrayLength());
			for(int i = 0; i < innerElementValue.length; ++i) {
				writeElementValue(innerElementValue[i], dos);
			}
		}
	}
	
	/** Writes attribute General
     * @param attr - attribute to be written
     * @param dos - output stream
     * @throws IOException - if any I/O errors occurs
	 */
	protected void writeAttrGeneral(General attr,
                                     DataOutputStream dos)
		throws IOException {
		dos.write(attr.getBytes());
	}
    
    /** Writes attribute EnclosingMethod
     * @param attr - attribute to be written
     * @param dos - output stream
     * @throws IOException - if any I/O errors occurs
     */
    protected void writeAttrEnclosingMethod(EnclosingMethod attr,
                                            DataOutputStream dos)
        throws IOException {
        if (attr.isDefined(EnclosingMethod.CLASS_INDEX)) {
            dos.writeShort(attr.getClassIndex());
        }
        if (attr.isDefined(EnclosingMethod.METHOD_INDEX)) {
            dos.writeShort(attr.getMethodIndex());
        }
    }
    
    /** Writes attribute Signature
     * @param attr - attribute to be written
     * @param dos - output stream
     * @throws IOException - if any I/O errors occurs
     */
    protected void writeAttrSignature(Signature attr, 
                                      DataOutputStream dos)
        throws IOException {
        if (attr.isDefined(Signature.SIGNATURE_INDEX)) {
            dos.writeShort(attr.getSignatureIndex());
        }
    }
	
	/** Writes attribute ConstantValue
     * @param attr - attribute to be written
     * @param dos - output stream
     * @throws IOException - if any I/O errors occurs
	 */
	protected void writeAttrConstantValue(ConstantValue attr,
                                          DataOutputStream dos)
		throws IOException {
		if (attr.isDefined(ConstantValue.CONSTANTVALUE_INDEX)) {
			dos.writeShort(attr.getConstantValueIndex());
		}
	}

	protected void writeAttr_Code(Code ca, DataOutputStream dos)
		throws IOException {

		if (ca.isDefined(Code.MAX_STACK)) {
			dos.writeShort(ca.getMaxStack());
		}
		if (ca.isDefined(Code.MAX_LOCALS)) {
			dos.writeShort(ca.getMaxLocals());
		}
		if (ca.isDefined(Code.CODE_LENGTH)) {
			dos.writeInt(ca.getCodeLength());
		}
		dos.write(ca.getCodeAsByteArray());
		if (ca.isDefined(Code.EXCEPTION_TABLE_LENGTH)) {
			dos.writeShort(ca.getExceptionTableLength());
		}
		for (int i = 0; i < ca.getActualExceptionTableLength(); i++) {
			Code.exception e = ca.exceptionAt(i);
			if (e.isDefined(Code.exception.START_PC)) {
				dos.writeShort(e.getStartPC());
			}
			if (e.isDefined(Code.exception.END_PC)) {
				dos.writeShort(e.getEndPC());
			}
			if (e.isDefined(Code.exception.HANDLER_PC)) {
				dos.writeShort(e.getHandlerPC());
			}
			if (e.isDefined(Code.exception.CATCH_TYPE)) {
				dos.writeShort(e.getCatchType());
			}
		}
		if (ca.isDefined(Code.ATTRIBUTES_COUNT)) {
			dos.writeShort(ca.getAttributesCount());
		}
		writeAttributes(ca.getAttributes(), dos);
	}
	
	protected void writeAttr_Exceptions(Exceptions ea, DataOutputStream dos)
		throws IOException {

		if (ea.isDefined(Exceptions.NUMBER_OF_EXCEPTIONS)) {
			dos.writeShort(ea.getNumberOfExceptions());
		}
		for (int i = 0; i < ea.getExceptionTableSize(); i++) {
			dos.writeShort(ea.exceptionAt(i));
		}
	}

	protected void writeAttr_InnerClasses(InnerClasses ica, DataOutputStream dos)
		throws IOException {
		
		if (ica.isDefined(InnerClasses.NUMBER_OF_CLASSES)) {
			dos.writeShort(ica.getNumberOfClasses());
		}
		for (int i = 0; i < ica.getClassesSize(); i++) {
			InnerClasses.inner_class ic = ica.classAt(i);
			if (ic.isDefined(InnerClasses.inner_class.INNER_CLASS_INFO_INDEX)) {
				dos.writeShort(ic.getInnerClassInfoIndex());
			}
			if (ic.isDefined(InnerClasses.inner_class.OUTER_CLASS_INFO_INDEX)) {
				dos.writeShort(ic.getOuterClassInfoIndex());
			}
			if (ic.isDefined(InnerClasses.inner_class.INNER_NAME_INDEX)) {
				dos.writeShort(ic.getInnerNameIndex());
			}
			if (ic.isDefined(InnerClasses.inner_class.INNER_CLASS_ACCESS_FLAGS)) {
				dos.writeShort(ic.getInnerClassAccessFlags());
			}
		}
	}

	protected void writeAttr_Synthetic(Synthetic sa, DataOutputStream dos)
		throws IOException {
	}	

	protected void writeAttr_SourceFile(SourceFile sfa, DataOutputStream dos)
		throws IOException {
		
		if (sfa.isDefined(SourceFile.SOURCEFILE_INDEX)) {
			dos.writeShort(sfa.getSourceFileIndex());
		}
	}	

	protected void writeAttr_LNT(LineNumberTable attr, DataOutputStream dos)
		throws IOException {

		if (attr.isDefined(LineNumberTable.LINE_NUMBER_TABLE_LENGTH)) {
			dos.writeShort(attr.getLineNumberTableLength());
		}
		for (int i = 0; i < attr.getLineNumberTableSize(); i++) {
			LineNumberTable.line_number ln = attr.lineNumberAt(i);
			if (ln.isDefined(LineNumberTable.line_number.START_PC)) {
				dos.writeShort(ln.getStratPC());
			}
			if (ln.isDefined(LineNumberTable.line_number.LINE_NUMBER)) {
				dos.writeShort(ln.getLineNumber());
			}
		}
	}	
	
	protected void writeAttr_LVT(LocalVariableTable attr, DataOutputStream dos)
		throws IOException {

		if (attr.isDefined(LocalVariableTable.LOCAL_VARIABLE_TABLE_LENGTH)) {
			dos.writeShort(attr.getLocalVariableTableLength());
		}
		for (int i = 0; i < attr.getLocalVariableTableSize(); i++) {
			LocalVariableTable.local_variable lv = attr.localVariableAt(i);
			if (lv.isDefined(LocalVariableTable.local_variable.START_PC)) {
				dos.writeShort(lv.getStartPC());
			}
			if (lv.isDefined(LocalVariableTable.local_variable.LENGTH)) {
				dos.writeShort(lv.getLength());
			}
			if (lv.isDefined(LocalVariableTable.local_variable.NAME_INDEX)) {
				dos.writeShort(lv.getNameIndex());
			}
			if (lv.isDefined(LocalVariableTable.local_variable.DESCRIPTOR_INDEX)) {
				dos.writeShort(lv.getDescriptorIndex());
			}
			if (lv.isDefined(LocalVariableTable.local_variable.INDEX)) {
				dos.writeShort(lv.getIndex());
			}
		}
	}
	
	protected void writeAttr_LVTT(LocalVariableTypeTable attr, DataOutputStream dos)
	throws IOException {

		if (attr.isDefined(LocalVariableTypeTable.LOCAL_VARIABLE_TYPE_TABLE_LENGTH)) {
			dos.writeShort(attr.getLocalVariableTypeTableLength());
		}
		for (int i = 0; i < attr.getLocalVariableTypeTableSize(); i++) {
			LocalVariableTypeTable.local_variable lv = attr.localVariableAt(i);
			if (lv.isDefined(LocalVariableTypeTable.local_variable.START_PC)) {
				dos.writeShort(lv.getStartPC());
			}
			if (lv.isDefined(LocalVariableTypeTable.local_variable.LENGTH)) {
				dos.writeShort(lv.getLength());
			}
			if (lv.isDefined(LocalVariableTypeTable.local_variable.NAME_INDEX)) {
				dos.writeShort(lv.getNameIndex());
			}
			if (lv.isDefined(LocalVariableTypeTable.local_variable.SIGNATURE_INDEX)) {
				dos.writeShort(lv.getSignatureIndex());
			}
			if (lv.isDefined(LocalVariableTypeTable.local_variable.INDEX)) {
				dos.writeShort(lv.getIndex());
			}
		}
	}

	protected void writeAttr_Deprecated(DeprecatedAttr da, DataOutputStream dos)
		throws IOException {
	}
	
	protected void writeAttr_SourceDebugExtension(SourceDebugExtension sde, DataOutputStream dos)
	throws IOException {
		byte [] debug_extension = sde.getDebugExtension();
		if(debug_extension == null) return;
		dos.write(debug_extension);
	}
}
