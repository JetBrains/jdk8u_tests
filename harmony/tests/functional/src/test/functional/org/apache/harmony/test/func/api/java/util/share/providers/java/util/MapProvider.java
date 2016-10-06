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
import java.util.jar.*;



public class MapProvider{

    static public final int ATTRIBUTES          = 0; 
    static public final int HASHMAP             = 1;
    static public final int HASHTABLE           = 2;
    static public final int IDENTITYHASHMAP     = 3;
    static public final int TREEMAP             = 4;
    static public final int WEAKHASHMAP         = 5;         
    
    public static Map giveMe(int pos) throws NoSuchElementException {
        Map toRet;

        switch (pos) {
        case 0: {
            toRet = new Attributes();
            return toRet;
        }
        case 1: {
            toRet = new HashMap();
            return toRet;
        }
        case 2: {
            toRet = new Hashtable();
            return toRet;
        }
        case 3: {
            toRet = new IdentityHashMap();
            return toRet;
        }
        case 4: {
            toRet = new TreeMap();
            return toRet;
        }
        case 5: {
            toRet = new WeakHashMap();
           return toRet;
        }        
        default: {
            throw new NoSuchElementException();
        }
        }
    }

    public int size() {
        return 6;
    }
}