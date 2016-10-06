/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
/**
 */
package org.apache.harmony.test.func.api.java.util.share.providers.java.util;

import java.util.*;

public class ListProvider{

    static public final int LINKEDLIST = 0;
    static public final int ARRAYLIST  = 1;
    static public final int VECTOR     = 2;
    static public final int STACK      = 3;

    public static List giveMe(int pos) throws NoSuchElementException {
        List toRet;

        switch (pos) {
        case 0: {
            toRet = new LinkedList();
            return toRet;
        }
        case 1: {
            toRet = new ArrayList();
            return toRet;
        }
        case 2: {
            toRet = new Vector();
            return toRet;
        }
        case 3: {
            toRet = new Stack();
            return toRet;
        }
        default: {
            throw new NoSuchElementException();
        }
        }
    }

    public int size() {
        return 4;
    }
}