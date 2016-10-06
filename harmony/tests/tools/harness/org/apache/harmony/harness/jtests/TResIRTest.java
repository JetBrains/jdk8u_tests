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
 * @version $Revision: 1.11 $
 */
package org.apache.harmony.harness.jtests;

import java.io.File;
import java.util.ArrayList;

import org.apache.harmony.harness.TResIR;

import junit.framework.TestCase;

public class TResIRTest extends TestCase {

    /**
     * Constructor for TResIRTest.
     * 
     * @param name
     */
    public TResIRTest(String name) {
        super(name);
    }

    public final void testTResIR() {
        TResIR tres = new TResIR("qwerty", "tmp", "asdfg", "zxcvbn");
        assertTrue("qwerty".equals(tres.getTestedOS()));
        assertTrue("asdfg".equals(tres.getTestedVM()));
        assertTrue("zxcvbn".equals(tres.getTestID()));
        assertTrue(tres.getDate() != null);
    }

    public final void testGetTestedOS() {
        TResIR tres = new TResIR("", "", "", "");
        tres.setTestedOS("qwerty");
        assertTrue("qwerty".equals(tres.getTestedOS()));
        tres.setTestedOS("qWeRtY");
        assertTrue("qWeRtY".equals(tres.getTestedOS()));
        tres.setTestedOS(null);
        assertTrue("Unspecified".equals(tres.getTestedOS()));
    }

    public final void testSetTestedOS() {
        testGetTestedOS();
    }

    public final void testGetTestedVM() {
        TResIR tres = new TResIR("", "", "", "");
        tres.setTestedVM("qwerty");
        assertTrue("qwerty".equals(tres.getTestedVM()));
        tres.setTestedVM("qWeRtY");
        assertTrue("qWeRtY".equals(tres.getTestedVM()));
        tres.setTestedVM(null);
        assertTrue("Unspecified".equals(tres.getTestedVM()));
    }

    public final void testSetTestedVM() {
        testGetTestedVM();
    }

    public final void testGetDate() {
        TResIR tres = new TResIR("", "", "", "");
        tres.setDate("qwerty");
        assertTrue("qwerty".equals(tres.getDate()));
        tres.setDate("qWeRtY");
        assertTrue("qWeRtY".equals(tres.getDate()));
        tres.setDate(null);
        assertTrue(tres.getDate() != null);
    }

    public final void testSetDate() {
        testGetDate();
    }

    public final void testGetTestID() {
        TResIR tres = new TResIR("", "", "", "");
        tres.setTestID("qwerty");
        assertTrue("qwerty".equals(tres.getTestID()));
        tres.setTestID("qWeRtY");
        assertTrue("qWeRtY".equals(tres.getTestID()));
        tres.setTestID(null);
        assertTrue("Unspecified".equals(tres.getTestID()));
    }

    public final void testSetTestID() {
        testGetTestID();
    }

    public final void testGetOutMsg() {
        TResIR tres = new TResIR("", "", "", "");
        tres.setOutMsg("qwerty");
        assertTrue("qwerty".equals(tres.getOutMsg()[0]));
        tres.setOutMsg("qWeRtY");
        assertTrue("qWeRtY".equals(tres.getOutMsg()[1]));
        tres.setOutMsg((String)null);
        assertTrue(tres.getOutMsg().length == 0);
    }

    public final void testSetOutMsg() {
        testGetOutMsg();
    }

    public final void testGetRepFile() {
        TResIR tres = new TResIR("", "", "", "");
        tres.setRepFile("qwerty");
        assertTrue("qwerty".equals(tres.getRepFile()));
        tres.setRepFile("qWeRtY");
        assertTrue("qWeRtY".equals(tres.getRepFile()));
        tres.setRepFile(null);
        assertTrue((System.getProperty("user.dir") + File.separator + "testRes")
            .equals(tres.getRepFile()));
    }

    public final void testSetRepFile() {
        testGetRepFile();
    }

    public final void testGetExecCmd() {
        TResIR tres = new TResIR("", "", "", "");
        String[] tmpStr = null;
        ArrayList tmpArr = null;
        tres.setExecCmd("qwerty", tmpStr);
        assertTrue(tres.getExecCmd().length == 0);
        tres.setExecCmd("qwerty", tmpArr);
        assertTrue(tres.getExecCmd().length == 0);
        tmpStr = new String[1];
        tmpStr[0] = "asdf";
        tmpArr = new ArrayList();
        tmpArr.add("asdf");
        tres.setExecCmd("qwerty", tmpStr);
        assertTrue("qwerty".equals(tres.getExecCmd()[0]));
        assertTrue("asdf".equals(tres.getExecCmd()[1]));
        tres.setExecCmd("qwerty", tmpArr);
        assertTrue("qwerty".equals(tres.getExecCmd()[2]));
        assertTrue("asdf".equals(tres.getExecCmd()[3]));
    }

    /*
     * Class under test for String setExecCmd(String, String[])
     */
    public final void testSetExecCmdStringStringArray() {
        testGetExecCmd();
    }

    /*
     * Class under test for String setExecCmd(String, ArrayList)
     */
    public final void testSetExecCmdStringArrayList() {
        testGetExecCmd();
    }

    public final void testGetExecStat() {
        TResIR tres = new TResIR("", "", "", "");
        tres.setExecStat(0);
        assertTrue(tres.getExecStat() == 0);
        tres.setExecStat(Integer.MAX_VALUE);
        assertTrue(tres.getExecStat() == Integer.MAX_VALUE);
        tres.setExecStat(Integer.MIN_VALUE);
        assertTrue(tres.getExecStat() == Integer.MIN_VALUE);
    }

    public final void testSetExecStat() {
        testGetExecStat();
    }

    public final void testGetProperty() {
        TResIR tres = new TResIR("", "", "", "");
        tres.setProperty("qwerty", "asdf");
        assertTrue("asdf".equals(tres.getProperty("qwerty")));
        tres.setProperty("qWeRtY", "zxcv");
        assertTrue("zxcv".equals(tres.getProperty("qWeRtY")));
        tres.setProperty(null, "qwerty");
        assertTrue("qwerty".equals(tres.getProperty(null)));
        assertTrue("asdf".equals(tres.getProperty("qwerty")));
        assertTrue("zxcv".equals(tres.getProperty("qWeRtY")));
    }

    public final void testSetProperty() {
        testGetProperty();
    }
}