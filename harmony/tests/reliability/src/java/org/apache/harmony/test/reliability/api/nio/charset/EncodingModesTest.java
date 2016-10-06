/*
 * Copyright 2006 The Apache Software Foundation or its licensors, as applicable
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author Oleg Oleinik
 * @version $Revision: 1.2 $
 */

package org.apache.harmony.test.reliability.api.nio.charset;

import org.apache.harmony.test.reliability.share.Test;

import java.util.SortedMap;
import java.util.Random;
import java.nio.CharBuffer;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;


/**
 * Goal: find memory leaks invoking encodings for each available charset using different 
 *       combinations of onUnmappable/onMalformed.
 *
 * The test does:
 *    1. Reads parameters, which are:
 *          param[0] - number of iterations. For each string, each encoding -
 *                     - number of cycles with all combinations of malformed, unmappable actions flags.
 *
 *    2. Creates 3 strings of length 0xFFFF:
 *       string[0] = all chars are those for which isJavaIdentifierPart(ch) returns true, from 0 to 0xFFFF
 *       string[1] = all chars in range 0..0xFFFF, including malformed
 *       string[2] = all chars are those for which isJavaIdentifierPart(ch) returns true, randomly chosen
 *    3. For each of the string:
 *          1) receives all charsets through availableCharsets().
 *          2) for each charset:
 *              2.1) starts a cycle of param[0] iterations. On each iteration:
 *                   a) sets replaceWith() bytes
 *                   b) choses next combination of onUnmappable/onMalformed
 *                   c) invokes encode(CharBuffer, ByteBuffer, false)
 *                   d) repeats b-c until all 9 combinations of onUnmappable/onMalformed are chosen
 *              2.2) runs System.gc()
 *
 */

public class EncodingModesTest extends Test {

    public int callSystemGC = 1;

    public int SWITCH_ITERATIONS = 100;

    public static void main(String[] args) {
        System.exit(new EncodingModesTest().test(args));
    }

    public int test(String[] params) {

        parseParams(params);
        String[] strs_to_encode = new String[3];

        strs_to_encode[0] = StringCreator.getValidString((char)0, (char)0xFFFF, StringCreator.START_TO_END);
        strs_to_encode[1] = StringCreator.getInvalidString();
        strs_to_encode[2] = StringCreator.getValidString((char)0, (char)0xFFFF, StringCreator.RANDOM);

        CodingErrorAction[] action = {CodingErrorAction.REPLACE,
                                         CodingErrorAction.IGNORE,
                                         CodingErrorAction.REPORT};

        for (int k = 0; k < strs_to_encode.length; ++k) {

            SortedMap allCharsets = Charset.availableCharsets();
            Object[] names = allCharsets.keySet().toArray();

            for (int i = 0; i < names.length; ++i) {
                Charset chset = (Charset) allCharsets.get(names[i]);
                if (!chset.canEncode()) {
                    continue;
                }
                CharsetEncoder ce = chset.newEncoder().reset();
                ByteBuffer bytes;
                CharBuffer chars;

                // log.add("Charset " + names[i]);
                for (int j = 0; j < SWITCH_ITERATIONS; ++j) {
                    for (int x = 0; x < action.length; ++x) {
                        for (int y = 0; y < action.length; ++y) {
                            try {
                                chars = CharBuffer.wrap(strs_to_encode[k]
                                    .subSequence(0, strs_to_encode[k].length()));
                                ce = ce.replaceWith(getReplacementBytes());
                                ce.onMalformedInput(action[x]);
                                ce.onUnmappableCharacter(action[y]);
                                ce.encode(chars, (bytes = ByteBuffer
                                    .wrap(new byte[strs_to_encode[k].length() * 2])), false);

                            } catch (Throwable t) {
                                // log.add(t);
                            }
                        }
                    }
                }
                if (callSystemGC != 0) {
                    System.gc();
                }
            }
        }
        return pass("OK");
    }


    public void parseParams(String[] params) {
        if (params.length >= 1) {
            SWITCH_ITERATIONS = Integer.parseInt(params[0]);
        }
    }


    public byte[] getReplacementBytes() {
        byte[] b = new byte[1];
        b[0] = (byte) 0;
        return b;
    }
}

class StringCreator {
    public static final int START_TO_END = 2;
    public static final int RANDOM = 3;

    public static String getValidString(char start, char end, int order) {
 
        char[] ch = new char[end - start];  
        // creates a String of (end-start) length, consisting of chars for which
        // isJavaIdentifierPart(c) returns true. If we checked all chars between 
        // <start> and <end> but, the array is not fully complete, copy the last 
        // isJavaIdentifierPart(c) char at each position in the rest of the array.

        if (order != RANDOM) {
            int i = 0;
            
            for (char c = start; c < end; ++c) { 
                if (Character.isJavaIdentifierPart(c)) {
                    ch[i++] = c;
                }
            }

            if (i == 0) {
                ch[i++] = 'A';
            }

            for (; i < ch.length; ++i) {
                ch[i] = ch[i - 1];
            }
            return new String(ch);
   
        } else if (order == RANDOM) {
            // creates a String of (end-start) length, consisting of random chars
  
            Random rand = new Random(10);
            int i = 0;
            while (i < ch.length) {

                int c = rand.nextInt(end - start) + start;
                if (Character.isJavaIdentifierPart((char)c)) {
                    ch[i++] = (char)c;
                }
            }

            return new String(ch);
  
        } else {
            return "";
        }
    }

    public static String getInvalidString() {
          
        // creates a String of 0xFFFF length, consisting of all Unicode chars,
        // including "private use", "hi surrogate", "low surrogate" and other 
        // invalid chars (sequences).

        char[] ch = new char[65535];
        for (int i = 0; i < ch.length; ++i){
            ch[i] = (char) i;
        }
        return new String(ch);
    }
}



