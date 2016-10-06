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
package org.apache.harmony.vts.test.vm.jni.register_natives;

import java.io.FileInputStream;
import org.apache.harmony.vts.test.vm.jni.share.JNITest;

/** 
 * @author Petr Ivanov, Vladimir Nenashev
 * @version $Revision: 1.4 $
 */  

/**
 * Test for RegisterNatives()
 */
public class RegisterNativesTest extends JNITest {
    private native boolean nativeExecute(byte[] raw, int length, Object classLoader);
    private String filePath;
    
    /**
     * native defines the java class with native methods, then
     * registers these methods to this class and returns class in java;
     * java then just calls one of the returned class'
     * native methods and makes sure it works as expected. 
     * 
     * @see org.apache.harmony.vts.test.vm.jni.share.JNITest#execute()
     */
    public boolean execute() throws Exception{
        if(testArgs.length < 1) {
            filePath = "ConstructedClass.hidden";
        } else {
            filePath = testArgs[0];
        }
    	FileInputStream hiddenFile = new FileInputStream( filePath );
    	int buf = hiddenFile.read();
	    int counter = 0;
	    byte[] buffer = new byte[50000];
	    while (buf != -1){
		    buffer[counter] = (byte)buf;
		    buf = hiddenFile.read();
		    counter++;
	    }
	    hiddenFile.close();
	    byte[] data = new byte[counter];
        for (int k = 0; k < counter; k++) {
            data[k] = buffer[k];
        }
	    return nativeExecute(data, counter, this.getClass().getClassLoader());
    }

    public static void main(String[] args){
	    RegisterNativesTest testInstance = new RegisterNativesTest();
	    System.exit(testInstance.test(args));
    }
}
