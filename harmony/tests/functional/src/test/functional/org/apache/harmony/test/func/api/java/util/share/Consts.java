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
 * Created on 27.09.2005
 */
package org.apache.harmony.test.func.api.java.util.share;

public class Consts {

    public final static int NUMBER_OF_TESTS = 4096;

    public final static int MAX_OF_BIG_ARRAY_SIZE = 2048;

    public final static int MIN_OF_BIG_ARRAY_SIZE = 128;

    public final static int MAX_ARRAY_SIZE = 128;

    public final static int MIN_ARRAY_SIZE = 1;

    public final static int MAX_STRING_SIZE = 8;

    public final static int MIN_STRING_SIZE = 1;

    public final static String[][] STRING_ARRAY = new String[][] {
            { "a", "a", "a", "a" }, { "a", "d", "e", "b", "b", "a", "a" },
            { "a", "d", "e", "b", "b", "a", "a", "c", "c", "c", "c", "c" },
            { "9", "8", "7", "6", "5", "4", "3", "2", "1" },
            { "2", "3", "9", "4", "2" }, { "a" }, { "" } };

    public final static String[] STRING = new String[] { "a", "d", "a", "7",
            "2", "a", "" };

    public final static String[] STRING2 = new String[] { "b", "f", "g", "11",
            "23", "", "a" };

    public final static String[][][] STRING_TWO_DIMENSIONAL_ARRAY = new String[][][] {
            { { "4", "5" }, { "1", "3" }, { "4", "1" }, { "1", "2" },
                    { "4", "7" }, { "1", "4" } },
            { { "1", "1", "1" }, { "2", "2", "1" }, { "3", "3", "1" },
                    { "2", "4", "1" }, { "2", "5", "1" }, { "2", "6", "1" },
                    { "3", "7", "1" }, { "4", "8", "1" }, { "1", "9", "1" },
                    { "5", "8", "1" }, { "2", "7", "1" }, { "3", "6", "1" },
                    { "9", "5", "1" }, { "3", "7", "2" }, { "4", "8", "2" },
                    { "1", "9", "2" }, { "5", "8", "2" }, { "2", "7", "2" },
                    { "3", "6", "2" }, { "9", "5", "2" } } };
}