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

import java.beans.XMLDecoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 * Under test: XMLEncoder, XMLDecoder
 * <ul>
 * <li>Object is encoded with help DRL Encoder: result is xml files.
 * <li>XML files are decoded with help DRL Encoder
 * <li>Objects obtained from zero step and from step#2 are compared with help
 * equals(Object) method
 * 
 */
public class EncoderDecoderTest extends MultiCase {

    public static void main(String[] args) throws Exception {
        System.exit(new EncoderDecoderTest().test(args));
    }

    public Result testtest() {
        for (int i = 0; i < GetXML.getNumberOfMethods(); i++) {
            try {
                encodeDecode(i);
            } catch (Exception e) {
                System.out.println("Test " + GetXML.getMethodName(i)
                    + " FAILED: " + e.getMessage());
                e.printStackTrace();
                assertTrue(false);
            }
        }
        return result();
    }

    private static InputStream getXML(int i) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GetXML.encodeObject(out, i);
        // System.out.println(new String(out.toByteArray()));
        return new ByteArrayInputStream(out.toByteArray());
    }

    private void encodeDecode(int i) throws Exception {
        Object object1 = new XMLDecoder(getXML(i)).readObject();
        Object object2 = GetXML.getBeanFromMethod(GetXML.getMethodName(i));
        GetXML.compareObjects(object1, object2);
        System.out.println("Test " + GetXML.getMethodName(i) + " PASSED");
    }
}