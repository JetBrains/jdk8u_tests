/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
/**
 */
package org.apache.harmony.test.func.reg.vm.btest7252;

import java.io.*;
import java.util.logging.Logger;
import org.apache.harmony.test.share.reg.RegressionTest;

public class Btest7252 extends RegressionTest {

    public static void main(String[] args) {
        System.exit(new Btest7252().test(Logger.global, args));
    }

    public int test(Logger logger, String[] args) {
        try {
            InputStream in = Btest7252.class.getClassLoader().
                    getResourceAsStream("/org/apache/harmony/test/func/reg/vm/btest7252/Btest7252.class");

            // first detect length of class file
            int length = 0;

            while (in.read() != -1) {
                length++;
            }
            byte b[] = new byte[length]; 
            in.close();
            in = Btest7252.class.getClassLoader().
                    getResourceAsStream("/org/apache/harmony/test/func/reg/vm/btest7252/Btest7252.class");

            // read class file to byte array
            int done = in.read(b);

            while (done < length) {
                done += in.read(b, done, length - done);
            }
            in.close();

            // try to load class 
            ClassLoader7252 clt = new ClassLoader7252();

            try {
                Class cl = clt.defineKlass(null /*name*/, b);
                System.out.println("Test passes: class name: " 
                        + cl.getName());
                return pass();
            } catch (Throwable e) {
                System.out.println("Test fails (in): unexpected error");
                e.printStackTrace(System.out);            
            }
        } catch  (Throwable e) {
            System.out.println("Test fails (out): unexpected error");
            e.printStackTrace(System.out);                        
        }
        return fail();
    }
}

class ClassLoader7252 extends ClassLoader {
    public Class defineKlass(String name, byte[] b) {
        return defineClass(name, b, 0, b.length);
    }
}
