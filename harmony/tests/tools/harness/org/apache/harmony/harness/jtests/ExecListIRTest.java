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

import org.apache.harmony.harness.ExecListIR;
import org.apache.harmony.harness.TestIR;

import junit.framework.TestCase;

public class ExecListIRTest extends TestCase {

    public final void testAdd() {
        ExecListIR ex = new ExecListIR();
        TestIR test = new TestIR();
        TestIR test1 = new TestIR();
        ex.add(test);
        ex.add(test1);
        assertTrue(ex.size() == 2);
    }

    public final void testRemove() {
        ExecListIR ex = new ExecListIR();
        TestIR test = new TestIR();
        TestIR test1 = new TestIR();
        ex.add(test);
        ex.add(test1);
        assertTrue(ex.size() == 2);
        assertTrue(test == ex.remove());
        assertTrue(ex.size() == 1);
        assertTrue(test1 == ex.remove());
    }

    public final void testSize() {
        ExecListIR ex = new ExecListIR();
        TestIR test = new TestIR();
        assertTrue(ex.size() == 0);
        ex.add(test);
        assertTrue(ex.size() == 1);
        ex.add(test);
        assertTrue(ex.size() == 2);
    }
}