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
package org.apache.harmony.test.func.api.java.nio.charset.CharsetEncoder;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;

import org.apache.harmony.test.func.api.java.nio.share.CharsetEncoderImpl;
import org.apache.harmony.test.func.api.java.nio.share.CharsetImpl;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

public final class CharsetEncoderTest extends MultiCase {
    public static void main(String[] args) {
        System.exit(new CharsetEncoderTest().test(args));
    }

    public Result testConstructor() throws IOException {
        new CharsetEncoderImpl(null, 1, 1);
        try {
            new CharsetEncoderImpl(null, 1, 1, null);
            return failed("expected IllegalArgumentException 1");
        } catch (IllegalArgumentException e) {
        }

        try {
            new CharsetEncoderImpl(null, 1, 1, new byte[] {});
            return failed("expected IllegalArgumentException 2");
        } catch (IllegalArgumentException e) {
        }

        new CharsetEncoderImpl(null, 1, 1, new byte[] { 0 });

        Charset cs = new CharsetImpl("abcd", null);

        try {
            new CharsetEncoderImpl(cs, Float.POSITIVE_INFINITY, 1);
            return failed("expected IllegalArgumentException 3");
        } catch (IllegalArgumentException e) {
        }

        new CharsetEncoderImpl(cs, 1, Float.POSITIVE_INFINITY);
        new CharsetEncoderImpl(cs, Float.NaN, 1);
        new CharsetEncoderImpl(cs, 1, Float.NaN);

        try {
            new CharsetEncoderImpl(cs, 0, 1);
            return failed("expected IllegalArgumentException 4");
        } catch (IllegalArgumentException e) {
        }

        try {
            new CharsetEncoderImpl(cs, 1, 0);
            return failed("expected IllegalArgumentException 5");
        } catch (IllegalArgumentException e) {
        }

        try {
            new CharsetEncoderImpl(cs, -1, 1);
            return failed("expected IllegalArgumentException 6");
        } catch (IllegalArgumentException e) {
        }

        try {
            new CharsetEncoderImpl(cs, 1, -1);
            return failed("expected IllegalArgumentException 7");
        } catch (IllegalArgumentException e) {
        }

        return passed();
    }

    public Result testIsLegalReplacement() throws Exception {
        CharsetEncoder ce = Charset.forName("UTF-8").newEncoder();
        try {
            ce.isLegalReplacement(null);
            return failed("expected NPE");
        } catch(NullPointerException e) {
        }
        if(!ce.isLegalReplacement(new byte [] { })) {
            return failed("expected true 1");
        }

        if(!ce.isLegalReplacement(new byte [] { 0 })) {
            return failed("expected true 2");
        }

        if(ce.isLegalReplacement(new byte [] { (byte)0xd8, 0, (byte)0xdc, 0})) {
            return failed("expected false");
        }
        
        return passed();
    }

    public Result testEncode() throws Exception {
        String s = "These words were followed by a very long silence, "
                + "broken only by an occasional exclamation of `Hjckrrh!' from the "
                + "Gryphon, and the constant heavy sobbing of the Mock Turtle. "
                + "Alice was very nearly getting up and saying, "
                + "`Thank you, sir, for your interesting story,' but she could not help "
                + "thinking there must be more to come, so she sat still and said nothing.";

        File f = File.createTempFile("abc", "def");
        f.deleteOnExit();

        Charset cs = Charset.forName("ISO-8859-1");
        CharsetDecoder cd = cs.newDecoder();
        CharsetEncoder ce = cs.newEncoder();

        ByteBuffer bb = ByteBuffer.allocateDirect(1024);
        CharBuffer cb = CharBuffer.wrap(s);

        FileOutputStream fos = new FileOutputStream(f);
        FileChannel fc = fos.getChannel();

        if (ce.encode(cb, bb, false) != CoderResult.UNDERFLOW) {
            return failed("expected encode() to return CoderResult.UNDERFLOW");
        }

        bb.flip();
        fc.write(bb);
        fos.close();

        try {
            ce.flush(bb);
            return failed("expected IllegalStateException");
        } catch (IllegalStateException e) {
        }

        ce.encode(cb, bb, true);

        if (ce.flush(bb) != CoderResult.UNDERFLOW) {
            return failed("expected flush() to return CoderResult.UNDERFLOW");
        }

        FileReader fr = new FileReader(f);
        String s1 = new BufferedReader(fr).readLine();
        fr.close();

        if (!s.equals(s1)) {
            return failed("expected returned string to be equal to original, got: "
                    + s1);
        }

        return passed();
    }
    
    
}

