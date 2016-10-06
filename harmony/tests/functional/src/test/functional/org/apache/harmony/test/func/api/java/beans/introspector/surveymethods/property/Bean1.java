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
package org.apache.harmony.test.func.api.java.beans.introspector.surveymethods.property;

/**
 * There are 9 right properties and 2 wrong properties in this class.
 * 
 */
public class Bean1 {
    private boolean  prop1;
    private String[] prop2;
    private int      i;

    public boolean isProp1() {
        return prop1;
    }

    // Only isProp1() method have to use as read method.
    public boolean getProp1() {
        return prop1;
    }

    public void setProp1(boolean prop1) {
        this.prop1 = prop1;
    }

    // Begin methods for prop2
    public String[] getProp2() {
        return prop2;
    }

    public void setProp2(String[] prop2) {
        this.prop2 = prop2;
    }

    public String getProp2(int a) {
        return prop2[a];
    }

    public void setProp2(int a, String prop2) {
        this.prop2[a] = prop2;
    }

    // End methods for prop2

    // Bad setter method
    public void set(int i) {
    }

    /**
     * Only getter
     */
    public boolean getProp3() {
        return prop1;
    }

    /**
     * Only setter
     */
    public void setProp4(int i) {
        this.i = i;
    }

    /**
     * Not correct property: setter method returns value.
     */
    public boolean setProp5(int i) {
        return prop1;
    }

    /**
     * Not correct property: method doesn't public.
     */
    public int getProp6(boolean i) {
        return 1;
    }

    public String getProp7() {
        return null;
    }

    public String getProp8(int i) {
        return null;
    }

    public void setProp9(int i, String str) {

    }

    public boolean getProp10() {
        return true;
    }

    public boolean getURL() {
        return true;
    }

}