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
/** 
 * @author Gregory Shimansky, Petr Ivanov
 * @version $Revision: 1.1 $
 */  
/*
 * Created on 03.11.2004
 *
 */
package org.apache.harmony.vts.test.vm.jni.share;

import org.apache.harmony.share.*;

/**
 * @author Gregory Shimansky
 *
 * Abstract JNI test class, defines methods for test execution.
 */
public abstract class JNITest extends Test{
    static boolean NoLibrary = false;
    static {
        try{
            System.loadLibrary("jnitests");
        }
        catch(Throwable e){
            NoLibrary = true; 
        }
    }
    public int test(){
	    boolean result;
        if (NoLibrary) return fail("Native lib is not loaded");
	    try {
		    result = execute();
	    }
            catch( Throwable e ){
		    e.printStackTrace();
		    return fail("Failed because of exception");
	    }	    
	    return result ? pass() : fail("Failed") ;
    }
    /**
     * Main test execution method.
     * @return test result true for passed, false for failed.
     */
    public abstract boolean execute() throws Exception;
//	    return false;
//    }
}
