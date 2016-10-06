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
import java.io.FileWriter;
import java.io.IOException;

import org.apache.harmony.vmtt.ir.ClassFile;
import org.apache.harmony.vmtt.ir.constants.CPTags;

/**
 * @author Aleksey V. Golubitsky
 * @version $Revision: 1.3 $
 */

public abstract class CodeFileGenerator 
    extends CPTags {

	public static final int CT_ASM = 1;
	public static final int CT_BIN = 1 << 1;

	public static ClassFile classFile = null; // TODO: non-static member 
	protected FileWriter fw = null;
	protected int code_type = 0;
	
	public CodeFileGenerator() {}
	
	public CodeFileGenerator(ClassFile cf, File outputFile)
	throws NullPointerException, IOException {
	    setClassFile(cf);
	    setOutputFile(outputFile);
	}
	
	public void setClassFile(ClassFile cf)
	throws NullPointerException {
	    if (cf == null) {
	        throw new NullPointerException();
	    }
	    classFile = cf;
	}
	
	public ClassFile getClassFile() {
	    return classFile;
	}
	
	public void setOutputFile(File outputFile)
	throws NullPointerException, IOException {
	    if (outputFile == null) {
	        throw new NullPointerException();
	    }
	    fw = new FileWriter(outputFile);
	}
	
	public void generateAsmCode()
	throws IOException {
	    code_type = CT_ASM;
	    generate();
	}
	
	public void generateBinCode()
	throws IOException {
	    code_type = CT_BIN;
	    generate();
	}

	protected abstract void generate() throws IOException;
}
