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
 * Created on 11.11.2004
 *  
 */
package org.apache.harmony.test.func.api.java.io.share.BufferedOutputStream;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.harmony.test.func.api.java.io.share.FilterOutputStream.FilterOutputStreamTestShared;
import org.apache.harmony.test.func.api.java.io.share.MockOutputStream;
import org.apache.harmony.test.func.api.java.io.share.MultiThreadRunner;
import org.apache.harmony.share.Result;

public class BufferedOutputStreamTestShared extends FilterOutputStreamTestShared {
    protected OutputStream getOutputStream(OutputStream os) {
        return new BufferedOutputStream(os);
    }

    public Result testClose() throws IOException {
        return super.testClose();
    }

    public Result testFlush() throws IOException {
        return super.testFlush();
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

    public Result testWrite0002() throws IOException {
        MockOutputStream mos = new MockOutputStream(5);
        BufferedOutputStream bos = new BufferedOutputStream(mos);
        
        MultiThreadRunner.waitAtBarrier();
        bos.write("abc".getBytes());

        if (mos.getCount() != 0) {
            return failed("no buffering");
        }
        return passed();
    }

    public Result testWrite0003() throws IOException {
        MockOutputStream mos = new MockOutputStream(5);
        BufferedOutputStream bos = new BufferedOutputStream(mos);

        MultiThreadRunner.waitAtBarrier();
        bos.write('x');

        if (mos.getCount() != 0) {
            return failed("no buffering");
        }
        return passed();
    }

    public Result testWrite0004() throws IOException {
        MockOutputStream mos = new MockOutputStream(5);
        BufferedOutputStream bos = new BufferedOutputStream(mos, 2);
        
        MultiThreadRunner.waitAtBarrier();
        bos.write('x');
        if (mos.getCount() != 0) {
            return failed("no buffering");
        }
        bos.write('y');
        if (mos.getCount() != 0) {
            return failed("no buffering");
        }
        bos.write('z');
        if (mos.getCount() == 0) {
            return failed("extra buffering - requested buffer size == 2");
        }
        if (!"xy".equals(mos.getWrittenAsString())) {
            return failed("expected 2 bytes in the underlying stream");
        }
        return passed();
    }

    public Result testWrite0005() throws IOException {
        MockOutputStream mos = new MockOutputStream(5);
        BufferedOutputStream bos = new BufferedOutputStream(mos, 2);

        MultiThreadRunner.waitAtBarrier();
        bos.write("abc".getBytes());

        if (!"abc".equals(mos.getWrittenAsString())) {
            return failed("expected whole bytes array in the underlying stream");
        }
        return passed();
    }

    public Result testWrite0006() throws IOException {
        MockOutputStream mos = new MockOutputStream(5);
        BufferedOutputStream bos = new BufferedOutputStream(mos, 2);

        MultiThreadRunner.waitAtBarrier();
        bos.write('x');
        bos.write("abc".getBytes());

        if (!"xabc".equals(mos.getWrittenAsString())) {
            return failed("expected whole bytes array and data written previously in the underlying stream");
        }
        return passed();
    }

    public Result testWrite0007() throws IOException {
        MockOutputStream mos = new MockOutputStream(5);
        BufferedOutputStream bos = new BufferedOutputStream(mos, 3);

        MultiThreadRunner.waitAtBarrier();
        bos.write("ab".getBytes());
        bos.write("xy".getBytes());

        if (!"ab".equals(mos.getWrittenAsString())) {
            return failed("expected only first data portion in the underlying stream");
        }
        return passed();
    }

    public Result testNegativeSize() {
        MockOutputStream mos = new MockOutputStream(5);
        try {
            BufferedOutputStream bos = new BufferedOutputStream(mos, -5);
        } catch (IllegalArgumentException e) {
            return passed();
        }

        return failed("expected IllegalArgumentException");
    }

    public Result testZeroSize() {
        MockOutputStream mos = new MockOutputStream(5);
        try {
            BufferedOutputStream bos = new BufferedOutputStream(mos, 0);
        } catch (IllegalArgumentException e) {
            return passed();
        }

        return failed("expected IllegalArgumentException");
    }

}