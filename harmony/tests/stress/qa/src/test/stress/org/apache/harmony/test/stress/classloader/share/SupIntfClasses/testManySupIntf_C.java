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

/**
 * @author: Vera Y.Petrashkova
 * @version: $revision$
 */

package org.apache.harmony.test.stress.classloader.share.SupIntfClasses;

import org.apache.harmony.test.stress.classloader.share.SupIntfClasses.*;

public class testManySupIntf_C implements testManySupIntf_CA500 {

    public static final boolean PUBSTATFIN_F0_1 = testManySupIntf_CA0.PUBSTATFIN_F0;

    public static final boolean PUBSTATFIN_F1_1 = testManySupIntf_CA1.PUBSTATFIN_F1;

    public static final long PUBSTATFIN_F2_1 = testManySupIntf_CA2.PUBSTATFIN_F2;

    public static final float PUBSTATFIN_F3_1 = testManySupIntf_CA3.PUBSTATFIN_F3;

    public static final String PUBSTATFIN_F4_1 = testManySupIntf_CA4.PUBSTATFIN_F4;

    public static final double PUBSTATFIN_F5_1 = testManySupIntf_CA5.PUBSTATFIN_F5;

    public static final Object PUBSTATFIN_F498_1 = testManySupIntf_CA498.PUBSTATFIN_F498;

    public static final Object[] PUBSTATFIN_F499_1 = testManySupIntf_CA499.PUBSTATFIN_F499;

    public static final Object[][] PUBSTATFIN_F500_1 = testManySupIntf_CA500.PUBSTATFIN_F500;

    private int testF;

    public int get() {
        return testF;
    }

    public void put(int t) {
        testF = t;
    }

    public int get0() {
        return testF + 777;
    }

    public void put0(int t) {
        testF = t;
    }

    public int get1() {
        return testF + 100;
    }

    public void put1(int t) {
        testF = t;
    }

    public int get2() {
        return testF + 200;
    }

    public void put2(int t) {
        testF = t;
    }

    public int get3() {
        return testF + 300;
    }

    public void put3(int t) {
        testF = t;
    }

    public int get4() {
        return testF + 400;
    }

    public void put4(int t) {
        testF = t;
    }

    public int get5() {
        return testF + 500;
    }

    public void put5(int t) {
        testF = t;
    }

    public int get498() {
        return testF;
    }

    public void put498(int t) {
        testF = t + 498;
    }

    public int get499() {
        return testF;
    }

    public void put499(int t) {
        testF = t + 499;
    }

    public int get500() {
        return testF;
    }

    public void put500(int t) {
        testF = t + 500;
    }

}
