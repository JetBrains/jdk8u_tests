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
package org.apache.harmony.test.func.api.java.beans.eventhandler;

/**
 * Method of this class is invoked when one invokes method of listener.
 * 
 */
public class Target {
    boolean            isInvoked = false;
    int                i         = -1;
    private int        property1;
    private ClassType1 classType1;

    public ClassType1 getClassType1() {
        return classType1;
    }

    public void setClassType1(ClassType1 classType1) {
        this.classType1 = classType1;
    }

    public void methodWithoutParam() {
        isInvoked = true;
    }

    public void methodWithParam(int i) {
        this.i = i;
    }

    public void methodThrowException() {
        throw new SimpleException("Forced exception");
    }

    public int getProperty1() {
        return property1;
    }

    public void setProperty1(int property1) {
        isInvoked = true;
        this.property1 = property1;
    }
}