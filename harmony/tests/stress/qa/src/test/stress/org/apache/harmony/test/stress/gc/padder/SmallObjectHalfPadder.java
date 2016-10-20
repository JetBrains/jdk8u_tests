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

import java.util.List;

/**
 * Memory padder by small objects (int[10][10][10]).
 * 
 * @author Alexander D. Shipilov
 * @version $Revision: 1.4 $
 * 
 */
public class SmallObjectHalfPadder extends ObjectPadder {
    private static boolean skip;

    /**
     * Adds small object to list specified.
     * 
     * @param arrayOfObjects
     *            List to add small object.
     */
    public void addObject(List arrayOfObjects) {
        if (skip) {
            skipObj = new int[100][100][100];
            skipObj = null;
        } else {
            arrayOfObjects.add(new int[10][10][10]);
        }
    }
}
