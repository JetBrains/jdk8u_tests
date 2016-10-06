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

import java.util.EventObject;

/**
 */
public class FredEvent extends EventObject {
    private static int        i          = 6;
    private static ClassType3 classType3 = new ClassType3(i);

    public FredEvent(Object arg0) {
        super(arg0);
    }

    public static int getI() {
        return i;
    }

    public static void setI(int j) {
        i = j;
    }

    public static ClassType3 getClassType3() {
        return classType3;
    }

    public static void setClassType3(ClassType3 classType31) {
        classType3 = classType31;
    }
}