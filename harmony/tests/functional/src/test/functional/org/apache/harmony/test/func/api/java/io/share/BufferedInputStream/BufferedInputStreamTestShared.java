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


package org.apache.harmony.test.func.api.java.io.share.BufferedInputStream;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.harmony.test.func.api.java.io.share.FilterInputStream.FilterInputStreamTestShared;
import org.apache.harmony.test.func.api.java.io.share.MultiThreadRunner;
import org.apache.harmony.share.Result;

public class BufferedInputStreamTestShared extends FilterInputStreamTestShared {
    public InputStream getTestedInputStream(InputStream is) {
        return new BufferedInputStream(is, 512);
    }

    public Result testAvailable() throws IOException {
        InputStream is = getTestedInputStream();
        MultiThreadRunner.waitAtBarrier();
        is.read();
        //!!!!!!!!! this is wrong according to Java API but reference JRE
        // behaves this way
        if (is.available() == 511) {
            return passed();
        }
        return failed("unexpected value of available : " + is.available());
    }

    public Result testClose() throws IOException {
        return super.testClose();
    }

    public Result testMark() throws IOException {
        return super.testMark();
    }

    public Result testMarkSupported() throws IOException {
        MultiThreadRunner.waitAtBarrier();
        if (getTestedInputStream().markSupported()) {
            return passed();
        }
        return failed("BufferedInputStream.markSupported should return true");
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

    public Result testReadBytes0004() throws IOException {
        int readLimit = (int) (Math.random() * 10) + 10;
        InputStream is = getTestedInputStream(new LimitedInputStream(readLimit,
                LimitedInputStream.MARK_BY_AVAILABLE));

        MultiThreadRunner.waitAtBarrier();
        int read = is.read(new byte[512]);

        //!!!!!!!!! this is wrong according to Java API but reference JRE
        // behaves this way
        if (read == 512) {
            return passed();
        }

        return failed("wrong number of bytes read : " + read);
    }

    public Result testReadBytes0005() throws IOException {
        int readLimit = (int) (Math.random() * 10) + 10;
        InputStream is = getTestedInputStream(new LimitedInputStream(readLimit,
                LimitedInputStream.MARK_BY_VALUE));

        MultiThreadRunner.waitAtBarrier();
        int read = is.read(new byte[512]);

        if (read == readLimit) {
            return passed();
        }

        return failed("wrong number of bytes read : " + read);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.test.func.api.java.io.InputStream.InputStreamTest#testReset0001()
     */
    public Result testReset0001() throws IOException {
        return super.testReset0001();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.test.func.api.java.io.InputStream.InputStreamTest#testReset0002()
     */
    public Result testReset0002() throws IOException {
        return super.testReset0002();
    }

    public Result testReset0003() throws IOException {
        InputStream is = getTestedInputStream();

        try {
            int skipBytes = (int) (Math.random() * 10) + 10;

            MultiThreadRunner.waitAtBarrier();
            for (int i = 0; i < skipBytes; ++i) {
                is.read();
            }

            is.mark(10);

            for (int i = 0; i < 1024; ++i) {
                is.read();
            }

            is.reset();
        } catch (IOException e) {
            return passed();
        }
        return failed("mark/reset behaved incorrectly");
    }

    public Result testExceptionAfterClose() throws IOException {
        return super._testExceptionAfterClose();
    }

}

class LimitedInputStream extends InputStream {
    int read = 0;

    int limit;

    int mode = MARK_BY_AVAILABLE;

    static final int MARK_BY_AVAILABLE = 0;

    static final int MARK_BY_VALUE = 1;

    int i = 'a' - 1;

    /**
     * @param limit
     *            number of bytes to return before failure
     */
    public LimitedInputStream(int limit, int mode) {
        super();
        this.limit = limit;
        this.mode = mode;
    }

    public int read() throws IOException {
        if (read == limit && mode == MARK_BY_VALUE) {
            return -1;
        }

        read++;
        return ++i;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.io.InputStream#available()
     */
    public int available() throws IOException {
        if (mode == MARK_BY_AVAILABLE) {
            return (limit > read) ? (limit - read) : 0;
        }
        return 1;
    }
}