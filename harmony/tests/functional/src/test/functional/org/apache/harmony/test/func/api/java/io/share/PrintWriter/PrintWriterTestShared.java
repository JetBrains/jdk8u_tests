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
package org.apache.harmony.test.func.api.java.io.share.PrintWriter;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import org.apache.harmony.test.func.api.java.io.share.Writer.WriterTestShared;
import org.apache.harmony.test.func.api.java.io.share.MockWriter;
import org.apache.harmony.test.func.api.java.io.share.PrepareTestCleanup;
import org.apache.harmony.test.func.api.java.io.share.Utils;
import org.apache.harmony.share.Result;

//TODO: add tests for uncovered methods
public class PrintWriterTestShared extends WriterTestShared implements PrepareTestCleanup {
    static File testDir;

    public void setTestDir(File dir) {
        testDir = dir;
    }

    public int prepare(File dir) throws IOException {
        return pass();
    }

    public int cleanup(File dir) throws IOException {
        for (int i = -1; i <= Utils.THREADS; ++i) {
            File f = new File(dir, (i == -1 ? "main" : "Thread-" + i) + ".txt");
            f.delete();
        }
        new File(dir,  "Main Thread.txt").delete();
        return pass();
    }

    private static String getDefaultOutputFileName() {
        return testDir.getAbsolutePath() + "/" + Thread.currentThread().getName()
        + ".txt";
    }

    protected String getWriterAsString(Writer w) throws IOException {
        try {
            FileInputStream fis = new FileInputStream(
                    getDefaultOutputFileName());
            byte[] arr = new byte[1000]; //must be sufficient for tests
            int len = fis.read(arr);
            fis.close();
            return new String(arr, 0, len);
        } catch (IOException e) {
            fail("error opening file " + getDefaultOutputFileName());
            return null;
        }
    }

    protected Writer getTestedWriter() throws IOException {
        return new PrintWriter(new FileWriter(getDefaultOutputFileName()));
    }

    protected Writer getTestedWriter(Object lock) {
        throw new IllegalArgumentException("this kind of test is not supported");
    }

    public Result testClose() throws IOException {
        return super.testClose();
    }

    public Result testFlush() throws IOException {
        return super.testFlush();
    }

    public Result testWriteArray() throws IOException {
        return super.testWriteArray();
    }

    public Result testWriteArrayBigLength() throws IOException {
        return super.testWriteArrayBigLength();
    }

    public Result testWriteArrayNegativeLength() throws IOException {
        return super.testWriteArrayNegativeLength();
    }

    public Result testWriteArrayNegativeStart() throws IOException {
        return super.testWriteArrayNegativeStart();
    }

    public Result testWriteArraySlice() throws IOException {
        return super.testWriteArraySlice();
    }

    public Result testWriteInt() throws IOException {
        return super.testWriteInt();
    }

    public Result testWriteNullArray() throws IOException {
        return super.testWriteNullArray();
    }

    public Result testWriteNullString() throws IOException {
        return super.testWriteNullString();
    }

    public Result testWriteString() throws IOException {
        return super.testWriteString();
    }

    public Result testWriteStringBigLength() throws IOException {
        return super.testWriteStringBigLength();
    }

    public Result testWriteStringNegativeLength() throws IOException {
        return super.testWriteStringNegativeLength();
    }

    public Result testWriteStringNegativeStart() throws IOException {
        return super.testWriteStringNegativeStart();
    }

    public Result testWriteStringSlice() throws IOException {
        return super.testWriteStringSlice();
    }

    public Result testOutField() throws IOException {
        MockWriter mw = new MockWriter();
        PrintWriterExposedOut w = new PrintWriterExposedOut(mw);

        if (w.getOut() == mw) {
            return passed();
        }

        return failed("expected writer to be saved to 'out' field");
    }

    public Result testWriterNoAutoFlush() throws IOException {
        MockWriter mw = new MockWriter();
        Writer w = new PrintWriter(new BufferedWriter(mw), false);
        w.write("xyz".toCharArray());
        try {
            if (mw.toString().length() == 0) {
                return passed();
            }
        } finally {
            w.close();
        }

        return failed("expected data to be buffered");
    }

    //TODO: add test for '\n'
    public Result testWriterAutoFlush() throws IOException {
        final int EOF_LENGTH = System.getProperty("line.separator").length();

        MockWriter mw;
        PrintWriter pw;

        mw = new MockWriter();
        pw = new PrintWriter(new BufferedWriter(mw), true);
        pw.print("xyz".getBytes());
        if (mw.toString().length() != 0) {
            return failed("expected bufferization after write(byte[])");
        }

        mw = new MockWriter();
        pw = new PrintWriter(new BufferedWriter(mw), true);
        pw.println();
        if (mw.toString().length() != EOF_LENGTH) {
            return failed("expected flush after println() "
                    + mw.toString().length());
        }

        mw = new MockWriter();
        pw = new PrintWriter(new BufferedWriter(mw), true);
        pw.println(true);
        if (mw.toString().length() != 4 + EOF_LENGTH) {
            return failed("expected flush after println(boolean) "
                    + mw.toString().length());
        }

        mw = new MockWriter();
        pw = new PrintWriter(new BufferedWriter(mw), true);
        pw.println('c');
        if (mw.toString().length() != 1 + EOF_LENGTH) {
            return failed("expected flush after println(char)"
                    + mw.toString().length());
        }

        mw = new MockWriter();
        pw = new PrintWriter(new BufferedWriter(mw), true);
        pw.println("abc".toCharArray());
        if (mw.toString().length() != 3 + EOF_LENGTH) {
            return failed("expected flush after println(char[])"
                    + mw.toString().length() + " " + EOF_LENGTH);
        }

        mw = new MockWriter();
        pw = new PrintWriter(new BufferedWriter(mw), true);
        pw.println((int) 1);
        if (mw.toString().length() != 1 + EOF_LENGTH) {
            return failed("expected flush after println(int)"
                    + mw.toString().length() + " " + EOF_LENGTH);
        }

        mw = new MockWriter();
        pw = new PrintWriter(new BufferedWriter(mw), true);
        pw.println(1L);
        if (mw.toString().length() != 1 + EOF_LENGTH) {
            return failed("expected flush after println(long)"
                    + mw.toString().length() + " " + EOF_LENGTH);
        }

        mw = new MockWriter();
        pw = new PrintWriter(new BufferedWriter(mw), true);
        pw.println((float) 1.0);
        if (mw.toString().length() != 3 + EOF_LENGTH) {
            return failed("expected flush after println(float)"
                    + mw.toString().length() + " " + EOF_LENGTH);
        }

        mw = new MockWriter();
        pw = new PrintWriter(new BufferedWriter(mw), true);
        pw.println((double) 1.0);
        if (mw.toString().length() != 3 + EOF_LENGTH) {
            return failed("expected flush after println(double)"
                    + mw.toString().length() + " " + EOF_LENGTH);
        }

        mw = new MockWriter();
        pw = new PrintWriter(new BufferedWriter(mw), true);
        pw.println("abc");
        if (mw.toString().length() != 3 + EOF_LENGTH) {
            return failed("expected flush after println(String)"
                    + mw.toString().length() + " " + EOF_LENGTH);
        }

        mw = new MockWriter();
        pw = new PrintWriter(new BufferedWriter(mw), true);
        pw.println((Object) null);
        if (mw.toString().length() != 4 + EOF_LENGTH) {
            return failed("expected flush after println(Object)"
                    + mw.toString().length() + " " + EOF_LENGTH);
        }

        mw = new MockWriter();
        pw = new PrintWriter(new BufferedWriter(mw, 16), true);
        pw.print("qwerty");
        if (mw.toString().length() != 0) {
            return failed("expected print(String) to be buffered");
        }
        pw.flush();
        if (mw.toString().length() != 6) {
            return failed("expected flush() to push data");
        }

        return passed();
    }

    public Result testOutputStreamNoAutoFlush() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Writer w = new PrintWriter(new BufferedOutputStream(baos));
        w.write("xyz".toCharArray());
        try {
            if (baos.size() == 0) {
                return passed();
            }
        } finally {
            w.close();
        }

        return failed("expected data to be buffered");
    }

    //TODO: add test for '\n'
    public Result testOutputStreamAutoFlush() throws IOException {
        final int EOF_LENGTH = System.getProperty("line.separator").length();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter pw;

        baos.reset();
        pw = new PrintWriter(new BufferedOutputStream(baos), true);
        pw.print("xyz".getBytes());
        if (baos.size() != 0) {
            return failed("expected bufferization after write(byte[])");
        }

        baos.reset();
        pw = new PrintWriter(new BufferedOutputStream(baos), true);
        pw.println();
        if (baos.size() != EOF_LENGTH) {
            return failed("expected flush after println() " + baos.size());
        }

        baos.reset();
        pw = new PrintWriter(new BufferedOutputStream(baos), true);
        pw.println(true);
        if (baos.size() != 4 + EOF_LENGTH) {
            return failed("expected flush after println(boolean) "
                    + baos.size());
        }

        baos.reset();
        pw = new PrintWriter(new BufferedOutputStream(baos), true);
        pw.println('c');
        if (baos.size() != 1 + EOF_LENGTH) {
            return failed("expected flush after println(char)" + baos.size());
        }

        baos.reset();
        pw = new PrintWriter(new BufferedOutputStream(baos), true);
        pw.println("abc".toCharArray());
        if (baos.size() != 3 + EOF_LENGTH) {
            return failed("expected flush after println(char[])" + baos.size()
                    + " " + EOF_LENGTH);
        }

        baos.reset();
        pw = new PrintWriter(new BufferedOutputStream(baos), true);
        pw.println((int) 1);
        if (baos.size() != 1 + EOF_LENGTH) {
            return failed("expected flush after println(int)" + baos.size()
                    + " " + EOF_LENGTH);
        }

        baos.reset();
        pw = new PrintWriter(new BufferedOutputStream(baos), true);
        pw.println(1L);
        if (baos.size() != 1 + EOF_LENGTH) {
            return failed("expected flush after println(long)" + baos.size()
                    + " " + EOF_LENGTH);
        }

        baos.reset();
        pw = new PrintWriter(new BufferedOutputStream(baos), true);
        pw.println((float) 1.0);
        if (baos.size() != 3 + EOF_LENGTH) {
            return failed("expected flush after println(float)" + baos.size()
                    + " " + EOF_LENGTH);
        }

        baos.reset();
        pw = new PrintWriter(new BufferedOutputStream(baos), true);
        pw.println((double) 1.0);
        if (baos.size() != 3 + EOF_LENGTH) {
            return failed("expected flush after println(double)" + baos.size()
                    + " " + EOF_LENGTH);
        }

        baos.reset();
        pw = new PrintWriter(new BufferedOutputStream(baos), true);
        pw.println("abc");
        if (baos.size() != 3 + EOF_LENGTH) {
            return failed("expected flush after println(String)" + baos.size()
                    + " " + EOF_LENGTH);
        }

        baos.reset();
        pw = new PrintWriter(new BufferedOutputStream(baos), true);
        pw.println((Object) null);
        if (baos.size() != 4 + EOF_LENGTH) {
            return failed("expected flush after println(Object)" + baos.size()
                    + " " + EOF_LENGTH);
        }

        baos.reset();
        pw = new PrintWriter(new BufferedOutputStream(baos, 16), true);
        pw.print("qwerty");
        if (baos.size() != 0) {
            return failed("expected print(String) to be buffered");
        }
        pw.flush();
        if (baos.size() != 6) {
            return failed("expected flush() to push data");
        }

        return passed();
    }

    public Result testCheckError() throws IOException {
        for (int i = 0; i < 12; ++i) {
            PrintWriter pw = new PrintWriter(new ByteArrayOutputStream());
            if (pw.checkError()) {
                return failed("expected no error after constructor");
            }
            pw.close();
            if (pw.checkError()) {
                return failed("expected no error after close");
            }

            switch (i) {
            case 0:
                pw.print("xyz".getBytes());
                break;
            case 1:
                pw.println();
                break;
            case 2:
                pw.println(true);
                break;
            case 3:
                pw.println('c');
                break;
            case 4:
                pw.println("abc".toCharArray());
                break;
            case 5:
                pw.println((int) 1);
                break;
            case 6:
                pw.println(1L);
                break;
            case 7:
                pw.println((float) 1.0);
                break;
            case 8:
                pw.println((double) 1.0);
                break;
            case 9:
                pw.println("abc");
                break;
            case 10:
                pw.println((Object) null);
                break;
            case 11:
                pw.print("qwerty");
                break;
            }
            if (!pw.checkError()) {
                return failed("expected error after writing to a closed writer");
            }

        }
        return passed();
    }
}

class PrintWriterExposedOut extends PrintWriter {
    public PrintWriterExposedOut(Writer arg0) {
        super(arg0);
    }

    public Writer getOut() {
        return out;
    }
}
