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
 * Created on 01.09.2005
 */
package org.apache.harmony.test.func.api.java.nio.byteBuffer;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;
import java.util.Arrays;
import java.util.Random;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

public class ByteBufferTest2 extends MultiCase {

    private ByteBuffer testByteBuffer;

    private byte[] arrayOfTestByteBuffer;

    final static int MAX_BUFFER_SIZE = 1048576;

    final static int MIN_BUFFER_SIZE = 8;

    static Random r = new Random();

    public static int getBufferSize() {
        return MIN_BUFFER_SIZE + r.nextInt(MAX_BUFFER_SIZE - MIN_BUFFER_SIZE);
    }

    public static byte[] getArrayOfRandomByte(int len) {
        byte[] b = new byte[len];
        r.nextBytes(b);
        //Need only for Linux, because if b have element < 0, than originated
        // problem with String(b) in Linux
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte) (Math.abs(b[i]) + 1);
        }
        return b;
    }

    public static byte[] getArrayOfRandomByte() {
        return getArrayOfRandomByte(getBufferSize());
    }

    public Result testCreateByteBuffer() {
        try {
            arrayOfTestByteBuffer = getArrayOfRandomByte();
            testByteBuffer = ByteBuffer.wrap(arrayOfTestByteBuffer);
        } catch (Throwable e) {
            return failed("cannot create ByteBuffer with wrap(byte[]), throw "
                    + e);
        }
        return passed();
    }

    public static void main(String[] args) {
        System.exit(new ByteBufferTest2().test(args));
    }

    public Result testWrap() {
        testCreateByteBuffer();
        if (testByteBuffer.capacity() != arrayOfTestByteBuffer.length)
            return failed("wrap(byte[]) create ByteBuffer with wrong capacity");
        if (testByteBuffer.limit() != arrayOfTestByteBuffer.length)
            return failed("wrap(byte[]) create ByteBuffer with wrong limit");
        if (testByteBuffer.position() != 0)
            return failed("wrap(byte[]) create ByteBuffer with wrong position");
        if (testByteBuffer.arrayOffset() != 0)
            return failed("wrap(byte[]) create ByteBuffer with wrong arrayOffset");
        if (!testByteBuffer.hasArray())
            return failed("wrap(byte[]) create ByteBuffer with wrong backing array");
        else if (!java.util.Arrays.equals(arrayOfTestByteBuffer, testByteBuffer
                .array()))
            return failed("wrap(byte[]) create ByteBuffer with wrong backing array");
        return passed();
    }

    public Result testWrap2() {
        byte[] b = getArrayOfRandomByte();
        int offset = r.nextInt(b.length / 2);
        int length = r.nextInt(b.length - offset - 1);
        try {
            testByteBuffer = ByteBuffer.wrap(b, offset, length);
        } catch (Throwable e) {
            return failed("cannot create ByteBuffer with wrap(byte[], int, int)");
        }
        if (testByteBuffer.capacity() != b.length)
            return failed("wrap(byte[], int, int) create ByteBuffer with wrong capacity");
        if (testByteBuffer.limit() != offset + length)
            return failed("wrap(byte[], int, int) create ByteBuffer with wrong limit");
        if (testByteBuffer.position() != offset)
            return failed("wrap(byte[], int, int) create ByteBuffer with wrong position");
        if (testByteBuffer.arrayOffset() != 0)
            return failed("wrap(byte[], int, int) create ByteBuffer with wrong arrayOffset");
        if (!testByteBuffer.hasArray())
            return failed("wrap(byte[]) create ByteBuffer with wrong backing array");
        else if (!Arrays.equals(b, testByteBuffer.array()))
            return failed("wrap(byte[], int, int) create ByteBuffer with wrong backing array");
        try {
            testByteBuffer = ByteBuffer.wrap(b, 1, b.length);
            return failed("must throw IndexOutOfBoundsException, because offset + length > length of array");
        } catch (IndexOutOfBoundsException e) {
        } catch (Throwable e) {
            return failed("must throw IndexOutOfBoundsException, because offset + length > length of array, but throw "
                    + e);
        }
        try {
            testByteBuffer = ByteBuffer.wrap(b, 0, b.length);
            testByteBuffer = ByteBuffer.wrap(b, b.length, 0);
        } catch (Throwable e) {
            return failed("throw " + e
                    + ", when offset + length = length of array");
        }
        return passed();
    }

    public Result testGet() {
        testCreateByteBuffer();
        byte[] b = new byte[testByteBuffer.capacity() + 1];
        try {
            testByteBuffer.get(b);
            return failed("must throw BufferUnderflowException, because ByteBuffer.position + b.length > ByteBuffer.capacity of ByteBuffer");
        } catch (BufferUnderflowException e) {
        } catch (Throwable e) {
            return failed("must throw BufferUnderflowException, because ByteBuffer.position + b.length > ByteBuffer.capacity of ByteBuffer, but throw "
                    + e);
        }
        try {
            testByteBuffer.position(r.nextInt(testByteBuffer.capacity() / 3));
            b = new byte[testByteBuffer.capacity() - testByteBuffer.position()
                    + 1];
            testByteBuffer.get(b);
            return failed("must throw BufferUnderflowException, because ByteBuffer.position + b.length > ByteBuffer.capacity of ByteBuffer");
        } catch (BufferUnderflowException e) {
        } catch (Throwable e) {
            return failed("must throw BufferUnderflowException, because ByteBuffer.position + b.length > ByteBuffer.capacity of ByteBuffer, but throw "
                    + e);
        }
        testByteBuffer.position(0);
        b = new byte[0];
        try {
            testByteBuffer.get(b);
            if (b.length > 0)
                return failed("lentgh of array after ByteBuffer.get(b) > 0, but must = 0, because before b.length = 0");
            if (testByteBuffer.position() > 0)
                return failed("position in ByteBuffer after ByteBuffer.get(b) > 0, but must = 0, because before b.length = 0");
        } catch (Throwable e) {
            return failed("throw " + e + ", when b.length = 0");
        }
        b = new byte[testByteBuffer.capacity()];
        try {
            testByteBuffer.get(b);
            if (!Arrays.equals(arrayOfTestByteBuffer, b))
                return failed("ByteBuffer.get(b) return wrong array");
            testByteBuffer.position(0);
        } catch (Throwable e) {
            return failed("throw " + e
                    + ", when b.length = capacity of ByteBuffer");
        }
        int pos = r.nextInt(testByteBuffer.capacity() / 3);
        int len = r.nextInt(testByteBuffer.capacity() / 2);
        b = new byte[len];
        try {
            testByteBuffer.position(pos);
            testByteBuffer.get(b);
            if (b.length != len)
                return failed("lentgh of array b have changed after ByteBuffer.get(b)");
            if (testByteBuffer.position() != pos + len)
                return failed("position in ByteBuffer after ByteBuffer.get(b) have changed not on value of b.length");
            byte[] bb = new byte[len];
            System.arraycopy(arrayOfTestByteBuffer, pos, bb, 0, bb.length);
            if (!Arrays.equals(b, bb))
                return failed("ByteBuffer.get(b) return wrong array");
            testByteBuffer.position(0);
        } catch (Throwable e) {
            return failed("throw " + e
                    + ", when b.length = capacity of ByteBuffer");
        }
        return passed();
    }

    public Result testArrayOffset() {
        testByteBuffer = ByteBuffer.allocate(getBufferSize());
        if (testByteBuffer.arrayOffset() != 0)
            return failed("ByteBuffer.allocate(int) create ByteBuffer with wrong arrayOffset");
        testCreateByteBuffer();
        if (testByteBuffer.arrayOffset() != 0)
            return failed("ByteBuffer.wrap(byte[]) create ByteBuffer with wrong arrayOffset");
        int pos = r.nextInt(testByteBuffer.capacity());
        testByteBuffer.position(pos);
        if (testByteBuffer.arrayOffset() != 0)
            return failed("ByteBuffer.wrap(byte[]) create ByteBuffer with wrong arrayOffset");
        try {
            testByteBuffer.asReadOnlyBuffer().arrayOffset();
            return failed("must throw ReadOnlyBufferException");
        } catch (ReadOnlyBufferException e) {
        } catch (Throwable e) {
            return failed("must throw ReadOnlyBufferException, but throw " + e);
        }
        return passed();
    }

    public Result testHasArray() {
        testByteBuffer = ByteBuffer.allocate(getBufferSize());
        if (!testByteBuffer.hasArray())
            return failed("ByteBuffer.allocate(int) create ByteBuffer without array back array");
        testCreateByteBuffer();
        if (!testByteBuffer.hasArray())
            return failed("ByteBuffer.wrap(byte[]) create ByteBuffer without array back array");
        return passed();
    }
}