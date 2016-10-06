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
package org.apache.harmony.test.func.api.java.io.share.PushBackInputStream;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

import org.apache.harmony.test.func.api.java.io.share.FilterInputStream.FilterInputStreamTestShared;
import org.apache.harmony.test.func.api.java.io.share.MultiThreadRunner;
import org.apache.harmony.share.Result;

public class PushBackInputStreamTestShared extends FilterInputStreamTestShared {
    public InputStream getTestedInputStream() throws IOException {
        return new PushbackInputStream(super.getTestedInputStream());
    }

    public InputStream getTestedInputStream(InputStream is) {
        return new PushbackInputStream(is);
    }

    public Result testAvailable() throws IOException {
        return super.testAvailable();
    }

    public Result testClose() throws IOException {
        return super.testClose();
    }

    public Result testMark() throws IOException {
        return super.testMark();
    }

    public Result testMarkSupported() throws IOException {
        return super.testMarkSupported();
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

    public Result testSkip() throws IOException {
        return super.testSkip();
    }

    public Result testUnreadBeforeRead() throws IOException {
        MultiThreadRunner.waitAtBarrier();
        

        PushbackInputStream strm = new PushbackInputStream(
                new ByteArrayInputStream(new byte[] { 4 }));
        strm.unread(5);
        if (strm.read() != 5) {
            return failed("expected another value");
        }
        return passed();
    }

    public Result testUnreadBeforeRead0001() throws IOException {
        MultiThreadRunner.waitAtBarrier();

        PushbackInputStream strm = new PushbackInputStream(
                new ByteArrayInputStream(new byte[] { 4 }), 3);
        strm.unread('a');
        strm.unread('b');
        strm.unread('c');
        if (strm.read() != 'c' || strm.read() != 'b' || strm.read() != 'a') {
            return failed("expected another value");
        }
        return passed();
    }

    public Result testFields() throws IOException {
        MultiThreadRunner.waitAtBarrier();

        PushbackInputStreamExposedFields strm = new PushbackInputStreamExposedFields(
                new ByteArrayInputStream(new byte[] { 4 }), 1);
        if (strm.getBuf()[0] != 0) {
            return failed("buf is not zeroed");
        }
        if (strm.getPos() != 1) {
            return failed("pos is not 1 : " + strm.getPos());
        }
        if (strm.read() != 4) {
            return failed("expected to read '4'");
        }
        if (strm.getBuf()[0] != 0) {
            return failed("buf is not zero after read");
        }
        if (strm.getPos() != 1) {
            return failed("pos is not 1 after read : " + strm.getPos());
        }
        strm.unread(5);
        if (strm.getBuf()[0] != 5) {
            return failed("wrong value in buf after unread");
        }
        if (strm.getPos() != 0) {
            return failed("pos is not 0 after unread: " + strm.getPos());
        }
        return passed();
    }

    public Result testAvailable0001() throws IOException {
        PushbackInputStream strm = new PushbackInputStream(
                new ByteArrayInputStream(new byte[] { 4 }), 1234);

        MultiThreadRunner.waitAtBarrier();

        for (int i = 0; i < 1234; ++i) {
            if (strm.available() != i + 1) {
                return failed("expected available to be " + (i + 1));
            }
            strm.unread('x');
        }
        return passed();
    }

    public Result testSkip0001() throws IOException {
        PushbackInputStream strm = new PushbackInputStream(new InputStream() {
            public int read() throws IOException {
                return 0;
            }

            public long skip(long l) throws IOException {
                throw new EOFException("uups");
            }
        });
        
        MultiThreadRunner.waitAtBarrier();

        strm.unread('a');
        if (strm.skip(1) != 1) {
            return failed("expected skip(1) to return 1");
        }
        try {
            strm.skip(1);
            return failed("expected EOFException - 1");
        } catch (EOFException e) {
        }
        strm.unread('x');
        if (strm.skip(1) != 1) {
            return failed("expected skip(1) to return 1");
        }
        strm.unread('y');
        try {
            strm.skip(100);
            return failed("expected EOFException - 2");
        } catch (EOFException e) {
        }
        return passed();
    }

    public Result testUnread() throws IOException {
        PushbackInputStream strm = new PushbackInputStream(
                new ByteArrayInputStream(new byte[] { 0 }), 2);

        MultiThreadRunner.waitAtBarrier();
        
        strm.unread('a');
        strm.unread('b');
        try {
            strm.unread('c');
            return failed("expected IOException");
        } catch (IOException e) {
        }
        return passed();
    }

    public Result testUnread0001() throws IOException {
        PushbackInputStream strm = new PushbackInputStream(
                new ByteArrayInputStream(new byte[] { 0 }), 2);

        MultiThreadRunner.waitAtBarrier();

        strm.unread(new byte[] { 'a', 'b' });
        if (strm.read() == 'a' && strm.read() == 'b' && strm.read() == 0) {
            return passed();
        }
        return failed("expected another value");
    }

    public Result testUnread0002() throws IOException {
        PushbackInputStream strm = new PushbackInputStream(
                new ByteArrayInputStream(new byte[] { 0 }), 2);

        MultiThreadRunner.waitAtBarrier();
        
        strm.unread(new byte[] { 'a', 'b', 'c', 'd' }, 1, 2);
        if (strm.read() == 'b' && strm.read() == 'c' && strm.read() == 0) {
            return passed();
        }
        return failed("expected another value");
    }

    public Result testUnreadBytesNull() throws IOException { //TCK - test that
        PushbackInputStream strm = new PushbackInputStream(
                new ByteArrayInputStream(new byte[] { 0 }), 2);
        try {

            strm.unread(null);
        } catch (NullPointerException e) {
            return passed();
        } finally {
            strm.close();
        }

        return failed("expected NullPointerException");
    }

    public Result testUnreadBytes0001() throws IOException { //TCK - test that
        PushbackInputStream strm = new PushbackInputStream(
                new ByteArrayInputStream(new byte[] { 0 }), 2);

        try {
            strm.unread(new byte[1], -1, 1);
        } catch (IndexOutOfBoundsException e) {
            return passed();
        } finally {
            strm.close();
        }

        return failed("expected IndexOutOfBoundsException");
    }

    public Result testUnreadBytes0002() throws IOException { //TCK -
        // test
        // that
        PushbackInputStream strm = new PushbackInputStream(
                new ByteArrayInputStream(new byte[] { 0 }), 2);
        try {
            strm.unread(new byte[1], 0, -1);
        } catch (IndexOutOfBoundsException e) {
            return passed();
        } finally {
            strm.close();
        }

        return failed("expected IndexOutOfBoundsException");
    }

    public Result testUnreadBytes0003() throws IOException { //TCK - test
        PushbackInputStream strm = new PushbackInputStream(
                new ByteArrayInputStream(new byte[] { 0 }), 2);
        try {
            strm.unread(new byte[1], 0, 5);
        } catch (IOException e) {
            return passed();
        } finally {
            strm.close();
        }

        return failed("expected IOException");
    }

    public Result testUnreadBytes0004() throws IOException { //TCK - test
        PushbackInputStream strm = new PushbackInputStream(
                new ByteArrayInputStream(new byte[] { 0 }), 20);
        try {
            strm.unread(new byte[1], 0, 5);
        } catch (IndexOutOfBoundsException e) {
            return passed();
        } finally {
            strm.close();
        }

        return failed("expected IndexOutOfBoundsException");
    }

    public Result testConstructorException() throws IOException {
        try {
            PushbackInputStream strm = new PushbackInputStream(
                    new ByteArrayInputStream(new byte[] { 0 }), -1);
            return failed("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        try {
            PushbackInputStream strm = new PushbackInputStream(
                    new ByteArrayInputStream(new byte[] { 0 }), 0);
            return failed("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        return passed();
    }
}

class PushbackInputStreamExposedFields extends PushbackInputStream {
    public PushbackInputStreamExposedFields(InputStream arg0) {
        super(arg0);
    }

    public PushbackInputStreamExposedFields(InputStream stream, int i) {
        super(stream, i);
    }

    public byte[] getBuf() {
        return buf;
    }

    public int getPos() {
        return pos;
    }

    public InputStream getIn() {
        return in;
    }
}

