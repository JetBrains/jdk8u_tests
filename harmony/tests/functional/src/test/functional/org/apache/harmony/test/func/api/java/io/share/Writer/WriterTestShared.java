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
 * Created on 19.11.2004
 *
 */
package org.apache.harmony.test.func.api.java.io.share.Writer;

import java.io.IOException;
import java.io.Writer;

import org.apache.harmony.test.func.api.java.io.share.IOMultiCase;
import org.apache.harmony.test.func.api.java.io.share.MockWriter;
import org.apache.harmony.test.func.api.java.io.share.MultiThreadRunner;
import org.apache.harmony.share.Result;

public class WriterTestShared extends IOMultiCase {

    protected Writer getTestedWriter() throws IOException {
        return new MockWriter();
    }

    protected Writer getTestedWriter(Object lock) {
        return new MockWriter(lock);
    }

    protected String getWriterAsString(Writer w) throws IOException {
        return w.toString();
    }

    public Result testWriteInt() throws IOException {
        Writer w = getTestedWriter();
        try {
            MultiThreadRunner.waitAtBarrier();

            w.write('a');
            w.write((0xFFFF << 16) | (int) 'b');
            w.close();

            if (getWriterAsString(w).equals("ab")) {
                return passed();
            }
        } finally {
            w.close();
        }

        return failed("expected another value: " + getWriterAsString(w));
    }

    public Result testWriteNullArray() throws IOException {
        Writer w = getTestedWriter();

        try {
            w.write((char[]) null);
        } catch (NullPointerException e) {
            return passed();
        } finally {
            w.close();
        }

        return failed("expected NullPointerException");
    }

    public Result testWriteNullString() throws IOException {
        Writer w = getTestedWriter();

        try {
            w.write((String) null);
        } catch (NullPointerException e) {
            return passed();
        } finally {
            w.close();
        }

        return failed("expected NullPointerException");
    }

    public Result testWriteArray() throws IOException {
        Writer w = getTestedWriter();

        try {
            char[] buf = { 'a', 'b', 'c' };

            MultiThreadRunner.waitAtBarrier();

            w.write(buf);
            w.close();

            if ("abc".equals(getWriterAsString(w))) {
                return passed();
            }
        } finally {
            w.close();
        }

        return failed("expected another value");
    }

    public Result testWriteString() throws IOException {
        Writer w = getTestedWriter();

        try {
            MultiThreadRunner.waitAtBarrier();

            w.write("abc\u0000");
            w.close();

            if ("abc\u0000".equals(getWriterAsString(w))) {
                return passed();
            }
        } finally {
            w.close();
        }

        return failed("expected another value");
    }

    public Result testWriteArraySlice() throws IOException {
        Writer w = getTestedWriter();

        try {
            MultiThreadRunner.waitAtBarrier();

            w.write(new char[] { 'a', 'b', 'c', 'd', 'e', 'f' }, 2, 3);
            w.close();

            if ("cde".equals(getWriterAsString(w))) {
                return passed();
            }
        } finally {
            w.close();
        }

        return failed("expected another value");
    }

    public Result testWriteStringSlice() throws IOException {
        Writer w = getTestedWriter();

        try {
            MultiThreadRunner.waitAtBarrier();
            
            w.write("abcdef", 2, 3);

            w.close();

            if ("cde".equals(getWriterAsString(w))) {
                return passed();
            }
        } finally {
            w.close();
        }

        return failed("expected another value");
    }

    public Result testWriteArrayNegativeStart() throws IOException {
        Writer w = getTestedWriter();

        try {
            MultiThreadRunner.waitAtBarrier();
            w.write(new char[] { 'a', 'b', 'c', 'd', 'e', 'f' }, -2, 3);
        } catch (IndexOutOfBoundsException e) {
            return passed();
        } catch(Throwable e) {
            e.printStackTrace();
            failed("expected IndexOutOfBoundsException");
        } finally {
            w.close();
        }

        return failed("expected IndexOutOfBoundsException");
    }

    public Result testWriteArrayNegativeLength() throws IOException {
        Writer w = getTestedWriter();

        try {
            MultiThreadRunner.waitAtBarrier();
            w.write(new char[] { 'a', 'b', 'c', 'd', 'e', 'f' }, 0, -5);
        } catch (IndexOutOfBoundsException e) {
            return passed();
        } finally {
            w.close();
        }

        return failed("expected IndexOutOfBoundsException");
    }

    public Result testWriteArrayBigLength() throws IOException {
        Writer w = getTestedWriter();

        try {
            MultiThreadRunner.waitAtBarrier();
            w.write(new char[] { 'a', 'b', 'c', 'd', 'e', 'f' }, 4, 3);
        } catch (IndexOutOfBoundsException e) {
            return passed();
        } finally {
            w.close();
        }

        return failed("expected IndexOutOfBoundsException");
    }

    public Result testWriteStringNegativeStart() throws IOException {
        Writer w = getTestedWriter();

        try {
            MultiThreadRunner.waitAtBarrier();
            w.write("qwerty", -2, 3);
        } catch (IndexOutOfBoundsException e) {
            return passed();
        } finally {
            w.close();
        }

        return failed("expected IndexOutOfBoundsException");
    }

    public Result testWriteStringNegativeLength() throws IOException {
        Writer w = getTestedWriter();

        try {
            MultiThreadRunner.waitAtBarrier();
            w.write("qwwety", 0, -5);
        } catch (IndexOutOfBoundsException e) {
            return passed();
        } catch (NegativeArraySizeException e) {
            return passed();
        } catch(Throwable e) {
            e.printStackTrace();
            return failed("expected IndexOutOfBoundsException");
        } finally {
            w.close();
        }

        return failed("expected IndexOutOfBoundsException");
    }

    public Result testWriteStringBigLength() throws IOException {
        Writer w = getTestedWriter();

        String s;

        try {
            MultiThreadRunner.waitAtBarrier();
            w.write("qwerty", 4, 3);
        } catch (IndexOutOfBoundsException e) {
            return passed();
        } finally {
            w.close();
        }

        return failed("expected IndexOutOfBoundsException");
    }

    public Result testClose() throws IOException {
        MultiThreadRunner.waitAtBarrier();
        getTestedWriter().close();
        return passed();
    }

    public Result testFlush() throws IOException {
        Writer w = getTestedWriter();
        try {
            MultiThreadRunner.waitAtBarrier();
            w.flush();
        } finally {
            w.close();
        }
        return passed();
    }
}

