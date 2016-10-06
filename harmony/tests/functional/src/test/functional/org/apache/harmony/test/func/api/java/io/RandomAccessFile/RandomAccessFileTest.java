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
package org.apache.harmony.test.func.api.java.io.RandomAccessFile;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.harmony.test.func.api.java.io.share.IOMultiCase;
import org.apache.harmony.test.func.api.java.io.share.MultiThreadRunner;
import org.apache.harmony.test.func.api.java.io.share.PrepareTestCleanup;
import org.apache.harmony.test.func.api.java.io.share.PrepareTestCleanupRunner;
import org.apache.harmony.test.func.api.java.io.share.Utils;
import org.apache.harmony.share.Result;

/**
 *  
 */
//TODO: add tests for uncovered methods
public final class RandomAccessFileTest extends IOMultiCase implements
        PrepareTestCleanup {
    static File testDir;

    public void setTestDir(File dir) {
        testDir = dir;
    }

    public static void main(String[] args) {
        try {
            System.exit(PrepareTestCleanupRunner.run(args,
                    new RandomAccessFileTest()));
        } catch (IOException e) {
            System.err.println("Got exception - " + e.getMessage());
        }
    }

    public int prepare(File dir) throws IOException {
        FileWriter fw = new FileWriter(new File(dir.getAbsolutePath()
                + "/4chars.txt"));
        fw.write(new char[] { 0, 1, 2, 3 });
        fw.close();

        FileOutputStream fos = new FileOutputStream(new File(dir, "gold.txt"));
        fos.write("abcdefghijklmnop".getBytes());
        fos.close();

        return pass();
    }
    
    public String getTestFilename() {
        return "test" + Thread.currentThread().getName() + ".txt";
    }

    public int cleanup(File dir) throws IOException {
        for (int i = 0; i <= Utils.THREADS; ++i) {
            if(i == 0) {
                new File(dir, "testmain.txt").delete();
                new File(dir, "testMain Thread.txt").delete();
            }
            File f = new File(dir, "testThread-" + i + ".txt");
            f.delete();
        }

        if (!new File(dir, "4chars.txt").delete()) {
            return fail("can't delete " + dir.getAbsolutePath() + "/4chars.txt");
        }

        if (!new File(dir, "gold.txt").delete()) {
            return fail("can't delete " + dir.getAbsolutePath() + "/gold.txt");
        }

        return pass();
    }

    public RandomAccessFile getRandomAccessFile() throws IOException {
        return getRandomAccessFile("rw");
    }

    public RandomAccessFile getRandomAccessFile(String mode) throws IOException {
        return new RandomAccessFile(new File(testDir, getTestFilename()), mode);
    }

    public Result testClose() throws IOException {
        RandomAccessFile raf = getRandomAccessFile();
        raf.close();
        return passed();
    }

    public Result testModes() throws IOException {
        String[] modes = new String[] { "r", "rw", "rws", "rwd", "w", "wr",
                "RW", "R", "s", "swd", "", "\u0000" };
        RandomAccessFile raf = null;
        
        MultiThreadRunner.waitAtBarrier();
        
        try {
            for (int i = 0; i < modes.length; ++i) {
                if (i < 4) {
                    raf = getRandomAccessFile(modes[i]);
                    raf.close();
                } else {
                    try {
                        getRandomAccessFile(modes[i]);
                        return failed("failed at mode : " + modes[i]);
                    } catch (IllegalArgumentException e) {
                    }
                }
            }
        } finally {
            raf.close();
        }
        return passed();
    }

    public Result testFileNotFoundException() throws IOException {
        try {
            new RandomAccessFile(testDir, "rw");
            return failed("expected FileNotFoundException here");
        } catch (FileNotFoundException e) {
        }

        try {
            new RandomAccessFile(new File(testDir,
                    "this\nis\ris\na\0nonexistent\tfile\n"), "r");
            return failed("expected FileNotFoundException here");
        } catch (FileNotFoundException e) {
        }
        return passed();
    }

    public Result testSeek() throws IOException {
        new File(testDir, getTestFilename()).delete();

        MultiThreadRunner.waitAtBarrier();
        RandomAccessFile raf = getRandomAccessFile();
        raf.seek(1000000);
        raf.close();

        raf = getRandomAccessFile();
        if (raf.length() != 0) {
            return failed("expected file length to be 0 here");
        }

        raf.seek(1000000);
        raf.writeBoolean(false);
        raf.close();

        raf = getRandomAccessFile();
        if (raf.length() != 1000001) {
            return failed("expected file length to be 1000001 here");
        }
        raf.close();

        raf = getRandomAccessFile();
        try {
            raf.seek(-1);
            return failed("expected IOException");
        } catch (IOException e) {
        } finally {
            raf.close();
        }

        try {
            raf = getRandomAccessFile();
            raf.seek(1);
            raf.writeByte('a');
            raf.seek(1);
            raf.writeByte('b');
            raf.seek(0);

            if (raf.readByte() != 0 || raf.readByte() != 'b'
                    || raf.readByte() != 0) {
                return failed("expected other values");
            }
        } finally {
            raf.close();
        }

        raf = getRandomAccessFile();
        raf.close();
        try {
            raf.seek(5);
            return failed("expected IOException");
        } catch (IOException e) {
        }

        raf = getRandomAccessFile();
        try {
            raf.seek(303);
            raf.writeByte('x');
            raf.seek(300);
            if (raf.readByte() != 0 || raf.readByte() != 0
                    || raf.readByte() != 0 || raf.readByte() != 'x') {
                return failed("seek & write failed");
            }
        } finally {
            raf.close();
        }

        return passed();
    }

    public Result testChannel() throws IOException {
        new File(testDir, getTestFilename()).delete();

        RandomAccessFile raf = getRandomAccessFile();
        
        MultiThreadRunner.waitAtBarrier();
        
        try {
            FileChannel fc = raf.getChannel();
            if (fc == null) {
                return failed("expected channel to be not null");
            }
            if (fc.position() != 0) {
                return failed("expected channel position to be 0");
            }
            raf.seek(7);
            if (fc.position() != 7) {
                return failed("expected channel position to be 7");
            }
            fc.position(3);
            raf.writeByte('a');
            fc.position(0);
            if (raf.length() != 4 || raf.readByte() != 0 || raf.readByte() != 0
                    || raf.readByte() != 0 || raf.readByte() != 'a') {
                return failed("setting channel position failed");
            }

            fc.truncate(0);
            if (raf.length() != 0) {
                return failed("truncate failed - 1");
            }
            if (new File(testDir, getTestFilename()).length() != 0) {
                return failed("truncate failed - 2");
            }
        } finally {
            raf.close();
        }

        return passed();
    }

    public Result testLength() throws IOException {
        new File(testDir, getTestFilename()).delete();

        
        
        RandomAccessFile raf = getRandomAccessFile();
        
        MultiThreadRunner.waitAtBarrier();
        
        
        try {
            if (raf.length() != 0) {
                return failed("expected length of newly created file to be 0");
            }
            raf.close();
            raf = new RandomAccessFile(new File(testDir, "4chars.txt"), "r");
            if (raf.length() != 4) {
                return failed("expected length to be 4");
            }
            raf.close();
            try {
                raf.length();
                return failed("expected IOException");
            } catch (IOException e) {
            }
        } finally {
            raf.close();
        }

        return passed();
    }

    public Result testReadByte() throws IOException {
        RandomAccessFile raf = new RandomAccessFile(new File(testDir,
                "gold.txt"), "r");

        MultiThreadRunner.waitAtBarrier();
        
        try {
            int i = raf.read();
            if (i == 'a') {
                return passed();
            }
        } finally {
            raf.close();
        }

        return failed("expected another value");
    }

    public Result testReadBytes() throws IOException {
        RandomAccessFile raf = new RandomAccessFile(new File(testDir,
                "gold.txt"), "r");

        byte[] b = new byte[3];

        try {
            MultiThreadRunner.waitAtBarrier();
            
            int i = raf.read(b);

            if (i == 3 && b[0] == 'a' && b[1] == 'b' && b[2] == 'c') {
                return passed();
            }
        } finally {
            raf.close();
        }

        return failed("expected another value");
    }

    public Result testReadBytesSlice() throws IOException {
        RandomAccessFile raf = new RandomAccessFile(new File(testDir,
                "gold.txt"), "r");
        byte[] b = new byte[3];

        try {
            MultiThreadRunner.waitAtBarrier();
            
            int i = raf.read(b, 1, 2);

            if (i == 2 && b[0] == 0 && b[1] == 'a' && b[2] == 'b') {
                return passed();
            }
        } finally {
            raf.close();
        }

        return failed("expected another value");
    }

    public Result testReadBytesNull() throws IOException {
        RandomAccessFile raf = new RandomAccessFile(new File(testDir,
                "gold.txt"), "r");
        
        MultiThreadRunner.waitAtBarrier();
        
        try {
            raf.read(null);
        } catch (NullPointerException e) {
            return passed();
        } finally {
            raf.close();
        }

        return failed("expected NullPointerException");
    }

    public Result testReadBytes0001() throws IOException {
        RandomAccessFile raf = new RandomAccessFile(new File(testDir,
                "gold.txt"), "r");
        
        MultiThreadRunner.waitAtBarrier();
        
        try {
            raf.read(new byte[1], -1, 1);
        } catch (IndexOutOfBoundsException e) {
            return passed();
        } finally {
            raf.close();
        }

        return failed("expected IndexOutOfBoundsException");
    }

    public Result testReadBytes0002() throws IOException {
        RandomAccessFile raf = new RandomAccessFile(new File(testDir,
                "gold.txt"), "r");

        MultiThreadRunner.waitAtBarrier();
        
        try {
            raf.read(new byte[1], 0, -1);
        } catch (IndexOutOfBoundsException e) {
            return passed();
        } finally {
            raf.close();
        }

        return failed("expected IndexOutOfBoundsException");
    }

    public Result testReadBytes0003() throws IOException {
        RandomAccessFile raf = new RandomAccessFile(new File(testDir,
                "gold.txt"), "r");
        
        MultiThreadRunner.waitAtBarrier();
        
        try {
            raf.read(new byte[1], 0, 5);
        } catch (IndexOutOfBoundsException e) {
            return passed();
        } finally {
            raf.close();
        }

        return failed("expected IndexOutOfBoundsException");
    }

    public Result testReadFully() throws IOException {
        RandomAccessFile raf = new RandomAccessFile(new File(testDir,
                "gold.txt"), "r");

        byte[] b = new byte[3];
        
        MultiThreadRunner.waitAtBarrier();

        raf.readFully(b);

        try {
            if (b[0] != 'a' || b[1] != 'b' || b[2] != 'c') {
                return failed("expected other values");
            }
            raf.readFully(new byte[100]);
            return failed("expected EOFException");
        } catch (EOFException e) {
        } finally {
            raf.close();
        }

        return passed();
    }

    public Result testReadFullySlice() throws IOException {
        RandomAccessFile raf = new RandomAccessFile(new File(testDir,
                "gold.txt"), "r");
        byte[] b = new byte[3];

        try {
            MultiThreadRunner.waitAtBarrier();

            raf.readFully(b, 1, 2);

            if (b[0] == 0 && b[1] == 'a' && b[2] == 'b') {
                return passed();
            }
        } finally {
            raf.close();
        }

        return failed("expected another value");
    }

    public Result testReadFullyNull() throws IOException {
        RandomAccessFile raf = new RandomAccessFile(new File(testDir,
                "gold.txt"), "r");
        try {
            raf.readFully(null);
        } catch (NullPointerException e) {
            return passed();
        } finally {
            raf.close();
        }

        return failed("expected NullPointerException");
    }

    public Result testReadFullyNull0001() throws IOException {
        RandomAccessFile raf = new RandomAccessFile(new File(testDir,
                "gold.txt"), "r");
        try {
            raf.readFully(null, 0, 1);
        } catch (NullPointerException e) {
            return passed();
        } finally {
            raf.close();
        }

        return failed("expected NullPointerException");
    }

    public Result testReadFully0001() throws IOException {
        RandomAccessFile raf = new RandomAccessFile(new File(testDir,
                "gold.txt"), "r");
        try {
            raf.readFully(new byte[1], -1, 1);
        } catch (IndexOutOfBoundsException e) {
            return passed();
        } finally {
            raf.close();
        }

        return failed("expected IndexOutOfBoundsException");
    }

    public Result testReadFully0002() throws IOException {
        RandomAccessFile raf = new RandomAccessFile(new File(testDir,
                "gold.txt"), "r");

        try {
            raf.readFully(new byte[1], 0, -1);
        } catch (IndexOutOfBoundsException e) {
            return passed();
        } finally {
            raf.close();
        }

        return failed("expected IndexOutOfBoundsException");
    }

    public Result testReadFully0003() throws IOException {
        RandomAccessFile raf = new RandomAccessFile(new File(testDir,
                "gold.txt"), "r");
        try {
            raf.readFully(new byte[1], 0, 5);
        } catch (IndexOutOfBoundsException e) {
            return passed();
        } finally {
            raf.close();
        }

        return failed("expected IndexOutOfBoundsException");
    }

    public Result testReadFully0004() throws IOException {
        RandomAccessFile raf = new RandomAccessFile(new File(testDir,
                "gold.txt"), "r");

        try {
            raf.close();
            raf.readFully(new byte[2], 0, 1);
        } catch (IOException e) {
            return passed();
        }

        return failed("expected IndexOutOfBoundsException");
    }

    public Result testWrite() throws IOException {
        new File(testDir, getTestFilename()).delete();

        RandomAccessFile raf = new RandomAccessFile(new File(testDir,
                getTestFilename()), "rw");

        MultiThreadRunner.waitAtBarrier();

        raf.write("abc".getBytes());
        raf.close();

        if (new File(testDir, getTestFilename()).length() != 3) {
            return failed("expected file length to be 3");
        }

        FileInputStream fis = new FileInputStream(new File(testDir, getTestFilename()));
        try {
            if (fis.read() != 'a' || fis.read() != 'b' || fis.read() != 'c') {
                return failed("wrong values returned");
            }
        } finally {
            fis.close();
        }

        raf = new RandomAccessFile(new File(testDir, getTestFilename()), "r");
        try {
            raf.write(new byte[] { 'a' });
            return failed("expected IOException");
        } catch (IOException e) {
        } finally {
            raf.close();
        }

        return passed();
    }

    public Result testWriteExceptions() throws IOException {
        new File(testDir, getTestFilename()).delete();

        RandomAccessFile raf = new RandomAccessFile(new File(testDir,
                getTestFilename()), "rw");

        try {
            try {
                raf.write(null);
                return failed("expected NPE");
            } catch (NullPointerException e) {
            }

            try {
                raf.write(null, 0, 1);
                return failed("expected NPE");
            } catch (NullPointerException e) {
            }

            try {
                raf.write(new byte[1], -1, 1);
                return failed("expected IndexOutOfBoundsException");
            } catch (IndexOutOfBoundsException e) {
            }

            try {
                raf.write(new byte[1], 0, -1);
                return failed("expected IndexOutOfBoundsException");
            } catch (IndexOutOfBoundsException e) {
            }

            try {
                raf.write(new byte[1], 0, 5);
                return failed("expected IndexOutOfBoundsException");
            } catch (IndexOutOfBoundsException e) {
            }

            try {
                raf.close();
                raf.write(new byte[2]);
                return failed("expected IOException");
            } catch (IOException e) {
            }
        } finally {
            raf.close();
        }

        return passed();
    }

    public Result testWriteByte() throws IOException {
        new File(testDir, getTestFilename()).delete();

        RandomAccessFile raf = new RandomAccessFile(new File(testDir,
                getTestFilename()), "rw");

        MultiThreadRunner.waitAtBarrier();

        
        try {
            raf.write(0xf0 | 'a');
            raf.write(0xf0 | 'b');
            raf.write(0xf0 | 'c');
        } finally {
            raf.close();
        }

        if (new File(testDir, getTestFilename()).length() != 3) {
            return failed("expected file length to be 3");
        }

        FileInputStream fis = new FileInputStream(new File(testDir, getTestFilename()));
        try {
            if (fis.read() != (0xf0 | 'a') || fis.read() != (0xf0 | 'b')
                    || fis.read() != (0xf0 | 'c')) {
                return failed("wrong values returned");
            }
        } finally {
            fis.close();
        }

        return passed();
    }

    public Result testWriteInt() throws IOException {
        new File(testDir, getTestFilename()).delete();

        RandomAccessFile raf = new RandomAccessFile(new File(testDir,
                getTestFilename()), "rw");

        try {
            raf.writeInt(0xf0 | 'a');
            raf.writeInt(0xf0 | 'b');
            raf.writeInt(0xf0 | 'c');
        } finally {
            raf.close();
        }

        if (new File(testDir, getTestFilename()).length() != 12) {
            return failed("expected file length to be 12");
        }

        FileInputStream fis = new FileInputStream(new File(testDir, getTestFilename()));
        try {
            if (fis.read() != 0 || fis.read() != 0 || fis.read() != 0
                    || fis.read() != (0xf0 | 'a') || fis.read() != 0
                    || fis.read() != 0 || fis.read() != 0
                    || fis.read() != (0xf0 | 'b') || fis.read() != 0
                    || fis.read() != 0 || fis.read() != 0
                    || fis.read() != (0xf0 | 'c')) {
                return failed("wrong values returned");
            }
        } finally {
            fis.close();
        }

        return passed();
    }

    public Result testWriteUTF() throws IOException {
        RandomAccessFile raf = null;
        FileInputStream fis = null;
        try {
            
            Map testMap = new HashMap();
            testMap.put("", new byte[] { 0, 0 });
            testMap.put("\u0000", new byte[] { 0, 2, -64, -128 });
            testMap.put("\u007f", new byte[] { 0, 1, (byte) 0x7f });
            testMap.put(new String(new char[] { 1513 }), new byte[] { 0, 2,
                    (byte) 0xd7, (byte) 0xa9 });
            testMap.put(new String(new char[] { 1513, 64463 }), new byte[] { 0,
                    5, (byte) 0xd7, (byte) 0xa9, (byte) 0xef, (byte) 0xaf,
                    (byte) 0x8f });
            testMap.put(new String(new char[] { 64463 }), new byte[] { 0, 3,
                    (byte) 0xef, (byte) 0xaf, (byte) 0x8f });

            MultiThreadRunner.waitAtBarrier();
            
            for (Iterator iter = testMap.entrySet().iterator(); iter.hasNext();) {
                Map.Entry entry = (Entry) iter.next();
                raf = getRandomAccessFile();
                raf.setLength(0);
                raf.writeUTF((String) entry.getKey());
                byte[] barr = (byte[]) entry.getValue();
                if (raf.length() != barr.length) {
                    return failed("expected length to be " + barr.length);
                }
                raf.close();
                fis = new FileInputStream(new File(testDir, getTestFilename()));

                for (int i = 0; i < barr.length; ++i) {
                    byte b;
                    if ((b = (byte) fis.read()) != barr[i]) {
                        return failed("reading failed - expected  " + barr[i]
                                + " , got " + b);
                    }
                }
                fis.close();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            raf.close();
            if(fis != null) {
                fis.close();
            }
        }
        return passed();
    }
}
