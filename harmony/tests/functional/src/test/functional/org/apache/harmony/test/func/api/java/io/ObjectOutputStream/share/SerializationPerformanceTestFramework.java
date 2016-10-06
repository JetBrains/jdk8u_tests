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
package org.apache.harmony.test.func.api.java.io.ObjectOutputStream.share;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.apache.harmony.test.func.api.java.io.share.PerformanceTestFramework;

public abstract class SerializationPerformanceTestFramework extends
        PerformanceTestFramework {
    private static boolean TEST_OUT = false; //if we test speed of

    // ObjectOutputStream
    // (serialization)
    //or ObjectInputStream (deserialization)
    //private static int MEMORY_CONSUMPTION_IN_MEGABYTES = 50; //how much
    // memory can we eat for the test
    private InputStream dataStream = null;
    

    protected void parseArgs(String[] params) {
        super.parseArgs(params);
        if (params == null || params.length == 0) {
            return;
        }
        for (int i = 0; i < params.length; ++i) {
            if ("-inout".equalsIgnoreCase(params[i])) {
                TEST_OUT = "out".equals(params[++i]);
            }
        }
    }

    protected void beforeTestOut(int tests) {
    }

    protected void afterTestOut(int tests) {
    }

    protected void afterTestIn(int tests) {
    }

    protected final void beforeTest(int tests) {
        if (TEST_OUT) {
            beforeTestOut(tests);
            return;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        beforeTestOut(tests);
        try {
            testOut(new ObjectOutputStream(baos), tests);
        } catch (IOException e) {
            e.printStackTrace();
        }
        dataStream = new ByteArrayInputStream(baos.toByteArray());
    }

    protected abstract void testIn(ObjectInputStream ois, int tests)
            throws IOException, ClassNotFoundException;

    protected abstract void testOut(ObjectOutputStream oos, int tests)
            throws IOException;

    protected final void afterTest(int tests) {
        if (TEST_OUT) {
            afterTestOut(tests);
        } else {
            afterTestIn(tests);
        }
    }

    protected final int test(int tests) {
        try {
            if (TEST_OUT) {
                testOut(new ObjectOutputStream(new DevNullStream()), tests);
            } else {
                dataStream.reset();
                testIn(new ObjectInputStream(dataStream), tests);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return fail(e.getMessage());
        }

        return pass();
    }
}

class DevNullStream extends OutputStream {
    public void write(int arg0) throws IOException {
    }

    public void write(byte[] arg0, int arg1, int arg2) throws IOException {
    }

    public void write(byte[] arg0) throws IOException {
    }

    public void close() throws IOException {
    }

    public void flush() throws IOException {
    }
}