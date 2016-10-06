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
package org.apache.harmony.test.func.api.java.beans.introspector.location.beans.subclasses.p1;

import org.apache.harmony.test.func.api.java.beans.introspector.location.beans.thesamepackage.Bean1;

/**
 */
public class Bean3 extends Bean1 {
    private int i;
    private int property8;

    /**
     * getter method for property8.
     */
    public int getProperty8() {
        return property8;
    }

    /**
     * setter method for property8.
     */
    public void setProperty8(int property8) {
        this.property8 = property8;
    }

    /**
     * Returns the Ii property.
     */
    public int getIi() {
        return i;
    }

    /**
     * set Ii property.
     */
    public void setIi(int i) {
        this.i = i;
    }
}