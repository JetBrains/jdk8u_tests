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

package org.apache.harmony.test.stress.gc.padder;

import junit.framework.TestCase;

import org.apache.harmony.test.share.stress.ReliabilityRunner;
import org.apache.harmony.test.share.stress.util.CriticalSectionController;

import java.util.LinkedList;
import java.util.List;

/**
 * Garbage collector stress test. Pads memory by big objects.
 * 
 * @author Alexander D. Shipilov
 * @version $Revision: 1.6 $
 * 
 */
public abstract class ObjectPadder extends TestCase {
    public static List arrayOfObjects = new LinkedList();

    private final static int MIN_PERCENT = 10;

    private static int iterationNumber = 0;

    protected Object skipObj;

    /**
     * Adds object to the list. If <code>OutOfMemoryError</code> happened,
     * releases some memory.<Br>
     * Performs the test. Test parameters (class name and constructor arguments)
     * are passed through testArgs variable, which can be either initialized
     * explicitly or by calling the method test(String[] args) (parses
     * parameters and invokes method test()).
     */
    public void testPadding() {
        try {
            addObject(arrayOfObjects);
        } catch (OutOfMemoryError er) {
            CriticalSectionController.setCriticalSection(true);
            int percentToRelease;
            iterationNumber++;
            percentToRelease = 100 - iterationNumber;
            if (percentToRelease < MIN_PERCENT) {
                percentToRelease = MIN_PERCENT;
            }
            for (int k = 0; k < ((ObjectPadder.arrayOfObjects.size() * percentToRelease) / 100); k++) {
                ObjectPadder.arrayOfObjects.remove(0);
            }
            CriticalSectionController.setCriticalSection(false);
        }
    }

    /**
     * Adds object to the list.
     * 
     * @param arrayOfObjects
     *            List to add object.
     */
    public abstract void addObject(List arrayOfObjects);
}
