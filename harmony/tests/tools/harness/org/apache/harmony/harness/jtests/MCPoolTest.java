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
 * @version $Revision: 1.7 $
 */
package org.apache.harmony.harness.jtests;

import org.apache.harmony.harness.MCPool;
import org.apache.harmony.harness.MCoreIR;

import junit.framework.TestCase;

public class MCPoolTest extends TestCase {

    public final void testGet() {
        MCPool mcp = new MCPool();
        assertTrue(mcp.get(-1) == null);
        assertTrue(mcp.get(0) == null);
        assertTrue(mcp.get(1) == null);
    }

    public final void testSize() {
        MCPool mcp = new MCPool();
        assertTrue(mcp.size() == 0);
        MCoreIR mc = null;
        mcp.add(mc);
        assertTrue(mcp.size() == 0);
    }
}