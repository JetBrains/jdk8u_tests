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
package org.apache.harmony.test.func.api.java.awt.Dimension;

import org.apache.harmony.share.*;

import java.awt.Dimension;

public class DimensionTests extends Test {

    private int testStatus;

    public DimensionTests() {
        super();
        testStatus = super.pass("");
    }

    public int test() {
        double dx = Math.rint(1.0 + Math.random() * 100);
        double dy = Math.rint(1.0 + Math.random() * 100);
        double ndx = Math.rint((-1.0 + Math.random()) * 100);
        double ndy = Math.rint((-1.0 + Math.random()) * 100);

        log.info("dx is: \t" + dx + "\t dy is: \t" + dy);
        log.info("ndx is: \t" + ndx + "\t ndy is: \t" + ndy);

        Dimension dimens_1 = new Dimension();
        Dimension dimens_2 = (Dimension) (new Dimension((int) dx, (int) dy)).clone();
        Dimension dimens_3 = (Dimension) (new Dimension().clone());
        Dimension dimens_4 = new Dimension(dimens_3);

        dimens_1.setSize(dx, dy);
        log.info("dimens_1 is: \t" + dimens_1.toString());
        log.info("dimens_2 is: \t" + dimens_2.toString());

        dimens_3.setSize(dimens_2);
        log.info("dimens_3 is: \t" + dimens_3);

        dimens_4 = dimens_1.getSize();
        log.info("dimens_4 is: \t" + dimens_4);

        if (!dimens_1.equals(dimens_2)) {
            testStatus = fail(" ! dimens_1.equals(dimens_2) \n" +
                    " dimens_1 is: \t" + dimens_1 + "\n" +
                    " dimens_2 is: \t" + dimens_2);
        }

        if (dimens_3.hashCode() != dimens_4.hashCode()) {
            testStatus = fail(" dimens_3.hashCode() != dimens_4.hashCode() \n" +
                    " dimens_3 is: \t" + dimens_3 + "\n" +
                    " dimens_4 is: \t" + dimens_4);
        }

        if (((Dimension) (new Dimension((int) ndx, (int) ndy)).clone()).getHeight() != (int) ndy) {
            testStatus = fail(" ( (Dimension) (new Dimension((int)ndx, (int)ndy)).clone()).getHeight() != (int)dy \n" +
                    " ndy is: \t" + ndy + "\n" +
                    "( (Dimension) (new Dimension((int)ndx, (int)ndy)).clone()).getHeight() is:\t" +
                    ((Dimension) (new Dimension((int) ndx, (int) ndy)).clone()).getHeight());
        }

        dimens_1.setSize(dx * dy, ndx * ndy);
        if (dimens_1.getWidth() != dx * dy) {
            testStatus = fail(" dimens_1.getWidth() != dx*dy \n" +
                    " dimens_1 is: \t" + dimens_1 + "\n" +
                    " dx*dy is:\t" + dx * dy);
        }

        dimens_1.setSize(Math.round(ndx * ndy), Math.round(dx * dy));
        if (dimens_1.getWidth() != Math.round(ndx * ndy)) {
            testStatus = fail(" dimens_1.getWidth() != Math.round(ndx * ndy) \n" +
                    " dimens_1 is: \t" + dimens_1 + "\n" +
                    " Math.round(ndx * ndy) is:\t" + Math.round(dx * dy));
        }


        return testStatus;
    }

    public static void main(String[] args) {
        System.exit(new DimensionTests().test(args));
    }

}

