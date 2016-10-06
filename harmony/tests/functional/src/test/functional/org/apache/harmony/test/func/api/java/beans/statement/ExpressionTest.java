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

import java.beans.Expression;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 * Under test: Expression.
 * <p>
 * Purpose: Verify that Expression class invokes correctly a method, which
 * points in constructor of this class. Also verify that getValue() method
 * returns value which returns the invoked method.
 * 
 */
public class ExpressionTest extends MultiCase {

    public static void main(String[] args) {
        try {
            System.exit(new ExpressionTest().test(args));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Verify, that Expression creates instance of class.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create type, which has constructor with parameters.
     * <li>Create Expression instance: new Expression(Type.class,"new",new
     * Object[]{args}).
     * <li>Execute expression.
     * <li>Verify constructor was invoked with selected arguments.
     * <li>Verify that method getValue() of Expression returns instance of
     * type.
     */
    public Result testConstructor() throws Exception {
        Expression expression = new Expression(Methods.class, "new",
            new Object[] { "abcde", new Integer(7) });
        expression.execute();
        assertTrue(Methods.constructor);
        if (expression.getValue() instanceof Methods)
            return passed();
        return failed("getValue() returns not instance of Class1");
    }

    /**
     * Verify, that Expression invokes static method, which returns primitive
     * type.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create type.
     * <li>Create static method without parameters in this type. Also this
     * method always returns the same value of primitive type.
     * <li>Create Expression instance: new Expression(Type.class,methodName,new
     * Object[]{}).
     * <li>Execute expression.
     * <li>Verify that method getValue() of Expression returns wrapper of
     * primitive type and verify value of this wrapper.
     */
    public Result testReturnPrimitiveType() throws Exception {
        Expression expression = new Expression(Methods.class,
            "static_return_primitive", new Object[] {});
        expression.execute();
        assertEquals(expression.getValue(), new Integer(5));
        return passed();
    }

    /**
     * Verify that Expression returns a value of element of array.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create Integer array.
     * <li>Create Expression instance: new Expression(array,"get",new
     * Object[]{numberOfElement}).
     * <li>Execute expression.
     * <li>Verify that method getValue() of Expression returns value of
     * selected element of array.
     */
    public Result testGetValueElementOfArray() throws Exception {
        Integer[] integers = new Integer[] { new Integer(4), new Integer(6) };
        Expression expression = new Expression(integers, "get",
            new Object[] { new Integer(0) });
        expression.execute();
        assertEquals(expression.getValue(), new Integer(4));
        return passed();
    }
}