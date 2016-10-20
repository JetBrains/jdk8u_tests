/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */    
/** 
 * @author Anton Luht
 * @version $Revision: 1.2 $
 */  
package org.apache.harmony.test.stress.api.java.io.ObjectOutputStream.writeObjectReadObject0044;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.harmony.test.stress.api.java.io.ObjectOutputStream.share.SerializationTestFramework;


public class writeObjectReadObject0044 extends SerializationTestFramework {
    public static void main(String[] args) {
        System.exit(new writeObjectReadObject0044().test(args));
    }

    protected int testIn(ObjectInputStream ois) throws IOException,
            ClassNotFoundException {
        waitAtBarrier();
        Object o = ois.readObject();

        if (!((o instanceof String))) {
            return fail("wrong type restored");
        }

        if (generateBigString().equals(o)) {
            return pass();
        }

        return fail("wrong value restored");
    }

    protected int testOut(ObjectOutputStream oos) throws IOException {
        String s = generateBigString();
        waitAtBarrier();
        oos.writeObject(s);
        return pass();
    }

    private static String generateBigString() {
        InputStream is = writeObjectReadObject0044.class
                .getClassLoader()
                .getResourceAsStream(
                        "org/apache/harmony/test/stress/api/java/io/ObjectOutputStream/share/BigSimple.class");
        StringBuffer sb;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i;

        try {
            while ((i = is.read()) != -1) {
                baos.write(i);
            }
        } catch (IOException e) {
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }

            try {
                baos.close();
            } catch (IOException e) {
            }

        }
        try {
            return baos.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return baos.toString();
        }
    }
}

