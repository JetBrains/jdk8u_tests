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

/*
 * Created on 19.11.2004
 *
 */
package org.apache.harmony.test.func.api.java.io.share.ByteArrayInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.harmony.test.func.api.java.io.share.InputStream.InputStreamTestShared;
import org.apache.harmony.test.func.api.java.io.share.MultiThreadRunner;
import org.apache.harmony.share.Result;

public class ByteArrayInputStreamTestShared extends InputStreamTestShared {
    private static final byte[] BYTE_ARRAY = { 'a', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'i', 'j', 'k' };

    public InputStream getTestedInputStream() {
        InputStream is = new ByteArrayInputStream(BYTE_ARRAY);
        try {
            is.close(); //to test that all actions can be performed with closed
                        // input stream, too
        } catch (IOException e) {
            System.err.println("error closing stream");
        }
        return new ByteArrayInputStream(BYTE_ARRAY);
    }

    public Result testAvailable() throws IOException {
        InputStream is = getTestedInputStream();

        MultiThreadRunner.waitAtBarrier();
        
        if (is.available() != BYTE_ARRAY.length) {
            return failed("expected available() initially to be equal to the length of the underlying array");
        }

        int readBytes = (int) Math.random() * BYTE_ARRAY.length;

        for (int i = 0; i < readBytes; ++i) {
            is.read();
        }

        if (getTestedInputStream().available() + readBytes != BYTE_ARRAY.length) {
            return failed("expected available() to be array length - read count");
        }

        return passed();
    }

    public Result testConstructorNoCopy() throws IOException {
        byte[] barr = (byte[]) BYTE_ARRAY.clone();
        InputStream is = new ByteArrayInputStream(barr);

        MultiThreadRunner.waitAtBarrier();
        is.mark(0);

        byte[] b = new byte[3];

        if (!(is.read(b) == 3 && b[0] == 'a' && b[1] == 'b' && b[2] == 'c')) {
            return failed("wrong values read");
        }

        barr[0] = 'x';
        barr[1] = 'y';
        barr[2] = 'z';

        is.reset();

        if (!(is.read(b) == 3 && b[0] == 'x' && b[1] == 'y' && b[2] == 'z')) {
            return failed("wrong values read");
        }

        return passed();
    }

    public Result testOffsetConstructor0001() throws IOException {
        InputStream is = new ByteArrayInputStream(BYTE_ARRAY, 3, 5);
        MultiThreadRunner.waitAtBarrier();
        if (is.read() != 'd') {
            return failed("wrong value read");
        }
        is.reset();
        if (is.read() != 'd') {
            return failed("wrong value read after reset");
        }

        is.mark(1);
        is.read();

        if (is.read() != 'f') {
            return failed("wrong value read after mark");
        }

        is.reset();

        if (is.read() != 'e') {
            return failed("wrong value read after mark/reset");
        }

        return passed();
    }

    public Result testOffsetConstructor0002() throws IOException {
        InputStream is = new ByteArrayInputStream(BYTE_ARRAY, 3, 5);

        int val;
        MultiThreadRunner.waitAtBarrier();
        if ((val = is.read(new byte[1000])) != 5) {
            return failed("wrong value read: " + val);
        }

        is.reset();
        if (is.read(new byte[1000]) != 5) {
            return failed("wrong value read after reset");
        }

        return passed();
    }

    public Result testSelfRead0001() throws IOException {
        byte[] b = { 'a', 'b', 'c', 'd' };

        InputStream is = new ByteArrayInputStream(b);

        MultiThreadRunner.waitAtBarrier();
        is.read(b, 1, b.length - 1);
        if (b[0] == 'a' && b[1] == 'a' && b[2] == 'b' && b[3] == 'c') {
            return passed();
        }

        return failed("wrong values read");
    }

    public Result testSelfRead0002() throws IOException {
        byte[] b = { 'a', 'b', 'c', 'd' };

        InputStream is = new ByteArrayInputStream(b, 1, b.length - 1);
        
        MultiThreadRunner.waitAtBarrier();
        is.read(b);

        if (b[0] == 'b' && b[1] == 'c' && b[2] == 'd' && b[3] == 'd') {
            return passed();
        }

        return failed("wrong values read");
    }

    public Result testSkip0001() throws IOException {
        InputStream is = getTestedInputStream();
        MultiThreadRunner.waitAtBarrier();
        if (is.skip(BYTE_ARRAY.length * 100) == BYTE_ARRAY.length) {
            return passed();
        }
        return failed("skip() returned wrong value");
    }

    public Result testSkip0002() throws IOException {
        InputStream is = getTestedInputStream();
        MultiThreadRunner.waitAtBarrier();
        if (is.skip(BYTE_ARRAY.length / 2) == (BYTE_ARRAY.length / 2)) {
            return passed();
        }
        return failed("skip() returned wrong value");
    }

    public Result testClose() throws IOException {
        return super.testClose();
    }

    public Result testMark() throws IOException {
        return super.testMark();
    }

    public Result testMarkSupported() {
        MultiThreadRunner.waitAtBarrier();
        if (getTestedInputStream().markSupported() == true) {
            return passed();
        }

        return failed("ByteArrayInputStream markSupported must return true");
    }

    public Result testReadByte() throws IOException {
        return super.testReadByte();
    }

    public Result testReadBytes() throws IOException {
        return super.testReadBytes();
    }

    public Result testReadBytes0001() throws IOException {
        return super.testReadBytes0001();
    }

    public Result testReadBytes0002() throws IOException {
        return super.testReadBytes0002();
    }

    public Result testReadBytes0003() throws IOException {
        return super.testReadBytes0003();
    }

    public Result testReadBytesNull() throws IOException {
        return super.testReadBytesNull();
    }

    public Result testReadBytesSlice() throws IOException {
        return super.testReadBytesSlice();
    }

    public Result testReset() throws IOException {
        return super.testReset();
    }

    public Result testReset0001() throws IOException {
        return super.testReset0001();
    }

    public Result testReset0002() throws IOException {
        return super.testReset0002();
    }

    public Result testSkip() throws IOException {
        return super.testSkip();
    }
}