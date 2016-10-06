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
package org.apache.harmony.test.func.api.java.io.share.OutputStream;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.harmony.test.func.api.java.io.share.IOMultiCase;
import org.apache.harmony.test.func.api.java.io.share.MockOutputStream;
import org.apache.harmony.test.func.api.java.io.share.MultiThreadRunner;
import org.apache.harmony.share.Result;

public class OutputStreamTestShared extends IOMultiCase {

    protected OutputStream getTestedOutputStream(int maxSize)
            throws IOException {
        return new MockOutputStream(maxSize);
    }

    protected String getWrittenAsString(OutputStream os) {
        return ((MockOutputStream) os).getWrittenAsString();
    }

    public Result testClose() throws IOException { //TCK - test that this
        // method exists
        MultiThreadRunner.waitAtBarrier();
        //        System.err.println("here in testClose " +
        // Thread.currentThread().getName());
        getTestedOutputStream(0).close();
        return passed();
    }

    public Result _testExceptionAfterClose() throws IOException {
        OutputStream os = getTestedOutputStream(5);
        os.close();

        try {
            os.write(new byte[1]);
            return failed("expected exception writing to a closed stream");
        } catch (IOException e) {
        }

        try {
            os.flush();
            return failed("expected exception flushing closed stream");
        } catch (IOException e) {
        }

        try {
            os.write('a');
            return failed("expected exception writing to a closed stream");
        } catch (IOException e) {
        }

        return passed();
    }

    public Result testFlush() throws IOException { //TCK - test that this
        // method exists
        MultiThreadRunner.waitAtBarrier();
        //        System.err.println("here in testFlush " +
        // Thread.currentThread().getName());
        OutputStream os = getTestedOutputStream(0);
        try {
            os.flush();
        } finally {
            os.close();
        }
        return passed();
    }

    public Result testWrite0001() throws IOException {
        OutputStream os = getTestedOutputStream(5);

        MultiThreadRunner.waitAtBarrier();
        //        System.err.println("here in testWrite0001 " +
        // Thread.currentThread().getName());
        os.write("abc".getBytes());
        os.flush();
        os.close();

        if (!"abc".equals((getWrittenAsString(os)))) {
            return failed("wrong values written");
        }

        return passed();
    }

    public Result testWriteNull() throws IOException { //TCK - test that this
        // NPE is thrown if we try to write null
        OutputStream os = getTestedOutputStream(5);
        MultiThreadRunner.waitAtBarrier();
        //        System.err.println("here in testWriteNull " +
        // Thread.currentThread().getName());
        try {
            os.write(null);
        } catch (NullPointerException e) {
            return passed();
        } finally {
            os.close();
        }

        return failed("expected NPE here");
    }

    public Result testWriteSlice() throws IOException {
        OutputStream os = getTestedOutputStream(5);
        MultiThreadRunner.waitAtBarrier();
        //        System.err.println("here in testWriteSlice " +
        // Thread.currentThread().getName());
        try {
            os.write("abc".getBytes(), 1, 2);
            os.flush();
            String s = getWrittenAsString(os);
            if (!"bc".equals(s)) {
                return failed("wrong values written: " + s);
            }
        } finally {
            os.close();
        }

        return passed();
    }

    public Result testWriteByte() throws IOException {
        OutputStream os = getTestedOutputStream(5);
        MultiThreadRunner.waitAtBarrier();
        //        System.err.println("here in testWriteByte " +
        // Thread.currentThread().getName());

        try {

            os.write('a');
            os.flush();

            if (!"a".equals((getWrittenAsString(os)))) {
                return failed("wrong values written");
            }

        } finally {
            os.close();
        }

        return passed();
    }

    public Result testWriteIndexOutOfBounds0001() throws IOException { //TCK -
        // test
        // that
        // IndexOutOfBoundsException is thrown if second argument to
        // writeBytes() is negative
        OutputStream os = getTestedOutputStream(5);
        MultiThreadRunner.waitAtBarrier();
        //        System.err.println("here in testWriteIndexOutOfBounds0001 " +
        // Thread.currentThread().getName());

        try {
            os.write("abc".getBytes(), -1, 2);
        } catch (IndexOutOfBoundsException e) {
            return passed();
        } finally {
            os.close();
        }

        return failed("expected exception here");
    }

    public Result testWriteIndexOutOfBounds0002() throws IOException { //TCK -
        // test
        // that
        //  IndexOutOfBoundsException third argument to writeBytes() is negative
        OutputStream os = getTestedOutputStream(5);
        MultiThreadRunner.waitAtBarrier();
        //        System.err.println("here in testWriteIndexOutOfBounds0003 " +
        // Thread.currentThread().getName());

        try {
            os.write("abc".getBytes(), 1, -2);
        } catch (IndexOutOfBoundsException e) {
            return passed();
        } finally {
            os.close();
        }

        return failed("expected exception here");
    }

    public Result testWriteIndexOutOfBounds0003() throws IOException { //TCK -
        // test
        // that
        //IndexOutOfBoundsException is thrown if second argument + third
        // argument to writeBytes
        //method is more than bytes array length
        OutputStream os = getTestedOutputStream(5);
        MultiThreadRunner.waitAtBarrier();
        //        System.err.println("here in testWriteIndexOutOfBounds0004 " +
        // Thread.currentThread().getName());

        try {
            os.write("abc".getBytes(), 1, 100);
        } catch (IndexOutOfBoundsException e) {
            return passed();
        } finally {
            os.close();
        }

        return failed("expected exception here");
    }
}
