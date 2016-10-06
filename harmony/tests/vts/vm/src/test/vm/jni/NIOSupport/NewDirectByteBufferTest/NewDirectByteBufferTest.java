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
 * @author Petr Ivanov
 * @version $Revision: 1.5 $
 */  
/*
 * Created on 03.25.2005
 */
package org.apache.harmony.vts.test.vm.jni.NIOSupport;

import org.apache.harmony.vts.test.vm.jni.share.JNITest;

/**
 * Test calls native, which gets the memory region, fills it
 * with some data, and then returns the direct buffer object, 
 * which maps this area region; then java manipulates this object and
 * checks that it is correctly mapped. 
 */
public class NewDirectByteBufferTest extends JNITest{
    private native java.nio.ByteBuffer nativeExecute();
    private native boolean nativeExecuteRelease(java.nio.ByteBuffer buffer);

    /**
     * Test logics method
     * @see org.apache.harmony.vts.test.vm.jni.share.JNITest#execute()
     * @return test result true for passed, false for failed.
     */
    public boolean execute() throws Exception{
        boolean result = true;
        java.nio.ByteBuffer buf = nativeExecute();
        int length = buf.capacity();
        byte[] buffer = new byte[length];
        buf.get(buffer);
        for(int i = 0; i < buffer.length; i++){
            if(buffer[i] != i) result = false;
            else buffer[i] = (byte)(i + 1);
        }
        buf.rewind();
        buf.put(buffer);
        if (!result){
            nativeExecuteRelease(buf);
            return false;
        }
        else{
            return nativeExecuteRelease(buf);
        }
    }
    public static void main(String[] args){
        System.exit(new NewDirectByteBufferTest().test());
    }
}

