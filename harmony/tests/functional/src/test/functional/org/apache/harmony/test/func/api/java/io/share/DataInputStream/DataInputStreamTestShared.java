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
package org.apache.harmony.test.func.api.java.io.share.DataInputStream;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UTFDataFormatException;

import org.apache.harmony.test.func.api.java.io.share.FilterInputStream.FilterInputStreamTestShared;
import org.apache.harmony.test.func.api.java.io.share.MultiThreadRunner;
import org.apache.harmony.test.func.api.java.io.share.PrepareTestCleanup;
import org.apache.harmony.share.Result;

public class DataInputStreamTestShared extends FilterInputStreamTestShared implements
        PrepareTestCleanup {
    static File testDir;

    public void setTestDir(File dir) {
        testDir = dir;
    }

    public int prepare(File dir) throws IOException {
        FileOutputStream fos = new FileOutputStream(dir.getAbsolutePath()
                + "/gold.txt");
        fos.write("abcdefghijklmno".getBytes());
        fos.close();

        fos = new FileOutputStream(dir.getAbsolutePath() + "/gold1.txt");
        for (int i = 0; i < 3; ++i) {
            fos.write(new byte[] { -128, -64, -1, 0, 1, 32, 127 });
        }
        fos.close();

        DataOutputStream dos = new DataOutputStream(new FileOutputStream(dir
                .getAbsolutePath()
                + "/float.txt"));
        dos.writeFloat(Float.MAX_VALUE);
        dos.writeFloat(Float.MIN_VALUE);
        dos.writeFloat(Float.NaN);
        dos.writeFloat(Float.NEGATIVE_INFINITY);
        dos.writeFloat(Float.POSITIVE_INFINITY);
        dos.writeFloat(1.0000044e20f);
        dos.writeFloat(-1.6000008e11f);
        dos.writeByte(-1);
        dos.close();

        dos = new DataOutputStream(new FileOutputStream(dir.getAbsolutePath()
                + "/double.txt"));
        dos.writeDouble(Double.MAX_VALUE);
        dos.writeDouble(Double.MIN_VALUE);
        dos.writeDouble(Double.NaN);
        dos.writeDouble(Double.NEGATIVE_INFINITY);
        dos.writeDouble(Double.POSITIVE_INFINITY);

        dos.writeDouble(Float.MAX_VALUE);
        dos.writeDouble(Float.MIN_VALUE);
        dos.writeDouble(Float.NaN);
        dos.writeDouble(Float.NEGATIVE_INFINITY);
        dos.writeDouble(Float.POSITIVE_INFINITY);

        dos.writeDouble(1.0000044e20);
        dos.writeDouble(-1.6000008e11);
        dos.writeByte(-1);
        dos.close();

        return pass();
    }

    public int cleanup(File dir) throws IOException {
        if (!new File(dir.getAbsolutePath() + "/gold.txt").delete()) {
            return fail("can't delete " + dir.getAbsolutePath() + "/gold.txt");
        }
        if (!new File(dir.getAbsolutePath() + "/gold1.txt").delete()) {
            return fail("can't delete " + dir.getAbsolutePath() + "/gold1.txt");
        }
        if (!new File(dir.getAbsolutePath() + "/float.txt").delete()) {
            return fail("can't delete " + dir.getAbsolutePath() + "/float.txt");
        }
        if (!new File(dir.getAbsolutePath() + "/double.txt").delete()) {
            return fail("can't delete " + dir.getAbsolutePath() + "/double.txt");
        }
        return pass();
    }

    public InputStream getTestedInputStream() throws IOException {
        return new DataInputStream(new FileInputStream(testDir
                .getAbsolutePath()
                + "/gold.txt"));
    }

    public DataInputStream getDataInputStream() throws IOException {
        return new DataInputStream(new FileInputStream(testDir
                .getAbsolutePath()
                + "/gold1.txt"));
    }

    public InputStream getTestedInputStream(InputStream is) {
        return new DataInputStream(is);
    }

    public Result testAvailable() throws IOException {
        InputStream is = getTestedInputStream();
        try {
            if (is.available() == new File(testDir.getAbsolutePath()
                    + "/gold.txt").length()) {
                return passed();
            }
        } finally {
            is.close();
        }
        return failed("InputStream 'available' method must return length of file");
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

    public Result testReadBoolean() throws IOException {
        DataInputStream dis = getDataInputStream();
        
        MultiThreadRunner.waitAtBarrier();
        try {
            if (dis.readBoolean() && dis.readBoolean() && dis.readBoolean()
                    && !dis.readBoolean() && dis.readBoolean()
                    && dis.readBoolean() && dis.readBoolean())
                return passed();
        } finally {
            dis.close();
        }
        return failed("expected other values");
    }


    public Result testReadSignedByte() throws IOException {
        DataInputStream dis = getDataInputStream();
        MultiThreadRunner.waitAtBarrier();
        try {
            if (dis.readByte() == -128 && dis.readByte() == -64
                    && dis.readByte() == -1 && dis.readByte() == 0
                    && dis.readByte() == 1 && dis.readByte() == 32
                    && dis.readByte() == 127)
                return passed();
        } finally {
            dis.close();
        }
        return failed("expected other values");
    }

    public Result testReadUnsignedByte() throws IOException {
        DataInputStream dis = getDataInputStream();

        try {
            MultiThreadRunner.waitAtBarrier();
            if (dis.readUnsignedByte() == 128 && dis.readUnsignedByte() == 192
                    && dis.readUnsignedByte() == 255
                    && dis.readUnsignedByte() == 0
                    && dis.readUnsignedByte() == 1
                    && dis.readUnsignedByte() == 32
                    && dis.readUnsignedByte() == 127)
                return passed();
        } finally {
            dis.close();
        }

        return failed("expected other values");
    }

    public Result testReadSignedShort() throws IOException {
        DataInputStream dis = getDataInputStream();
        try {
            MultiThreadRunner.waitAtBarrier();
            if (dis.readShort() != -32576 || dis.readShort() != -256
                    || dis.readShort() != 288 || dis.readShort() != 32640
                    || dis.readShort() != -16129 || dis.readShort() != 1
                    || dis.readShort() != 8319 || dis.readShort() != -32576
                    || dis.readShort() != -256 || dis.readShort() != 288) {
                return failed("expected other values");
            }
            try {
                dis.readShort();
                return failed("expected EOFException");
            } catch (EOFException e) {
            }
        } finally {
            dis.close();
        }
        return passed();
    }

    public Result testReadUnsignedShort() throws IOException {
        DataInputStream dis = getDataInputStream();
        try {
            MultiThreadRunner.waitAtBarrier();
            if (dis.readUnsignedShort() != 32960
                    || dis.readUnsignedShort() != 65280
                    || dis.readUnsignedShort() != 288
                    || dis.readUnsignedShort() != 32640
                    || dis.readUnsignedShort() != 49407
                    || dis.readUnsignedShort() != 1
                    || dis.readUnsignedShort() != 8319
                    || dis.readUnsignedShort() != 32960
                    || dis.readUnsignedShort() != 65280
                    || dis.readUnsignedShort() != 288) {
                return failed("expected other values");
            }
            try {
                dis.readUnsignedShort();
                return failed("expected EOFException");
            } catch (EOFException e) {
            }
        } finally {
            dis.close();
        }
        return passed();
    }

    public Result testReadChar() throws IOException {
        DataInputStream dis = getDataInputStream();
        try {
            MultiThreadRunner.waitAtBarrier();
            if (dis.readChar() != '\u80c0' || dis.readChar() != '\uff00'
                    || dis.readChar() != '\u0120' || dis.readChar() != '\u7f80'
                    || dis.readChar() != '\uc0ff' || dis.readChar() != '\u0001'
                    || dis.readChar() != '\u207f' || dis.readChar() != '\u80c0'
                    || dis.readChar() != '\uff00' || dis.readChar() != '\u0120') {
                return failed("expected other values");
            }
            try {
                dis.readChar();
                return failed("expected EOFException");
            } catch (EOFException e) {
            }
        } finally {
            dis.close();
        }
        return passed();
    }

    public Result testReadInt() throws IOException {
        DataInputStream dis = getDataInputStream();
        try {
            MultiThreadRunner.waitAtBarrier();
            if (dis.readInt() != -2134835456 || dis.readInt() != 18907008
                    || dis.readInt() != -1057030143
                    || dis.readInt() != 545226944 || dis.readInt() != -16776928) {
                return failed("expected other values");
            }
            try {
                dis.readInt();
                return failed("expected EOFException");
            } catch (EOFException e) {
            }
        } finally {
            dis.close();
        }
        return passed();
    }

    public Result testReadLong() throws IOException {
        DataInputStream dis = getDataInputStream();
        try {
            MultiThreadRunner.waitAtBarrier();
            if (dis.readLong() != -9169048465842339968L
                    || dis.readLong() != -4539909894525976384L) {
                return failed("expected other values");
            }
            try {
                dis.readLong();
                return failed("expected EOFException");
            } catch (EOFException e) {
            }
        } finally {
            dis.close();
        }
        return passed();
    }

    public Result testReadFloat() throws IOException {
        DataInputStream dis = new DataInputStream(new FileInputStream(testDir
                .getAbsolutePath()
                + "/float.txt"));

        try {
            MultiThreadRunner.waitAtBarrier();
            if (dis.readFloat() != Float.MAX_VALUE) {
                return failed("expected Float.MAX_VALUE");
            }
            if (dis.readFloat() != Float.MIN_VALUE) {
                return failed("expected Float.MIN_VALUE");
            }
            if (!Float.isNaN(dis.readFloat())) {
                return failed("expected Float.NaN");
            }
            if (dis.readFloat() != Float.NEGATIVE_INFINITY) {
                return failed("expected Float.NEGATIVE_INFINITY");
            }
            if (dis.readFloat() != Float.POSITIVE_INFINITY) {
                return failed("expected Float.POSITIVE_INFINITY");
            }
            if (dis.readFloat() != 1.0000044e20f) {
                return failed("expected 1.0000044e20f");
            }
            if (dis.readFloat() != -1.6000008e11f) {
                return failed("expected -1.6000008e11f");
            }
            try {
                dis.readFloat();
                return failed("expected EOFException");
            } catch (EOFException e) {
            }
        } finally {
            dis.close();
        }

        return passed();
    }

    public Result testReadDouble() throws IOException {
        DataInputStream dis = new DataInputStream(new FileInputStream(testDir
                .getAbsolutePath()
                + "/double.txt"));

        try {
            MultiThreadRunner.waitAtBarrier();
            if (dis.readDouble() != Double.MAX_VALUE) {
                return failed("expected Double.MAX_VALUE");
            }
            if (dis.readDouble() != Double.MIN_VALUE) {
                return failed("expected Double.MIN_VALUE");
            }
            if (!Double.isNaN(dis.readDouble())) {
                return failed("expected Double.NaN");
            }
            if (dis.readDouble() != Double.NEGATIVE_INFINITY) {
                return failed("expected Double.NEGATIVE_INFINITY");
            }
            if (dis.readDouble() != Double.POSITIVE_INFINITY) {
                return failed("expected Double.POSITIVE_INFINITY");
            }

            if (dis.readDouble() != Float.MAX_VALUE) {
                return failed("expected Float.MAX_VALUE");
            }
            if (dis.readDouble() != Float.MIN_VALUE) {
                return failed("expected Float.MIN_VALUE");
            }
            if (!Float.isNaN((float) dis.readDouble())) {
                return failed("expected Float.NaN");
            }
            if (dis.readDouble() != Float.NEGATIVE_INFINITY) {
                return failed("expected Float.NEGATIVE_INFINITY");
            }
            if (dis.readDouble() != Float.POSITIVE_INFINITY) {
                return failed("expected Float.POSITIVE_INFINITY");
            }

            if (dis.readDouble() != 1.0000044e20) {
                return failed("expected 1.0000044e20");
            }
            if (dis.readDouble() != -1.6000008e11) {
                return failed("expected -1.6000008e11");
            }

            try {
                dis.readDouble();
                return failed("expected EOFException");
            } catch (EOFException e) {
            }
        } finally {
            dis.close();
        }

        return passed();
    }

    public Result testReadUTF() throws IOException {
        DataInputStream dis = null;
        String s;

        try {
            MultiThreadRunner.waitAtBarrier();
            dis = new DataInputStream(
                    new ByteArrayInputStream(new byte[] { 0 }));
            try {
                dis.readUTF();
                return failed("failed at 0");
            } catch (EOFException e) {
            }

            dis = new DataInputStream(new ByteArrayInputStream(new byte[] { 0,
                    0, 0 }));
            if (!dis.readUTF().equals("")) {
                return failed("failed at 0, 0, 0");
            }

            dis = new DataInputStream(new ByteArrayInputStream(new byte[] { 0,
                    1, 0 }));
            if (!dis.readUTF().equals("\u0000")) {
                return failed("failed at 0, 1, 0");
            }

            dis = new DataInputStream(new ByteArrayInputStream(new byte[] { 0,
                    2, -64, -128 }));
            if (!dis.readUTF().equals("\u0000")) {
                return failed("failed at 0, 2, -64, -128");
            }

            dis = new DataInputStream(new ByteArrayInputStream(new byte[] { 0,
                    1, (byte) 0x7F }));
            if (!dis.readUTF().equals("\u007F")) {
                return failed("failed at 0, 1, 0x7F");
            }

            dis = new DataInputStream(new ByteArrayInputStream(new byte[] { 0,
                    1, (byte) 0xAF }));
            try {
                dis.readUTF();
                return failed("failed at 0, 1, 0xAF");
            } catch (UTFDataFormatException e) {
            }

            dis = new DataInputStream(new ByteArrayInputStream(new byte[] { 0,
                    1, (byte) 0xF0 }));
            try {
                dis.readUTF();
                return failed("failed at 0, 1, 0xF0");
            } catch (UTFDataFormatException e) {
            }

            dis = new DataInputStream(new ByteArrayInputStream(new byte[] { 0,
                    1, (byte) 0x8F }));
            try {
                dis.readUTF();
                return failed("failed at 0, 1, 0x8F");
            } catch (UTFDataFormatException e) {
            }

            dis = new DataInputStream(new ByteArrayInputStream(new byte[] { 0,
                    1, (byte) 0x99 }));
            try {
                dis.readUTF();
                return failed("failed at 0, 1, 0x99");
            } catch (UTFDataFormatException e) {
            }

            dis = new DataInputStream(new ByteArrayInputStream(new byte[] { 0,
                    2, (byte) 0xd7, (byte) 0xa9 }));
            s = dis.readUTF();
            if (s.length() != 1 || s.charAt(0) != 1513) {
                return failed("failed at 0, 1, 0xd7, 0xa9 : "
                        + (int) s.charAt(0));
            }

            dis = new DataInputStream(new ByteArrayInputStream(new byte[] { 0,
                    2, (byte) 0xd7, (byte) 0xff }));
            try {
                dis.readUTF();
                return failed("failed at 0, 2, 0xd7, 0xff");
            } catch (UTFDataFormatException e) {
            }

            dis = new DataInputStream(new ByteArrayInputStream(new byte[] { 0,
                    2, (byte) 0xd7, (byte) 0x00 }));
            try {
                dis.readUTF();
                return failed("failed at 0, 2, 0xd7, 0x00");
            } catch (UTFDataFormatException e) {
            }

            dis = new DataInputStream(new ByteArrayInputStream(new byte[] { 0,
                    3, (byte) 0xef, (byte) 0xaf, (byte) 0x8f }));
            s = dis.readUTF();
            if (s.length() != 1 || s.charAt(0) != 64463) {
                return failed("failed at 0, 3,  0xef , 0xaf, 0x8f : "
                        + (int) s.charAt(0));
            }

            dis = new DataInputStream(new ByteArrayInputStream(new byte[] { 0,
                    3, (byte) 0xef, (byte) 0xaf, (byte) 0x8f, (byte) 0xAF }));
            s = dis.readUTF();
            if (s.length() != 1 || s.charAt(0) != 64463) {
                return failed("failed at 0, 3,  0xef , 0xaf, 0x8f, 0xaf : "
                        + (int) s.charAt(0));
            }

            dis = new DataInputStream(new ByteArrayInputStream(new byte[] { 0,
                    3, (byte) 0xef, (byte) 0xaf, (byte) 0xff }));
            try {
                dis.readUTF();
                return failed("failed at 0, 3, 0xef , 0xaf,  0xff");
            } catch (UTFDataFormatException e) {
            }

            dis = new DataInputStream(new ByteArrayInputStream(new byte[] { 0,
                    3, (byte) 0xef, (byte) 0x00, (byte) 0xaf }));
            try {
                dis.readUTF();
                return failed("failed at 0, 3, 0xef , 0x00, 0xaf");
            } catch (UTFDataFormatException e) {
            }

            dis = new DataInputStream(new ByteArrayInputStream(new byte[] { 0,
                    2, (byte) 0xe1, (byte) 0xb5 }));
            try {
                dis.readUTF();
                return failed("failed at 0, 2, 0xe1 , 0xb5");
            } catch (UTFDataFormatException e) {
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return failed("oopsla");
        } finally {
            dis.close();
        }

        return passed();
    }

    public Result testStaticReadUTF() throws IOException {
        MultiThreadRunner.waitAtBarrier();
        try {
            DataInputStream.readUTF(new DataInputStream(
                    new ByteArrayInputStream(new byte[] { 0 })));
            return failed("failed at 0");
        } catch (EOFException e) {
        }

        if (!DataInputStream.readUTF(
                new DataInputStream(new ByteArrayInputStream(new byte[] { 0, 0,
                        0 }))).equals("")) {
            return failed("failed at 0, 0, 0");
        }

        if (!DataInputStream.readUTF(
                new DataInputStream(new ByteArrayInputStream(new byte[] { 0, 1,
                        0 }))).equals("\u0000")) {
            return failed("failed at 0, 1, 0");
        }

        if (!DataInputStream.readUTF(
                new DataInputStream(new ByteArrayInputStream(new byte[] { 0, 2,
                        -64, -128 }))).equals("\u0000")) {
            return failed("failed at 0, 2, -64, -128");
        }

        if (!DataInputStream.readUTF(
                new DataInputStream(new ByteArrayInputStream(new byte[] { 0, 1,
                        (byte) 0x7F }))).equals("\u007F")) {
            return failed("failed at 0, 1, 0x7F");
        }

        try {
            DataInputStream
                    .readUTF(new DataInputStream(new ByteArrayInputStream(
                            new byte[] { 0, 1, (byte) 0xAF })));
            return failed("failed at 0, 1, 0xAF");
        } catch (UTFDataFormatException e) {
        }

        try {
            DataInputStream
                    .readUTF(new DataInputStream(new ByteArrayInputStream(
                            new byte[] { 0, 1, (byte) 0xF0 })));
            return failed("failed at 0, 1, 0xF0");
        } catch (UTFDataFormatException e) {
        }

        try {
            DataInputStream
                    .readUTF(new DataInputStream(new ByteArrayInputStream(
                            new byte[] { 0, 1, (byte) 0x8F })));
            return failed("failed at 0, 1, 0x8F");
        } catch (UTFDataFormatException e) {
        }

        try {
            DataInputStream
                    .readUTF(new DataInputStream(new ByteArrayInputStream(
                            new byte[] { 0, 1, (byte) 0x99 })));
            return failed("failed at 0, 1, 0x99");
        } catch (UTFDataFormatException e) {
        }

        String s = DataInputStream.readUTF(new DataInputStream(
                new ByteArrayInputStream(new byte[] { 0, 2, (byte) 0xd7,
                        (byte) 0xa9 })));
        if (s.length() != 1 || s.charAt(0) != 1513) {
            return failed("failed at 0, 1, 0xd7, 0xa9 : " + (int) s.charAt(0));
        }

        try {
            DataInputStream.readUTF(new DataInputStream(
                    new ByteArrayInputStream(new byte[] { 0, 2, (byte) 0xd7,
                            (byte) 0xff })));
            return failed("failed at 0, 2, 0xd7, 0xff");
        } catch (UTFDataFormatException e) {
        }

        try {
            DataInputStream.readUTF(new DataInputStream(
                    new ByteArrayInputStream(new byte[] { 0, 2, (byte) 0xd7,
                            (byte) 0x00 })));
            return failed("failed at 0, 2, 0xd7, 0x00");
        } catch (UTFDataFormatException e) {
        }

        s = DataInputStream.readUTF(new DataInputStream(
                new ByteArrayInputStream(new byte[] { 0, 3, (byte) 0xef,
                        (byte) 0xaf, (byte) 0x8f })));
        if (s.length() != 1 || s.charAt(0) != 64463) {
            return failed("failed at 0, 3,  0xef , 0xaf, 0x8f : "
                    + (int) s.charAt(0));
        }

        s = DataInputStream.readUTF(new DataInputStream(
                new ByteArrayInputStream(new byte[] { 0, 3, (byte) 0xef,
                        (byte) 0xaf, (byte) 0x8f, (byte) 0xAF })));
        if (s.length() != 1 || s.charAt(0) != 64463) {
            return failed("failed at 0, 3,  0xef , 0xaf, 0x8f, 0xaf : "
                    + (int) s.charAt(0));
        }

        try {
            DataInputStream.readUTF(new DataInputStream(
                    new ByteArrayInputStream(new byte[] { 0, 3, (byte) 0xef,
                            (byte) 0xaf, (byte) 0xff })));
            return failed("failed at 0, 3, 0xef , 0xaf,  0xff");
        } catch (UTFDataFormatException e) {
        }

        try {
            DataInputStream.readUTF(new DataInputStream(
                    new ByteArrayInputStream(new byte[] { 0, 3, (byte) 0xef,
                            (byte) 0x00, (byte) 0xaf })));
            return failed("failed at 0, 3, 0xef , 0x00, 0xaf");
        } catch (UTFDataFormatException e) {
        }

        try {
            DataInputStream.readUTF(new DataInputStream(
                    new ByteArrayInputStream(new byte[] { 0, 2, (byte) 0xe1,
                            (byte) 0xb5 })));
            return failed("failed at 0, 2, 0xe1 , 0xb5");
        } catch (UTFDataFormatException e) {
        }

        return passed();
    }

    public Result testReadFullyNullBuffer() throws IOException {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(
                new byte[] { 0 }));
        try {
            dis.readFully(null);
            return failed("expected NPE here");
        } catch (NullPointerException e) {
        }
        return passed();
    }

    public Result testReadFullyEmptyBuffer() throws IOException {
        DataInputStream dis = new DataInputStream(new ExceptionStream());
        dis.readFully(new byte[] {});
        return passed();
    }

    public Result testReadFully0001() throws IOException {
        DataInputStream dis = new DataInputStream(ReadFullyStream.getInstance(
                new int[] { 'a', 'b', 'c', 'd', 'e' }, 1));
        byte[] barr = new byte[2];
        MultiThreadRunner.waitAtBarrier();
        dis.readFully(barr);
        if (!new String(barr).equals("ab")) {
            return failed("expected 'ab' but got " + new String(barr));
        }
        return passed();
    }

    public Result testReadFully0002() throws IOException {
        DataInputStream dis = new DataInputStream(ReadFullyStream.getInstance(
                new int[] { 'a', 'b', 'c', 'd', 'e' }, 1));
        byte[] barr = new byte[4];
        MultiThreadRunner.waitAtBarrier();
        dis.readFully(barr, 1, 2);
        if (!new String(barr, 1, 2).equals("ab")) {
            return failed("expected 'ab' but got " + new String(barr, 1, 2));
        }
        return passed();
    }

    public Result testSkipBytes() throws IOException {
        DataInputStream dis = new DataInputStream(ReadFullyStream.getInstance(
                new int[] { 'a', 'b', 'c', 'd', 'e' }, 1));
        MultiThreadRunner.waitAtBarrier();
        int skipped = dis.skipBytes(3);
        if (skipped != 3) {
            return failed("expected 3 bytes to be skipped but got " + skipped);
        }
        return passed();
    }

    public Result testSkipBytesEOF() throws IOException {
        DataInputStream dis = new DataInputStream(ReadFullyStream.getInstance(
                new int[] { 'a', 'b', 'c' }, 1));
        MultiThreadRunner.waitAtBarrier();
        int skipped = dis.skipBytes(5);
        if (skipped != 3) {
            return failed("expected 3 bytes to be skipped but got " + skipped);
        }
        return passed();
    }
}

class ExceptionStream extends InputStream {
    public int read() throws IOException {
        throw new IllegalArgumentException("shouldn't be called");
    }

    public int available() throws IOException {
        throw new IllegalArgumentException("shouldn't be called");
    }

    public void close() throws IOException {
        throw new IllegalArgumentException("shouldn't be called");
    }

    public synchronized void mark(int arg0) {
        throw new IllegalArgumentException("shouldn't be called");
    }

    public boolean markSupported() {
        throw new IllegalArgumentException("shouldn't be called");
    }

    public int read(byte[] arg0, int arg1, int arg2) throws IOException {
        throw new IllegalArgumentException("shouldn't be called");
    }

    public int read(byte[] arg0) throws IOException {
        throw new IllegalArgumentException("shouldn't be called");
    }

    public synchronized void reset() throws IOException {
        throw new IllegalArgumentException("shouldn't be called");
    }

    public long skip(long arg0) throws IOException {
        throw new IllegalArgumentException("shouldn't be called");
    }
}

class ReadFullyStream extends InputStream {
    private int[] buf;

    private int count = 0;

    private int in = 0;

    private int out = 0;

    private boolean closed = false;

    private boolean eof = false;

    private ReadFullyStream() {
        super();
    };

    private static void debug(String s) {
        System.err.println(s);
    }

    //this is a factory method but not a constructor because we're going to
    //start a thread in it
    public static ReadFullyStream getInstance(int[] source, int maxReadAtOnce) {
        //        debug("getInstance " + source.length + " " + maxReadAtOnce);

        ReadFullyStream mis = new ReadFullyStream();
        mis.buf = new int[maxReadAtOnce];
        new ProducerThread(mis, source).start();
        return mis;
    }

    public synchronized int available() throws IOException {
        if (isEof()) {
            throw new IOException("source exhausted");
        }

        //        debug("avalable returns " + count);

        return count;
    }

    public synchronized void close() throws IOException {
        super.close();
        closed = true;
    }

    public synchronized boolean isClosed() {
        return closed;
    }

    synchronized boolean isEof() {
        return eof;
    }

    synchronized void setEof(boolean eof) {
        this.eof = eof;
        notify();
    }

    public synchronized int read() throws IOException {
        int toReturn = -1;
        if (isEof()) {
            return -1;
        }

        while (count == 0 && !isEof()) {
            try {
                wait();
            } catch (InterruptedException e) {
                return -1;
            }
        }
        if (isEof()) {
            return -1;
        }

        toReturn = buf[out];
        out = (out + 1) % buf.length;
        --count;
        notify();

        //debug("read returns " + toReturn);

        return toReturn;
    }

    synchronized void put(int i) {
        //        debug("put " + i);

        if (isClosed()) {
            return;
        }

        while (count == buf.length) {
            try {
                wait();
            } catch (InterruptedException e) {
                return;
            }
        }

        buf[in] = i;
        ++count;
        in = (in + 1) % buf.length;

        notify();
    }
}

class ProducerThread extends Thread {
    private ReadFullyStream mis;

    private int[] source;

    ProducerThread(ReadFullyStream mis, int[] source) {
        super();
        this.mis = mis;
        this.source = new int[source.length];
        System.arraycopy(source, 0, this.source, 0, source.length);
    }

    public void run() {
        for (int i = 0; i < source.length; ++i) {
            if (mis.isClosed()) {
                return;
            }
            mis.put(source[i]);
            try {
                sleep((int) (Math.random() * 100));
            } catch (InterruptedException e) {
            }
        }
        mis.setEof(true);
    }
}
