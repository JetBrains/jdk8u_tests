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
package org.apache.harmony.test.func.api.java.beans.persistence;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 * Under test: XMLEncoder
 * <ul>
 * <li>Object is encoded with help DRL Encoder: result is XML file.
 * <li>XML file is decoded with help reference implementation Encoder to java
 * object
 * <li>Objects obtained from zero step and from step#2 are compared with help
 * equals(Object) method
 * 
 */
public class EncoderTest extends MultiCase {
    static String cmd;

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("args.length=0");
        } else {
            cmd = args[0]+" ";
            System.out.println("cmd: "+cmd);
        }
        System.exit(new EncoderTest().test(args));
    }

    public Result testtest() {
        for (int i = 0; i < GetXML.getNumberOfMethods(); i++) {
            try {
                File file = File
                    .createTempFile(GetXML.getMethodName(i), ".xml");
                // encode with DRL Encoder to XML file
                GetXML.encodeObject(new FileOutputStream(file, false), i);
                // decode XML file with reference implementation
                Object object1 = GetXML.decodeXMLwithReference(cmd, file
                    .getAbsolutePath());
                Object object2 = GetXML.getBeanFromMethod(GetXML
                    .getMethodName(i));
                GetXML.compareObjects(object1, object2);
                file.delete();
                System.out.println("Test " + GetXML.getMethodName(i)
                    + " PASSED.");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Test " + GetXML.getMethodName(i)
                    + " FAILED: " + e.getMessage());
                assertTrue(false);
            }
        }
        return result();
    }
}