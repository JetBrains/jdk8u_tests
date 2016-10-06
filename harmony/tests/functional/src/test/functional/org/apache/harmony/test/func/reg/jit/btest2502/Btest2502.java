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
 
package org.apache.harmony.test.func.reg.jit.btest2502;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.TreeSet;

public class Btest2502 {   

    public static void main(String[] args) throws Exception {
        if("-out".equals(args[0])) {
            System.err.println("Start write temporary file...");    
            ObjectOutputStream oos = 
                    new ObjectOutputStream(new FileOutputStream (args[1]));
            oos.writeObject(new LinkedList());
            oos.writeObject(new TreeSet());
            oos.writeObject(new Hashtable());
            oos.close();
            System.err.println("End write temporary file... OK!");    
        } else if("-in".equals(args[0])) {
            System.err.println("Start read temporary file...");    
            ObjectInputStream ois = 
                    new ObjectInputStream(new FileInputStream(args[1]));
            ois.readObject();
            ois.readObject();
            ois.readObject();
            ois.close();
            System.err.println("End read temporary file... OK!");    
        } else if ("-delete".equals(args[0])) {
            File tmp = new File(args[1]);
            if (tmp.exists()) {
                System.err.println("Delete file " + args[1] + " ...");        
                if (tmp.delete()) {
                    System.err.println(
                            "File " + args[1] + " successfuly deleted");
                } else {
                    System.err.println(
                            "Can not delete file " + args[1]);
                    System.exit(-99);
                }
            }
        }
    }
}
