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
package org.apache.harmony.test.func.api.java.nio.channels.fileChannel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.NonReadableChannelException;
import java.nio.channels.NonWritableChannelException;
import java.nio.channels.OverlappingFileLockException;
import java.util.Random;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;
import org.apache.harmony.test.func.api.java.nio.byteBuffer.ByteBufferTest2;

public class FileChannelTest extends MultiCase {
    public static void main(String[] args) {
        System.exit(new FileChannelTest().test(args));
    }

    String stringWrittenToFile;

    private final String CHARSET = "UTF-16BE";

    String failed1, failed2;

    boolean failed;

    FileLock fl, fl2;

    static Random r = new Random();

    FileChannel fc;

    public Result testLock() {
        String testFileName = "testLock";
        try {
            stringWrittenToFile = getRandomString();
            writeToFile(stringWrittenToFile, testFileName);
        } catch (Exception e) {
            return failed("Throws " + e
                    + ",when we create or write in temporary file");
        }
        try {
            fc = new RandomAccessFile(getNameOfFile(testFileName, "txt"), "rw")
                    .getChannel();
        } catch (Throwable e) {
            return failed("Throws "
                    + e
                    + ",when we create FileChannel with RandomAccessFile(filename,'rw').getChannel()");
        }
        failed1 = failed2 = "";
        failed = false;
        Thread t1 = new Thread() {
            public void run() {
                try {
                    fl = fc.lock();
                } catch (Throwable e) {
                    failed1 = "" + e;
                }
            }
        };
        Thread t2 = new Thread() {
            public void run() {
                try {
                    fc.lock();
                    failed = true;
                } catch (OverlappingFileLockException e) {
                } catch (Throwable e) {
                    failed2 = "" + e;
                }
            }
        };
        try {
            t1.start();
            t1.join();
            t2.start();
            t2.join();
        } catch (Throwable e) {
            return failed("Throws "
                    + e
                    + ", when thread lock FileChannel, that locked by another Thread, but must throw OverlappingFileLockException");
        }
        try {
            fl.release();
            if (fl.isValid())
                return failed("FileLock, after release()");
        } catch (Throwable e) {
            return failed("Throws " + e + ", when we call FileLock.release()");
        }
        if (failed1.length() > 0)
            return failed("Throws "
                    + failed1
                    + ", when thread lock FileChannel, that locked by another Thread, but must throw OverlappingFileLockException");
        if (failed2.length() > 0)
            return failed("Throws "
                    + failed1
                    + ", when thread lock FileChannel, that locked by another Thread, but must throw OverlappingFileLockException");
        if (failed)
            return failed("Don`t throws OverlappingFileLockException, when thread lock FileChannel, that locked by another Thread");
        final int pos = r.nextInt(stringWrittenToFile.length() / 3);
        final int len = r.nextInt(stringWrittenToFile.length() / 2);
        final int len2 = r.nextInt(stringWrittenToFile.length() - pos - len);
        Thread t3 = new Thread() {
            public void run() {
                try {
                    fl = fc.lock(pos, len, true);
                } catch (Throwable e) {
                    failed1 = "" + e;
                }
            }
        };
        Thread t4 = new Thread() {
            public void run() {
                try {
                    fc.lock(pos + len - 1, pos + len + len2, true);
                    failed = true;
                } catch (OverlappingFileLockException e) {
                } catch (Throwable e) {
                    failed2 = "" + e;
                }
            }
        };
        try {
            t3.start();
            t3.join();
            t4.start();
            t4.join();
        } catch (Throwable e) {
            return failed("Throws "
                    + e
                    + ", when another thread is already blocked in this method and is attempting to lock an overlapping region, but must throw OverlappingFileLockException");
        }
        if (failed1.length() > 0)
            return failed("Throws "
                    + failed1
                    + ", when another thread is already blocked in this method and is attempting to lock an overlapping region, but must throw OverlappingFileLockException");
        if (failed2.length() > 0)
            return failed("Throws "
                    + failed1
                    + ", when another thread is already blocked in this method and is attempting to lock an overlapping region, but must throw OverlappingFileLockException");
        if (failed)
            return failed("Don`t throws OverlappingFileLockException, when another thread is already blocked in this method and is attempting to lock an overlapping region");
        Thread t5 = new Thread() {
            public void run() {
                try {
                    fl2 = fc.lock(pos + len, pos + len + len2, true);
                } catch (Throwable e) {
                    failed2 = "" + e;
                }
            }
        };
        try {
            t5.start();            
            t5.join();
        } catch (Throwable e) {
            return failed("Throws "
                    + e
                    + ", when another thread is already blocked in this method and is attempting to lock an not overlapping region");
        }
        if (failed1.length() > 0)
            return failed("Throws "
                    + failed1
                    + ", when another thread is already blocked in this method and is attempting to lock an not overlapping region");
        if (failed2.length() > 0)
            return failed("Throws "
                    + failed1
                    + ", when another thread is already blocked in this method and is attempting to lock an not overlapping region");
        try {
            fc.close();
        } catch (Throwable e) {
            return failed("Can`t close FileChannel");
        }
        if (!new File(getNameOfFile(testFileName, "txt")).delete())
            return failed("Cannot delete temporary file");
        return passed();
    }

    public Result testLockAndTryLock() {
        FileChannel fc, fcr, fcw;
        String testFileName = "testLockAndTryLock";
        try {
            writeToFile(getRandomString(), testFileName);
        } catch (Exception e) {
            return failed("Throws " + e
                    + ",when we create or write in temporary file");
        }
        try {
            fcr = new RandomAccessFile(getNameOfFile(testFileName, "txt"), "r")
                    .getChannel();
        } catch (Throwable e) {
            return failed("Throws "
                    + e
                    + ",when we create FileChannel with RandomAccessFile(filename,'r').getChannel()");
        }
        try {
            fc = new RandomAccessFile(getNameOfFile(testFileName, "txt"), "rw")
                    .getChannel();
        } catch (Throwable e) {
            return failed("Throws "
                    + e
                    + ",when we create FileChannel with RandomAccessFile(filename,'rw').getChannel()");
        }
        try {
            fcw = new FileOutputStream(getNameOfFile(testFileName, "txt"))
                    .getChannel();
        } catch (Throwable e) {
            return failed("Throws "
                    + e
                    + ",when we create FileChannel with FileOutputStream().getChannel()");
        }
        if (File.separatorChar == '\\') {
            if (new File(getNameOfFile(testFileName, "txt")).delete()) {
                return failed("We have don`t close Channel on file, but can delete this file");
            }
        }
        try {
            if (fcr.lock() != null)
                return failed("Can lock, but must throw NonWritableChannelException, because FileChannel wasn't open for write");
        } catch (NonWritableChannelException e) {
        } catch (Throwable e) {
            return failed("Throws "
                    + e
                    + ", but must throw NonWritableChannelException, because FileChannel is closed");
        }
        try {
            if (fcw.lock(0L, Long.MAX_VALUE, true) != null)
                return failed("Can lock, but must throw NonReadableChannelException, because FileChannel wasn't open for write");
        } catch (NonReadableChannelException e) {
        } catch (Throwable e) {
            return failed("Throws "
                    + e
                    + ", but must throw NonReadableChannelException, because FileChannel is closed");
        }
        try {
            if (fc.lock() == null)
                return failed("Can't lock");
        } catch (OverlappingFileLockException e) {
        } catch (Throwable e) {
            return failed("Throws " + e);
        }
        try {
            if (fcw.tryLock() != null)
                return failed("Can lock, but must throw OverlappingFileLockException, because FileChannel is locked");
        } catch (OverlappingFileLockException e) {
        } catch (Throwable e) {
            return failed("Throws "
                    + e
                    + ", but must throw OverlappingFileLockException, because file is locked");
        }
        try {
            ByteBuffer bb2 = ByteBuffer.allocate(ByteBufferTest2
                    .getBufferSize());
            fcr.read(bb2);
            return failed("Must throw IOException, because file is locked");
        } catch (IOException e) {
        } catch (Throwable e) {
            return failed("Throws " + e
                    + ", but must throw IOException, because file is locked");
        }
        try {
            fcw.write(ByteBuffer.wrap(getRandomString().getBytes()));
            return failed("Must throw IOException, because file is locked");
        } catch (IOException e) {
        } catch (Throwable e) {
            return failed("Throws " + e
                    + ", but must throw IOException, because file is locked");
        }
        try {
            fc.close();
        } catch (Throwable e) {
            return failed("Can`t close FileChannel");
        }
        try {
            if (fc.lock() != null)
                return failed("Can lock, but must throw ClosedChannelException, because FileChannel is closed");
        } catch (ClosedChannelException e) {
        } catch (Throwable e) {
            return failed("Throws "
                    + e
                    + ", but must throw ClosedChannelException, because FileChannel is closed");
        }
        try {
            if (fcw.lock() == null)
                return failed("Can't lock");
        } catch (ClosedChannelException e) {
        } catch (Throwable e) {
            return failed("Throws " + e);
        }
        try {
            fcr.close();
            fcw.close();
        } catch (Throwable e) {
            return failed("Can`t close FileChannel");
        }
        if (!new File(getNameOfFile(testFileName, "txt")).delete())
            return failed("Cannot delete temporary file");
        return passed();
    }

    static String getNameOfFile(String pref, String suff) {
        String s = "";
        try {
            File f = File.createTempFile(pref, suff);
            s = f.getParent();
            f.delete();
        } catch (IOException e) {
            log.info("can't create temporarily file " + e.getMessage());
        }
        return s + File.separator + pref + "." + suff;
    }

    public String readFromFile(String fileName) throws FileNotFoundException,
            IOException {
        int len = (int) new File(getNameOfFile(fileName, "txt")).length();
        FileInputStream in = new FileInputStream(getNameOfFile(fileName, "txt"));
        byte[] buff = new byte[len];
        in.read(buff);
        in.close();
        String str = new String(buff, CHARSET);
        return str;
    }

    public void writeToFile(String st, String fileName)
            throws FileNotFoundException, IOException {
        FileOutputStream out = new FileOutputStream(getNameOfFile(fileName,
                "txt"));
        out.write(st.getBytes(CHARSET));
        out.close();
    }

    public String getRandomString() {
        return new String(ByteBufferTest2.getArrayOfRandomByte());
    }

    public Result testRead() {
        String testFileName = "testRead";
        String stringWritedToFile = getRandomString();
        FileChannel fc;
        try {
            writeToFile(stringWritedToFile, testFileName);
        } catch (Throwable e) {
            return failed("Throws " + e
                    + ",when we create or write in temporary file");
        }
        try {
            fc = new FileInputStream(getNameOfFile(testFileName, "txt"))
                    .getChannel();
        } catch (Throwable e) {
            return failed("Throws "
                    + e
                    + ",when we create FileChannel with FileInputStream().getChannel()");
        }
        ByteBuffer bb = ByteBuffer.allocate(stringWritedToFile.length() * 2);
        try {
            fc.read(bb);
        } catch (Throwable e) {
            return failed("Throws " + e
                    + ",when we try FileChannel.read(ByteBuffer)");
        }
        try {
            fc.close();
        } catch (Throwable e) {
            return failed("Throws " + e + ",when we try FileChannel.close()");
        }
        try {
            String st = new String(bb.array(), CHARSET);
            if (!stringWritedToFile.equals(st))
                return failed("FileChannel.read(ByteBuffer) have read wrong information");
        } catch (Throwable e) {
            return failed("Cannot read from temporary file");
        }
        if (!new File(getNameOfFile(testFileName, "txt")).delete())
            return failed("Cannot delete temporary file");
        return passed();
    }

    public Result testWrite() {
        String testFileName = "testWrite";
        String stringWritedToFile = getRandomString();
        FileChannel fc;
        try {
            fc = new FileOutputStream(getNameOfFile(testFileName, "txt"))
                    .getChannel();
        } catch (Throwable e) {
            return failed("Throws "
                    + e
                    + ",when we create FileChannel with FileOutputStream().getChannel()");
        }
        try {
            if (fc.write(ByteBuffer.wrap(stringWritedToFile.getBytes(CHARSET))) != stringWritedToFile
                    .length() * 2)
                return failed("FileChannel.write(ByteBuffer) write not all or more than need information");
            ;
        } catch (Throwable e) {
            return failed("Throws " + e
                    + ",when we try FileChannel.write(ByteBuffer)");
        }
        try {
            fc.close();
        } catch (Throwable e) {
            return failed("Throws " + e + ",when we try FileChannel.close()");
        }
        try {
            if (!readFromFile(testFileName).equals(stringWritedToFile))
                return failed("FileChannel.write(ByteBuffer) have write wrong information");
        } catch (Throwable e) {
            return failed("Cannot read from temporary file");
        }
        if (!new File(getNameOfFile(testFileName, "txt")).delete())
            return failed("Cannot delete temporary file");
        return passed();
    }
}