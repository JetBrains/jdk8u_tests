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
 
package org.apache.harmony.test.func.reg.jit.btest5942;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;

public class Btest5942 {   
    public static void main (String [] args) {  
        String newName = "";
        String S_APP = "aaaaaaaaaabbbbbbbbbbccccccccccddddddddddeeeeeeeeee";
        StringBuffer sBuf = new StringBuffer(S_APP);
        for (int t = 0; t < 1300; t++) {
            sBuf.append(S_APP);
        }
        newName = sBuf.toString();
 
        byte [] b1;
 
        try {
            System.err.println("Start Btest5942 test...");
            b1 = readToByteArray(args[0], newName);
            if (b1 == null) {
                System.err.println("The test failed: can not read");
                return;
            }

            myClassLoader clLoad = new myClassLoader();
            Class ccc = clLoad.defineKlass(newName, b1, 0, b1.length);
            System.err.println("Class name length: " + ccc.getName().length());
            ccc.newInstance();
            System.err.println("~PASSED!");
        } catch (Throwable e) {
            System.err.println("Test failed: unexpected error" + e);
            e.printStackTrace();
        }
    }

    public static byte [] readToByteArray(String nameOld, String nameNew) {
        try {
            File f = new File(nameOld.concat(".class"));
            byte b [] = new byte[(int)f.length()];  
            FileInputStream stream = new FileInputStream(f);
            stream.read(b);
            stream.close(); 
            
            ByteArrayInputStream is = new ByteArrayInputStream(b);
            DataInputStream dIn = new DataInputStream(is); 
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            DataOutputStream dOut = new DataOutputStream(os);
 
            byte [] b1 = new byte [500];
            int n = dIn.read(b1, 0, 11);
            dOut.write(b1, 0, n);
            
            dIn.readUTF();            
            dOut.writeUTF(nameNew);
            n = dIn.read(b1, 0, b1.length);
            dOut.write(b1, 0, n);
           
            dOut.close(); 
            return os.toByteArray();
        
        } catch (Throwable e) {
            e.printStackTrace(System.out);
            return null;
        }
    }

}

class myClassLoader extends ClassLoader {
    public Class defineKlass(String name, byte [] ar, int st, int len) {
        return super.defineClass(name, ar, st, len);
    }
}
