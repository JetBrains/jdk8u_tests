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
package org.apache.harmony.test.func.api.java.io.share.InputStream;

import java.io.IOException;
import java.io.InputStream;

import org.apache.harmony.test.func.api.java.io.share.IOMultiCase;
import org.apache.harmony.test.func.api.java.io.share.MultiThreadRunner;
import org.apache.harmony.share.Result;

public class InputStreamTestShared extends IOMultiCase {

    //methods that override this one should return input stream
    //that returns 'a' ... 'j' in first 10 read()'s
    public InputStream getTestedInputStream() throws IOException {
        return new InputStream() {
            int i = 'a';

            public int read() {
                //                    System.err.println("read called");
                return i++;
            }
            //                public int available() throws IOException {
            //                    System.err.println("available called");
            //                    return super.available();
            //                    }

        };
    }

    public Result testClose() throws IOException { //TCK - test that this
        // method exists
        MultiThreadRunner.waitAtBarrier();
        getTestedInputStream().close();
        return passed();
    }

    public Result testAvailable() throws IOException { //TCK - test that this
        // method exists
        InputStream is = getTestedInputStream();
        try {
            MultiThreadRunner.waitAtBarrier(); 
            if (is.available() == 0) {
                return passed();
            }

            return failed("InputStream 'available' method must return 0, got " + is.available());
        } finally {
            is.close();
        }
    }

    public Result testMarkSupported() throws IOException { //TCK - test that
                                                           // this
        // method exists and returns false by default
        InputStream is = getTestedInputStream();
        try {
            MultiThreadRunner.waitAtBarrier();
            if (is.markSupported() == false) {
                return passed();
            }

            return failed("InputStream markSupported must  return false");
        } finally {
            is.close();
        }

    }

    public Result testReset() throws IOException { //TCK - test that this
        //method exists and throws IOException in default case
        InputStream is = getTestedInputStream();
        try {
            MultiThreadRunner.waitAtBarrier();
            is.mark(0);
            is.reset();
        } catch (IOException e) {
            if (!is.markSupported()) {
                return passed();
            } else {
                return failed("'reset' method should throw an exception");
            }
        } finally {
            is.close();
        }
        if (is.markSupported()) {
            return passed();
        } else {
            return failed("InputStream 'reset' method should throw IOException");
        }
    }

    public Result testReset0001() throws IOException {
        InputStream is = getTestedInputStream();
        if (!is.markSupported()) {
            is.close();
            return passed("this test is for ony streams with markSupported");
        }

        int i1, i2;

        try {
            MultiThreadRunner.waitAtBarrier();

            int skipBytes = (int) (Math.random() * 10) + 10;
            for (int i = 0; i < skipBytes; ++i) {
                is.read();
            }

            is.mark(10);

            i1 = is.read();

            for (int i = 0; i < 5; ++i) {
                is.read();
            }

            is.reset();

            if (is.read() == i1) {
                return passed();
            }
        } catch (IOException e) {
            return failed("got ioexception");
        } finally {
            is.close();
        }
        return failed("mark/reset behaved incorrectly ");
    }

    public Result testReset0002() throws IOException {
        InputStream is = getTestedInputStream();
        if (!is.markSupported()) {
            is.close();
            return passed("this test is for ony streams with markSupported");
        }

        try {
            MultiThreadRunner.waitAtBarrier();

            int skipBytes = (int) (Math.random() * 10) + 10;
            for (int i = 0; i < skipBytes; ++i) {
                is.read();
            }

            is.mark(10);

            int i1 = is.read();

            for (int i = 0; i < 9; ++i) {
                is.read();
            }

            is.reset();

            if (is.read() == i1) {
                return passed();
            }
        } catch (IOException e) {
            return failed("got ioexception");
        } finally {
            is.close();
        }
        return failed("mark/reset behaved incorrectly");
    }

    public Result testMark() throws IOException { //TCK - test that this
        //method exists
        InputStream is = getTestedInputStream();
        try {
            MultiThreadRunner.waitAtBarrier();

            is.mark(0);
        } finally {
            is.close();
        }

        return passed();
    }

    public Result testReadByte() throws IOException { //TCK - test that this
        //method exists
        InputStream is = getTestedInputStream();

        try {
            MultiThreadRunner.waitAtBarrier();

            int i = is.read();
            if (i == 'a') {
                return passed();
            }
        } finally {
            is.close();
        }

        return failed("expected another value");
    }

    public Result testReadBytes() throws IOException { //TCK - test that this
        //method exists
        InputStream is = getTestedInputStream();

        byte[] b = new byte[3];

        try {
            MultiThreadRunner.waitAtBarrier();

            int i = is.read(b);

            if (i == 3 && b[0] == 'a' && b[1] == 'b' && b[2] == 'c') {
                return passed();
            }
        } finally {
            is.close();
        }

        return failed("expected another value");
    }

    public Result testReadBytesSlice() throws IOException { //TCK - test that
                                                            // this
        //method exists
        InputStream is = getTestedInputStream();
        byte[] b = new byte[3];

        try {
            MultiThreadRunner.waitAtBarrier();

            int i = is.read(b, 1, 2);

            if (i == 2 && b[0] == 0 && b[1] == 'a' && b[2] == 'b') {
                return passed();
            }
        } finally {
            is.close();
        }

        return failed("expected another value");
    }

    public Result testReadBytesNull() throws IOException { //TCK - test that
                                                           // this
        //method exists
        InputStream is = getTestedInputStream();
        try {

            is.read(null);
        } catch (NullPointerException e) {
            return passed();
        } finally {
            is.close();
        }

        return failed("expected NullPointerException");
    }

    public Result testReadBytes0001() throws IOException { //TCK - test that
                                                           // this
        //method exists
        InputStream is = getTestedInputStream();
        try {
            MultiThreadRunner.waitAtBarrier();

            is.read(new byte[1], -1, 1);
        } catch (IndexOutOfBoundsException e) {
            return passed();
        } finally {
            is.close();
        }

        return failed("expected IndexOutOfBoundsException");
    }

    public Result testReadBytes0002() throws IOException { //TCK - test that
                                                           // this
        //method exists
        InputStream is = getTestedInputStream();
        try {
            MultiThreadRunner.waitAtBarrier();

            is.read(new byte[1], 0, -1);
        } catch (IndexOutOfBoundsException e) {
            return passed();
        } finally {
            is.close();
        }

        return failed("expected IndexOutOfBoundsException");
    }

    public Result testReadBytes0003() throws IOException { //TCK - test that
                                                           // this
        //method exists
        InputStream is = getTestedInputStream();
        try {
            MultiThreadRunner.waitAtBarrier();

            is.read(new byte[1], 0, 5);
        } catch (IndexOutOfBoundsException e) {
            return passed();
        } finally {
            is.close();
        }

        return failed("expected IndexOutOfBoundsException");
    }

    public final Result testReadBytesException0001() throws IOException { //TCK
        try {
            InputStream is = new InputStream() {
                public int read() throws IOException {
                    throw new IOException();
                }
            };
            MultiThreadRunner.waitAtBarrier();

            is.read(new byte[5]);
        } catch (IOException e) {
            return passed();
        }

        return failed("expected IOException");
    }

    public final Result testReadBytesException0002() throws IOException { //TCK
        InputStream is = new InputStream() {
            int i = 'a';

            public int read() throws IOException {
                if (i++ > 'c') {
                    throw new IOException();
                }
                return i;
            }
        };

        byte[] b = new byte[5];
        MultiThreadRunner.waitAtBarrier();

        int read = is.read(b);
        if (read == 3 && b[0] == 'b' && b[1] == 'c' && b[2] == 'd') {
            return passed();
        }

        return failed("wrong values read");
    }

    public Result testSkip() throws IOException { //TCK
        InputStream is = getTestedInputStream();
        try {
            MultiThreadRunner.waitAtBarrier();

            if (is.skip(3) == 3 && is.read() == 'd') {
                return passed();
            }
        } finally {
            is.close();
        }

        return failed("InputStream 'skip' method returned wrong value");
    }
    
    //this test method is for real streams only - shouldn't be called for
    //InputStream itself
    public Result _testExceptionAfterClose() throws IOException {
        InputStream is = getTestedInputStream();

        is.close();

        try {
            is.read();
            return failed("expected IOException in read() after close()");
        } catch (IOException e) {
        }

        try {
            is.read(new byte[3]);
            return failed("expected IOException in read(byte[3]) after close()");
        } catch (IOException e) {
        }

        try {
            is.available();
            return failed("expected IOException in available() after close()");
        } catch (IOException e) {
        }

        return passed();
    }
}
