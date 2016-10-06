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
package org.apache.harmony.test.func.api.java.io.share.FileWriter;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.harmony.test.func.api.java.io.share.Writer.WriterTestShared;
import org.apache.harmony.test.func.api.java.io.share.MultiThreadRunner;
import org.apache.harmony.test.func.api.java.io.share.PrepareTestCleanup;
import org.apache.harmony.test.func.api.java.io.share.Utils;
import org.apache.harmony.share.Result;

public class FileWriterTestShared extends WriterTestShared implements PrepareTestCleanup {
    static File testDir;

    public void setTestDir(File dir) {
        testDir = dir;
    }

    public int prepare(File dir) throws IOException {
        File f = new File(dir.getAbsolutePath() + "/readonly.txt");
        f.createNewFile();
        f.setReadOnly();
        return pass();
    }

    public int cleanup(File dir) throws IOException {
        if (!new File(dir.getAbsolutePath() + "/readonly.txt").delete()) {
            return fail("can't delete " + dir.getAbsolutePath()
                    + "/readonly.txt");
        }
        new File(dir, "Main Thread.txt").delete();
        for (int i = -1; i <= Utils.THREADS; ++i) {
            File f = new File(dir, (i == -1 ? "main" : "Thread-" + i) + ".txt");
            f.delete();
        }
        return pass();
    }

    protected Writer getTestedWriter() throws IOException {
        return new FileWriter(getDefaultOutputFileName());
    }

    protected Writer getTestedWriter(Object lock) {
        throw new UnsupportedOperationException(
                "This test is not applicable for FileWriter");
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

    private static String getDefaultOutputFileName() {
        return testDir.getAbsolutePath() + "/" + Thread.currentThread().getName()
        + ".txt";
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

    public Result testOpenDirectory() throws IOException {
        try {
            new FileWriter(testDir);
            return failed("expected FileNotFoundException here");
        } catch (FileNotFoundException e) {
        }

        try {
            new FileWriter(testDir, false);
            return failed("expected FileNotFoundException here");
        } catch (FileNotFoundException e) {
        }

        try {
            new FileWriter(testDir, true);
            return failed("expected FileNotFoundException here");
        } catch (FileNotFoundException e) {
        }
        return passed();
    }

    public Result testDoubleOpen() throws IOException {
        Writer w = getTestedWriter();
        Writer w1 = getTestedWriter();
        MultiThreadRunner.waitAtBarrier();

        w.write('a');
        w1.write('b');
        w.write('c');
        w1.write('d');
        w.close();
        w1.close();
        if (getWriterAsString(w).equals("bd")) {
            return passed();
        }
        return failed("expected 'bd' but got '" + getWriterAsString(w) + "'");
    }

    public Result testFileDescriptor() throws IOException {
        FileOutputStream os = new FileOutputStream(getDefaultOutputFileName());
        FileWriter w = null;
        MultiThreadRunner.waitAtBarrier();

        try {
            FileDescriptor fd = os.getFD();
            if (!fd.valid()) {
                return failed("expected file descriptor of an open stream to be valid");
            }

            w = new FileWriter(fd);
            w.write('x');
            os.write('a');
            w.write('b');
            os.write('c');
            w.write('d');
            os.close();

            w.write('e');

            try {
                w.close();
                return failed("expected IOException");
            } catch (IOException e) {
            }

            if (fd.valid()) {
                return failed("expected file descriptor to be invalid");
            }

            if (!getWriterAsString(w).equals("ac")) {
                return failed("expected written data to be 'ac' but got '"
                        + getWriterAsString(w) + "'");
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return failed("got exception : " + e.getClass().getName() + " "
                    + e.getMessage());
        } finally {
            os.close();
        }
        return passed();
    }

    public Result testReadonly() throws IOException {
        FileWriter w = null;
        MultiThreadRunner.waitAtBarrier();

        try {
            w = new FileWriter(testDir.getAbsolutePath() + "/readonly.txt");
            return failed("expected FileNotFoundException opening read-only file for writing");
        } catch (FileNotFoundException e) {
        } finally {
            if (w != null) {
                w.close();
            }
        }

        try {
            w = new FileWriter(testDir.getAbsolutePath() + "/readonly.txt",
                    true);
            return failed("expected FileNotFoundException opening read-only file for writing");
        } catch (FileNotFoundException e) {
        } finally {
            if (w != null) {
                w.close();
            }
        }

        try {
            w = new FileWriter(testDir.getAbsolutePath() + "/readonly.txt",
                    false);
            return failed("expected FileNotFoundException opening read-only file for writing");
        } catch (FileNotFoundException e) {
        } finally {
            if (w != null) {
                w.close();
            }
        }
        return passed();
    }

    public Result testAppend() throws IOException {
        FileWriter w = (FileWriter) getTestedWriter();
        MultiThreadRunner.waitAtBarrier();

        try {
            w.write(new char[] { 'a', 'b' });
            w.close();
            if (!getWriterAsString(w).equals("ab")) {
                return failed("expected 'ab' here");
            }
            w = new FileWriter(getDefaultOutputFileName(), true);
            w.write(new char[] { 'c', 'd' });
            w.close();
            if (!getWriterAsString(w).equals("abcd")) {
                return failed("expected 'abcd' here");
            }
            w = new FileWriter(getDefaultOutputFileName(), false);
            w.write(new char[] { 'e', 'f' });
            w.close();
            if (!getWriterAsString(w).equals("ef")) {
                return failed("expected 'ef' here");
            }
        } finally {
            w.close();
        }

        return passed();
    }
}
