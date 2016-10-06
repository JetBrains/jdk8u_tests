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
 * @author Nikolay Y. Amosov
 * @version $Revision $
 */


package org.apache.harmony.vts.test.vm.jvms.classFile.limitations.methods.methods01;

import org.apache.harmony.share.Result;

import java.io.ByteArrayOutputStream;

public class Methods01 {

//  Number of method to generate
//  ----------------------------------------------------------------------------------------
    static int methodsQuantity = 65518;
//  ----------------------------------------------------------------------------------------

// Class File Template
    static String templateClassFilename = "ClassFileTest_";
    static byte[] template = {-54, -2, -70, -66, 0, 0, 0, 49, 0, 21, 10, 0, 5, 0, 15, 7, 0, 16, 10, 0, 2, 0,
                              15, 10, 0, 2, 0, 17, 7, 0, 18, 1, 0, 6, 60, 105, 110, 105, 116, 62, 1, 0, 3, 40,
                              41, 86, 1, 0, 4, 67, 111, 100, 101, 1, 0, 4, 109, 97, 105, 110, 1, 0, 22, 40, 91,
                              76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59,
                              41, 86, 1, 0, 4, 116, 101, 115, 116, 1, 0, 3, 40, 41, 73, 1, 0, 12, 109, 101, 116,
                              104, 111, 100, 95, 48, 48, 48, 48, 49, 1, 0, 12, 109, 101, 116, 104, 111, 100, 95,
                              48, 48, 48, 48, 50, 12, 0, 6, 0, 7, 1, 0, 14, 67, 108, 97, 115, 115, 70, 105, 108,
                              101, 84, 101, 115, 116, 95, 12, 0, 11, 0, 12, 1, 0, 16, 106, 97, 118, 97, 47, 108,
                              97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 1, 0, 12, 109, 101, 116, 104, 111, 100,
                              95, 48, 48, 48, 48, 51, 1, 0, 12, 109, 101, 116, 104, 111, 100, 95, 48, 48, 48, 48, 52,
                              0, 32, 0, 2, 0, 5, 0, 0, 0, 0, 0, 7, 0, 0, 0, 6, 0, 7, 0, 1, 0, 8, 0, 0, 0, 17, 0, 1,
                              0, 1, 0, 0, 0, 5, 42, -73, 0, 1, -79, 0, 0, 0, 0, 0, 9, 0, 9, 0, 10, 0, 1, 0, 8, 0, 0,
                              0, 24, 0, 2, 0, 1, 0, 0, 0, 12, -69, 0, 2, 89, -73, 0, 3, -74, 0, 4, 87, -79, 0, 0, 0,
                              0, 0, 1, 0, 11, 0, 12, 0, 1, 0, 8, 0, 0, 0, 15, 0, 1, 0, 1, 0, 0, 0, 3, 16, 104, -84, 0,
                              0, 0, 0, 0, 0, 0, 13, 0, 7, 0, 1, 0, 8, 0, 0, 0, 13, 0, 0, 0, 1, 0, 0, 0, 1, -79, 0, 0,
                              0, 0, 0, 0, 0, 14, 0, 7, 0, 1, 0, 8, 0, 0, 0, 13, 0, 0, 0, 1, 0, 0, 0, 1, -79, 0, 0, 0,
                              0, 0, 0, 0, 19, 0, 7, 0, 1, 0, 8, 0, 0, 0, 13, 0, 0, 0, 1, 0, 0, 0, 1, -79, 0, 0, 0, 0,
                              0, 0, 0, 20, 0, 7, 0, 1, 0, 8, 0, 0, 0, 13, 0, 0, 0, 1, 0, 0, 0, 1, -79, 0, 0, 0, 0, 0, 0};

    public static void main(String[] args) throws Exception {
        System.exit((new Methods01()).test(args));
    }

    public int test(String[] args) throws Exception {
        ByteArrayOutputStream testedClass; //  = new ByteArrayOutputStream();
        testedClass = generateClassByTemplate(template);
        testCL cl = new testCL();
        cl.loadClass(testedClass);
        return Result.PASS;
    }

    /**
     * This method generates class by template and return it in ByteArrayOutputStream
     *
     * @param classTemplate - class's binary template in array of bytes
     * @return class in ByteArrayOutputStream
     */
    public ByteArrayOutputStream generateClassByTemplate(byte[] classTemplate) /*throws IOException */ {
        ByteArrayOutputStream toRet = new ByteArrayOutputStream();
        byte[] methodsStr = new byte[]{1, 0, 12,

                                       "m".getBytes()[0],
                                       "e".getBytes()[0],
                                       "t".getBytes()[0],
                                       "h".getBytes()[0],
                                       "o".getBytes()[0],
                                       "d".getBytes()[0],
                                       "_".getBytes()[0]};
        byte[] method_number;

        int templMethodCount = 4;

        toRet.write(classTemplate, 0, 8 - 0);
// Change constant pool counter
        int constant_pool_counter = (classTemplate[9] + (methodsQuantity - templMethodCount));
// Write constant pool counter as 2 byte
        toRet.write( intTo2Bytes (constant_pool_counter), 0, 2 );

// Add methods name to constant_pool_table
        toRet.write(classTemplate, 10, 204 - 10);

        for (int i = templMethodCount; i < methodsQuantity; ++i) {
            method_number = fixFiveNun(i + 1).getBytes();
            toRet.write(methodsStr, 0, methodsStr.length);
            toRet.write(method_number, 0, method_number.length);
        }

// Constant part
        toRet.write(classTemplate, 204, 214 - 204 /*215*/);

// Write 2 byte to methods_count
        int method_counter = (methodsQuantity + 3);
// Write method_counter as 2 byte
        toRet.write( intTo2Bytes (method_counter), 0, 2 );

// Constant part
        toRet.write(classTemplate, 216, 422 - 216);

// Add methods bodies
        for (int i = templMethodCount; i < methodsQuantity; ++i) {
            toRet.write(classTemplate, 395, 397 - 395);
            int name_index = (classTemplate[398] + (i + 1 - templMethodCount));
// write name_index as 2 byte
            toRet.write( intTo2Bytes (name_index), 0, 2 );

            toRet.write(classTemplate, 399, 422 - 399);
        }
        toRet.write(classTemplate, 422, 424 - 422);
        return toRet;
    }


    byte[] intTo2Bytes(int int_value) {
        byte[] toRet = new byte[2];
        toRet[0] = (byte) (int_value >> 8);
        toRet[1] = (byte) (int_value & 0xFF);
        return toRet;
    }


    /**
     * This method converts integer to 5 digits string
     *
     * @param num - integer to convert to String
     * @return String represented last 5 numbers of num
     */
    static String fixFiveNun(int num) {
        StringBuffer toRet = new StringBuffer();
        for (int i = 0; i < 5; ++i) {
            toRet.insert(0, num % 10);
            num /= 10;
        }
        return toRet.toString();
    }


    class testCL extends ClassLoader {
        public Class loadClass(ByteArrayOutputStream classInStream)
                throws ClassNotFoundException {
            byte[] dt = classInStream.toByteArray();
            return defineClass(templateClassFilename, dt, 0, dt.length);
        }
    }

}