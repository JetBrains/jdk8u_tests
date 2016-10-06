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

package org.apache.harmony.test.func.api.java.util.Vector;

import java.util.Vector;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/*
 * Mar 10, 2006
 */
public class Vector_setTest extends MultiCase {

    public static void main(String[] args) {
        System.exit(new Vector_setTest().test(args));
    }

    public Result testSet() throws Throwable {
        Vector v = new Vector(7);

        v.setSize(7);
        try {
            v.set(7, new Integer(7));
            return failed("1");
        } catch (ArrayIndexOutOfBoundsException e) {
        } catch (Throwable e) {
            return failed("2");
        }

        try {
            v.set(-1, new Integer(7));
            return failed("1");
        } catch (ArrayIndexOutOfBoundsException e) {
        } catch (Throwable e) {
            return failed("2");
        }

        v.set(5, new Integer(5));
        v.set(0, new Integer(0));
        v.set(2, new Integer(2));
        v.set(3, new Integer(3));
        v.set(1, new Integer(1));
        v.set(4, new Integer(4));
        v.set(6, new Integer(6));

        for (int i = 0; i < v.size(); i++) {
            if (!v.get(i).equals(new Integer(i))) {
                return failed("3");
            }
        }

        v.set(3, "Changed");

        for (int i = 0; i < v.size(); i++) {
            if (i != 3 && !v.get(i).equals(new Integer(i))) {
                return failed("4");
            } else if (i == 3 && !v.get(i).equals("Changed")) {
                return failed("5");
            }
        }

        return passed();
    }
}
