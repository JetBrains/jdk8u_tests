/*
 * Copyright 2007 The Apache Software Foundation or its licensors, as applicable
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author Aleksey Ignatenko
 * @version $Revision: 1.0 $
 */

/**
 *  GOAL: Test j.l.Compiler functionality
 *  see additional description in ClassMultiBas 
 *  
 *  The test does:
 *  a. finds all class files from JRE
 *  b. tries to load all these classes
 *  c. calls compileClass for every successfully loaded Class
 *  d. for every  NUMBER_OF_CYCLE_TO_DISABLE_COMPILER-th iteration try 
 *  to compile class in Compiler.disable(); mode 
 *  
 *   No hangs, fails, crashes or hangs are expected.
 *   Note: due to spec to test JIT compiler "java.compiler" property is to be set
 *   and VM is to support j.l.Compiler functionality
 */

package org.apache.harmony.test.reliability.api.kernel.compiler;

import org.apache.harmony.test.reliability.share.ClassMultiTestBase;

public class JitCompilerTest extends ClassMultiTestBase{
    int class_compiled_counter = 0;
    final int NUMBER_OF_CYCLE_TO_DISABLE_COMPILER = 10;
    
    public static void main(String[] args){
        System.exit(new JitCompilerTest().test(args));
    }
    
    public void initCycle() {
        // do nothing
    }
    public void releaseCycle() {
        // do nothing
    }
    
    public void testContent(Class cls) {
        // for every  NUMBER_OF_CYCLE_TO_DISABLE_COMPILER-th iteration 
        // try to compile class in Compiler.disable(); mode 
        if ((class_compiled_counter%NUMBER_OF_CYCLE_TO_DISABLE_COMPILER) == 0){
            Compiler.disable();
            Compiler.compileClass(cls);
            Compiler.enable();
        }

        Compiler.compileClass(cls);
       
        class_compiled_counter++;
    }

}