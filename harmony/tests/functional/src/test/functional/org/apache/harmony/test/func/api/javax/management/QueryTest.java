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
package org.apache.harmony.test.func.api.javax.management;

import java.math.BigDecimal;

import javax.management.AttributeValueExp;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.Query;
import javax.management.QueryEval;
import javax.management.QueryExp;
import javax.management.ValueExp;

import org.apache.harmony.test.func.api.javax.management.share.Hello;
import org.apache.harmony.test.func.api.javax.management.share.framework.Test;
import org.apache.harmony.test.func.api.javax.management.share.framework.TestRunner;

/**
 * Test for the class javax.management.Query
 * 
 */
public class QueryTest extends Test {

    /**
     * Object names.
     */
    private ObjectName[] names;

    /**
     * MBean instances.
     */
    private Hello[]      instances;

    /**
     * MBean server.
     */
    MBeanServer          mbs;

    /**
     * Test for the constructor Query()
     * 
     * @see javax.management.Query#Query()
     */
    public final void testQuery() {
        new Query();
    }

    /**
     * Test for the method and(javax.management.QueryExp,
     * javax.management.QueryExp)
     * 
     * @throws Exception
     * @see javax.management.Query#and(javax.management.QueryExp,
     *      javax.management.QueryExp)
     */
    public final void testAnd() throws Exception {
        QueryExp exp1 = Query.eq(Query.attr("Attribute1"), Query
            .value("attribute1"));
        QueryExp exp2 = Query.eq(Query.attr("Attribute2"), Query
            .value("attribute2"));
        QueryExp exp3 = Query.eq(Query.attr("IntNumber1"), Query.value(1));

        assertTrue(Query.and(exp1, exp2).apply(names[0]));
        assertFalse(Query.and(exp1, exp3).apply(names[0]));
    }

    /**
     * Test for the method anySubString(javax.management.AttributeValueExp,
     * javax.management.StringValueExp)
     * 
     * @see javax.management.Query#anySubString(javax.management.AttributeValueExp,
     *      javax.management.StringValueExp)
     */
    public final void testAnySubString() throws Exception {
        assertTrue(Query.anySubString(Query.attr("Attribute1"),
            Query.value("1")).apply(names[0]));
        assertTrue(Query.anySubString(Query.attr("Attribute2"),
            Query.value("attr")).apply(names[0]));
        assertFalse(Query.anySubString(Query.attr("Attribute3"),
            Query.value("Attr")).apply(names[0]));
    }

    /**
     * Test for the method attr(java.lang.String)
     * 
     * @see javax.management.Query#attr(java.lang.String)
     */
    public final void testAttrString() throws Exception {
        for (instances[2].point = 0; instances[2].point < 8; instances[2].point++) {
            try {
                Query.attr("NumberSubcl").apply(names[2]);
            } catch (Exception e) {
                fail("Attribute type: "
                    + instances[2].getNumberSubcl().getClass().getName(), e);
            }
        }

        assertTrue(Query.match(Query.attr("Attribute1"),
            Query.value("attribute1")).apply(names[0]));
        assertFalse(Query.match(Query.attr("Attribute2"),
            Query.value("attribute1")).apply(names[0]));
    }

    /**
     * Test for the method attr(java.lang.String, java.lang.String)
     * 
     * @see javax.management.Query#attr(java.lang.String, java.lang.String)
     */
    public final void testAttrStringString() throws Exception {
        for (instances[2].point = 0; instances[2].point < 8; instances[2].point++) {
            try {
                Query.attr(Hello.class.getName(), "NumberSubcl")
                    .apply(names[2]);
            } catch (Exception e) {
                fail("Attribute type: "
                    + instances[2].getNumberSubcl().getClass().getName(), e);
            }
        }

        assertTrue(Query.eq(
            Query.attr(Hello.class.getName(), "Number").apply(names[1]),
            Query.value(123)).apply(names[1]));
        assertTrue(Query.eq(
            Query.attr(Hello.class.getName(), "Number1").apply(names[1]),
            Query.value(123123123)).apply(names[1]));
        assertTrue(Query.eq(
            Query.attr(Hello.class.getName(), "Number1").apply(names[1]),
            Query.value(new BigDecimal((double) 123123123))).apply(names[1]));
        assertFalse(Query.eq(
            Query.attr(Hello.class.getName(), "Number1").apply(names[1]),
            Query.value(new BigDecimal((double) 123))).apply(names[1]));
    }

    /**
     * Test for the method between(javax.management.ValueExp,
     * javax.management.ValueExp, javax.management.ValueExp)
     * 
     * @see javax.management.Query#between(javax.management.ValueExp,
     *      javax.management.ValueExp, javax.management.ValueExp)
     */
    public final void testBetween() throws Exception {
        assertTrue(Query
            .between(Query.value(2), Query.value(1), Query.value(3)).apply(
                names[0]));
        assertFalse(Query.between(Query.value(2), Query.value(4),
            Query.value(3)).apply(names[0]));
    }

    /**
     * Test for the method classattr()
     * 
     * @see javax.management.Query#classattr()
     */
    public final void testClassattr() throws Exception {
        assertNotNull(Query.classattr());
        assertTrue(AttributeValueExp.class.isAssignableFrom(Query.classattr()
            .getClass()));
    }

    /**
     * Test for the method div(javax.management.ValueExp,
     * javax.management.ValueExp)
     * 
     * @see javax.management.Query#div(javax.management.ValueExp,
     *      javax.management.ValueExp)
     */
    public final void testDiv() throws Exception {
        assertTrue(Query.eq(Query.value(3),
            Query.div(Query.value(10), Query.value(3))).apply(names[0]));
        assertTrue(Query.eq(Query.value(0),
            Query.div(Query.value(0), Query.value(3))).apply(names[0]));
        assertFalse(Query.eq(Query.value(0),
            Query.div(Query.value(10), Query.value(3))).apply(names[0]));
    }

    /**
     * Test for the method eq(javax.management.ValueExp,
     * javax.management.ValueExp)
     * 
     * @see javax.management.Query#eq(javax.management.ValueExp,
     *      javax.management.ValueExp)
     */
    public final void testEq() throws Exception {
        assertTrue(Query.eq(Query.attr("IntNumber1"), Query.attr("IntNumber2"))
            .apply(names[1]));
        assertFalse(Query
            .eq(Query.attr("IntNumber1"), Query.attr("IntNumber2")).apply(
                names[2]));

        assertTrue(Query.eq(Query.attr("LongNumber1"),
            Query.attr("LongNumber2")).apply(names[1]));
        assertFalse(Query.eq(Query.attr("LongNumber1"),
            Query.attr("LongNumber2")).apply(names[2]));

        assertTrue(Query.eq(Query.attr("DoubleNumber1"),
            Query.attr("DoubleNumber2")).apply(names[1]));
        assertFalse(Query.eq(Query.attr("DoubleNumber1"),
            Query.attr("DoubleNumber2")).apply(names[2]));

        assertTrue(Query.eq(Query.attr("FloatNumber1"),
            Query.attr("FloatNumber2")).apply(names[1]));
        assertFalse(Query.eq(Query.attr("FloatNumber1"),
            Query.attr("FloatNumber2")).apply(names[2]));

        assertTrue(Query.eq(Query.attr("Boolean1"), Query.attr("Boolean2"))
            .apply(names[1]));
        assertFalse(Query.eq(Query.attr("Boolean1"), Query.attr("Boolean2"))
            .apply(names[2]));

        assertTrue(Query.eq(Query.attr("Number1"), Query.attr("Number1"))
            .apply(names[2]));
        assertFalse(Query.eq(Query.attr("Number1"), Query.attr("Number2"))
            .apply(names[2]));
    }

    /**
     * Test for the method finalSubString(javax.management.AttributeValueExp,
     * javax.management.StringValueExp)
     * 
     * @see javax.management.Query#finalSubString(javax.management.AttributeValueExp,
     *      javax.management.StringValueExp)
     */
    public final void testFinalSubString() throws Exception {
        assertTrue(Query.finalSubString(Query.attr("Attribute1"),
            Query.value("1")).apply(names[0]));
        assertTrue(Query.finalSubString(Query.attr("Attribute2"),
            Query.value("ibute2")).apply(names[0]));
        assertFalse(Query.finalSubString(Query.attr("Attribute1"),
            Query.value("attribute")).apply(names[0]));
    }

    /**
     * Test for the method geq(javax.management.ValueExp,
     * javax.management.ValueExp)
     * 
     * @see javax.management.Query#geq(javax.management.ValueExp,
     *      javax.management.ValueExp)
     */
    public final void testGeq() throws Exception {
        String[] attrs = new String[] { "IntNumber1", "IntNumber2",
            "LongNumber1", "LongNumber2", "DoubleNumber1", "DoubleNumber2",
            "FloatNumber1", "FloatNumber2", "Number1", "Number2" };

        for (int i = 0; i < attrs.length; i += 2) {
            assertTrue(attrs[i + 1] + " > " + attrs[i], Query.geq(
                Query.attr(attrs[i + 1]), Query.attr(attrs[i])).apply(names[2]));
            assertTrue(attrs[i] + " == " + attrs[i], Query.geq(
                Query.attr(attrs[i]), Query.attr(attrs[i])).apply(names[2]));
            assertFalse(attrs[i] + " > " + attrs[i + 1], Query.geq(
                Query.attr(attrs[i]), Query.attr(attrs[i + 1])).apply(names[2]));
        }
    }

    /**
     * Test for the method gt(javax.management.ValueExp,
     * javax.management.ValueExp)
     * 
     * @see javax.management.Query#gt(javax.management.ValueExp,
     *      javax.management.ValueExp)
     */
    public final void testGt() throws Exception {
        String[] attrs = new String[] { "IntNumber1", "IntNumber2",
            "LongNumber1", "LongNumber2", "DoubleNumber1", "DoubleNumber2",
            "FloatNumber1", "FloatNumber2", "Number1", "Number2" };

        for (int i = 0; i < attrs.length; i += 2) {
            assertTrue(attrs[i + 1] + " > " + attrs[i], Query.gt(
                Query.attr(attrs[i + 1]), Query.attr(attrs[i])).apply(names[2]));
            assertFalse(attrs[i] + " > " + attrs[i + 1], Query.gt(
                Query.attr(attrs[i]), Query.attr(attrs[i + 1])).apply(names[2]));
        }
    }

    /**
     * Test for the method in(javax.management.ValueExp,
     * javax.management.ValueExp[])
     * 
     * @see javax.management.Query#in(javax.management.ValueExp,
     *      javax.management.ValueExp[])
     */
    public final void testIn() throws Exception {
        String[] attrs = new String[] { "IntNumber1", "IntNumber2",
            "LongNumber1", "LongNumber2", "DoubleNumber1", "DoubleNumber2",
            "FloatNumber1", "FloatNumber2", "Number1", "Number2" };

        ValueExp[] v1 = new ValueExp[] { Query.value(10), Query.value(20),
            Query.value(30) };
        ValueExp[] v2 = new ValueExp[] { Query.value(20), Query.value(30),
            Query.value(40) };

        for (int i = 0; i < attrs.length; i += 2) {
            assertTrue(attrs[i] + " in [10, 20, 30]", Query.in(
                Query.attr(attrs[i]), v1).apply(names[2]));
            assertFalse(attrs[i] + " in [20, 30, 40]", Query.in(
                Query.attr(attrs[i]), v2).apply(names[2]));
        }
    }

    /**
     * Test for the method initialSubString(javax.management.AttributeValueExp,
     * javax.management.StringValueExp)
     * 
     * @see javax.management.Query#initialSubString(javax.management.AttributeValueExp,
     *      javax.management.StringValueExp)
     */
    public final void testInitialSubString() throws Exception {
        assertTrue(instances[0].getAttribute1(), Query.initialSubString(
            Query.attr("Attribute1"), Query.value("attribute")).apply(names[0]));
        assertTrue(Query.initialSubString(Query.attr("Attribute2"),
            Query.value("a")).apply(names[0]));
        assertFalse(Query.initialSubString(Query.attr("Attribute1"),
            Query.value("ttribute1")).apply(names[0]));
    }

    /**
     * Test for the method leq(javax.management.ValueExp,
     * javax.management.ValueExp)
     * 
     * @see javax.management.Query#leq(javax.management.ValueExp,
     *      javax.management.ValueExp)
     */
    public final void testLeq() throws Exception {
        String[] attrs = new String[] { "IntNumber1", "IntNumber2",
            "LongNumber1", "LongNumber2", "DoubleNumber1", "DoubleNumber2",
            "FloatNumber1", "FloatNumber2", "Number1", "Number2" };

        for (int i = 0; i < attrs.length; i += 2) {
            assertTrue(attrs[i + 1] + " < " + attrs[i], Query.leq(
                Query.attr(attrs[i]), Query.attr(attrs[i + 1])).apply(names[2]));
            assertTrue(attrs[i] + " == " + attrs[i], Query.leq(
                Query.attr(attrs[i]), Query.attr(attrs[i])).apply(names[2]));
            assertFalse(attrs[i] + " < " + attrs[i + 1], Query.leq(
                Query.attr(attrs[i + 1]), Query.attr(attrs[i])).apply(names[2]));
        }
    }

    /**
     * Test for the method lt(javax.management.ValueExp,
     * javax.management.ValueExp)
     * 
     * @see javax.management.Query#lt(javax.management.ValueExp,
     *      javax.management.ValueExp)
     */
    public final void testLt() throws Exception {
        String[] attrs = new String[] { "IntNumber1", "IntNumber2",
            "LongNumber1", "LongNumber2", "DoubleNumber1", "DoubleNumber2",
            "FloatNumber1", "FloatNumber2", "Number1", "Number2" };

        for (int i = 0; i < attrs.length; i += 2) {
            assertTrue(attrs[i + 1] + " < " + attrs[i], Query.lt(
                Query.attr(attrs[i]), Query.attr(attrs[i + 1])).apply(names[2]));
            assertFalse(attrs[i] + " < " + attrs[i + 1], Query.lt(
                Query.attr(attrs[i + 1]), Query.attr(attrs[i])).apply(names[2]));
        }
    }

    /**
     * Test for the method match(javax.management.AttributeValueExp,
     * javax.management.StringValueExp)
     * 
     * @see javax.management.Query#match(javax.management.AttributeValueExp,
     *      javax.management.StringValueExp)
     */
    public final void testMatch() throws Exception {
        assertTrue(Query.match(Query.attr("Attribute1"),
            Query.value("attribute1")).apply(names[0]));
        assertFalse(Query.match(Query.attr("Attribute1"),
            Query.value("attribute2")).apply(names[0]));

        instances[0].setAttribute1("abcdefg");
        testMatch(new String[] { "abcdefg", "??cdefg", "??cdef?*", "??c?ef*",
            "??c??fg", "??c*f?**", "*****?******", "*?d*?", "*?????g***",
            "*abcdefg*" }, false);

        instances[0].setAttribute1("abcabcabc");
        testMatch(new String[] { "a*a?*c", "a**?*c" }, false);

        instances[0].setAttribute1("abcwwwaxybcaxzbc");
        testMatch(new String[] { "a**?c*a*xz*", "a***a??b*" }, false);

        instances[0].setAttribute1("aasfgfwty");
        testMatch(
            new String[] { "?a*gfw*t*y", "aasfgfw***y", "[!bcd]asfgfwty" },
            false);

        instances[0].setAttribute1("aasf?fw*y");
        testMatch(new String[] { "aasf\\?fw\\*y", "aasf\\?fw[\\*-\\*]y",
            "aasf[\\?-\\?]fw*y" }, false);

        instances[0].setAttribute1("a*s?[f]g!f-wty");
        testMatch(new String[] { "a\\*s\\?\\[f]g!f-wty" }, false);

        instances[0].setAttribute1("aasfgfwty?");
        testMatch(new String[] { "*[a-z]*", "[a-z]asfgfwty?",
            "aasfgfwty[!\\!]", "aasfgfwty[\\?]", "aasfgfwty[ab\\?]",
            "**a[r-t][e-f]gfwty?" }, false);

        instances[0].setAttribute1("[a-b]a[r-t][!e-g]g]f[w*t?y");
        testMatch(new String[] { "\\[a-b]a\\[r-t]\\[!e-g]g]f\\[w\\*t\\?y" },
            false);

        instances[0].setAttribute1("aasfgfwty");
        testMatch(new String[] { "?a*gfw*t", "?aasfgfwty", "aasfgfwty?",
            "aasfgfwt?y*", "***a*a*s*f*g*f*w*t*y?", "?sfgfwty",
            "aasfgfwty[a-z]", "aasfgfwt[!x-z]" }, true);
    }

    /**
     * Test for the Query.match() method.
     * 
     * @param expr The array, containing reqular expressions.
     * @param invert if true - the match() method should return false.
     * @throws Exception
     */
    private void testMatch(String[] expr, boolean invert) throws Exception {
        for (int i = 0; i < expr.length; i++) {
            try {
                if (invert) {
                    assertFalse("Query.match(\"" + instances[0].getAttribute1()
                        + "\", \"" + expr[i] + "\")", Query.match(
                        Query.attr("Attribute1"), Query.value(expr[i])).apply(
                        names[0]));
                } else {
                    assertTrue("Query.match(\"" + instances[0].getAttribute1()
                        + "\", \"" + expr[i] + "\")", Query.match(
                        Query.attr("Attribute1"), Query.value(expr[i])).apply(
                        names[0]));
                }
            } catch (Throwable ex) {
                fail("Query.match(\"" + instances[0].getAttribute1() + "\", \""
                    + expr[i] + "\")", ex);
            }
        }
    }

    /**
     * Test for the method minus(javax.management.ValueExp,
     * javax.management.ValueExp)
     * 
     * @see javax.management.Query#minus(javax.management.ValueExp,
     *      javax.management.ValueExp)
     */
    public final void testMinus() throws Exception {
        String[] attrs = new String[] { "IntNumber1", "IntNumber2",
            "LongNumber1", "LongNumber2", "DoubleNumber1", "DoubleNumber2",
            "FloatNumber1", "FloatNumber2", "Number1", "Number2" };

        for (int i = 0; i < attrs.length; i += 2) {
            assertTrue(attrs[i + 1] + " - " + attrs[i], Query.eq(
                Query.minus(Query.attr(attrs[i + 1]), Query.attr(attrs[i])),
                Query.value(90)).apply(names[2]));
            assertFalse(attrs[i] + " - " + attrs[i + 1], Query.eq(
                Query.minus(Query.attr(attrs[i]), Query.attr(attrs[i + 1])),
                Query.value(90)).apply(names[2]));
        }
    }

    /**
     * Test for the method not(javax.management.QueryExp)
     * 
     * @see javax.management.Query#not(javax.management.QueryExp)
     */
    public final void testNot() throws Exception {
        String[] attrs = new String[] { "IntNumber1", "IntNumber2",
            "LongNumber1", "LongNumber2", "DoubleNumber1", "DoubleNumber2",
            "FloatNumber1", "FloatNumber2", "Number1", "Number2" };

        for (int i = 0; i < attrs.length; i += 2) {
            assertTrue(Query.not(
                Query.lt(Query.attr(attrs[i + 1]), Query.attr(attrs[i])))
                .apply(names[2]));
            assertFalse(Query.not(
                Query.lt(Query.attr(attrs[i]), Query.attr(attrs[i + 1])))
                .apply(names[2]));
        }
    }

    /**
     * Test for the method or(javax.management.QueryExp,
     * javax.management.QueryExp)
     * 
     * @see javax.management.Query#or(javax.management.QueryExp,
     *      javax.management.QueryExp)
     */
    public final void testOr() throws Exception {
        String[] attrs = new String[] { "IntNumber1", "IntNumber2",
            "LongNumber1", "LongNumber2", "DoubleNumber1", "DoubleNumber2",
            "FloatNumber1", "FloatNumber2", "Number1", "Number2" };

        for (int i = 0; i < attrs.length; i += 2) {
            assertTrue(Query.or(
                Query.eq(Query.attr(attrs[i]), Query.attr(attrs[i])),
                Query.eq(Query.attr(attrs[i]), Query.attr(attrs[i]))).apply(
                names[2]));
            assertTrue(Query.or(
                Query.lt(Query.attr(attrs[i]), Query.attr(attrs[i + 1])),
                Query.lt(Query.attr(attrs[i]), Query.attr(attrs[i + 1])))
                .apply(names[2]));
            assertFalse(Query.or(
                Query.eq(Query.attr(attrs[i]), Query.attr(attrs[i + 1])),
                Query.eq(Query.attr(attrs[i]), Query.attr(attrs[i + 1])))
                .apply(names[2]));
        }
    }

    /**
     * Test for the method plus(javax.management.ValueExp,
     * javax.management.ValueExp)
     * 
     * @see javax.management.Query#plus(javax.management.ValueExp,
     *      javax.management.ValueExp)
     */
    public final void testPlus() throws Exception {
        String[] attrs = new String[] { "IntNumber1", "IntNumber2",
            "LongNumber1", "LongNumber2", "DoubleNumber1", "DoubleNumber2",
            "FloatNumber1", "FloatNumber2", "Number1", "Number2" };

        for (int i = 0; i < attrs.length; i += 2) {
            ValueExp v1 = Query.attr(attrs[i]).apply(names[2]);
            ValueExp v2 = Query.attr(attrs[i + 1]).apply(names[2]);
            ValueExp res = Query.plus(v1, v2).apply(names[2]);
            assertTrue(v1 + " + " + v2 + " = " + res, Query.eq(
                Query.plus(Query.attr(attrs[i + 1]), Query.attr(attrs[i])),
                Query.value(110)).apply(names[2]));
            assertFalse(v1 + " + " + v2 + " = " + res, Query.eq(
                Query.plus(Query.attr(attrs[i + 1]), Query.attr(attrs[i])),
                Query.value(111)).apply(names[2]));
        }
    }

    /**
     * Test for the method times(javax.management.ValueExp,
     * javax.management.ValueExp)
     * 
     * @see javax.management.Query#times(javax.management.ValueExp,
     *      javax.management.ValueExp)
     */
    public final void testTimes() throws Exception {
        String[] attrs = new String[] { "IntNumber1", "IntNumber2",
            "LongNumber1", "LongNumber2", "DoubleNumber1", "DoubleNumber2",
            "FloatNumber1", "FloatNumber2", "Number1", "Number2" };

        for (int i = 0; i < attrs.length; i += 2) {
            ValueExp v1 = Query.attr(attrs[i]).apply(names[2]);
            ValueExp v2 = Query.attr(attrs[i + 1]).apply(names[2]);
            ValueExp res = Query.times(v1, v2).apply(names[2]);
            assertTrue(v1 + " * " + v2 + " = " + res, Query.eq(
                Query.times(Query.attr(attrs[i + 1]), Query.attr(attrs[i])),
                Query.value(1000)).apply(names[2]));
            assertFalse(v1 + " * " + v2 + " = " + res, Query.eq(
                Query.times(Query.attr(attrs[i + 1]), Query.attr(attrs[i])),
                Query.value(10001)).apply(names[2]));
        }
    }

    /**
     * Test for the method value(java.lang.Number)
     * 
     * @see javax.management.Query#value(java.lang.Number)
     */
    public final void testValueNumber() {
        // Tested in the above testcases.
    }

    /**
     * Test for the method value(java.lang.String)
     * 
     * @see javax.management.Query#value(java.lang.String)
     */
    public final void testValueString() {
        // Tested in the above testcases.
    }

    /**
     * Test for the method value(boolean)
     * 
     * @see javax.management.Query#value(boolean)
     */
    public final void testValueboolean() {
        // Tested in the above testcases.
    }

    /**
     * Test for the method value(double)
     * 
     * @see javax.management.Query#value(double)
     */
    public final void testValuedouble() {
        // Tested in the above testcases.
    }

    /**
     * Test for the method value(float)
     * 
     * @see javax.management.Query#value(float)
     */
    public final void testValuefloat() {
        // Tested in the above testcases.
    }

    /**
     * Test for the method value(int)
     * 
     * @see javax.management.Query#value(int)
     */
    public final void testValueint() {
        // Tested in the above testcases.
    }

    /**
     * Test for the method value(long)
     * 
     * @see javax.management.Query#value(long)
     */
    public final void testValuelong() {
        // Tested in the above testcases.
    }

    /**
     * Create MBean server, register MBeans.
     */
    public final void setUp() throws Exception {
        // Get the Platform MBean Server
        mbs = MBeanServerFactory.createMBeanServer();

        // Sets the MBean server on which the query is to be performed.
        new QueryEval() {
            private static final long serialVersionUID = 3832902139541599280L;
        }.setMBeanServer(mbs);

        instances = new Hello[10];
        names = new ObjectName[instances.length];

        instances[0] = new Hello();
        instances[1] = new Hello();
        instances[2] = new Hello();
        instances[1].setNumber(new Integer(123));
        instances[1].setNumber1(new BigDecimal((double) 123123123));
        instances[2].setIntNumber1(10);
        instances[2].setIntNumber2(100);
        instances[2].setLongNumber1(10);
        instances[2].setLongNumber2(100);
        instances[2].setDoubleNumber1(10);
        instances[2].setDoubleNumber2(100);
        instances[2].setFloatNumber1(10);
        instances[2].setFloatNumber2(100);
        instances[2].setBoolean1(true);
        instances[2].setBoolean2(false);
        instances[2].setNumber1(new BigDecimal((double) 10));
        instances[2].setNumber2(new BigDecimal((double) 100));

        // Register the Hello MBean
        for (int i = 0; i < instances.length; i++) {
            if (instances[i] != null) {
                names[i] = new ObjectName(
                    "org.apache.harmony.test.func.api.javax.management:type=Hello"
                        + i);
                mbs.registerMBean(instances[i], names[i]);
            }
        }
    }

    public void tearDown() {
        MBeanServerFactory.releaseMBeanServer(mbs);
        mbs = null;
        names = null;
        instances = null;
    }

    /**
     * Run the test.
     * 
     * @param args command line arguments.
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.exit(TestRunner.run(QueryTest.class, args));
    }
}
