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
package org.apache.harmony.test.func.api.java.lang.F_StringBufferTest_03;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * 
 * scenario test 
 * Tests String class methods and StringBuffer class methods
 */
public class F_StringBufferTest_03 extends ScenarioTest {
    private String str = new String();

    public F_StringBufferTest_03() {
        super();
    }

    /**
     * operates with String argument using some methods of String, Character,
     * StringBuffer classes
     */
    public int test() {
        str = new String("true");
        StringBuffer buffer = new StringBuffer();
        buffer.append(3 == 3);
        if (!str.equalsIgnoreCase(buffer.toString()))
            return fail("Probably StringBuffer.append(boolean) works incorrectly.");
        char[] ch = { 'P', 'i', '=', '3', '.', '1', '4' };
        buffer.setLength(0);
        buffer.append(ch);
        str = new String(ch);
        if (!str.equalsIgnoreCase(buffer.toString()))
            return fail("Probably StringBuffer.append(char[]) works incorrectly.");
        try {
            buffer.setLength(-1);
            return fail("StringBuffer.setLength(negative) works incorrectly, IndexOutOfBoundsException must be thrown.");
        } catch (IndexOutOfBoundsException e) {
        }
        buffer.setLength(0);
        try {
            buffer.charAt(1);
            return fail("StringBuffer.charAt(int greater than or equal to length()) works incorrectly, IndexOutOfBoundsException must be thrown.");
        } catch (IndexOutOfBoundsException e) {
        }
        buffer.append(ch, 3, 4);
        if (!String.valueOf(3.14f).equalsIgnoreCase("3.14"))
            return fail("String.valueOf(float) works incorrectly.");
        if (!String.valueOf(3.14f).equalsIgnoreCase(buffer.toString()))
            return fail("Probably StringBuffer.append(char[],int,int) works incorrectly.");
        try {
            buffer.charAt(-1);
            return fail("StringBuffer.charAt(negative) works incorrectly, IndexOutOfBoundsException must be thrown.");
        } catch (StringIndexOutOfBoundsException e) {
        } catch (IndexOutOfBoundsException e) {
        }
        str = null;
        try {
            buffer.insert(-1, str);
            return fail("StringBuffer.insert(negative, String) works incorrectly, StringIndexOutOfBoundsException must be thrown.");
        } catch (StringIndexOutOfBoundsException e) {
        }
        buffer.insert(1, str);
        if (!String.valueOf("3null.14").equalsIgnoreCase(buffer.toString()))
            return fail("Probably StringBuffer.insert(null) works incorrectly.");
        buffer.setLength(0);
        buffer.insert(0, str);
        if (!String.valueOf("null").equalsIgnoreCase(buffer.toString()))
            return fail("StringBuffer.insert(string) works incorrectly.");
        return pass();
    }

    public static void main(String[] args) {
        System.exit(new F_StringBufferTest_03().test(args));
    }
}