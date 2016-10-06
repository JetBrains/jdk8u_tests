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

import java.io.FileWriter;
import java.io.IOException;

import org.apache.harmony.vmtt.Utils;
import org.apache.harmony.vmtt.ir.ClassFile;
import org.apache.harmony.vmtt.ir.Field;
import org.apache.harmony.vmtt.ir.Method;
import org.apache.harmony.vmtt.ir.attributes.Attribute;
import org.apache.harmony.vmtt.ir.attributes.Code;
import org.apache.harmony.vmtt.ir.attributes.ConstantValue;
import org.apache.harmony.vmtt.ir.attributes.Exceptions;
import org.apache.harmony.vmtt.ir.modifiers.ClassModifiers;
import org.apache.harmony.vmtt.ir.modifiers.FieldModifiers;
import org.apache.harmony.vmtt.ir.modifiers.MethodModifiers;
import org.apache.harmony.vmtt.ir.modifiers.ModifiersWrapper;

/**
 * @author Aleksey V. Golubitsky
 * @version $Revision: 1.5 $
 */

public class AsmGenerator extends AsmFileGenerator {
	
	private final String new_line = "\n";
	private final String space = " ";
	
	public void generate(ClassFile cf, FileWriter fileWriter)
			throws IOException {
		
		classFile = cf;
		fw = fileWriter;
		
		writeHeader();
		writeImplements();
		writeFields();
		writeMethods();
		
		fw.close();
	}
	
	protected void writeHeader()
		throws IOException {

		writeSource();
		writeClass();
		writeSuper();
	}
	
	protected void writeSource()
		throws IOException {
	}

	protected void writeClass()
		throws IOException {
		String directive = Directives.getDirective(Directives.D_CLASS);
		String access_flags = ModifiersWrapper.toString(classFile.getAccessFlags(), ClassModifiers.classModifiers).toLowerCase();
		String class_name = Utils.refToValue(classFile, "#" + classFile.getThisClass()).replaceAll("\"", "");
		fw.write(directive + space + access_flags + space + class_name + new_line);
	}
	
	protected void writeSuper()
		throws IOException {
		String directive = Directives.getDirective(Directives.D_SUPER);
		String class_name = Utils.refToValue(classFile, "#" + classFile.getSuperClass()).replaceAll("\"", "");
		fw.write(directive + space + class_name + new_line);
	}
	
	protected void writeImplements()
		throws IOException {
		String directive = Directives.getDirective(Directives.D_IMPLEMENTS);
		for (int i = 0; i < classFile.getActualInterfacesCount(); i++) {
			String interface_name = Utils.refToValue(classFile, "#" + classFile.interfaceAt(i)).replaceAll("\"", "");
			fw.write(directive + space + interface_name + new_line);
		}
	}
	
	protected void writeFields()
		throws IOException {
		
		fw.write(new_line);

		String directive = Directives.getDirective(Directives.D_FIELD);

		for (int i = 0; i < classFile.getActualFieldsCount(); i++) {
			Field f = classFile.fieldAt(i);
			
			fw.write(directive + space);
			
			String access_flag = ModifiersWrapper.toString(f.getAccessFlag(), FieldModifiers.fieldModifiers).toLowerCase();
			fw.write(access_flag + space);

			String field_name = refToValue(f.getNameIndex());
			fw.write(field_name + space);
			
			String descriptor = refToValue(f.getDescriptorIndex());
			fw.write(descriptor + space);

			for (int j = 0; j < f.getActualAttributesCount(); j++) {
				Attribute attr = f.attributeAt(j);
				if (attr instanceof ConstantValue) {
					fw.write("= ");
					writeAttr_ConstantValue((ConstantValue) attr);
					j = f.getActualAttributesCount();
				}
			}

			fw.write(new_line);
		}
	}
	
	protected void writeMethods()
		throws IOException {

		String s_directive = Directives.getDirective(Directives.D_METHOD);
		String e_directive = Directives.getDirective(Directives.D_END);
		
		for (int i = 0; i < classFile.getActualMethodsCount(); i++) {
			Method m = classFile.methodAt(i);
			
			fw.write(new_line + s_directive + space);
			
			String access_flag = ModifiersWrapper.toString(m.getAccessFlag(), MethodModifiers.methodModifiers).toLowerCase();
			fw.write(access_flag + space);
			
			String field_name = refToValue(m.getNameIndex());
			fw.write(field_name);
			
			String descriptor = refToValue(m.getDescriptorIndex());
			fw.write(descriptor + new_line);
			
			for (int j = 0; j < m.getActualAttributesCount(); j++) {
				Attribute attr = m.attributeAt(j);
				if (attr instanceof Code) {
					writeAttr_Code((Code) attr);
				} else if (attr instanceof Exceptions) {
					writeAttr_Exceptions((Exceptions) attr);
				}
			}
			
			fw.write(e_directive + " method" + new_line);
		}
	}
	
	protected void writeAttr_ConstantValue(ConstantValue attr)
		throws IOException {
		
		String cv = refToValue(((ConstantValue) attr).getConstantValueIndex());
		fw.write(cv);
	}
	
	protected void writeAttr_Code(Code attr)
		throws IOException {
		
		fw.write("\t" + Directives.getDirective(Directives.D_LIMIT) + " stack ");
		fw.write(Integer.toString(attr.getMaxStack()) + new_line);

		fw.write("\t" + Directives.getDirective(Directives.D_LIMIT) + " locals ");
		fw.write(Integer.toString(attr.getMaxLocals()) + new_line);
		
		byte[] code = attr.getCodeAsByteArray();
		fw.write(InstructionsTable.toString(code));
	}
	
	protected void writeAttr_Exceptions(Exceptions attr)
		throws IOException {
		
		String directive = Directives.getDirective(Directives.D_THROWS);
		
		for (int i = 0; i < attr.getExceptionTableSize(); i++) {
			fw.write("\t" + directive + space);
			fw.write(refToValue(attr.exceptionAt(i)) + new_line);
		}
	}

	private String refToValue(short ref) {
		return Utils.refToValue(classFile, "#" + ref).replaceAll("\"", ""); 
	}
}
