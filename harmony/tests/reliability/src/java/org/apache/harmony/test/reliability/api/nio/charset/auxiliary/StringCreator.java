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

package org.apache.harmony.test.reliability.api.nio.charset.auxiliary;


/**
 * @author Olessia Salmina
 * @version $Revision: 1.1 $
 */



import java.util.Random;


public class StringCreator {

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
