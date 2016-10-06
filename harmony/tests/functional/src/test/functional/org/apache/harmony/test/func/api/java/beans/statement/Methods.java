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
package org.apache.harmony.test.func.api.java.beans.statement;

/**
 * There are very different methods in this class: static, no static, with
 * parameters, without parameters. All methods in this class are invoked by
 * ExpressionTest and StatementTest tests.
 * 
 */
public class Methods {
    static boolean static_without_params = false,
        no_static_without_params = false, constructor = false;

    public Methods() {
    }

    public Methods(String str, int a) throws StatementException {
        if (!str.equals("abcde"))
            throw new StatementException("String is not abcde");
        if (a != 7)
            throw new StatementException("a != 7");
        constructor = true;
    }

    public static void static_without_params() {
        static_without_params = true;
    }

    public void no_static_without_params() {
        no_static_without_params = true;
    }

    public static String static_return_string() {
        return "abc";
    }

    public static int static_return_primitive() {
        return 5;
    }

    public static void static_array_of_primitive_as_param(int[] ints)
        throws StatementException {
        if (ints[0] != 3)
            throw new StatementException("ints[0]!=3");
    }

    public static void static_with_params(String str, int a)
        throws StatementException {
        if (!str.equals("abc"))
            throw new StatementException("String is not abc");
        if (a != 9)
            throw new StatementException("a != 9");
    }

    public static void static_with_params_2(Integer a)
        throws StatementException {
        if (!a.equals(new Integer(10)))
            throw new StatementException("a != 10");
    }

    public void no_static_with_params(String str, int a)
        throws StatementException {
        if (!str.equals("abcd"))
            throw new StatementException("String is not abcd");
        if (a != 8)
            throw new StatementException("a != 8");
    }

    public static void throwException() throws StatementException {
        throw new StatementException();
    }
}