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

package org.apache.harmony.test.func.reg.jit.btest2788;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;

public class Btest2788 {

    int NUMBER = 1000;
    String filename = "test.tmp";
    
    public static void main(String[] args) {
        Btest2788 t = new Btest2788();
        if (args.length > 0) {
            t.filename = args[0];
            t.testRegression01();
            new File(t.filename).delete();      
        }
    }

    public void testRegression01() {
        try {
            System.err.println("Start file writing...");
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            
            Hashtable hashes[] = new Hashtable[NUMBER];
            for (int i = 0; i < NUMBER; i++) {
                hashes[i] = new Hashtable();
                hashes[i].put(new Integer(i), "aaaaaaaaa");
                hashes[i].put(new Integer(i + NUMBER + 1), "bbbbbbbb");
                hashes[i].put(new Integer(i + 1), "cccccccc");
            }

            for (int i = 0; i < NUMBER; i++) {
                oos.writeInt(i);
                oos.writeObject(hashes[i]);
            }

            oos.close();
            fos.close();            
            System.err.println("End file writing...");

            System.err.println("Start file reading...");            
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);

            for (int i = 0; i < NUMBER; i++) {
                ois.readInt();
                Hashtable h = (Hashtable) ois.readObject();
            }
            System.err.println("End file reading...");            

        } catch (Exception e) {
            System.err.println("Unexpected exception was thrown:");
            e.printStackTrace();
            System.exit(-99);
        }
        
        System.err.println(
                "File writing and reading were completed correctly!");
    }

}

