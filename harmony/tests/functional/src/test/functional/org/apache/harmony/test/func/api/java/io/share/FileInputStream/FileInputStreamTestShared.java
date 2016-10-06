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
package org.apache.harmony.test.func.api.java.io.share.FileInputStream;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;

import org.apache.harmony.test.func.api.java.io.share.InputStream.InputStreamTestShared;
import org.apache.harmony.test.func.api.java.io.share.MultiThreadRunner;
import org.apache.harmony.test.func.api.java.io.share.PrepareTestCleanup;
import org.apache.harmony.test.func.api.java.io.share.Utils;
import org.apache.harmony.share.Result;

public class FileInputStreamTestShared extends InputStreamTestShared implements
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
        fos.write(new byte[] { 0, 0, 7 });
        fos.close();
        return pass();
    }

    public int cleanup(File dir) throws IOException {
        if (!new File(dir.getAbsolutePath() + "/gold.txt").delete()) {
            return fail("can't delete " + dir.getAbsolutePath() + "/gold.txt");
        }
        if (!new File(dir.getAbsolutePath() + "/gold1.txt").delete()) {
            return fail("can't delete " + dir.getAbsolutePath() + "/gold1.txt");
        }
        return pass();
    }

    public InputStream getTestedInputStream() throws IOException {
        return new FileInputStream(testDir.getAbsolutePath() + "/gold.txt");
    }

    public Result testAvailable() throws IOException {
        InputStream is = getTestedInputStream();
        MultiThreadRunner.waitAtBarrier();
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

    public Result testExceptionAfterClose() throws IOException {
        return super._testExceptionAfterClose();
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

    public Result testReadByte0001() throws IOException {
        InputStream is = new FileInputStream(testDir.getAbsolutePath()
                + "/gold1.txt");

        try {
            MultiThreadRunner.waitAtBarrier();
            int i = is.read();
            if (i == '\u0000') {
                return passed();
            }
        } finally {
            is.close();
        }

        return failed("expected another value");

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

    public Result testReadBytes0004() throws IOException { //TCK - test that
        // this
        //method exists
        InputStream is = new FileInputStream(testDir.getAbsolutePath()
                + "/gold1.txt");

        byte[] b = new byte[5];

        try {
            MultiThreadRunner.waitAtBarrier();
            if (is.read(b) == 3 && b[0] == 0 && b[1] == 0 && b[2] == 7) {
                return passed();
            }
        } finally {
            is.close();
        }

        return failed("expected another value");
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

    public Result testFileNotFoundException() throws IOException {
        try {
            new FileInputStream(testDir.getAbsolutePath() + "/nonexistent.txt");
            return failed("Expected FileNotFoundException");
        } catch (FileNotFoundException e) {
        }
        try {
            new FileInputStream(testDir.getAbsolutePath());
            return failed("Expected FileNotFoundException");
        } catch (FileNotFoundException e) {
        }
        try {
            new FileInputStream(new File(testDir.getAbsolutePath()
                    + "/nonexistent.txt"));
            return failed("Expected FileNotFoundException");
        } catch (FileNotFoundException e) {
        }
        try {
            new FileInputStream(testDir);
            return failed("Expected FileNotFoundException");
        } catch (FileNotFoundException e) {
        }
        return passed();
    }

    public Result testFileDescriptor() throws IOException {
        FileInputStream is = (FileInputStream) getTestedInputStream();
        FileInputStream fis = null;
        try {
            MultiThreadRunner.waitAtBarrier();
            FileDescriptor fd = is.getFD();
            if (!fd.valid()) {
                return failed("expected file descriptor of an open stream to be valid");
            }
            fis = new FileInputStream(fd);
            if (fis.getFD() != fd) {
                return failed("expected file descriptors of two streams to be equal");
            }
            if (fis.read() != 'a' || fis.read() != 'b' || fis.read() != 'c') {
                return failed("expected another value");
            }
            if (is.read() != 'd') {
                return failed("expected another value");
            }
            is.close();
            if (fd.valid()) {
                return failed("expected file descriptor to be invalid");
            }
            fis.close();
        } finally {
            is.close();
            if (fis != null) {
                fis.close();
            }
        }
        return passed();
    }

    public Result testFinalize() throws IOException {
        FileInputStream fis = (FileInputStream) getTestedInputStream();
        FileChannel fc = fis.getChannel();
        if(!fc.isOpen()) {
            return failed("expected channel to be open");
        }
        
        fis = null;
        
        //hope that finalize will be called
        for(int i = 0; i < 50; ++i) {
            System.gc(); 
        }

        if(!fc.isOpen()) {
            return failed("expected channel to be closed");
        }

        return passed();
     }
}
