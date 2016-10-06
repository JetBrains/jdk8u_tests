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
 * @version $Revision: 1.8 $
 */
package org.apache.harmony.harness.jtests;

import java.util.ArrayList;

import org.apache.harmony.harness.TestIR;

import junit.framework.TestCase;

public class TestIRTest extends TestCase {

    /*
     * Class under test for void TestIR()
     */
    public final void testTestIR() {
        TestIR test = new TestIR();
        assertTrue(test.getRunnerID() == null);
        assertTrue(test.getTestID() == null);
        assertTrue(test.getRunnerParam() instanceof ArrayList);
        assertTrue(test.getRunnerParam().size() == 0);
        assertTrue(test.getTestTimeout() == 0);
        assertTrue(test.getKeywords() == null);
        assertTrue(test.getResources() == null);
        assertTrue(test.getModifications() == null);
        assertFalse(test.getOtherVMOnly());
        assertFalse(test.getSameVMOnly());
        assertFalse(test.getSameVMOne());
    }

    /*
     * Class under test for void TestIR(String)
     */
    public final void testTestIRString() {
        TestIR test = new TestIR("qwerty");
        assertTrue("Runtime".equals(test.getRunnerID()));
        assertTrue("qwerty".equals(test.getTestID()));
        assertTrue(test.getRunnerParam() instanceof ArrayList);
        assertTrue(test.getRunnerParam().size() == 0);
        assertTrue(test.getTestTimeout() == 1);
        assertTrue(test.getKeywords() instanceof String[]);
        assertTrue(test.getKeywords().length == 0);
        assertTrue(test.getResources() instanceof String[]);
        assertTrue(test.getResources().length == 0);
        assertTrue(test.getModifications() == null);
        assertFalse(test.getOtherVMOnly());
        assertFalse(test.getSameVMOnly());
        assertFalse(test.getSameVMOne());
    }

    /*
     * Class under test for void TestIR(String, String)
     */
    public final void testTestIRStringString() {
        TestIR test = new TestIR("asdfg", "qwerty");
        assertTrue("asdfg".equals(test.getRunnerID()));
        assertTrue("qwerty".equals(test.getTestID()));
        assertTrue(test.getRunnerParam() instanceof ArrayList);
        assertTrue(test.getRunnerParam().size() == 0);
        assertTrue(test.getTestTimeout() == 1);
        assertTrue(test.getKeywords() instanceof String[]);
        assertTrue(test.getKeywords().length == 0);
        assertTrue(test.getResources() instanceof String[]);
        assertTrue(test.getResources().length == 0);
        assertTrue(test.getModifications() == null);
        assertFalse(test.getOtherVMOnly());
        assertFalse(test.getSameVMOnly());
        assertFalse(test.getSameVMOne());
    }

    /*
     * Class under test for void TestIR(String, ArrayList, String, int,
     * String[], String[], boolean, boolean, boolean)
     */
    public final void testTestIRStringArrayListStringintStringArrayStringArraybooleanbooleanboolean() {
        ArrayList list = new ArrayList();
        String[] arr1 = { "qwerty" };
        String[] arr2 = { "asdfg" };
        TestIR test = new TestIR("odin", list, "dva", 33, arr1, arr2, true,
            true, true);
        assertTrue("odin".equals(test.getRunnerID()));
        assertTrue("dva".equals(test.getTestID()));
        assertTrue(test.getRunnerParam() == list);
        assertTrue(test.getTestTimeout() == 33);
        assertTrue(test.getKeywords() == arr1);
        assertTrue(test.getResources() == arr2);
        assertTrue(test.getModifications() == null);
        assertTrue(test.getOtherVMOnly());
        assertTrue(test.getSameVMOnly());
        assertTrue(test.getSameVMOne());
    }

    public final void testGetRunnerID() {
        TestIR test = new TestIR();
        test.setRunnerID("qwerty");
        assertTrue("qwerty".equals(test.getRunnerID()));
        test.setRunnerID("qWeRtY");
        assertTrue("qWeRtY".equals(test.getRunnerID()));
        test.setRunnerID(null);
        assertTrue("Unspecified".equals(test.getRunnerID()));
    }

    public final void testSetRunnerID() {
        testGetRunnerID();
    }

    public final void testGetRunnerParam() {
        TestIR test = new TestIR();
        ArrayList tmp = new ArrayList();
        test.setRunnerParam(tmp);
        assertTrue(tmp.equals(test.getRunnerParam()));
        try {
            test.setRunnerParam(null);
            assertTrue(false);
        } catch (NullPointerException e) {
            //do noting
        }
    }

    public final void testSetRunnerParam() {
        testGetRunnerParam();
    }

    public final void testGetModifications() {
        TestIR test = new TestIR();
        try {
            test.setModifications(null);
            assertTrue(false);
        } catch (NullPointerException e) {
            //do noting
        }
        ArrayList list = new ArrayList();
        test.setModifications(list);
        assertTrue(list == test.getModifications());
    }

    public final void testSetModifications() {
        testGetModifications();
    }

    public final void testGetTestID() {
        TestIR test = new TestIR();
        test.setTestID("qwerty");
        assertTrue("qwerty".equals(test.getTestID()));
        test.setTestID("qWeRtY");
        assertTrue("qWeRtY".equals(test.getTestID()));
        test.setTestID(null);
        assertTrue("Unspecified".equals(test.getTestID()));
    }

    public final void testSetTestID() {
        testGetTestID();
    }

    public final void testGetTestTimeout() {
        TestIR test = new TestIR();
        test.setTestTimeout(100);
        assertTrue(test.getTestTimeout() == 100);
        test.setTestTimeout(Integer.MAX_VALUE);
        assertTrue(test.getTestTimeout() == Integer.MAX_VALUE);
        try {
            test.setTestTimeout(-1);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            //do noting
        }
        try {
            test.setTestTimeout(0);
            assertTrue(false);
        } catch (NullPointerException e) {
            //do noting
        }
    }

    public final void testSetTestTimeout() {
        testGetTestTimeout();
    }

    public final void testGetKeywords() {
        TestIR test = new TestIR();
        String[] tst = new String[1];
        tst[0] = "qwerty";
        test.setKeywords(tst);
        assertTrue(tst.equals(test.getKeywords()));
        test.setKeywords(null);
        assertTrue(test.getKeywords() instanceof String[]);
    }

    public final void testSetKeywords() {
        testGetKeywords();
    }

    public final void testGetResources() {
        TestIR test = new TestIR();
        String[] tst = new String[1];
        tst[0] = "qwerty";
        test.setResources(tst);
        assertTrue(tst.equals(test.getResources()));
        test.setResources(null);
        assertTrue(test.getResources() instanceof String[]);
    }

    public final void testSetResources() {
        testGetResources();
    }

    public final void testGetSameVMOnly() {
        TestIR test = new TestIR();
        test.setSameVMOnly(true);
        assertTrue(test.getSameVMOnly());
        test.setSameVMOnly(false);
        assertFalse(test.getSameVMOnly());
    }

    public final void testSetSameVMOnly() {
        testGetSameVMOnly();
    }

    public final void testGetOtherVMOnly() {
        TestIR test = new TestIR();
        test.setOtherVMOnly(true);
        assertTrue(test.getOtherVMOnly());
        test.setOtherVMOnly(false);
        assertFalse(test.getOtherVMOnly());
    }

    public final void testSetOtherVMOnly() {
        testGetOtherVMOnly();
    }

    public final void testGetSameVMOne() {
        TestIR test = new TestIR();
        test.setSameVMOne(true);
        assertTrue(test.getSameVMOne());
        test.setSameVMOne(false);
        assertFalse(test.getSameVMOne());
    }

    public final void testSetSameVMOne() {
        testGetSameVMOne();
    }

    public final void testGetProperty() {
        TestIR test = new TestIR();
        test.setProperty("qwerty", "asdf");
        assertTrue("asdf".equals(test.getProperty("qwerty")));
        test.setProperty("qWeRtY", "zxcv");
        assertTrue("zxcv".equals(test.getProperty("qWeRtY")));
        test.setProperty(null, "qwerty");
        assertTrue("qwerty".equals(test.getProperty(null)));
        assertTrue("asdf".equals(test.getProperty("qwerty")));
        assertTrue("zxcv".equals(test.getProperty("qWeRtY")));
    }

    public final void testSetProperty() {
        testGetProperty();
    }
}