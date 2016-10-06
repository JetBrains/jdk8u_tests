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
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.harmony.vmtt.ir.constants.CPTags;

/**
 * @author Aleksey V. Golubitsky
 * @version $Revision: 1.3 $
 */

public abstract class ClassFileGenerator extends CPTags {

	protected ClassFile classFile = null;
	protected DataOutputStream dos = null;
	
	public ClassFileGenerator() {}
	
	public ClassFileGenerator(ClassFile cf, File outputFile)
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
	    dos = new DataOutputStream(new FileOutputStream(outputFile));
	}
	
	public abstract void generate() throws IOException; 
}
