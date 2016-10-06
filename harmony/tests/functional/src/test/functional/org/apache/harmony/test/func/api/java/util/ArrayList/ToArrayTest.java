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

package org.apache.harmony.test.func.api.java.util.ArrayList;

import java.util.ArrayList;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/*
 * Mar 10, 2006
 */
public class ToArrayTest extends MultiCase {
    private class MyType {

        public int id;

        public MyType(int id) {
            super();
            this.id = id;
        }

    }

    public static void main(String[] args) {
        System.exit(new ToArrayTest().test(args));
    }

    public Result testIsEqual() {
        ArrayList result = getArrayList();
        MyType[] array = new MyType[6];
        
        MyType[] otherArray = (MyType[])result.toArray(array);

        if (otherArray != array) {
            return failed("Other array returned");
        } else {
            return verifyArray(array);
        }
    }

    public Result testIsLess() {
        ArrayList result = getArrayList();
        MyType[] array = new MyType[5];
        for (int i = 0; i < array.length; i++) {
            array[i] = new MyType(i);
        }
        MyType[] otherArray = (MyType[])result.toArray(array);

        for (int i = 0; i < array.length; i++) {
            if (((MyType) array[i]).id != i) {
                return failed("Array has been corrupted");
            }
        }
        if (otherArray == array) {
            return failed("Same array returned");
        } else {
            return verifyArray(otherArray);
        }
    }
    
    public Result testIsGreater()
    {
        ArrayList result = getArrayList();
        MyType[] array = new MyType[9];
        for (int i = 0; i < array.length; i++) {
            array[i] = new MyType(i);
        }
    
        MyType[] otherArray = (MyType[])result.toArray(array);

        if (otherArray != array) {
            return failed("Other array returned");
        } else {
            return verifyArray(array);
        }
    }

    private Result verifyArray(Object[] array) {
        for (int i = 0; i < array.length; i++) {
            if (i < 6 && ((MyType) array[i]).id != i) {
                return failed("Invalid element: arr[" + i + "] = "
                        + ((MyType) array[i]).id);
            } else if (i == 6 && array[i] != null) {
                return failed("Last element of collection should be null");
            } else if (i > 6 && ((MyType) array[i]).id != i) {
                return failed("Array is corrupted at " + i);
            }
        }
        return passed();
    }

    private ArrayList getArrayList() {
        ArrayList result = new ArrayList();

        result.add(0, new MyType(3));
        result.add(0, new MyType(0));
        result.add(1, new MyType(2));
        result.add(1, new MyType(1));
        result.add(4, new MyType(5));
        result.add(4, new MyType(4));
        return result;
    }

}
