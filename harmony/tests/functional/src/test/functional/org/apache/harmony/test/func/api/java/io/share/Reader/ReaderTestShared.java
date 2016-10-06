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
package org.apache.harmony.test.func.api.java.io.share.Reader;

import java.io.IOException;
import java.io.Reader;

import org.apache.harmony.test.func.api.java.io.share.IOMultiCase;
import org.apache.harmony.test.func.api.java.io.share.MultiThreadRunner;
import org.apache.harmony.share.Result;

class ReaderImpl extends Reader {
    char c = 'a' - 1;

    public ReaderImpl() {
        super();
    }

    /**
     * @param lock
     *            see reader specification
     */
    public ReaderImpl(Object lock) {
        super(lock);
    }

    public void close() throws IOException {
    }

    public int read(char[] dest, int start, int length) throws IOException {
        //        System.err.println("called read(char[], " + start + "," + length +
        // ")");

        for (int i = start; i < start + length; ++i) {
            dest[i] = ++c;
            //          System.err.println("set " + i + "-th element to " + (c));
        }
        return length;
    }

}

public class ReaderTestShared extends IOMultiCase {
    protected Reader getTestedReader() throws IOException {
        return new ReaderImpl();
    }

    protected Reader getTestedReader(Object lock) throws IOException {
        return new ReaderImpl(lock);
    }

    public Result testNullObjectInConstructor() throws IOException {
        try {
            getTestedReader(null);
        } catch (NullPointerException e) {
            return passed();
        }
        return failed("expected NPE");
    }

    public Result testReadChar() throws IOException {
        Reader r = getTestedReader();

        try {
            MultiThreadRunner.waitAtBarrier();

            if (r.read() == 'a') {
                return passed();
            }
        } finally {
            r.close();
        }

        return failed("read() returned wrong value");
    }

    public Result testReadChars0001() throws IOException {
        Reader r = getTestedReader();

        char[] arr = new char[3];

        try {
            MultiThreadRunner.waitAtBarrier();
            if (r.read(arr) == 3 && arr[0] == 'a' && arr[1] == 'b'
                    && arr[2] == 'c') {
                return passed();
            }
        } finally {
            r.close();
        }

        return failed("read() returned wrong value " + arr[0]);
    }

    public Result testReadChars0002() throws IOException {
        Reader r = getTestedReader();

        char[] arr = new char[5];

        try {
            MultiThreadRunner.waitAtBarrier();

            if (r.read(arr, 1, 3) == 3 && arr[0] == 0 && arr[1] == 'a'
                    && arr[2] == 'b' && arr[3] == 'c' && arr[4] == 0) {
                return passed();
            }
        } finally {
            r.close();
        }

        return failed("read() returned wrong value");
    }

    public Result testSkipNegative() throws IOException {
        Reader r = getTestedReader();

        try {
            r.skip(-5);
        } catch (IllegalArgumentException e) {
            return passed();
        } finally {
            r.close();
        }
        return failed("expected IllegalArgumentException");
    }

    public Result testSkip() throws IOException {
        Reader r = getTestedReader();

        try {
            MultiThreadRunner.waitAtBarrier();
            r.skip(1);
            if (r.read() == 'b') {
                return passed();
            }
        } finally {
            r.close();
        }
        return failed("wrong value read from the reader after skip()");
    }

    public Result testReady() throws IOException {
        Reader r = getTestedReader();

        try {
            MultiThreadRunner.waitAtBarrier();
            if (!r.ready()) {
                return passed();
            }

        } finally {
            r.close();
        }
        return failed("expected reader to be not ready");
    }

    public Result testMarkSupported() throws IOException {
        Reader r = getTestedReader();

        try {
            MultiThreadRunner.waitAtBarrier();
            if (!r.markSupported()) {
                return passed();
            }
        } finally {
            r.close();
        }
        return failed("default reader shouldn't support mark()");
    }

    public Result testMark() throws IOException {
        Reader r = getTestedReader();
        try {
            MultiThreadRunner.waitAtBarrier();
            if (r.markSupported()) {
                getTestedReader().mark(0);
                return passed();
            }
            try {
                getTestedReader().mark(0);
            } catch (IOException e) {
                return passed();
            }
        } finally {
            r.close();
        }
        return failed("IOException expected");
    }

    public Result testMarkNegative() throws IOException {
        Reader r = getTestedReader();

        try {
            if (!r.markSupported()) {
                return passed("this test is for only readers with markSupported");
            }

            try {
                r.mark(-5);
            } catch (IllegalArgumentException e) {
                return passed();
            }
        } finally {
            r.close();
        }

        return failed("IllegalArgumentException expected");
    }

    public Result testReset() throws IOException {
        Reader r = getTestedReader();
        try {
            if (!r.markSupported()) {
                return passed("this test is for only readers with markSupported");
            }

            int i1, i2;

            try {
                int skipChars = (int) (Math.random() * 10) + 10;

                MultiThreadRunner.waitAtBarrier();

                for (int i = 0; i < skipChars; ++i) {
                    r.read();
                }

                r.mark(10);

                i1 = r.read();

                for (int i = 0; i < 9; ++i) {
                    r.read();
                }

                r.reset();

                if (r.read() == i1) {
                    return passed();
                }
            } catch (IOException e) {
                return failed("got ioexception");
            }
        } finally {
            r.close();
        }
        return failed("mark/reset behaved incorrectly ");
    }

    //this test method is for real readers only - shouldn't be called for
    // Reader itself
    public Result _testExceptionAfterClose() throws IOException {
        Reader r = getTestedReader();

        r.close();

        try {
        try {
            r.read();
            return failed("expected IOException in read() after close()");
        } catch (IOException e) {
        }

        try {
            r.ready();
            return failed("expected IOException in ready() after close()");
        } catch (IOException e) {
        }

        if (r.markSupported()) {
            try {
                r.mark(0);
                return failed("expected IOException in mark() after close()");
            } catch (IOException e) {
            }

            try {
                r.reset();
                return failed("expected IOException in reset() after close()");
            } catch (IOException e) {
            }

        }
        } catch(Throwable e) {
            return failed("got exception " + e.getClass().getName() + " " + e.getMessage());
        }

        return passed();
    }

    public Result testClose() throws IOException { //TCK - test that this
        // method exists
        MultiThreadRunner.waitAtBarrier();

        getTestedReader().close();
        return passed();
    }
}
