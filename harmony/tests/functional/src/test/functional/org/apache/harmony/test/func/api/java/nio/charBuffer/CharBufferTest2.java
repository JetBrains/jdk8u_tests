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
 * Created on 05.09.2005
 */
package org.apache.harmony.test.func.api.java.nio.charBuffer;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.CharBuffer;
import java.nio.ReadOnlyBufferException;
import java.util.Arrays;
import java.util.Random;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;
import org.apache.harmony.test.func.api.java.nio.byteBuffer.ByteBufferTest2;

public class CharBufferTest2 extends MultiCase {

    private CharBuffer testCharBuffer;

    private char[] arrayOfTestCharBuffer;

    static Random r = new Random();

    public static int getBufferSize() {
        return ByteBufferTest2.getBufferSize();
    }

    public static char[] getArrayOfRandomChar(int length) {
        return new String(ByteBufferTest2.getArrayOfRandomByte(length))
                .toCharArray();
    }

    public static char[] getArrayOfRandomChar() {
        return getArrayOfRandomChar(getBufferSize());
    }

    public Result testCreateCharBuffer() {
        try {
            arrayOfTestCharBuffer = getArrayOfRandomChar();
            testCharBuffer = CharBuffer.wrap(arrayOfTestCharBuffer);
        } catch (Throwable e) {
            return failed("throw "
                    + e
                    + "when call CharBuffer.wrap(char[]) and ByteBuffer.asCharBuffer()");
        }
        return passed();

    }

    public static void main(String[] args) {
        System.exit(new CharBufferTest2().test(args));
    }

    public Result testWrap() {
        testCreateCharBuffer();
        if (testCharBuffer.capacity() != arrayOfTestCharBuffer.length)
            return failed("wrap(char[]) create CharBuffer with wrong capacity");
        if (testCharBuffer.limit() != arrayOfTestCharBuffer.length)
            return failed("wrap(char[]) create CharBuffer with wrong limit");
        if (testCharBuffer.position() != 0)
            return failed("wrap(char[]) create CharBuffer with wrong position");
        if (testCharBuffer.arrayOffset() != 0)
            return failed("wrap(char[]) create CharBuffer with wrong arrayOffset");
        if (!testCharBuffer.hasArray())
            return failed("wrap(char[]) create CharBuffer with wrong backing array");
        else if (!java.util.Arrays.equals(arrayOfTestCharBuffer, testCharBuffer
                .array()))
            return failed("wrap(char[]) create CharBuffer with wrong backing array");
        return passed();
    }

    public Result testWrap2() {
        char[] b = getArrayOfRandomChar();
        int offset = r.nextInt(b.length / 2);
        int length = r.nextInt(b.length - offset - 1);
        try {
            testCharBuffer = CharBuffer.wrap(b, offset, length);
        } catch (Throwable e) {
            return failed("cannot create CharBuffer with wrap(char[], int, int)");
        }
        if (testCharBuffer.capacity() != b.length)
            return failed("wrap(char[], int, int) create CharBuffer with wrong capacity");
        if (testCharBuffer.limit() != offset + length)
            return failed("wrap(char[], int, int) create CharBuffer with wrong limit");
        if (testCharBuffer.position() != offset)
            return failed("wrap(char[], int, int) create CharBuffer with wrong position");
        if (testCharBuffer.arrayOffset() != 0)
            return failed("wrap(char[], int, int) create CharBuffer with wrong arrayOffset");
        if (!testCharBuffer.hasArray())
            return failed("wrap(char[]) create CharBuffer with wrong backing array");
        else if (!Arrays.equals(b, testCharBuffer.array()))
            return failed("wrap(char[], int, int) create CharBuffer with wrong backing array");
        try {
            testCharBuffer = CharBuffer.wrap(b, 1, b.length);
            return failed("must throw IndexOutOfBoundsException, because offset + length > length of array");
        } catch (IndexOutOfBoundsException e) {
        } catch (Throwable e) {
            return failed("must throw IndexOutOfBoundsException, because offset + length > length of array, but throw "
                    + e);
        }
        try {
            testCharBuffer = CharBuffer.wrap(b, 0, b.length);
            testCharBuffer = CharBuffer.wrap(b, b.length, 0);
        } catch (Throwable e) {
            return failed("throw " + e
                    + ", when offset + length = length of array");
        }
        return passed();
    }

    public Result testGet() {
        testCreateCharBuffer();
        char[] b = new char[testCharBuffer.capacity() + 1];
        try {
            testCharBuffer.get(b);
            return failed("must throw BufferUnderflowException, because CharBuffer.position + b.length > CharBuffer.capacity of CharBuffer");
        } catch (BufferUnderflowException e) {
        } catch (Throwable e) {
            return failed("must throw BufferUnderflowException, because CharBuffer.position + b.length > CharBuffer.capacity of CharBuffer, but throw "
                    + e);
        }
        try {
            testCharBuffer.position(r.nextInt(testCharBuffer.capacity() / 3));
            b = new char[testCharBuffer.limit() - testCharBuffer.position() + 1];
            testCharBuffer.get(b);
            return failed("must throw BufferUnderflowException, because CharBuffer.position + b.length > CharBuffer.capacity of CharBuffer");
        } catch (BufferUnderflowException e) {
        } catch (Throwable e) {
            return failed("must throw BufferUnderflowException, because CharBuffer.position + b.length > CharBuffer.capacity of CharBuffer, but throw "
                    + e);
        }
        testCharBuffer.position(0);
        b = new char[0];
        try {
            testCharBuffer.get(b);
            if (b.length > 0)
                return failed("lentgh of array after CharBuffer.get(b) > 0, but must = 0, because before b.length = 0");
            if (testCharBuffer.position() > 0)
                return failed("position in CharBuffer after CharBuffer.get(b) > 0, but must = 0, because before b.length = 0");
        } catch (Throwable e) {
            return failed("throw " + e + ", when b.length = 0");
        }
        b = new char[testCharBuffer.capacity()];
        try {
            testCharBuffer.get(b);
            if (!Arrays.equals(arrayOfTestCharBuffer, b))
                return failed("CharBuffer.get(b) return wrong array");
            testCharBuffer.position(0);
        } catch (Throwable e) {
            return failed("throw " + e
                    + ", when b.length = capacity of CharBuffer");
        }
        int pos = r.nextInt(testCharBuffer.capacity() / 3);
        int len = r.nextInt(testCharBuffer.capacity() / 2);
        b = new char[len];
        try {
            testCharBuffer.position(pos);
            testCharBuffer.get(b);
            if (b.length != len)
                return failed("lentgh of array b have changed after CharBuffer.get(b)");
            if (testCharBuffer.position() != pos + len)
                return failed("position in CharBuffer after CharBuffer.get(b) have changed not on value of b.length");
            char[] bb = new char[len];
            System.arraycopy(arrayOfTestCharBuffer, pos, bb, 0, bb.length);
            if (!Arrays.equals(b, bb))
                return failed("CharBuffer.get(b) return wrong array");
            testCharBuffer.position(0);
        } catch (Throwable e) {
            return failed("throw " + e
                    + ", when b.length = capacity of CharBuffer");
        }
        return passed();
    }

    public Result testGet2() {
        testCreateCharBuffer();
        char[] b = new char[r.nextInt(testCharBuffer.limit() / 2)];
        int off = r.nextInt(b.length);
        int len = b.length - off + 1;
        try {
            testCharBuffer.get(b, off, len);
            return failed("must throw IndexOutOfBoundsException, because (offset +  length > b.length)");
        } catch (IndexOutOfBoundsException e) {
        } catch (Throwable e) {
            return failed("must throw IndexOutOfBoundsException, because (offset +  length > b.length), but throw "
                    + e);
        }
        try {
            testCharBuffer.position(r.nextInt(testCharBuffer.limit() / 3));
            b = new char[testCharBuffer.limit() + off + 1];
            testCharBuffer.get(b, off, testCharBuffer.limit() + 1);
            return failed("must throw BufferUnderflowException, because (length > remaining)");
        } catch (BufferUnderflowException e) {
        } catch (Throwable e) {
            return failed("must throw BufferUnderflowException, because (length > remaining), but throw "
                    + e);
        }
        testCharBuffer.position(0);
        b = new char[testCharBuffer.limit()];
        try {
            testCharBuffer.get(b, 0, b.length);
            if (!Arrays.equals(arrayOfTestCharBuffer, b))
                return failed("CharBuffer.get(b) return wrong array");
            testCharBuffer.position(0);
        } catch (Throwable e) {
            return failed("throw " + e
                    + ", when b.length = capacity of CharBuffer");
        }
        int pos = r.nextInt(testCharBuffer.limit() / 3);
        b = new char[testCharBuffer.limit()];
        len = r.nextInt(b.length / 2);
        off = r.nextInt(b.length / 3);
        try {
            testCharBuffer.position(pos);
            testCharBuffer.get(b, off, len);
            if (testCharBuffer.position() != pos + len)
                return failed("position in CharBuffer after CharBuffer.get(b) have changed not on value of b.length");
            char[] bb = new char[b.length];
            System.arraycopy(arrayOfTestCharBuffer, pos, bb, off, len);
            if (!Arrays.equals(b, bb))
                return failed("CharBuffer.get(b) return wrong array");
            testCharBuffer.position(0);
        } catch (Throwable e) {
            return failed("throw " + e
                    + ", when b.length = capacity of CharBuffer");
        }
        return passed();
    }

    public Result testArrayOffset() {
        testCharBuffer = CharBuffer.allocate(getBufferSize());
        if (testCharBuffer.arrayOffset() != 0)
            return failed("CharBuffer.allocate(int) create CharBuffer with wrong arrayOffset");
        testCreateCharBuffer();
        if (testCharBuffer.arrayOffset() != 0)
            return failed("CharBuffer.wrap(char[]) create CharBuffer with wrong arrayOffset");
        int pos = r.nextInt(testCharBuffer.capacity());
        testCharBuffer.position(pos);
        if (testCharBuffer.arrayOffset() != 0)
            return failed("CharBuffer.wrap(char[]) create CharBuffer with wrong arrayOffset");
        try {
            testCharBuffer.asReadOnlyBuffer().arrayOffset();
            return failed("must throw ReadOnlyBufferException");
        } catch (ReadOnlyBufferException e) {
        } catch (Throwable e) {
            return failed("must throw ReadOnlyBufferException, but throw " + e);
        }
        return passed();
    }

    public Result testHasArray() {
        testCharBuffer = CharBuffer.allocate(getBufferSize());
        if (!testCharBuffer.hasArray())
            return failed("CharBuffer.allocate(int) create CharBuffer without back array");
        testCreateCharBuffer();
        if (!testCharBuffer.hasArray())
            return failed("CharBuffer.wrap(char[]) create CharBuffer without back array");
        return passed();
    }

    public Result testLength() {
        int size = getBufferSize();
        testCharBuffer = CharBuffer.allocate(size);
        if (testCharBuffer.length() != size)
            return failed("CharBuffer.allocate(int) create CharBuffer with wrong length() or length() return incorrect value");
        testCreateCharBuffer();
        if (testCharBuffer.length() != arrayOfTestCharBuffer.length)
            return failed("CharBuffer.wrap(char[]) create CharBuffer with wrong length() or length() return incorrect value");
        int pos = r.nextInt(testCharBuffer.capacity());
        testCharBuffer.position(pos);
        if (testCharBuffer.length() != arrayOfTestCharBuffer.length - pos)
            return failed("length() return incorrect value after calling position(int)");
        testCharBuffer.position(arrayOfTestCharBuffer.length);
        if (testCharBuffer.length() != 0)
            return failed("length() return incorrect value after calling position(int)");
        pos = r.nextInt(testCharBuffer.capacity() / 2);
        testCharBuffer.position(pos);
        int lim = r.nextInt(testCharBuffer.capacity()
                - testCharBuffer.position())
                + testCharBuffer.position();
        testCharBuffer.limit(lim);
        if (testCharBuffer.length() != lim - pos)
            return failed("length() return incorrect value after calling position(int)");
        return passed();
    }

    public Result testPut() {
        testCreateCharBuffer();
        char[] b = getArrayOfRandomChar(r.nextInt(testCharBuffer.limit() / 2));
        int off = r.nextInt(b.length);
        int len = b.length - off + 1;
        try {
            testCharBuffer.put(b, off, len);
            return failed("must throw IndexOutOfBoundsException, because (offset +  length > b.length)");
        } catch (IndexOutOfBoundsException e) {
        } catch (Throwable e) {
            return failed("must throw IndexOutOfBoundsException, because (offset +  length > b.length), but throw "
                    + e);
        }
        try {
            testCharBuffer.position(r.nextInt(testCharBuffer.limit() / 3));
            b = getArrayOfRandomChar(testCharBuffer.limit() + off + 1);
            testCharBuffer.put(b, off, testCharBuffer.limit() + 1);
            return failed("must throw BufferOverflowException, because (length > remaining)");
        } catch (BufferOverflowException e) {
        } catch (Throwable e) {
            return failed("must throw BufferOverflowException, because (length > remaining), but throw "
                    + e);
        }
        testCharBuffer.position(0);
        b = getArrayOfRandomChar(testCharBuffer.limit());
        try {
            testCharBuffer.put(b, 0, b.length);
            if (!Arrays.equals(testCharBuffer.array(), b))
                return failed("CharBuffer.put(b) return wrong array");
            testCharBuffer.position(0);
        } catch (Throwable e) {
            return failed("throw " + e
                    + ", when b.length = capacity of CharBuffer");
        }
        testCharBuffer.position(0);
        try {
            testCharBuffer.asReadOnlyBuffer().put(b, 0, b.length);
            return failed("must throw ReadOnlyBufferException");
        } catch (ReadOnlyBufferException e) {
        } catch (Throwable e) {
            return failed("must throw ReadOnlyBufferException, but throw " + e);
        }
        int pos = r.nextInt(testCharBuffer.limit() / 3);
        b = getArrayOfRandomChar(testCharBuffer.limit());
        len = r.nextInt(b.length / 2);
        off = r.nextInt(b.length / 3);
        try {
            testCharBuffer.position(pos);
            testCharBuffer.put(b, off, len);
            if (testCharBuffer.position() != pos + len)
                return failed("position in CharBuffer after CharBuffer.put(b) have changed not on value of b.length");
            char[] bb = b;
            System.arraycopy(testCharBuffer.array(), pos, bb, off, len);
            if (!Arrays.equals(b, bb))
                return failed("CharBuffer.put(b) return wrong array");
            testCharBuffer.position(0);
        } catch (Throwable e) {
            return failed("throw " + e
                    + ", when b.length = capacity of CharBuffer");
        }
        return passed();
    }
}