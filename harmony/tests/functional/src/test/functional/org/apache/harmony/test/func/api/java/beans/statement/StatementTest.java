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

import java.beans.Statement;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 * Under test: Statement.
 * <p>
 * Purpose: Verify that Statement class invokes correctly method, which points
 * in constructor of this class.
 * 
 */
public class StatementTest extends MultiCase {

    public static void main(String[] args) {
        try {
            System.exit(new StatementTest().test(args));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Verify, that Statement class invokes not static method without
     * parameters.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create type.
     * <li>Create not static method without parameters in this type.
     * <li>Create instance of this type.
     * <li>Create Statement instance: new Statement(object,methodName,new
     * Object[]{}).
     * <li>Execute statement.
     * <li>Verify selected method was invoked.
     */
    public Result testNotStaticWithoutParams() throws Exception {
        new Statement(new Methods(), "no_static_without_params", new Object[0])
            .execute();
        assertTrue(Methods.no_static_without_params);
        return passed();
    }

    /**
     * Verify, that Statement class invokes static method without parameters.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create type.
     * <li>Create static method without parameters in this type.
     * <li>Create Statement instance: new Statement(Type.class,methodName,new
     * Object[]{}).
     * <li>Execute statement.
     * <li>Verify selected method was invoked.
     */
    public Result testStaticWithoutParams() throws Exception {
        new Statement(Methods.class, "static_without_params", new Object[0])
            .execute();
        assertTrue(Methods.static_without_params);
        return passed();
    }

    /**
     * Verify, that Statement class invokes not static method, which has
     * primitive and not-primitive parameters.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create type.
     * <li>Create not static method in the type, which has primitive and
     * not-primitive parameters.
     * <li>Create instance of this type.
     * <li>Create Statement instance: new Statement(object,methodName,new
     * Object[]{args}).
     * <li>Execute statement.
     * <li>Verify selected method was invoked with selected arguments.
     */
    public Result testNotStaticWithParams() throws Exception {
        new Statement(new Methods(), "no_static_with_params", new Object[] {
            "abcd", new Integer(8) }).execute();

        return passed();
    }

    /**
     * Verify, that Statement class invokes static method, which has primitive
     * and not-primitive parameters.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create type.
     * <li>Create static method in the type, which has primitive and
     * not-primitive parameters.
     * <li>Create Statement instance: new Statement(Type.class,methodName,new
     * Object[]{args}).
     * <li>Execute statement.
     * <li>Verify selected method was invoked with selected arguments.
     */
    public Result testStaticWithParams() throws Exception {
        new Statement(Methods.class, "static_with_params", new Object[] {
            "abc", new Integer(9) }).execute();
        return passed();
    }

    /**
     * Verify, that Statement class invokes method, which has wrapper of
     * primitive as parameters.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create type.
     * <li>Create static method in the type, which has Integer class as
     * parameters.
     * <li>Create Statement instance: new Statement(Type.class,methodName,new
     * Object[]{args}).
     * <li>Execute statement.
     * <li>Verify selected method was invoked with selected arguments.
     */
    public Result testStaticWithIntegerAsParam() throws Exception {
        new Statement(Methods.class, "static_with_params_2",
            new Object[] { new Integer(10) }).execute();
        return passed();
    }

    /**
     * Verify that Statement class sets a value of element of array.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create Integer array.
     * <li>Create Statement instance: new Statement(array,"set",new
     * Object[]{numberOfElement,value}).
     * <li>Execute statement.
     * <li>Verify that selected element of array was set to given value.
     */
    public Result testSetValueElementOfArray() throws Exception {
        Integer[] integers = new Integer[] { new Integer(3), new Integer(4) };
        new Statement(integers, "set", new Object[] { new Integer(0),
            new Integer(6) }).execute();
        assertEquals(integers[0], new Integer(6));
        return passed();
    }

    /**
     * Verify that if method to be invoked throws exception, execute method of
     * Statement class also throws the same exception.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create type.
     * <li>Create method in the type, which throws exception.
     * <li>Create object of this type.
     * <li>Create Statement instance: new Statement(object,methodName,new
     * Object[]{}).
     * <li>Execute statement.
     * <li>Verify that exception was thrown.
     */

    public Result testException() throws Exception {
        try {
            new Statement(new Methods(), "throwException", new Object[0])
                .execute();
        } catch (StatementException e) {
            return passed();
        }
        return failed("Exception wasn't thrown");
    }
}