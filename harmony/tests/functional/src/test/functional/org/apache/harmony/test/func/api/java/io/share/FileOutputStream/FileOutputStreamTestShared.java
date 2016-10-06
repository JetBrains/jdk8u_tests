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
package org.apache.harmony.test.func.api.java.io.share.FileOutputStream;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.harmony.test.func.api.java.io.share.OutputStream.OutputStreamTestShared;
import org.apache.harmony.test.func.api.java.io.share.MultiThreadRunner;
import org.apache.harmony.test.func.api.java.io.share.PrepareTestCleanup;
import org.apache.harmony.test.func.api.java.io.share.Utils;
import org.apache.harmony.share.Result;

public class FileOutputStreamTestShared extends OutputStreamTestShared implements
        PrepareTestCleanup {
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
        for(int i = -1; i <= Utils.THREADS; ++i) {
            File f = new File(dir, (i == -1 ? "main" : "Thread-" + i )+  ".txt");
            f.delete();
        }
        new File(dir, "Main Thread.txt").delete();
        return pass();
    }

    protected OutputStream getTestedOutputStream(int maxSize)
            throws FileNotFoundException {
        return new FileOutputStream(getDefaultOutputFileName());
    }

    protected String getWrittenAsString(OutputStream os) {
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

    public Result testOpenDirectory() throws IOException {
        MultiThreadRunner.waitAtBarrier();
        try {
            new FileOutputStream(testDir);
            return failed("expected FileNotFoundException here");
        } catch (FileNotFoundException e) {
        }

        try {
            new FileOutputStream(testDir, false);
            return failed("expected FileNotFoundException here");
        } catch (FileNotFoundException e) {
        }

        try {
            new FileOutputStream(testDir, true);
            return failed("expected FileNotFoundException here");
        } catch (FileNotFoundException e) {
        }
        return passed();
    }

    public Result testDoubleOpen() throws IOException {
        OutputStream os = getTestedOutputStream(0);
        OutputStream os1 = getTestedOutputStream(0);
        MultiThreadRunner.waitAtBarrier();
        os.write('a');
        os1.write('b');
        os.write('c');
        os1.write('d');
        os1.close();
        os.close();
        if (getWrittenAsString(os).equals("bd")) {
            return passed();
        }
        return failed("expected 'bd' but got '" + getWrittenAsString(os) + "'");
    }

    public Result testFileDescriptor() throws IOException {
        FileOutputStream os = (FileOutputStream) getTestedOutputStream(0);
        FileOutputStream os1 = null;
        try {
            MultiThreadRunner.waitAtBarrier();
            FileDescriptor fd = os.getFD();
            if (!fd.valid()) {
                return failed("expected file descriptor of an open stream to be valid");
            }
            os1 = new FileOutputStream(fd);
            if (os1.getFD() != fd) {
                return failed("expected file descriptors of two streams to be equal");
            }
            
            os.write('a');
            os1.write('b');
            os.write('c');
            os1.write('d');
            os.close();

            try {
                os1.write('e');
                return failed("expected IOException");
            } catch (IOException e) {
            }

            os1.close();

            if (fd.valid()) {
                return failed("expected file descriptor to be invalid");
            }

            if (!getWrittenAsString(os).equals("abcd")) {
                return failed("expected written data to be 'abcd' but got '"
                        + getWrittenAsString(os) + "'");
            }

            if (fd.valid()) {
                return failed("expected file descriptor to be invalid");
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return failed("got exception : " + e.getClass().getName() + " "
                    + e.getMessage());
        } finally {
            os.close();
            if (os1 != null) {
                os1.close();
            }
        }
        return passed();
    }

    public Result testChannelNotNull() throws IOException {
        FileOutputStream os = (FileOutputStream) getTestedOutputStream(0);
        try {
            MultiThreadRunner.waitAtBarrier();
            if (os.getChannel() == null) {
                return failed("expected channel to be not null");
            }
        } finally {
            os.close();
        }
        return passed();
    }

    public Result testReadonly() throws IOException {
        FileOutputStream os = null;
        try {
            MultiThreadRunner.waitAtBarrier();
            os = new FileOutputStream(testDir.getAbsolutePath()
                    + "/readonly.txt");
            return failed("expected FileNotFoundException opening read-only file for writing");
        } catch (FileNotFoundException e) {
        } finally {
            if (os != null) {
                os.close();
            }
        }

        try {
            os = new FileOutputStream(testDir.getAbsolutePath()
                    + "/readonly.txt", true);
            return failed("expected FileNotFoundException opening read-only file for writing");
        } catch (FileNotFoundException e) {
        } finally {
            if (os != null) {
                os.close();
            }
        }

        try {
            os = new FileOutputStream(testDir.getAbsolutePath()
                    + "/readonly.txt", false);
            return failed("expected FileNotFoundException opening read-only file for writing");
        } catch (FileNotFoundException e) {
        } finally {
            if (os != null) {
                os.close();
            }
        }
        return passed();
    }

    public Result testAppend() throws IOException {
        FileOutputStream os = (FileOutputStream) getTestedOutputStream(0);
        try {
            MultiThreadRunner.waitAtBarrier();
            os.write(new byte[] { 'a', 'b' });
            os.close();
            if (!getWrittenAsString(os).equals("ab")) {
                return failed("expected 'ab' here");
            }
            os = new FileOutputStream(getDefaultOutputFileName(), true);
            os.write(new byte[] { 'c', 'd' });
            os.close();
            if (!getWrittenAsString(os).equals("abcd")) {
                return failed("expected 'abcd' here");
            }
            os = new FileOutputStream(getDefaultOutputFileName(), false);
            os.write(new byte[] { 'e', 'f' });
            os.close();
            
            String s = getWrittenAsString(os);
            if (!s.equals("ef")) {
                return failed("expected 'ef' here, got:" + s + ":");
            }
        } finally {
            os.close();
        }

        return passed();
    }
    
    public Result testFinalize() throws IOException {
       File f = new File(getDefaultOutputFileName() + ".testFinalize");
       f.deleteOnExit();
        
       FileOutputStream os = new FileOutputStream(f, true);
       os.write(new byte[] { 'a', 'b' });
       
       if(!Utils.isPathCaseSensitive() && f.length() != 0) {
           return failed("expected data not to be written immediately");
       }
       
       os = null;
       
       //hope that finalize will be called
       for(int i = 0; i < 50; ++i) {
           System.gc(); 
       }
       
       if(f.length() != 2) {
           return failed("expected data to be written in finalize()");
       }
       
       return passed();
    }
}
