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

import org.apache.harmony.harness.ConfigIR;

import junit.framework.TestCase;

public class ConfigIRTest extends TestCase {

    /*
     * Class under test for void ConfigIR()
     */
    public final void testConfigIR() {
        ConfigIR cfg = new ConfigIR();
        assertTrue(cfg.getGenTimeout() == 0);
    }
}