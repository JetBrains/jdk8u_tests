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
package org.apache.harmony.vmtt.cdecode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

import org.apache.harmony.vmtt.Options;
import org.apache.harmony.vmtt.Utils;
import org.apache.harmony.vmtt.disasm.InstructionsTable;
import org.apache.harmony.vmtt.ir.*;
import org.apache.harmony.vmtt.ir.attributes.*;
import org.apache.harmony.vmtt.ir.constants.*;
import org.apache.harmony.vmtt.ir.modifiers.*;

/**
 * @author Aleksey V. Golubitsky
 * @version $Revision: 1.17 $
 */

public class DefaultCodeFileGenerator 
    extends CodeFileGenerator
    implements Options {
	
    private final String NL                    = "\n";
    private final String NDL                   = "\n\n";
    private final String COMMENT               = "  // ";
	private final String MARGIN                = "";
	private final String INDENT_CP             = "  ";
	private final String INDENT_INTERFACE      = "  ";
	private final String INDENT_FIELD          = "  ";
	private final String INDENT_FIELD_PARAMS   = "  ";
	private final String INDENT_METHOD         = "  ";
	private final String INDENT_METHOD_PARAMS  = "  ";
	private final String INDENT_ATTR           = "  ";

	/**
	 * Creates new instance of the DefaultCodeFileGenerator
	 */
	public DefaultCodeFileGenerator() {
	    super();
	}
	
	/** Creates new instance of the DefaultCodeFileGenerator with set
     * parameters
	 * @param cf - class file which will be converted to ccode file
	 * @param outFile - output file
	 * @throws NullPointerException - if <b>cf == null</b> or <b>outFile == null</b>
     * @throws IOException - if any I/O error occurs while writing file.
	 */
	public DefaultCodeFileGenerator(ClassFile cf, File outFile)
	    throws NullPointerException, IOException {
	    super(cf, outFile);
	}
	
	/** Generates new ccode file from class file
	 * @see org.apache.harmony.vmtt.cdecode.CodeFileGenerator#generate()
	 */
	public void generate()
		throws IOException {
		writeHeader();
		writeMagic();
		writeMinorVersion();
		writeMajorVersion();
		writeConstantPoolCount();
		writeConstantPool();
		writeAccessFlags();
		writeThisClass();
		writeSuperClass();
		writeInterfacesCount();
		writeInterfaces();
		writeFieldsCount();
		writeFields();
		writeMethodsCount();
		writeMethods();
		writeAttributesCount();
		writeAttributes(MARGIN, classFile.getAttributes());
		fw.close();
	}
	
	/** Writes file header for generated ccode file. Header contains three
     * javadoc tags:
     * <ul>
     * <li>author - default value is "VMTT (Virtual Machine Testing Tools)"
     * <li>version - default value is "1.0"
     * <li>lastmod - default value is date and time when ccode file was created 
     * </ul>
	 * @throws IOException - if any I/O error occurs while writing file.
	 */
	protected void writeHeader()
	    throws IOException {
		fw.write("/**" + NL);
		fw.write(" * Generated ccode file" + NL);
		fw.write(" * @author VMTT (Virtual Machine Testing Tools)" + NL);
		fw.write(" * @version 1.0" + NL);
		fw.write(" * @lastmod: " + 
                 Calendar.getInstance().getTime().toString() + NL);
		fw.write(" */" + NDL);
	}
	
	/** Writes magic number of the class. Valid class has following value of the 
     * magic number 0xCAFEBABE.
     * @throws IOException - if any I/O error occurs while writing file.
	 */
	protected void writeMagic()
        throws IOException {
		fw.write(MARGIN + "magic = " + 
                 Utils.decToHex(classFile.getMagic()) + NDL);
	}
	
	/** Writes minor_version of the class file.
     * @throws IOException - if any I/O error occurs while writing file.
	 */
	protected void writeMinorVersion()
        throws IOException {
		fw.write(MARGIN + "minor_version = " +
                 classFile.getMinorVersion() + NDL);
	}

	/** Writes major_version of the class file.
     * @throws IOException - if any I/O error occurs while writing file.
	 */
	protected void writeMajorVersion()
        throws IOException {
		fw.write(MARGIN + "major_version = " +
                 classFile.getMajorVersion() + NDL);
	}
	
	/** Writes constant_pool_count of the class file. 
     * @throws IOException - if any I/O error occurs while writing file.
	 */
	protected void writeConstantPoolCount()
        throws IOException {
		fw.write(MARGIN + "constant_pool_count = ");
		fw.write(Integer.toString(classFile.getCPCount()) + NDL);
	}
	
	/** Writes constant pool of the class file.
     * @throws IOException - if any I/O error occurs while writing file.
	 */
	protected void writeConstantPool()
        throws IOException {
		fw.write(MARGIN + "constant_pool {" + NL);
		for (int i = 0; i < classFile.getActualCPCount(); i++) {
			Constant cpi = classFile.constantAt(i);
			if (cpi != null) {
				fw.write(MARGIN + INDENT_CP);
				fw.write("/* #" + Integer.toString(i + 1) + " */ ");
				fw.write(cpi.toString());
				fw.write(NL);
			}
		}
		fw.write(MARGIN + "}" + NDL);
	}

	/** Writes access_flags of the class file
     * @throws IOException - if any I/O error occurs while writing file.
	 */
	protected void writeAccessFlags()
		throws IOException {
		fw.write(MARGIN + "access_flags = ");
        fw.write(Utils.decToHex(classFile.getAccessFlags()) + COMMENT);
		fw.write(ModifiersWrapper.toString(classFile.getAccessFlags(), 
                                           ClassModifiers.classModifiers));
		fw.write(NDL);
	}
	
	/** Writes this_class index into the constant pool of the class.
     * @throws IOException - if any I/O error occurs while writing file.
	 */
	protected void writeThisClass()
        throws IOException {
		short this_class = classFile.getThisClass();
		fw.write(MARGIN + "this_class = #" + this_class);
		fw.write(COMMENT + Utils.refToValue(classFile, "#" +
                 Integer.toString(this_class)));
		fw.write(NDL);
	}
	
    /** Writes super_class index into the constant pool of the class.
     * @throws IOException - if any I/O error occurs while writing file.
     */
	protected void writeSuperClass()
        throws IOException {
		short super_class = classFile.getSuperClass();
		fw.write(MARGIN + "super_class = #" + super_class);
		if (super_class != 0) {
		    fw.write(COMMENT + Utils.refToValue(classFile, "#" +
                     Integer.toString(super_class)));
		}
		fw.write(NDL);
	}

	/** Writes interfaces_count of the class.
     * @throws IOException - if any I/O error occurs while writing file.
	 */
	protected void writeInterfacesCount()
        throws IOException {
		fw.write(MARGIN + "interfaces_count = " +
                 classFile.getInterfacesCount() + NDL);
	}
	
	/** Writes interfaces of the class file.
     * @throws IOException - if any I/O error occurs while writing file.
	 */
	protected void writeInterfaces()
		throws IOException {
		fw.write(MARGIN + "interfaces {" + NL);
		for (int i = 0; i < classFile.getActualInterfacesCount(); i++) {
			fw.write(MARGIN + INDENT_INTERFACE);
			fw.write("#" + classFile.interfaceAt(i));
			fw.write(COMMENT + Utils.refToValue(classFile, "#" +
                     Integer.toString(classFile.interfaceAt(i))));
			fw.write(NL);
		}
		fw.write(MARGIN + "}" + NDL);
	}
	
	/** Writes fields_count of the class file.
     * @throws IOException - if any I/O error occurs while writing file.
	 */
	protected void writeFieldsCount()
		throws IOException {
		fw.write(MARGIN + "fields_count = " + classFile.getFieldsCount() + NDL);
	}
	
	/** Writes fields of the class file. 
     * @throws IOException - if any I/O error occurs while writing file.
	 */
	protected void writeFields()
		throws IOException {
		if (classFile.getActualFieldsCount() == 0) {
			return;
		}
		fw.write(MARGIN + "fields {" + NL);
		for (int i = 0; i < classFile.getActualFieldsCount(); i++) {
			writeField(classFile.fieldAt(i));
		}
		fw.write(MARGIN + "}" + NDL);
	}
	
	/** Writes single field item of class file fields. 
	 * @param field - field to be written
     * @throws IOException - if any I/O error occurs while writing file.
	 */
	protected void writeField(Field field)
		throws IOException {
		String indent = INDENT_FIELD_PARAMS + INDENT_FIELD;
		fw.write(MARGIN + INDENT_FIELD + "field {" + NL);
		fw.write(MARGIN + indent  + "access_flag = " + Utils.decToHex(field.getAccessFlag()));
        fw.write(COMMENT + ModifiersWrapper.toString(field.getAccessFlag(), FieldModifiers.fieldModifiers) + NL);
		fw.write(MARGIN + indent + "name_index = #" + field.getNameIndex());
		fw.write(COMMENT + Utils.refToValue(classFile, "#" + field.getNameIndex()) + NL);
		fw.write(MARGIN + indent + "descriptor_index = #" + field.getDescriptorIndex());
		fw.write(COMMENT + Utils.refToValue(classFile, "#" + field.getDescriptorIndex()) + NL);
		fw.write(MARGIN + indent + "attributes_count = " + field.getAttributesCount() + NL);
		writeAttributes(MARGIN + indent, field.getAttributes());
		fw.write(MARGIN + INDENT_FIELD + "}" + NL);
	}
	
	/** Writes methods_count of the class.
     * @throws IOException - if any I/O error occurs while writing file.
	 */
	protected void writeMethodsCount()
        throws IOException {
		fw.write(MARGIN + "methods_count = " + classFile.getMethodsCount() + NDL);
	}
	
	/** Write methods of the class
     * @throws IOException - if any I/O error occurs while writing file.
	 */
	protected void writeMethods()
		throws IOException {
		if (classFile.getActualMethodsCount() == 0) {
			return;
		}
		fw.write(MARGIN + "methods {" + NL);
		for (int i = 0; i < classFile.getActualMethodsCount(); i++) {
			writeMethod(classFile.methodAt(i));
		}
		fw.write(MARGIN + "}" + NDL);
	}
	
	/** Writes single method item of the class file. 
	 * @param method - method to be written
     * @throws IOException - if any I/O error occurs while writing file.
	 */
	protected void writeMethod(Method method)
		throws IOException {
		String indent = INDENT_METHOD + INDENT_METHOD_PARAMS;
		fw.write(MARGIN + INDENT_METHOD + "method {" + NL);
		fw.write(MARGIN + indent  +"access_flag = " + Utils.decToHex(method.getAccessFlag()));
		fw.write(COMMENT + ModifiersWrapper.toString(method.getAccessFlag(), MethodModifiers.methodModifiers) + NL);
		fw.write(MARGIN + indent + "name_index = #" + method.getNameIndex());
		fw.write(COMMENT + Utils.refToValue(classFile, "#" + method.getNameIndex()) + NL);
		fw.write(MARGIN + indent + "descriptor_index = #" + method.getDescriptorIndex());
		fw.write(COMMENT + Utils.refToValue(classFile, "#" + method.getDescriptorIndex()) + NL);
		fw.write(MARGIN + indent + "attributes_count = " + method.getAttributesCount() + NL);
		writeAttributes(indent + INDENT_ATTR, method.getAttributes());
		fw.write(MARGIN + INDENT_METHOD + "}" + NL);
	}
	
	/** Writes attributes_count of the class.
     * @throws IOException - if any I/O error occurs while writing file.
	 */
	protected void writeAttributesCount()
		throws IOException {
		fw.write(MARGIN + "attributes_count = " + classFile.getAttributesCount() + NL);
	}
	

	/** Writes attributes of the class file
	 * @param indent - indent
	 * @param attributes - list of the attributes to be written
     * @throws IOException - if any I/O error occurs while writing file.
	 */
	protected void writeAttributes(String indent, ArrayList attributes)
		throws IOException {
		if (attributes.size() == 0) {
			return;
		}
		String attrIndent = "  ";
		String paramIndent = "  ";
		fw.write(indent + "attributes {" + NL);
		for (int i = 0; i < attributes.size(); i++) {
			Attribute attr = (Attribute) attributes.get(i);
            if (attr.isGeneral()) {
                writeAttrGeneral(indent + attrIndent, (General) attr);
            } else {
                fw.write(indent + attrIndent + "attribute ");
                Constant ci = classFile.constantAt(attr.getAttributeNameIndex() - 1);
                if (ci.getTag() == CONSTANT_Utf8) {
                    ConstantUtf8 utf8i = (ConstantUtf8) ci;
                    String name = new String(utf8i.getBytes());
                    fw.write(name + " {" + NL);
                }
                fw.write(indent + attrIndent + paramIndent + "attribute_name_index = #" + attr.getAttributeNameIndex());
                fw.write(COMMENT + Utils.refToValue(classFile, "#" + attr.getAttributeNameIndex()) + NL);
                fw.write(indent + attrIndent + paramIndent + "attribute_length = " + attr.getAttributeLength() + NL);
                if (attr instanceof ConstantValue) {
                    writeAttrConstantValue(indent + attrIndent + paramIndent,
                                           (ConstantValue) attr);
                } else if (attr instanceof Code) {
                    writeAttrCode(indent + attrIndent + paramIndent,
                                  (Code) attr);
                } else if (attr instanceof Exceptions) {
                    writeAttrExceptions(indent + attrIndent + paramIndent,
                                        (Exceptions) attr);
                } else if (attr instanceof InnerClasses) {
                    writeAttrInnerClasses(indent + attrIndent + paramIndent,
                                          (InnerClasses) attr);
                } else if (attr instanceof Synthetic) {
                    writeAttrSynthetic(indent + attrIndent + paramIndent,
                                       (Synthetic) attr);
                } else if (attr instanceof SourceFile) {
                    writeAttrSourceFile(indent + attrIndent + paramIndent,
                                        (SourceFile) attr);
                } else if (attr instanceof LineNumberTable) {
                    writeAttrLNT(indent + attrIndent + paramIndent,
                                 (LineNumberTable) attr);
                } else if (attr instanceof LocalVariableTable) {
                    writeAttrLVT(indent + attrIndent + paramIndent,
                                 (LocalVariableTable) attr);
                } else if (attr instanceof LocalVariableTypeTable) {
                    writeAttrLVTT(indent + attrIndent + paramIndent,
                                 (LocalVariableTypeTable) attr);
                } else if (attr instanceof DeprecatedAttr) {
                    writeAttrDeprecated(indent + attrIndent,
                                        (DeprecatedAttr) attr); 
                } else if (attr instanceof SourceDebugExtension) {
                	writeAttrSourceDebugExtension(indent + attrIndent,
                                        (SourceDebugExtension) attr); 
                } else if (attr instanceof Signature) {
                    writeAttrSignature(indent + attrIndent + paramIndent,
                                       (Signature) attr);
                } else if (attr instanceof EnclosingMethod) {
                    writeAttrEnclosingMethod(indent + attrIndent + paramIndent,
                                             (EnclosingMethod) attr);
                } else if (attr instanceof AnnotationDefault) {
                    writeAttrAnnotationDefault(indent + attrIndent + paramIndent,
                                             (AnnotationDefault) attr);
                } else if (attr instanceof RuntimeVisibleAnnotations) {
                    writeAttrRuntimeVisibleAnnotations(indent + attrIndent + paramIndent,
                                             (RuntimeVisibleAnnotations) attr);
                } else if (attr instanceof RuntimeInvisibleAnnotations) {
                        writeAttrRuntimeInvisibleAnnotations(indent + attrIndent + paramIndent,
                                                 (RuntimeInvisibleAnnotations) attr);
                } else if (attr instanceof RuntimeVisibleParameterAnnotations) {
                    writeAttrRuntimeVisibleParameterAnnotations(indent + attrIndent + paramIndent,
                                             (RuntimeVisibleParameterAnnotations) attr);
                } else if (attr instanceof RuntimeInvisibleParameterAnnotations) {
                    writeAttrRuntimeInvisibleParameterAnnotations(indent + attrIndent + paramIndent,
                                             (RuntimeInvisibleParameterAnnotations) attr);
                }
                fw.write(indent + attrIndent + "}" + NL);
            }
		}
		fw.write(indent + "}" + NL);
	}
    
    /** Writes attribute General
     * @param indent - indent
     * @param attr - attribute to be written
     * @throws IOException - if any I/O error occurs while writing file.
     */
    protected void writeAttrGeneral(String indent, General attr) 
        throws IOException {
        fw.write(indent + "attribute {" + COMMENT + 
                 "unknown attribute or wrong name_index" + NL);
        byte[] bytes = attr.getBytes();
        int columnsNumber = 8;
        int j = 0;
        for (int i = 0; i < bytes.length; i += j) {
            fw.write(indent + INDENT_ATTR);
            for (j = 0; j < columnsNumber && (i + j) < bytes.length; j++) {
                fw.write(Utils.decToHex(bytes[i + j]) + " ");
            }
            fw.write(NL);
        }
        fw.write(indent + "}" + NL);
    }
    
    /** Writes attribute EnclosingMethod
     * @param indent - indent
     * @param attr - attr to be written
     * @throws IOException - if any I/O error occurs while writing file.
     */
    protected void writeAttrEnclosingMethod(String indent, EnclosingMethod attr)
        throws IOException {
        fw.write(MARGIN + indent + "class_index = #");
        fw.write(Short.toString(attr.getClassIndex()));
        fw.write(COMMENT + Utils.refToValue(classFile,
                                            "#" + attr.getClassIndex()) + NL);
        fw.write(MARGIN + indent + "method_index = #");
        fw.write(Short.toString(attr.getMethodIndex()));
        fw.write(COMMENT + Utils.refToValue(classFile,
                                            "#" + attr.getMethodIndex()) + NL);
    }
    
    /** Writes attribute Signature
     * @param indent - indent
     * @param attr - attribute to be written
     * @throws IOException - if any I/O error occurs while writing file.
     */
    protected void writeAttrSignature(String indent, Signature attr)
        throws IOException {
        fw.write(MARGIN + indent + "signature_index = #");
        fw.write(Short.toString(attr.getSignatureIndex()));
        fw.write(COMMENT + Utils.refToValue(classFile,
                                            "#" + attr.getSignatureIndex()) + NL);
    }
    
	/** Writes attribute ConstantValue
	 * @param indent - indent
	 * @param attr - attribute to be written
     * @throws IOException - if any I/O error occurs while writing file.
	 */
	protected void writeAttrConstantValue(String indent, ConstantValue attr)
		throws IOException {
		fw.write(MARGIN + indent + "constantvalue_index = #");
		fw.write(Integer.toString(attr.getConstantValueIndex()));
		fw.write(COMMENT + Utils.refToValue(classFile,
                                            "#" + attr.getConstantValueIndex()) + NL);
	}
	
	/** Writes attribute Code
	 * @param indent - indent
	 * @param attr - attribute to be written
     * @throws IOException - if any I/O error occurs while writing file.
	 */
	protected void writeAttrCode(String indent, Code attr)
		throws IOException {
		fw.write(MARGIN + indent + "max_stack = ");
		fw.write(attr.getMaxStack() + NL);
		fw.write(MARGIN + indent + "max_locals = ");
		fw.write(attr.getMaxLocals() + NL);
		fw.write(MARGIN + indent + "code_length = ");
		fw.write(attr.getCodeLength() + NL);
		fw.write(MARGIN + indent + "code ");
		if ((code_type & CT_ASM) != 0) {
			fw.write("asm {" + NL);
			Vector vec = InstructionsTable.toStringVector(attr.getCodeAsByteArray());
			for (int i = 0; i < vec.size(); i++) {
				fw.write(MARGIN + indent + (String) vec.elementAt(i) + NL);
			}
		} else if ((code_type & CT_BIN) != 0) {
			fw.write("bin {" + NL);
			int ccount = 8;
			int j = 0;
			for (int i = 0; i < attr.getActualCodeLength(); i += j) {
				fw.write(MARGIN + indent + "       ");
				for (j = 0; j < ccount && (i + j) < attr.getActualCodeLength(); j++) {
					fw.write(Utils.decToHex(attr.codeAt(i + j), true));
					if (j != (ccount - 1)) {
						fw.write(" ");
					}
				}
				fw.write(NL);
			}
		}
		fw.write(MARGIN + indent + "}" + NL);
		fw.write(MARGIN + indent + "exception_table_length = " + 
                 attr.getExceptionTableLength() + NL);
		String elm = "  ";
		if (attr.getActualExceptionTableLength() > 0) {
			fw.write(MARGIN + indent + "exception_table {" + NL);
			fw.write(MARGIN + indent + elm + 
                     "// start_pc end_pc handler_pc catch_type" + NL);
			for (int i = 0; i < attr.getActualExceptionTableLength(); i++) {
				Code.exception ex = attr.exceptionAt(i);
				fw.write(MARGIN + indent + elm + ex.toString() + NL);
			}
			fw.write(MARGIN + indent + "}" + NL);
		}
		fw.write(MARGIN + indent + "attributes_count = " + attr.getAttributesCount() + NL);
		writeAttributes(indent + elm, attr.getAttributes());
	}
	
	/** Writes attribute Exceptions
	 * @param indent - indent
	 * @param attr - attribute to be written
     * @throws IOException - if any I/O error occurs while writing file.
	 */
	protected void writeAttrExceptions(String indent, Exceptions attr)
		throws IOException {
		short num = attr.getNumberOfExceptions();
		fw.write(MARGIN + indent + "number_of_exceptions = " + num + NL);
		fw.write(MARGIN + indent + "exception_index_table {" + NL);
		for (int i = 0; i < num; i++) {
			short exc = attr.exceptionAt(i);
			fw.write(MARGIN + indent + "  #" + exc);
			fw.write(COMMENT + Utils.refToValue(classFile, "#" + exc) + NL);
		}
		fw.write(MARGIN + indent + "}" + NL);
	}

	/** Writes attribute InnerClass
	 * @param indent - indent
	 * @param attr - attribute to be written
     * @throws IOException - if any I/O error occurs while writing file.
	 */
	protected void writeAttrInnerClasses(String indent, InnerClasses attr)
		throws IOException {

		fw.write(MARGIN + indent + "number_of_classes = ");
		fw.write(attr.getNumberOfClasses() + NL);
		String innerClass_indent = "  ";
		String clm = "  " + innerClass_indent;
		for (int i = 0; i < attr.getClassesSize(); i++) {
			fw.write(indent + "InnerClass {" + NL);
			InnerClasses.inner_class ic = attr.classAt(i);
			fw.write(indent + clm + "inner_class_info_index = #" + ic.getInnerClassInfoIndex());
			if (ic.getInnerClassInfoIndex() != 0) {
			    fw.write(COMMENT + Utils.refToValue(classFile, "#" + ic.getInnerClassInfoIndex()));
			}
			fw.write(NL);
			fw.write(indent + clm + "outer_class_info_index = #" + ic.getOuterClassInfoIndex());

            /* if inner class is not a member, then write special comment,
			 * else write constant pool value */
			if (ic.getOuterClassInfoIndex() == 0) {
			    fw.write(COMMENT + "inner class is not a member");
			} else {
			    fw.write(COMMENT + Utils.refToValue(classFile, "#" + ic.getOuterClassInfoIndex()));
			}
			fw.write(NL);

			fw.write(indent + clm + "inner_name_index = #" + ic.getInnerNameIndex());
			/* if inner class is anonymous, then write special comment,
			 * else write constant pool value */
			if (ic.getInnerNameIndex() == 0) {
			    fw.write(COMMENT + "anonymous inner class");
			} else {
			    fw.write(COMMENT + Utils.refToValue(classFile, "#" + ic.getInnerNameIndex()));
			}
			fw.write(NL);
			fw.write(indent + clm + "inner_class_access_flags = " + 
                     Utils.decToHex(ic.getInnerClassAccessFlags()) + COMMENT);
            fw.write(ModifiersWrapper.toString(ic.getInnerClassAccessFlags(), 
                                                 InnerClassModifiers.innerClassModifiers) + NL);
			fw.write(indent + "}" + NL);
		}
	}

	/** Writes attribute Synthetic
	 * @param indent - indent
	 * @param attr - attribute to be written
     * @throws IOException - if any I/O error occurs while writing file.
	 */
	protected void writeAttrSynthetic(String indent, Synthetic attr)
        throws IOException {
	}

	/** Writes attribute SourceFile
	 * @param indent - indent
	 * @param attr - attribute to be written
     * @throws IOException - if any I/O error occurs while writing file.
	 */
	protected void writeAttrSourceFile(String indent, SourceFile attr)
        throws IOException {
		short sfi = attr.getSourceFileIndex();
		fw.write(MARGIN + indent + "sourcefile_index = #" + sfi);
		fw.write(COMMENT + Utils.refToValue(classFile, "#" + sfi) + NL);
	}

	/** Writes attribute LineNumberTable
	 * @param indent - indent
	 * @param attr - attribute to be written
     * @throws IOException - if any I/O error occurs while writing file.
	 */
	protected void writeAttrLNT(String indent, LineNumberTable attr)
        throws IOException {
		fw.write(MARGIN + indent + "line_number_table_length = " + attr.getLineNumberTableLength() + NL);
		fw.write(MARGIN + indent + "line_number_table {" + NL);
		String llm_indent = "  ";
		String llm = "  " + llm_indent;
		fw.write(MARGIN + indent + llm + "// start_pc line_number" + NL);
		for (int i = 0; i < attr.getLineNumberTableSize(); i++) {
			fw.write(MARGIN + indent + llm);
			LineNumberTable.line_number ln = attr.lineNumberAt(i);
			fw.write(ln.toString() + NL);
		}
		fw.write(MARGIN + indent + "}" + NL);
	}

	/** Writes attribute LocalVariableTable
	 * @param indent - indent
	 * @param attr - attribute to be written
     * @throws IOException - if any I/O error occurs while writing file.
	 */
	protected void writeAttrLVT(String indent, LocalVariableTable attr)
		throws IOException {

		fw.write(MARGIN + indent + "local_variable_table_length = ");
		fw.write(attr.getLocalVariableTableLength() + NL);
		fw.write(MARGIN + indent + "local_variable_table {" + NL);
		String llm_indent = "  ";
		String llm = "  " + llm_indent;
		fw.write(MARGIN + indent + llm);
		fw.write("// start_pc length name_index descriptor_index index" + NL);
  		for (int i = 0; i < attr.getLocalVariableTableSize(); i++) {
  			fw.write(MARGIN + indent + llm);
			LocalVariableTable.local_variable lv = attr.localVariableAt(i);
			fw.write(lv.toString() + NL);
  		}
		fw.write(MARGIN + indent + "}" + NL);
	}
	
	/** Writes attribute LocalVariableTypeTable
	 * @param indent - indent
	 * @param attr - attribute to be written
     * @throws IOException - if any I/O error occurs while writing file.
	 */
	protected void writeAttrLVTT(String indent, LocalVariableTypeTable attr)
		throws IOException {

		fw.write(MARGIN + indent + "local_variable_type_table_length = ");
		fw.write(attr.getLocalVariableTypeTableLength() + NL);
		fw.write(MARGIN + indent + "local_variable_type_table {" + NL);
		String llm_indent = "  ";
		String llm = "  " + llm_indent;
		fw.write(MARGIN + indent + llm);
		fw.write("// start_pc length name_index signature_index index" + NL);
  		for (int i = 0; i < attr.getLocalVariableTypeTableSize(); i++) {
  			fw.write(MARGIN + indent + llm);
			LocalVariableTypeTable.local_variable lv = attr.localVariableAt(i);
			fw.write(lv.toString() + NL);
  		}
		fw.write(MARGIN + indent + "}" + NL);
	}

	/** Writes attribute Deprecated
	 * @param indent - indent
	 * @param attr - attribute to be written
     * @throws IOException - if any I/O error occurs while writing file.
	 */
	protected void writeAttrDeprecated(String indent, DeprecatedAttr attr)
        throws IOException {
	}
	
	protected void writeAttrSourceDebugExtension(String indent, SourceDebugExtension attr)
    throws IOException {
		byte [] debug_extension= attr.getDebugExtension();
		fw.write(MARGIN + indent+ INDENT_ATTR + "debug_extension {");
		if(debug_extension != null) {
			for(int i = 0; i < debug_extension.length; ++i) {
				if(i % 16 == 0) {
					fw.write(NL + MARGIN + indent + INDENT_ATTR);
				}
				fw.write(INDENT_ATTR + Utils.decToHex(debug_extension[i]));
			}
		}
		fw.write(NL + MARGIN + indent+ INDENT_ATTR + "}"  + NL);
	}
	/**
	 * writes element value subattribute
	 * @param indent
	 * @param elementValue
	 * @throws IOException
	 */
	protected void writeElementValue(String indent, ElementValue elementValue)
    throws IOException {
		byte tag = elementValue.getTag();
		fw.write(MARGIN + indent + "tag = " + ((char)tag) + NL);
		ElementValue.value value= elementValue.getValue();
		if(value instanceof ElementValue.const_value) {
			short const_value_index = ((ElementValue.const_value)value).getConstValueIndex();
			fw.write(MARGIN + indent+ "const_value_index = #" + const_value_index);
			fw.write(COMMENT + Utils.refToValue(classFile,"#" + const_value_index) + NL);
		}
		else if(value instanceof ElementValue.enum_value) {
			short type_name_index = ((ElementValue.enum_value)value).getTypeNameIndex();
			short const_name_index = ((ElementValue.enum_value)value).getConstNameIndex();
			fw.write(MARGIN + indent+ "type_name_index = #" + type_name_index);
			fw.write(COMMENT + Utils.refToValue(classFile,"#" + type_name_index) + NL);
			fw.write(MARGIN + indent+ "const_name_index = #" + const_name_index);
			fw.write(COMMENT + Utils.refToValue(classFile,"#" + const_name_index) + NL);
		}
		else if(value instanceof ElementValue.class_value) {
			short class_info_index = ((ElementValue.class_value)value).getClassInfoIndex();
			fw.write(MARGIN + indent+ "class_info_index = #" + class_info_index);
			fw.write(COMMENT + Utils.refToValue(classFile,"#" + class_info_index) + NL);
		}
		else if(value instanceof ElementValue.annotation_value) {
			Annotation annotation = ((ElementValue.annotation_value) value).getAnnotation();
			writeAnnotation(MARGIN + indent, annotation);
		}
		else if(value instanceof ElementValue.array_value) {
			ElementValue [] innerElementValue = ((ElementValue.array_value)value).getArrayValue();
			fw.write(MARGIN + indent + "array_length = " + innerElementValue.length + " {" + NL);
			for(int i = 0; i < innerElementValue.length; ++i) {
				writeElementValue(MARGIN + indent + INDENT_ATTR, innerElementValue[i]);
			}
			fw.write(MARGIN + indent + "}" + NL);
		}

	}
	
	protected void writeElementValuePairs(String indent, ElementValuePair elementValuePair)
    throws IOException {
		fw.write(MARGIN + indent+ "{" + NL);
		short element_name_index = elementValuePair.getElementNameIndex();
		fw.write(MARGIN + indent+ INDENT_ATTR + "element_name_index = #" + element_name_index);
		fw.write(COMMENT + Utils.refToValue(classFile,"#" + element_name_index) + NL);
		writeElementValue(MARGIN + indent + INDENT_ATTR, elementValuePair.getElementValue());
		fw.write(MARGIN + indent+ "}" + NL);
	}
	
	protected void writeAnnotation(String indent, Annotation annotation)
    throws IOException {
		fw.write(MARGIN + indent + "Annotation {" + NL);
		short type_index = annotation.getTypeIndex();
		fw.write(MARGIN + indent + INDENT_ATTR+ "type_index = #" + type_index);
		fw.write(COMMENT + Utils.refToValue(classFile,"#" + type_index) + NL);
		ElementValuePair [] evps = annotation.getElementValuePairs();
		int num_element_value_pairs = evps.length;
		fw.write(MARGIN + indent + INDENT_ATTR+ "num_element_value_pairs = " + num_element_value_pairs + NL);
		for(int i = 0; i < evps.length; ++i) {
			writeElementValuePairs(indent + INDENT_ATTR, evps[i]);
		}
		
		fw.write(MARGIN + indent + "}" + NL);
	}
	
	/**
	 * Writes AnnotationDefault attribute
	 * @param indent
	 * @param attr
	 * @throws IOException
	 */
	protected void writeAttrAnnotationDefault(String indent, AnnotationDefault attr)
    throws IOException {
		ElementValue elementValue = attr.getElementValue();
		writeElementValue(indent, elementValue);
	}
	
	/**
	 * Writes RuntimeVisibleAnnotations attribute
	 * @param indent
	 * @param attr
	 * @throws IOException
	 */
	protected void writeAttrRuntimeVisibleAnnotations(String indent, RuntimeVisibleAnnotations attr)
    throws IOException {
		Annotation [] annotations = attr.getAnnotations();
		int num_annotations = 0; 
		if (annotations!=null) {
			num_annotations = annotations.length;
		}
		fw.write(MARGIN + indent + "num_annotations = " + num_annotations + NL);
		for(int i = 0; i < annotations.length; ++i) {
			writeAnnotation(indent, annotations[i]);
		}
	}
	
	/**
	 * Writes RuntimeInvisibleAnnotations attribute
	 * @param indent
	 * @param attr
	 * @throws IOException
	 */
	protected void writeAttrRuntimeInvisibleAnnotations(String indent, RuntimeInvisibleAnnotations attr)
    throws IOException {
		Annotation [] annotations = attr.getAnnotations();
		int num_annotations = 0; 
		if (annotations!=null) {
			num_annotations = annotations.length;
		}
		fw.write(MARGIN + indent + "num_annotations = " + num_annotations + NL);
		for(int i = 0; i < annotations.length; ++i) {
			writeAnnotation(indent, annotations[i]);
		}
	}
	
	protected void writeAttrRuntimeVisibleParameterAnnotations(String indent, 
			RuntimeVisibleParameterAnnotations attr) throws IOException {
		RuntimeVisibleAnnotations [] annotations = attr.getAnnotations();
		int num_parameters = 0;
		if (annotations!=null) {
			num_parameters = annotations.length;
		}
		fw.write(MARGIN + indent + "num_parameters = " + num_parameters + NL);
		for(int i = 0; i < annotations.length; ++i) {
			fw.write(MARGIN + indent + '{' + NL);
			if(annotations[i] != null /*&& annotations[i].getNumAnnotations() > 0*/) {
				writeAttrRuntimeVisibleAnnotations(MARGIN + indent + INDENT_ATTR, annotations[i]);
			}
			fw.write(MARGIN + indent + '}' + NL);
		}
	}
	
	protected void writeAttrRuntimeInvisibleParameterAnnotations(String indent, 
			RuntimeInvisibleParameterAnnotations attr) throws IOException {
		RuntimeInvisibleAnnotations [] annotations = attr.getAnnotations();
		int num_parameters = 0;
		if (annotations!=null) {
			num_parameters = annotations.length;
		}
		fw.write(MARGIN + indent + "num_parameters = " + num_parameters + NL);
		for(int i = 0; i < annotations.length; ++i) {
			fw.write(MARGIN + indent + '{' + NL);
			if(annotations[i] != null /*&& annotations[i].getNumAnnotations() > 0*/) {
				writeAttrRuntimeInvisibleAnnotations(MARGIN + indent + INDENT_ATTR, annotations[i]);
			}
			fw.write(MARGIN + indent + '}' + NL);
		}
	}
	
} 