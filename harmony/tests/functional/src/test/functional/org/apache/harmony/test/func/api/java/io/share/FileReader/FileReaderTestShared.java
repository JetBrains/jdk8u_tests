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
 * Created on 17.11.2004
 *
 */
package org.apache.harmony.test.func.api.java.io.share.FileReader;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

import org.apache.harmony.test.func.api.java.io.share.Reader.ReaderTestShared;
import org.apache.harmony.test.func.api.java.io.share.MultiThreadRunner;
import org.apache.harmony.test.func.api.java.io.share.PrepareTestCleanup;
import org.apache.harmony.share.Result;

public class FileReaderTestShared extends ReaderTestShared implements PrepareTestCleanup {
    static File testDir;

    public void setTestDir(File dir) {
        testDir = dir;
    }

    public int prepare(File dir) throws IOException {
        FileWriter fw = new FileWriter(dir.getAbsolutePath() + "/gold.txt");
        fw.write(new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                'k', 'l' });
        fw.close();
        return pass();
    }

    public int cleanup(File dir) throws IOException {
        if (!new File(dir.getAbsolutePath() + "/gold.txt").delete()) {
            return fail("can't delete " + dir.getAbsolutePath() + "/gold.txt");
        }
        return pass();
    }

    protected Reader getTestedReader() throws IOException {
        return new FileReader(testDir + "/gold.txt");
    }

    protected Reader getTestedReader(Object lock) throws IOException {
        throw new UnsupportedOperationException("this kind of constructor is not applicable for FileReader");
    }

    public Result testExceptionAfterClose() throws IOException {
        return super._testExceptionAfterClose();
    }

    public Result testClose() throws IOException {
        return super.testClose();
    }

    public Result testMark() throws IOException {
        return super.testMark();
    }

    public Result testMarkNegative() throws IOException {
        return super.testMarkNegative();
    }

    public Result testMarkSupported() throws IOException {
        return super.testMarkSupported();
    }

    public Result testReadChar() throws IOException {
        return super.testReadChar();
    }

    public Result testReadChars0001() throws IOException {
        return super.testReadChars0001();
    }

    public Result testReadChars0002() throws IOException {
        return super.testReadChars0002();
    }

    public Result testReady() throws IOException {
        Reader r = getTestedReader();

        try {
            MultiThreadRunner.waitAtBarrier();
            if (r.ready()) {
                return passed();
            }

        } finally {
            r.close();
        }
        return failed("expected reader to be ready");
    }

    public Result testReset() throws IOException {
        return super.testReset();
    }

    public Result testSkip() throws IOException {
        return super.testSkip();
    }

    public Result testSkipNegative() throws IOException {
        return super.testSkipNegative();
    }
    
    public Result testFileNotFoundException() throws IOException {
        MultiThreadRunner.waitAtBarrier();
        try {
            new FileReader(testDir.getAbsolutePath() + "/nonexistent.txt");
            return failed("Expected FileNotFoundException");
        } catch (FileNotFoundException e) {
        }
        try {
            new FileReader(testDir.getAbsolutePath());
            return failed("Expected FileNotFoundException");
        } catch (FileNotFoundException e) {
        }
        try {
            new FileReader(new File(testDir.getAbsolutePath()
                    + "/nonexistent.txt"));
            return failed("Expected FileNotFoundException");
        } catch (FileNotFoundException e) {
        }
        try {
            new FileReader(testDir);
            return failed("Expected FileNotFoundException");
        } catch (FileNotFoundException e) {
        }
        return passed();
    }
    
    public Result testFileDescriptor() throws IOException {
        FileInputStream is = new FileInputStream(testDir.getAbsolutePath() + "/gold.txt");
        FileReader r = null;
        MultiThreadRunner.waitAtBarrier();

        try {
            FileDescriptor fd = is.getFD();
            if (!fd.valid()) {
                return failed("expected file descriptor of an open stream to be valid");
            }
            r = new FileReader(fd);
            
            if (!fd.valid()) {
                return failed("expected file descriptor to be valid");
            }

            if(is.read() != 'a') {
                return failed("expected another value");
             }
            
            if (r.read() != 'b' || r.read() != 'c' || r.read() != 'd') {
                return failed("expected another value - 1");
            }

            if (!fd.valid()) {
                return failed("expected file descriptor to be valid");
            }

            
            int c;
            if ((c = is.read()) != -1) {
                return failed("expected another value - 2 : " + c);
            }
            if (r.read() != 'e') {
                return failed("expected another value - 3");
            }
            if ((c = is.read()) != -1) {
                return failed("expected another value - 4 : " + c);
            }
            is.close();
            if (r.read() != 'f') {
                return failed("expected another value - 5");
            }
            if (fd.valid()) {
                return failed("expected file descriptor to be invalid");
            }
        } finally {
            is.close();
            if (r != null) {
                r.close();
            }
        }
        return passed();
    }

}
