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
/*
 * Created on 3.12.2004
 * Last modification G.Seryakova
 * Last modified on 3.12.2004
 * 
 * Test Object for reflection test.
 * 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.reflect.share;

/**
 * Test Object for reflection test.
 * 
 */
public class ReflectionTestObject {
    private static int           counter = 0;
    public byte                  objNumber;
    private ReflectionTestObject prevObj;

    public ReflectionTestObject() {
        objNumber = 0;
        prevObj = null;
    }

    private ReflectionTestObject(byte num, ReflectionTestObject obj) {
        objNumber = num;
        prevObj = obj;
    }

    public void setPrevObj(ReflectionTestObject obj) {
        prevObj = obj;
    }

    public ReflectionTestObject getPrevObj() {
        return prevObj;
    }

    private ReflectionTestObject getPrevObjPriv() {
        return prevObj;
    }
}