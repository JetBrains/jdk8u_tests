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
 * Created on 30.11.2004
 *
 */
package org.apache.harmony.test.func.api.java.io.share.DataOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import org.apache.harmony.test.func.api.java.io.share.FilterOutputStream.FilterOutputStreamTestShared;
import org.apache.harmony.test.func.api.java.io.share.MultiThreadRunner;
import org.apache.harmony.share.Result;

public class DataOutputStreamTestShared extends FilterOutputStreamTestShared {

    protected OutputStream getOutputStream(OutputStream os) {
        return new DataOutputStream(os);
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

    public Result testWriteBoolean() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        
        MultiThreadRunner.waitAtBarrier();

        if (dos.size() != 0) {
            return failed("expected written to be 0");
        }
        dos.writeBoolean(false);
        if (dos.size() != 1) {
            return failed("expected written to be 1");
        }
        dos.writeBoolean(true);
        if (dos.size() != 2) {
            return failed("expected written to be 2");
        }
        if (bos.toByteArray()[0] != 0) {
            return failed("expected 'false' to be written as '0'");
        }

        if (bos.toByteArray()[0] == 1) {
            return failed("expected 'true' to be written as '1'");
        }

        return passed();
    }

    public Result testWriteByte0001() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        byte[] barr = new byte[] { -128, -80, -64, -30, 0, 1, 5, 67, 76, 127 };

        MultiThreadRunner.waitAtBarrier();
        for (int i = 0; i < barr.length; ++i) {
            if (dos.size() != i) {
                return failed("expected written to be " + i);
            }
            dos.writeByte(barr[i]);
        }
        dos.close();
        if (!Arrays.equals(barr, bos.toByteArray())) {
            return failed("expected arrays to be equal");
        }

        return passed();
    }

    public Result testWriteShort() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        int[] sharr = new int[] { -32576, -256, 288, 32640, -16129, 1, 8319,
                -32576, -256, 288 };
        byte[] barr = new byte[] { -128, -64, -1, 0, 1, 32, 127, -128, -64, -1,
                0, 1, 32, 127, -128, -64, -1, 0, 1, 32 };

        for (int i = 0; i < sharr.length; ++i) {
            if (dos.size() != i * 2) {
                return failed("expected written to be " + i * 2 + " but it is "
                        + dos.size());
            }
            dos.writeShort(sharr[i]);
        }
        dos.close();
        if (!Arrays.equals(barr, bos.toByteArray())) {
            return failed("expected arrays to be equal");
        }
        return passed();
    }

    public Result testWriteChar() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        int[] charr = new int[] { -32576, -256, 288, 32640, -16129, 1, 8319,
                -32576, -256, 288 };
        byte[] barr = new byte[] { -128, -64, -1, 0, 1, 32, 127, -128, -64, -1,
                0, 1, 32, 127, -128, -64, -1, 0, 1, 32 };

        MultiThreadRunner.waitAtBarrier();
        for (int i = 0; i < charr.length; ++i) {
            if (dos.size() != i * 2) {
                return failed("expected written to be " + i * 2);
            }
            dos.writeChar(charr[i]);
        }
        dos.close();
        if (!Arrays.equals(barr, bos.toByteArray())) {
            return failed("expected arrays to be equal");
        }
        return passed();
    }

    public Result testWriteInt() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        int[] intarr = new int[] { -2134835456, 18907008, -1057030143,
                545226944, -16776928 };

        byte[] barr = new byte[] { -128, -64, -1, 0, 1, 32, 127, -128, -64, -1,
                0, 1, 32, 127, -128, -64, -1, 0, 1, 32 };

        MultiThreadRunner.waitAtBarrier();
        for (int i = 0; i < intarr.length; ++i) {
            if (dos.size() != i * 4) {
                return failed("expected written to be " + i * 4);
            }
            dos.writeInt(intarr[i]);
        }
        dos.close();

        if (!Arrays.equals(barr, bos.toByteArray())) {
            return failed("expected arrays to be equal");
        }
        return passed();
    }

    public Result testWriteLong() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        long[] longarr = new long[] { -9169048465842339968L,
                -4539909894525976384L };

        byte[] barr = new byte[] { -128, -64, -1, 0, 1, 32, 127, -128, -64, -1,
                0, 1, 32, 127, -128, -64 };

        MultiThreadRunner.waitAtBarrier();
        for (int i = 0; i < longarr.length; ++i) {
            if (dos.size() != i * 8) {
                return failed("expected written to be " + i * 8);
            }
            dos.writeLong(longarr[i]);
        }
        dos.close();

        if (!Arrays.equals(barr, bos.toByteArray())) {
            return failed("expected arrays to be equal");
        }
        return passed();
    }

    public Result testWriteFloat() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        float[] floatarr = new float[] { Float.MAX_VALUE, Float.MIN_VALUE,
                Float.NaN, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY,
                1.0000044e20f, -1.6000008e11f, -1 };

        byte[] barr = new byte[] { 127, 127, -1, -1, 0, 0, 0, 1, 127, -64, 0,
                0, -1, -128, 0, 0, 127, -128, 0, 0, 96, -83, 121, 30, -46, 21,
                2, -2, -65, -128, 0, 0 };

        MultiThreadRunner.waitAtBarrier();
        for (int i = 0; i < floatarr.length; ++i) {
            if (dos.size() != i * 4) {
                return failed("expected written to be " + i * 4);
            }
            dos.writeFloat(floatarr[i]);
        }

        if (!Arrays.equals(barr, bos.toByteArray())) {
            return failed("expected arrays to be equal");
        }

        dos.close();
        return passed();
    }

    public Result testWriteDouble() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        double[] doublearr = new double[] { Double.MAX_VALUE, Double.MIN_VALUE,
                Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,
                Float.MAX_VALUE, Float.MIN_VALUE, Float.NaN,
                Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY,
                1.0000044e20f, -1.6000008e11f, -1 };

        byte[] barr = new byte[] { 127, -17, -1, -1, -1, -1, -1, -1, 0, 0, 0,
                0, 0, 0, 0, 1, 127, -8, 0, 0, 0, 0, 0, 0, -1, -16, 0, 0, 0, 0,
                0, 0, 127, -16, 0, 0, 0, 0, 0, 0, 71, -17, -1, -1, -32, 0, 0,
                0, 54, -96, 0, 0, 0, 0, 0, 0, 127, -8, 0, 0, 0, 0, 0, 0, -1,
                -16, 0, 0, 0, 0, 0, 0, 127, -16, 0, 0, 0, 0, 0, 0, 68, 21, -81,
                35, -64, 0, 0, 0, -62, 66, -96, 95, -64, 0, 0, 0, -65, -16, 0,
                0, 0, 0, 0, 0 };
        
        MultiThreadRunner.waitAtBarrier();

        for (int i = 0; i < doublearr.length; ++i) {
            if (dos.size() != i * 8) {
                return failed("expected written to be " + i * 8);
            }
            dos.writeDouble(doublearr[i]);
        }

        if (!Arrays.equals(barr, bos.toByteArray())) {
            return failed("expected arrays to be equal");
        }

        dos.close();
        return passed();
    }

    public Result testWriteBytes() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        byte[] barr = new byte[] { 'a', 'b', 'c', 'd' };

        MultiThreadRunner.waitAtBarrier();
        
        dos.writeBytes("abcd");

        
        if (dos.size() != 4) {
            return failed("expected written to be 4");
        }
        dos.close();

        if (!Arrays.equals(barr, bos.toByteArray())) {
            return failed("expected arrays to be equal");
        }

        return passed();
    }

    public Result testWriteChars() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        byte[] barr = new byte[] { -6, -6, 0, 'a', 0, 'b', 0, 'c', 0, 'd' };

        MultiThreadRunner.waitAtBarrier();
        dos.writeChars("\ufafaabcd");

        if (dos.size() != 10) {
            return failed("expected written to be 10");
        }
        dos.close();

        if (!Arrays.equals(barr, bos.toByteArray())) {
            return failed("expected arrays to be equal");
        }
        //        for (int i = 0; i < bos.toByteArray().length; ++i) {
        //            System.err.println("," + bos.toByteArray()[i]);
        //        }

        return passed();
    }

    public Result testWriteUTF() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try {
            MultiThreadRunner.waitAtBarrier();
            dos.writeUTF("");
            if (dos.size() != 2
                    || !Arrays.equals(new byte[] { 0, 0 }, bos.toByteArray())) {
                return failed("case 1 failed");
            }
            bos.reset();

            dos.writeUTF("\u0000");
            if (dos.size() != 6
                    || !Arrays.equals(new byte[] { 0, 2, -64, -128 }, bos
                            .toByteArray())) {
                return failed("case 2 failed");
            }
            bos.reset();

            dos.writeUTF("\u007F");
            if (dos.size() != 9
                    || !Arrays.equals(new byte[] { 0, 1, (byte) 0x7f }, bos
                            .toByteArray())) {
                return failed("case 3 failed");
            }
            bos.reset();

            dos.writeUTF(new String(new char[] { 1513 }));
            if (dos.size() != 13
                    || !Arrays.equals(new byte[] { 0, 2, (byte) 0xd7,
                            (byte) 0xa9 }, bos.toByteArray())) {
                return failed("case 4 failed");
            }
            bos.reset();

            dos.writeUTF(new String(new char[] { 1513, 64463 }));
            if (dos.size() != 20
                    || !Arrays.equals(
                            new byte[] { 0, 5, (byte) 0xd7, (byte) 0xa9,
                                    (byte) 0xef, (byte) 0xaf, (byte) 0x8f },
                            bos.toByteArray())) {
                return failed("case 5 failed");
            }
            bos.reset();

            dos.writeUTF(new String(new char[] { 64463 }));
            if (dos.size() != 25
                    || !Arrays.equals(new byte[] { 0, 3, (byte) 0xef,
                            (byte) 0xaf, (byte) 0x8f }, bos.toByteArray())) {
                return failed("case 6 failed");
            }
            bos.reset();

        } finally {
            dos.close();
        }
        return passed();
    }

    public Result testWriteUTF0001() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        DataInputStream dis = null;

        String s;
        char[] carr = new char[1];

        try {
            MultiThreadRunner.waitAtBarrier();
            for (carr[0] = 0; carr[0] < 65535; ++carr[0]) {
                bos.reset();
                dos.writeUTF(new String(carr));
                dis = new DataInputStream(new ByteArrayInputStream(bos
                        .toByteArray()));
                s = dis.readUTF();
                if (s.length() != 1 || s.charAt(0) != carr[0]) {
                    return failed("failed at " + carr[0]);
                }
            }
        } finally {
            dos.close();
        }
        return passed();
    }

}
