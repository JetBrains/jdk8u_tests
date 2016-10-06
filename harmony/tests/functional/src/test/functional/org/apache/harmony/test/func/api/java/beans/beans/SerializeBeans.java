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
package org.apache.harmony.test.func.api.java.beans.beans;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/**
 * This class serializes beans to args[0] directory.
 * <p>
 * After generating files, you have to pack these files with directories to some
 * jar-file and include this jar-file to class path for BeansTest test.
 * 
 */
public class SerializeBeans {
    public final static char fileSeparator = System.getProperty(
                                               "file.separator").charAt(0);

    public static void main(String[] args) {
        try {
            if (args.length == 0) {
                System.out.println("Usage: java "
                    + SerializeBeans.class.getName() + " dir");
                System.out
                    .println("Parameter dir is directory in which serialized beans will be placed.");
                return;
            }
            String dir = args[0] + System.getProperty("file.separator");
            Bean1 o = new Bean1();
            o.i = 5;
            serialize(o, dir);
            serialize(new SimpleApplet2(4), dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Serialize obj to (dir+package) directory.
     * 
     * @param obj
     * @param dir+package
     * @throws Exception
     */
    private static void serialize(Object obj, String dir) throws Exception {
        String fullDir = dir
            + obj.getClass().getPackage().getName().replace('.', fileSeparator);
        if (!new File(fullDir).mkdirs()) {
            System.out
                .println("Directory for *.ser files for JavaBeans tests was not created.");
        }
        String fileName = dir
            + obj.getClass().getName().replace('.', fileSeparator).concat(
                ".ser");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
            new FileOutputStream(fileName, false));
        objectOutputStream.writeObject(obj);
        objectOutputStream.close();
    }
}