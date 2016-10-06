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
package org.apache.harmony.test.func.api.java.util.Hashtable.functional;

import java.util.HashMap;
import java.util.Hashtable;

import org.apache.harmony.test.func.share.synctest.SyncTestCaller;

/**
 */
public class HashtableTest extends SyncTestCaller {

    Hashtable ht;

    public HashtableTest() {
    }

    public HashtableTest(int id){
        super(id);
        ht = new Hashtable();
        lock = ht;
    }

    public SyncTestCaller getInstance(int id) {
        return new HashtableTest(id);
    }

    public void run() {
        switch (getCallerId()) {
            case 0:                
                ht.clear();
                setCallPassed();
                break;
            case 1:                
                ht.clone();
                setCallPassed();
                break;
            case 2:                
                ht.contains(new Integer(0));
                setCallPassed();
                break;
            case 3:                
                ht.containsKey(new Integer(0));
                setCallPassed();
                break;
            case 4:                
                ht.containsValue(new Integer(0));
                setCallPassed();
                break;
            case 5:                
                ht.elements();
                setCallPassed();
                break;
            case 6:
                /* Method entrySet() does not reflect the inner state of the object,
                 * so we do not include into the test
                 */
                // ht.entrySet();
                // setCallPassed();
                break;
            case 7:                
                ht.equals(new HashMap());
                setCallPassed();
                break;
            case 8:                
                ht.hashCode();
                setCallPassed();
                break;
            case 9:                
                ht.isEmpty();
                setCallPassed();
                break;
            case 10:                
                ht.keys();
                setCallPassed();
                break;
            case 11:
                /* Method keySet() does not reflect the inner state of the object,
                 * so we do not include into the test
                 */
                // ht.keySet();
                // setCallPassed();
                break;
            case 12:                
                ht.put(new Integer(0), new Integer(0));
                setCallPassed();
                break;
            case 13:                
                ht.putAll(new HashMap());
                setCallPassed();
                break;
            case 14:                
                ht.remove(new Integer(0));
                setCallPassed();
                break;
            case 15:                
                ht.size();
                setCallPassed();
                break;
            case 16:                
                ht.toString();
                setCallPassed();
                break;
            case 17:
            /* Method values() does not reflect the inner state of the object,
             * so we do not include it into the test
             */    
            //    ht.values();
            //    setCallPassed();
                break;
        }
    }

    protected void fillTestedMethods() {       
        registerMethodName("clear");
        registerMethodName("clone");
        registerMethodName("contains_Object");
        registerMethodName("containsKey_Object");
        registerMethodName("containsValue_Object");
        registerMethodName("elements");
        registerMethodName("entrySet");
        registerMethodName("equals_Object");        
        registerMethodName("hashCode");
        registerMethodName("isEmpty");
        registerMethodName("keys");
        registerMethodName("keySet");
        registerMethodName("put_Object_Object");
        registerMethodName("putAll_Map");       
        registerMethodName("remove_Object");
        registerMethodName("size");
        registerMethodName("toString");
        registerMethodName("values");
    }

    public static void main(String[] args){
        HashtableTest t = new HashtableTest();
        System.exit(t.test());
    }
}