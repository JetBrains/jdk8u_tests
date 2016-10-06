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
 * Created on 23.11.2004
 *
 */
package org.apache.harmony.test.func.api.java.io.share.BufferedWriter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.harmony.test.func.api.java.io.share.Writer.WriterTestShared;
import org.apache.harmony.test.func.api.java.io.share.MockWriter;
import org.apache.harmony.test.func.api.java.io.share.MultiThreadRunner;
import org.apache.harmony.share.Result;

public class BufferedWriterTestShared extends WriterTestShared {
    private ThreadLocal underlyingWriter = new ThreadLocal();
    

    private Writer getUnderlyingWriter() throws IOException {
        return getUnderlyingWriter(true);
    }

    private Writer getUnderlyingWriter(Object lock) {
        underlyingWriter.set(super.getTestedWriter(lock));

        return (Writer) underlyingWriter.get();
    }

    private Writer getUnderlyingWriter(boolean reset) throws IOException {
        if (reset) {
            underlyingWriter.set(null);
        }

        if (underlyingWriter.get() == null) {
            underlyingWriter.set(super.getTestedWriter());
        }

        return (Writer) underlyingWriter.get();
    }

    protected Writer getTestedWriter() throws IOException {
        return new BufferedWriter(getUnderlyingWriter());
    }

    protected Writer getTestedWriter(Object lock) {
        return new BufferedWriter(getUnderlyingWriter(lock));
    }

    protected String getWriterAsString(Writer w) throws IOException {
        return super.getWriterAsString(getUnderlyingWriter(false));
    }

    public Result testClose() throws IOException {
        MockWriter mw = new MockWriter();

        BufferedWriter bw = new BufferedWriter(mw);

        MultiThreadRunner.waitAtBarrier();

        bw.write('a');
        bw.close();

        if (mw.getLog().size() == 1 && mw.getLog().get(0).equals("close")
                && mw.toString().equals("a")) {
            return passed();
        }

        return failed("underlying stream was not closed correctly");
    }

    public Result testFlush() throws IOException {
        MockWriter mw = new MockWriter();

        BufferedWriter bw = new BufferedWriter(mw);
        MultiThreadRunner.waitAtBarrier();
        bw.write('a');
        bw.flush();

        if (mw.getLog().size() == 1 && mw.getLog().get(0).equals("flush")
                && mw.toString().equals("a")) {
            return passed();
        }

        return failed("underlying stream was not flushed");
    }

    public Result testWriteArray() throws IOException {
        return super.testWriteArray();
    }

    public Result testWriteArrayNegativeLength() throws IOException {
        return super.testWriteArrayNegativeLength();    
    }

    public Result testWriteArrayNegativeStart() throws IOException {
        return super.testWriteArrayNegativeStart();
    }

    public Result testWriteArraySlice() throws IOException {
        return super.testWriteArraySlice();
    }

    public Result testWriteInt() throws IOException {
        return super.testWriteInt();
    }

    public Result testWriteNullArray() throws IOException {
        return super.testWriteNullArray();
    }

    public Result testWriteNullString() throws IOException {
        return super.testWriteNullString();
    }

    public Result testWriteString() throws IOException {
        return super.testWriteString();
    }

    public Result testWriteStringBigLength() throws IOException {
        return super.testWriteStringBigLength();
    }

    public Result testWriteStringNegativeLength() throws IOException {
        MockWriter mw = new MockWriter();

        BufferedWriter bw = new BufferedWriter(mw);

        MultiThreadRunner.waitAtBarrier();
        bw.write("qwerty", 0, -5);

        bw.close();

        if (mw.toString().length() != 0) {
            return failed("data is written with negative string length");
        }

        return passed();
    }

    public Result testWriteStringNegativeStart() throws IOException {
        return super.testWriteStringNegativeStart();
    }

    public Result testWriteStringSlice() throws IOException {
        return super.testWriteStringSlice();
    }

    public Result testNewLine() throws IOException {
        BufferedWriter bw = (BufferedWriter) getTestedWriter();

        MultiThreadRunner.waitAtBarrier();
        bw.write("ab");
        bw.newLine();
        bw.write("cd");
        bw.close();

        String s = getWriterAsString(bw);
        if (s.equals("ab" + System.getProperty("line.separator") + "cd")) {
            return passed();
        }
        return failed("expected another value, got:" + getWriterAsString(bw)
                + ":");
    }

    public Result testBufferedOutput0001() throws IOException {
        try {
            MockWriter mw = new MockWriter();
            BufferedWriter bw = new BufferedWriter(mw, 10);

            MultiThreadRunner.waitAtBarrier();
            bw.write('a');
            if (mw.toString().length() != 0) {
                return failed("data is not buffered");
            }
            bw.flush();
            if (mw.toString().length() != 1) {
                return failed("data is not flushed");
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return passed();
    }

    public Result testBufferedOutput0002() throws IOException {
        MockWriter mw = new MockWriter();
        BufferedWriter bw = new BufferedWriter(mw, 10);

        MultiThreadRunner.waitAtBarrier();
        bw.write(new char[] { 'a', 'b' });
        if (mw.toString().length() != 0) {
            return failed("data is not buffered");
        }
        bw.flush();
        if (mw.toString().length() != 2) {
            return failed("data is not flushed");
        }

        return passed();
    }

    public Result testBufferedOutput0003() throws IOException {
        MockWriter mw = new MockWriter();
        BufferedWriter bw = new BufferedWriter(mw, 10);

        MultiThreadRunner.waitAtBarrier();
        bw.write(new char[] { 'a', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b',
                'b', 'b', 'b', 'b', 'b' });

        if (mw.toString().length() != 15) {
            return failed("data is not written directly");
        }

        return passed();
    }

    public Result testBufferedOutput0004() throws IOException {
        MockWriter mw = new MockWriter();
        BufferedWriter bw = new BufferedWriter(mw, 10);

        MultiThreadRunner.waitAtBarrier();
        bw.write("ba");
        if (mw.toString().length() != 0) {
            return failed("data is not buffered");
        }
        bw.flush();
        if (mw.toString().length() != 2) {
            return failed("data is not flushed");
        }

        return passed();
    }

    public Result testExceptionAfterClose() throws IOException {
        BufferedWriter bw = (BufferedWriter) getTestedWriter();
        bw.close();

        try {
            bw.flush();
            return failed("expected flush() to throw exception after close()");
        } catch (IOException e) {
        }

        try {
            bw.newLine();
            return failed("expected newLine() to throw exception after close()");
        } catch (IOException e) {
        }

        try {
            bw.write(new char[] { 'a', 'b' });
            return failed("expected write(char[]) to throw exception after close()");
        } catch (IOException e) {
        }

        try {
            bw.write('a');
            return failed("expected write(int) to throw exception after close()");
        } catch (IOException e) {
        }

        try {
            bw.write("a");
            return failed("expected write(String) to throw exception after close()");
        } catch (IOException e) {
        }

        return passed();
    }

}