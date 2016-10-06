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

package org.apache.harmony.test.func.api.java.util.zip.InflaterInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import org.apache.harmony.test.func.api.java.util.jar.share.IOMultiCase;
import org.apache.harmony.share.Result;

public class InflaterInputStreamTest extends IOMultiCase {
    public static void main(String[] args) throws IOException {
        System.exit(new InflaterInputStreamTest().test(args));
    }

    public Result testConstructor() throws IOException {
        try {
            new InflaterInputStream(null, new Inflater(), 1);
            return failed("expected NPE with InflaterInputStream(null, ..., ...)");
        } catch (NullPointerException e) {
        }

        try {
            new InflaterInputStream(new InputStreamImpl(), null, 1);
            return failed("expected NPE with InflaterInputStream(..., null, ...)");
        } catch (NullPointerException e) {
        }

        try {
            new InflaterInputStream(new InputStreamImpl(), new Inflater(), -1);
            return failed("expected IllegalArgumentException with InflaterInputStream(..., ..., -1)");
        } catch (IllegalArgumentException e) {
        }

        try {
            new InflaterInputStream(new InputStreamImpl(), new Inflater(), 0);
            return failed("expected IllegalArgumentException with InflaterInputStream(..., ..., 0)");
        } catch (IllegalArgumentException e) {
        }

        InputStreamImpl isi = new InputStreamImpl();
        InputStream is = new InflaterInputStream(isi, new Inflater(), 19);
        is.read();
        
        if(isi.readCount != 57) {
            System.err.println("expected readCount to be 57, got " + isi.readCount);
        }

        return passed();
    }
}

class InputStreamImpl extends InputStream {
    public int readCount = 0;
    private byte[] output = new byte[2000];
    
    InputStreamImpl() {
        String inputString = "The Functional test suite is a "
                + "collection of "
                + "micro scenarios for "
                + "testing various functional parts of "
                + "an implementation.";
        byte[] input = new byte[] {};
        try {
            input = inputString.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
        Deflater compresser = new Deflater();
        compresser.setInput(input);
        compresser.finish();
        compresser.deflate(output);
    }

    public int read() throws IOException {
        return output[readCount++];
    }
}