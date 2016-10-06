/*
    Copyright 2005-2006 The Apache Software Foundation or its licensors, as applicable

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

    See the License for the specific language governing permissions and
    limitations under the License.
*/
/**
 * @author V.Ivanov
 * @version $Revision: 1.6 $
 */
package org.apache.harmony.harness.jtests;

import org.apache.harmony.harness.TestIR;
import org.apache.harmony.harness.TestSuiteIR;

import junit.framework.TestCase;

public class TestSuiteIRTest extends TestCase {

    /**
     * Constructor for TestSuiteIRTest.
     * 
     * @param name
     */
    public TestSuiteIRTest(String name) {
        super(name);
    }

    public final void testGet() {
        TestSuiteIR ts = new TestSuiteIR();
        TestIR test = new TestIR();
        ts.add("qwerty", test);
        assertTrue(test.equals(ts.get("qwerty")));
    }

    public final void testAdd() {
        testGet();
    }

    public final void testRemove() {
        TestSuiteIR ts = new TestSuiteIR();
        TestIR test = new TestIR();
        ts.add("qwerty", test);
        ts.add("qwertyQ", test);
        assertTrue(test.equals(ts.get("qwerty")));
        ts.remove("qwerty");
        assertFalse(test.equals(ts.get("qwerty")));
        assertTrue(test.equals(ts.get("qwertyQ")));
    }

    public final void testRemoveAll() {
        TestSuiteIR ts = new TestSuiteIR();
        TestIR test = new TestIR();
        ts.add("qwerty", test);
        ts.add("qwertyQ", test);
        assertTrue(ts.size() == 2);
        ts.removeAll();
        assertTrue(ts.size() == 0);
    }

    public final void testSize() {
        TestSuiteIR ts = new TestSuiteIR();
        TestIR test = new TestIR();
        ts.add("qwerty", test);
        assertTrue(ts.size() == 1);
        ts.add("qwertyQ", test);
        assertTrue(ts.size() == 2);
        ts.add("qwertyQ", test);
        assertTrue(ts.size() == 2);
    }
}