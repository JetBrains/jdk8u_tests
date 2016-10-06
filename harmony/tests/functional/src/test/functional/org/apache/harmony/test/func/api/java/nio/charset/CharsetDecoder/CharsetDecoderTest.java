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
package org.apache.harmony.test.func.api.java.nio.charset.CharsetDecoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;

import org.apache.harmony.test.func.api.java.nio.share.CharsetDecoderImpl;
import org.apache.harmony.test.func.api.java.nio.share.CharsetImpl;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

public final class CharsetDecoderTest extends MultiCase {
    public static void main(String[] args) {
        System.exit(new CharsetDecoderTest().test(args));
    }

    public Result testConstructor() throws IOException {
        new CharsetDecoderImpl(null, 1, 1); //no exception
        Charset cs = new CharsetImpl("abcd", null);

        try {
            new CharsetDecoderImpl(cs, Float.POSITIVE_INFINITY, 1);
            return failed("expected IllegalArgumentException 1");
        } catch (IllegalArgumentException e) {
        }
        new CharsetDecoderImpl(cs, 1, Float.POSITIVE_INFINITY);
        new CharsetDecoderImpl(cs, Float.NaN, 1);
        new CharsetDecoderImpl(cs, 1, Float.NaN);

        try {
            new CharsetDecoderImpl(cs, 0, 1);
            return failed("expected IllegalArgumentException 2");
        } catch (IllegalArgumentException e) {
        }

        try {
            new CharsetDecoderImpl(cs, 1, 0);
            return failed("expected IllegalArgumentException 3");
        } catch (IllegalArgumentException e) {
        }

        try {
            new CharsetDecoderImpl(cs, -1, 1);
            return failed("expected IllegalArgumentException 4");
        } catch (IllegalArgumentException e) {
        }

        try {
            new CharsetDecoderImpl(cs, 1, -1);
            return failed("expected IllegalArgumentException 5");
        } catch (IllegalArgumentException e) {
        }

        return passed();
    }

    public Result testDecode() throws Exception {
        String s = "The Functional test suite is a "
                + "collection of "
                + "micro scenarios for "
                + "testing various functional parts of "
                + "an implementation.";

        File f = File.createTempFile("abc", "def");
        f.deleteOnExit();
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(s.getBytes("ISO-8859-1"));
        fos.close();

        Charset cs = Charset.forName("ISO-8859-1");
        CharsetDecoder cd = cs.newDecoder();
        CharsetEncoder ce = cs.newEncoder();

        ByteBuffer bb = ByteBuffer.allocateDirect(1024);
        CharBuffer cb = CharBuffer.allocate(1024);

        FileInputStream fis = new FileInputStream(f);
        FileChannel fc = fis.getChannel();

        String s1 = "";

        while ((fc.read(bb)) != -1) {
            bb.flip();
            if (cd.decode(bb, cb, false) != CoderResult.UNDERFLOW) {
                return failed("expected decode() to return CoderResult.UNDERFLOW");
            }
            cb.flip();
            s1 += cb.toString();
            bb.clear();
            cb.clear();
        }

        if (!s.equals(s1)) {
            return failed("expected returned string to be equal to original, got: "
                    + s1);
        }

        try {
            cd.flush(cb);
            return failed("expected IllegalStateException");
        } catch (IllegalStateException e) {
        }

        cd.decode(bb, cb, true);

        if (cd.flush(cb) != CoderResult.UNDERFLOW) {
            return failed("expected flush() to return CoderResult.UNDERFLOW");
        }

        return passed();
    }
}

