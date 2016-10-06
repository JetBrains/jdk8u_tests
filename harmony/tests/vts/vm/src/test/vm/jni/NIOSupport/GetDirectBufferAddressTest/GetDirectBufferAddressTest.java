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
 * Created on 03.30.2005
 */
package org.apache.harmony.vts.test.vm.jni.NIOSupport;

import org.apache.harmony.vts.test.vm.jni.share.JNITest;

/** 
 * Test creates the direct buffer and sends it to native,
 * native gets its address and modifies the data with some offset,
 * then java checks that the modification was correct.
 */
public class GetDirectBufferAddressTest extends JNITest{
    private native boolean nativeExecute(java.nio.ByteBuffer buffer);

    /**
     * Test logics method
     * @see org.apache.harmony.vts.test.vm.jni.share.JNITest#execute()
     * @return test result true for passed, false for failed.
     */
    public boolean execute() throws Exception{
        java.nio.ByteBuffer buf = java.nio.ByteBuffer.allocateDirect(10);
        if(!nativeExecute(buf)) return false;
        int length = buf.capacity();
        byte[] buffer = new byte[length];
        buf.get(buffer);
        return buffer[1] == 13;
    }
    public static void main(String[] args){
        System.exit(new GetDirectBufferAddressTest().test());
    }
}

