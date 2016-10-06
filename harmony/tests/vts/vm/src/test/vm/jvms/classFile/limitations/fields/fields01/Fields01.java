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

package org.apache.harmony.vts.test.vm.jvms.classFile.limitations.fields.fields01;

import org.apache.harmony.share.Result;

import java.io.ByteArrayOutputStream;

public class Fields01 {

//  Number of fields to generate
//  ----------------------------------------------------------------------------------------
    static int fieldsQuantity = 65524;
//  ----------------------------------------------------------------------------------------

// Class File Template
    static String templateClassFilename = "TooManyFields";
    static byte[] template =
            {-54, -2, -70, -66, // magic = xCAFEBABE        0-3
             0, 0, // minor_version = 0        4-5
             0, 49, // minor_version = 49       6-7
             0, 14, // constant_pool_counter   8-9
             // ---------- constant pool begin
             10, 0, 3, 0, 10, // method                  10-14
             7, 0, 11, // class                   15-17
             7, 0, 12, // class                   18-20
             1, 0, 6, 105, 48, 48, 48, 48, 49, // "i00001"                21-29
             1, 0, 1, 73, // "I"                     30-33
             1, 0, 6, 105, 48, 48, 48, 48, 50, // "i00002"                34-42
             1, 0, 6, 60, 105, 110, 105, 116, 62, // 43-51
             1, 0, 3, 40, 41, 86, // 52-57
             1, 0, 4, 67, 111, 100, 101, // 58-64
             12, 0, 7, 0, 8, // 65-69
             1, 0, 13, 84, 111, 111, 77, 97, 110, 121, 70, 105, 101, 108, 100, 115, // "TooManyFields"  70-84
             1, 0, 16, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, // "java/lang/Object"  85-103
             1, 0, 6, 105, 48, 48, 48, 48, 51, // "i00003"                104-112
             // ---------- constant pool end
             0, 33, // access_flags;                         113-114
             0, 2, // this_class                            115-116
             0, 3, // super_class                           117-118
             0, 0, // interfaces_count                      119-120
             0, 3, // fields_count                          121-122
             // begin fields
             0, 0, 0, 4, 0, 5, 0, 0, // i00001         123-130
             0, 0, 0, 6, 0, 5, 0, 0, // i00002         131-138
             0, 0, 0, 13, 0, 5, 0, 0, // i00003         139-146
             // end fields
             0, 1, 0, 1, 0, 7, 0, 8, 0, 1, 0, 9, 0, 0, 0,
             17, 0, 1, 0, 1, 0, 0, 0, 5, 42, -73, 0, 1, -79, 0, 0, 0, 0, 0, 0};


    public static void main(String[] args) throws Exception {
        System.exit((new Fields01()).test(args));
    }

    public int test(String[] args) throws Exception {
        ByteArrayOutputStream testedClass;
        testedClass = generateClassByTemplate(template);
        testCL cl = new testCL();
        cl.loadClass(testedClass);
        return Result.PASS;
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


    /**
     * This method generates class by template and return it in ByteArrayOutputStream
     *
     * @param classTemplate - class's binary template in array of bytes
     * @return class in ByteArrayOutputStream
     */
    public ByteArrayOutputStream generateClassByTemplate(byte[] classTemplate) /*throws IOException */ {
        ByteArrayOutputStream toRet = new ByteArrayOutputStream();
        byte[] fieldNumber;

        int templFieldsCount = 3;

        toRet.write(classTemplate, 0, 8 - 0);
        // Change constant pool counter
        int constant_pool_counter = (classTemplate[9] + (fieldsQuantity - templFieldsCount));
        // Write constant pool counter as 2 byte
        toRet.write(intTo2Bytes(constant_pool_counter), 0, 2);

        // Add fields name to constant_pool_table
        toRet.write(classTemplate, 10, 114 - 10);

        for (int i = templFieldsCount; i < fieldsQuantity; ++i) {
            toRet.write(classTemplate, 105, 109 - 105);
            fieldNumber = fixFiveNun(i + 1).getBytes();
            toRet.write(fieldNumber, 0, fieldNumber.length);
        }

        // Constant part
        toRet.write(classTemplate, 114, 122 - 114);

        // Write 2 byte to fields_count
        int field_counter = (fieldsQuantity);
        // Write fields_counter as 2 byte
        toRet.write(intTo2Bytes(field_counter), 0, 2);

        // Constant part
        toRet.write(classTemplate, 124, 148 - 124);

        // Add fields bodies
        for (int i = templFieldsCount; i < fieldsQuantity; ++i) {
            toRet.write(classTemplate, 140, 142 - 140);
            int name_index = (classTemplate[143] + (i + 1 - templFieldsCount));
            // write name_index as 2 byte
            toRet.write(intTo2Bytes(name_index), 0, 2);

            toRet.write(classTemplate, 144, 148 - 144);
        }
        toRet.write(classTemplate, 148, classTemplate.length - 148);
        return toRet;
    }

    byte[] intTo2Bytes(int int_value) {
        byte[] toRet = new byte[2];
        toRet[0] = (byte) (int_value >> 8);
        toRet[1] = (byte) (int_value & 0xFF);
        return toRet;
    }


    class testCL extends ClassLoader {
        public Class loadClass(ByteArrayOutputStream classInStream)
                throws ClassNotFoundException {
            byte[] dt = classInStream.toByteArray();
            return defineClass(templateClassFilename, dt, 0, dt.length);
        }
    }


}