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
package org.apache.harmony.test.func.api.java.beans.introspector.surveymethods.methods;

/**
 */
public class Bean1 {
    private int i;
    private int property8;

    public int getProperty8() {
        return property8;
    }

    private void setProperty9(int i) {

    }

    public void setProperty8(int property8) {
        this.property8 = property8;
    }

    public int ggetI() {
        return i;
    }

    public void ssetI(int i) {
        this.i = i;
    }

    public void addFredListener(FredListener fredListener) {
    }

    public void removeFredListener(FredListener fredListener) {
    }

    protected void f1() {
    }

    int f2() {
        return 3;
    }
}