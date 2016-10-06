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
package org.apache.harmony.test.func.api.java.io.share.ByteArrayOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.harmony.test.func.api.java.io.share.OutputStream.OutputStreamTestShared;
import org.apache.harmony.test.func.api.java.io.share.MultiThreadRunner;
import org.apache.harmony.share.Result;

public class ByteArrayOutputStreamTestShared extends OutputStreamTestShared {
    protected OutputStream getTestedOutputStream(int maxSize) {
        OutputStream os = new ByteArrayOutputStream(maxSize);
        try {
            os.close(); //another test - close() shouldn't affect anything
        } catch (IOException e) {
            System.err.println("error closing stream");
        }

        return os;
    }

    protected String getWrittenAsString(OutputStream os) {
        return ((ByteArrayOutputStream) os).toString();
    }

    public Result testClose() throws IOException {
        return super.testClose();
    }

    public Result testFlush() throws IOException {
        return super.testFlush();
    }

    public Result testWrite0001() throws IOException {
        return super.testWrite0001();
    }

    public Result testWriteByte() throws IOException {
        return super.testWriteByte();
    }

    public Result testWriteIndexOutOfBounds0001() throws IOException {
        return super.testWriteIndexOutOfBounds0001();
    }

    public Result testWriteIndexOutOfBounds0002() throws IOException {
        return super.testWriteIndexOutOfBounds0002();
    }

    public Result testWriteIndexOutOfBounds0003() throws IOException {
        return super.testWriteIndexOutOfBounds0003();
    }

    public Result testWriteNull() throws IOException {
        return super.testWriteNull();
    }

    public Result testWriteSlice() throws IOException {
        return super.testWriteSlice();
    }

    public Result testWriteToSelf() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        MultiThreadRunner.waitAtBarrier();
        os.write('a');
        os.writeTo(os);

        if ("aa".equals(os.toString())) {
            return passed();
        }

        return failed("expected another value");
    }

    public Result testReset() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream(0);

        MultiThreadRunner.waitAtBarrier();
        os.write("abcdef".getBytes());
        os.reset();
        os.write("bcde".getBytes());

        if ("bcde".equals(os.toString()) && os.size() == 4) {
            return passed();
        }

        return failed("expected another value");
    }

    public Result testToStringWithEncoding() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream(0);

        MultiThreadRunner.waitAtBarrier();
        os.write("abcdef".getBytes());

        if (!"abcdef".equals(os.toString("UTF8"))) {
            return failed("expected another value");
        }

        if (!"abcdef".equals(os.toString("ASCII"))) {
            return failed("expected another value");
        }

        return passed();
    }

    public Result testUnsupportedEncoding() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream(0);

        MultiThreadRunner.waitAtBarrier();
        os.write("abcdef".getBytes());

        try {
            os.toString("this is the name \n of a nonexistent encoding"
                    + Math.random());
        } catch (UnsupportedEncodingException e) {
            return passed();
        }

        return failed("expected UnsupportedEncodingException");
    }

    public Result testToByteArray() throws IOException {
        String s = "";
        for (int i = 1; i < 10; ++i) {
            s += Math.random();
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream(0);

        MultiThreadRunner.waitAtBarrier();
        os.write(s.getBytes());

        if (s.equals(new String(os.toByteArray()))) {
            return passed();
        }

        return failed("expected another value");
    }

}
