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
package org.apache.harmony.test.func.api.java.io.share.BufferedReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.harmony.test.func.api.java.io.share.Reader.ReaderTestShared;
import org.apache.harmony.test.func.api.java.io.share.MultiThreadRunner;
import org.apache.harmony.share.Result;

public class BufferedReaderTestShared extends ReaderTestShared {

    protected BufferedReader getBufferedReader(Reader r) {
        return getBufferedReader(r, 512);
    }

    protected BufferedReader getBufferedReader(Reader r, int size) {
        return new BufferedReader(r, size);
    }

    protected Reader getTestedReader() throws IOException {
        return getTestedReader(512);
    }

    protected Reader getTestedReader(Object lock) throws IOException {
        return getTestedReader(lock, 512);
    }

    protected Reader getTestedReader(int size) throws IOException {
        return new BufferedReader(super.getTestedReader(), size);
    }

    protected Reader getTestedReader(Object lock, int size) throws IOException {
        return new BufferedReader(super.getTestedReader(lock), size);
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

    public Result testMarkSupported() throws IOException {
        Reader r = getTestedReader();

        if (r.markSupported()) {
            return passed();
        }
        return failed("BufferedReader should support mark()");
    }

    public Result testNullObjectInConstructor() {
        try {
            getBufferedReader(null);
        } catch (NullPointerException e) {
            return passed();
        }
        return failed("NullPointerException expected");
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
        return super.testReady();
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

    public Result testBufferedRead0001() throws IOException {
        Reader r = getBufferedReader(new LimitedReader(3,
                LimitedReader.MARK_BY_READY));

        char[] buf = new char[5];
        MultiThreadRunner.waitAtBarrier();
        if (r.read(buf) == 3 && buf[0] == 'a' && buf[1] == 'b' && buf[2] == 'c'
                && buf[3] == 0) {
            return passed();
        }

        return failed("wrong values read");
    }

    public Result testBufferedRead0002() throws IOException {
        Reader r = getBufferedReader(new LimitedReader(3,
                LimitedReader.MARK_BY_VALUE));

        char[] buf = new char[5];

        MultiThreadRunner.waitAtBarrier();
        if (r.read(buf) == 3 && buf[0] == 'a' && buf[1] == 'b' && buf[2] == 'c'
                && buf[3] == 0) {
            return passed();
        }

        return failed("wrong values read");
    }

    public Result testBufferedRead0003() throws IOException {
        //first read will return '-1'
        Reader r = getBufferedReader(new LimitedReader(0,
                LimitedReader.MARK_BY_VALUE));
        MultiThreadRunner.waitAtBarrier();
        if (r.read(new char[5]) == -1) {
            return passed();
        }

        return failed("wrong values read");
    }

    public Result testBufferedRead0004() throws IOException {
        LimitedReader lr = new LimitedReader(1024, LimitedReader.MARK_BY_VALUE);

        int bufSize = 256 + (int) (Math.random() * 256);

        Reader r = getBufferedReader(lr, bufSize);

        char[] buf = new char[5];
        MultiThreadRunner.waitAtBarrier();
        if (r.read(buf) == 5 && buf[0] == 'a' && buf[1] == 'b' && buf[2] == 'c'
                && buf[3] == 'd' && buf[4] == 'e' && lr.lastRead == bufSize) {
            return passed();
        }

        return failed("wrong values read");
    }

    public Result testBufferedRead0005() throws IOException {
        LimitedReader lr = new LimitedReader(1024, LimitedReader.MARK_BY_READY);

        int bufSize = 256 + (int) (Math.random() * 256);

        Reader r = getBufferedReader(lr, bufSize);

        MultiThreadRunner.waitAtBarrier();
        
        //it should be ready by now because underlying stream is ready
        if (!r.ready()) {
            return failed("reader should be ready here");
        }

        //try to read more chars than buffer can hold - the read should pass
        // buffer
        if (r.read(new char[700]) != 700) {
            return failed("failed to read requested number of bytes from reader");
        }

        lr.read(new char[2000]); //no data in stream available any more

        if (r.ready()) {
            return failed("reader should not be ready here");
        }
        return passed();
    }

    public Result testBufferedRead0006() throws IOException {
        LimitedReader lr = new LimitedReader(1024, LimitedReader.MARK_BY_READY);

        int bufSize = 256 + (int) (Math.random() * 256);

        Reader r = getBufferedReader(lr, bufSize);

        //it should be ready by now because underlying stream is ready
        if (!r.ready()) {
            return failed("reader should be ready here");
        }

        //try to read less chars than buffer can hold - the rest should go to
        // buffer
        if (r.read(new char[200]) != 200) {
            return failed("failed to read requested number of bytes from reader");
        }

        lr.read(new char[2000]); //no data in stream available any more

        if (!r.ready()) {
            return failed("reader should be ready here");
        }
        return passed();
    }

    public Result testMarkNegative() throws IOException {
        return super.testMarkNegative();
    }

    public Result testReadLine() throws IOException {
        BufferedReader br = getBufferedReader(new LocalStringReader("\n"));
        
        MultiThreadRunner.waitAtBarrier();
        if (!"".equals(br.readLine())) {
            return failed("case 1 failed");
        }

        br = getBufferedReader(new LocalStringReader("\r"));
        if (!"".equals(br.readLine())) {
            return failed("case 2 failed");
        }

        br = getBufferedReader(new LocalStringReader("a\rb\r"));
        if (!("a".equals(br.readLine()) && "b".equals(br.readLine()))) {
            return failed("case 3 failed");
        }

        br = getBufferedReader(new LocalStringReader("a\nb\r"));
        if (!("a".equals(br.readLine()) && "b".equals(br.readLine()))) {
            return failed("case 4 failed");
        }

        br = getBufferedReader(new LocalStringReader("a\r\nb\r\nc\n"));
        if (!("a".equals(br.readLine()) && "b".equals(br.readLine()) && "c"
                .equals(br.readLine()))) {
            return failed("case 5 failed");
        }

        br = getBufferedReader(new LocalStringReader("a\r\nb\r\nc"));
        if (!("a".equals(br.readLine()) && "b".equals(br.readLine()) && "c".equals(br.readLine()))) {
            br = getBufferedReader(new LocalStringReader("z\r\nb\r\nc"));

            return failed("case 6 failed");
        }

        br = getBufferedReader(new LocalStringReader("a\n\rb\n\rc"));
        if (!("a".equals(br.readLine()) && "".equals(br.readLine()) && "b"
                .equals(br.readLine()))) {
            return failed("case 7 failed");
        }

        br = getBufferedReader(new LocalStringReader(""));
        if (br.readLine() != null) {
            return failed("case 8 failed");
        }

        //this test fails in the reference VM
        /*
        br = getBufferedReader(new NeverReadyStringReader("abcd\n"));
        if (br.readLine() != null) {
            return failed("case 9 failed (controversal case about ready() - fails in the reference JVM");
        }
        */
        
        return passed();
    }
}

class LimitedReader extends Reader {
    int totalRead = 0;

    int limit;

    int mode = MARK_BY_READY;

    static final int MARK_BY_READY = 0;

    static final int MARK_BY_VALUE = 1;

    char c = 'a' - 1;

    int lastRead = 0;

    /**
     * @param limit
     *            number of bytes to return before failure
     */
    public LimitedReader(int limit, int mode) {
        super();
        this.limit = limit;
        this.mode = mode;
    }

    public void close() throws IOException {
    }

    public int read(char[] dest, int start, int length) throws IOException {
        if (totalRead >= limit && mode == MARK_BY_VALUE) {
            return -1;
        }

        lastRead = 0;

        for (int i = start; i < start + length && totalRead < limit; ++i, ++lastRead, ++totalRead) {
            dest[i] = ++c;
        }
        return lastRead;
    }

    public boolean ready() throws IOException {
        if (mode == MARK_BY_READY) {
            return (limit > totalRead);
        }
        return true;
    }

    public int getLastRead() {
        return lastRead;
    }
}

class LocalStringReader extends Reader {
    private String src;

    int totalRead = 0;

    LocalStringReader(String src) {
        this.src = src;
    }

    public void close() throws IOException {
    }

    public int read(char[] arg0, int arg1, int arg2) throws IOException {
        if(!ready()) {
            return -1; 
         }
        
        int read = 0;
        for (int i = 0; i < arg2 && totalRead < src.length(); ++i, ++totalRead, ++read) {
            arg0[arg1 + i] = src.charAt(i);
        }

        return read;
    }

    public boolean ready() throws IOException {
        return (src.length() > totalRead);
    }
}

class NeverReadyStringReader extends Reader {
    private String src;

    NeverReadyStringReader(String src) {
        this.src = src;
    }

    public void close() throws IOException {
    }

    
    //returns stored string in cycle
    public int read(char[] arg0, int arg1, int arg2) throws IOException {
        int read = 0;
        for (int i = 0; i < arg2 && read < src.length(); ++i, ++read) {
            arg0[arg1 + i] = src.charAt(i);
        }
        return read;
    }

    public boolean ready() throws IOException {
        return false;
    }
}