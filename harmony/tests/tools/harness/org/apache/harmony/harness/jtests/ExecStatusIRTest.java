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

import org.apache.harmony.harness.ExecStatusIR;

import junit.framework.TestCase;

public class ExecStatusIRTest extends TestCase {

    public final void testGetStatus() {
        ExecStatusIR ex = new ExecStatusIR();
        ex.setStatus("qwerty", 100);
        assertTrue(ex.getStatus("qwerty") == 100);
        ex.setStatus("qwerty", 99);
        assertTrue(ex.getStatus("qwerty") == 99);
        ex.setStatus("qwertY", 100);
        assertTrue(ex.getStatus("qwertY") == 100);
    }

    public final void testSetStatus() {
        testGetStatus();
    }
}