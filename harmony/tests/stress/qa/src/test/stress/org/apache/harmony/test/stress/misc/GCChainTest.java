/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */    

/** * @author Vladimir Nenashev * @version $Revision: 1.5 $ */
package org.apache.harmony.test.stress.misc;

import junit.framework.TestCase;

public class GCChainTest extends TestCase
{
    public GCChainTest prev;
    public void test()
    {
        try
        {
            //int i = 0;
            GCChainTest p = null;
            while (true)
            {
                GCChainTest c = new GCChainTest();
                c.prev = p;
                p = c;
                //if (i % 1000 == 0)
                //    ReliabilityRunner.debug("i=" + i);
                //i++;
            }
        }
        catch (OutOfMemoryError e)
        {
        }
    }
}
