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
 * Created on 17.10.2005 
 * Last modification G.Seryakova 
 * Last modified on 17.10.2005 
 * 
 * Tests hashCode() for StackTraceElement and constructors for some Errors and Exceptions. 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.F_ExceptionTest_03;

import org.apache.harmony.test.func.share.ScenarioTest;
import java.math.BigInteger;

class SimpleHashTable {
    private static Item DISABLED = new Item(null, 0);
    public int          N        = 0;
    private int         size     = 0;
    private Item        table[];

    static class Item {
        Object key;
        int value;

        Item(Object key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    SimpleHashTable(int N) {
        this.N = N;
        table = new Item[N];
    }

    void put(Object key, int value) {
        int ind = findIndex(key);
        table[ind] = new Item(key, value);
        size++;
    }

    int get(Object key) {
        int ind = findKeyIndex(key);
        if (ind != -1) {
            return table[ind].value;
        } else {
            return 0;
        }
    }

    //        void remove(Object key) {
    //        int ind = findKeyIndex(key);
    //        if (ind != -1) table[ind] = DISABLED;
    //        size--;
    //    }
    //    
    void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
    }

    private int getHashValue(Object key, int shift) {
        BigInteger value = BigInteger.valueOf(key.hashCode() + shift);
        value = value.mod(BigInteger.valueOf(N));
        return value.intValue();
    }

    private int findIndex(Object key) {
        int ind = findKeyIndex(key);
        if (ind != -1) {
            return ind;
        }
        int shift = 0;
        while (true) {
            ind = getHashValue(key, shift);
            if (table[ind] == null || table[ind] == DISABLED) {
                return ind;
            } else {
                shift++;
            }
        }
    }

    private int findKeyIndex(Object key) {
        int shift = 0;
        while (true) {
            int ind = getHashValue(key, shift);
            if (table[ind] == null) {
                return -1;
            } else if (table[ind].key.equals(key)) {
                return ind;
            } else {
                shift++;
            }
        }
    }
}

/**
 * Tests hashCode() for StackTraceElement and constructors for some Errors and Exceptions.
 * 
 */

public class F_ExceptionTest_03 extends ScenarioTest {
    private SimpleHashTable table;
    private int stat = 0;
    
    class MyVirtualMachineError extends VirtualMachineError {
        
        MyVirtualMachineError() {
            super();
        }
        
        MyVirtualMachineError(String msg) {
            super(msg);
        }
    }
    
    public static void main(String[] args) {
        System.exit(new F_ExceptionTest_03().test(args));
    }

    public int test() {
        table = new SimpleHashTable(50);
        table.clear();
        
        int arr[] = new int[18];
        int num;
        
        table.put(testAssertionError1().getStackTrace()[0], 1);
        table.put(testAssertionError2().getStackTrace()[0], 1);
        table.put(testAssertionError3().getStackTrace()[0], 1);
        table.put(testAssertionError4().getStackTrace()[0], 1);
        table.put(testAssertionError5().getStackTrace()[0], 1);
        table.put(testAssertionError6().getStackTrace()[0], 1);
        table.put(testAssertionError7().getStackTrace()[0], 1);
        table.put(testAssertionError8().getStackTrace()[0], 1);
        table.put(testUnsupportedClassVersionError1().getStackTrace()[0], 1);
        table.put(testUnsupportedClassVersionError2().getStackTrace()[0], 1);
        table.put(testVirtualMachineError1().getStackTrace()[0], 1);
        table.put(testVirtualMachineError2().getStackTrace()[0], 1);
        table.put(testUnsupportedOperationException1().getStackTrace()[0], 1);
        table.put(testUnsupportedOperationException2().getStackTrace()[0], 1);
        table.put(testError1().getStackTrace()[0], 1);
        table.put(testError2().getStackTrace()[0], 1);
        table.put(testRuntimeException1().getStackTrace()[0], 1);
        table.put(testRuntimeException2().getStackTrace()[0], 1);
        
        for (int i = 0; i < arr.length; i++){
            arr[i] = 1;
        }
        
        for (int i = 0; i < 1000; i++) {
            num = (int)Math.round(Math.random() * 17);
            arr[num]++;
            StackTraceElement elem = null;
            switch(num) {
                case 0 : elem = testAssertionError1().getStackTrace()[0]; break; 
                case 1 : elem = testAssertionError2().getStackTrace()[0]; break;
                case 2 : elem = testAssertionError3().getStackTrace()[0]; break;
                case 3 : elem = testAssertionError4().getStackTrace()[0]; break;
                case 4 : elem = testAssertionError5().getStackTrace()[0]; break;
                case 5 : elem = testAssertionError6().getStackTrace()[0]; break;
                case 6 : elem = testAssertionError7().getStackTrace()[0]; break;
                case 7 : elem = testAssertionError8().getStackTrace()[0]; break;
                case 8 : elem = testUnsupportedClassVersionError1().getStackTrace()[0]; break;
                case 9 : elem = testUnsupportedClassVersionError2().getStackTrace()[0]; break;
                case 10 : elem = testVirtualMachineError1().getStackTrace()[0]; break;
                case 11 : elem = testVirtualMachineError2().getStackTrace()[0]; break;
                case 12 : elem = testUnsupportedOperationException1().getStackTrace()[0]; break;
                case 13 : elem = testUnsupportedOperationException2().getStackTrace()[0]; break;
                case 14 : elem = testError1().getStackTrace()[0]; break;
                case 15 : elem = testError2().getStackTrace()[0]; break;
                case 16 : elem = testRuntimeException1().getStackTrace()[0]; break;
                case 17 : elem = testRuntimeException2().getStackTrace()[0]; break;
            }
            table.put(elem, table.get(elem) + 1);
        }

        for (int i = 0; i < arr.length; i++) {
            StackTraceElement elem = null;
            switch(i) {
                case 0 : elem = testAssertionError1().getStackTrace()[0]; break; 
                case 1 : elem = testAssertionError2().getStackTrace()[0]; break;
                case 2 : elem = testAssertionError3().getStackTrace()[0]; break;
                case 3 : elem = testAssertionError4().getStackTrace()[0]; break;
                case 4 : elem = testAssertionError5().getStackTrace()[0]; break;
                case 5 : elem = testAssertionError6().getStackTrace()[0]; break;
                case 6 : elem = testAssertionError7().getStackTrace()[0]; break;
                case 7 : elem = testAssertionError8().getStackTrace()[0]; break;
                case 8 : elem = testUnsupportedClassVersionError1().getStackTrace()[0]; break;
                case 9 : elem = testUnsupportedClassVersionError2().getStackTrace()[0]; break;
                case 10 : elem = testVirtualMachineError1().getStackTrace()[0]; break;
                case 11 : elem = testVirtualMachineError2().getStackTrace()[0]; break;
                case 12 : elem = testUnsupportedOperationException1().getStackTrace()[0]; break;
                case 13 : elem = testUnsupportedOperationException2().getStackTrace()[0]; break;
                case 14 : elem = testError1().getStackTrace()[0]; break;
                case 15 : elem = testError2().getStackTrace()[0]; break;
                case 16 : elem = testRuntimeException1().getStackTrace()[0]; break;
                case 17 : elem = testRuntimeException2().getStackTrace()[0]; break;
            }
            if (table.get(elem) != arr[i]) {
                fail("FAIL: fail for " + i + " elemant.");
                stat++;
            }
        }
        
        if (stat == 0) {
            return pass();
        } else {
            return fail("test failed.");
        }
    }
    
    private Throwable testAssertionError1() {
        AssertionError as = new AssertionError();
        if (!(as instanceof AssertionError)) {
            fail("FAIL: In testAssertionError1() is not expected class");
            stat++;
        }
        if (as.getMessage() != null) {
            fail("FAIL: In testAssertionError1() getMessage() is not null");
            stat++;
        }
        return as;
    }
    
    private Throwable testAssertionError2() {
        String str = new String("test.");
        AssertionError as = new AssertionError(str);
        if (!(as instanceof AssertionError)) {
            fail("FAIL: In testAssertionError2() is not expected class");
            stat++;
        }
        if (!as.getMessage().trim().equals(str)) {
            fail("FAIL: In testAssertionError2() getMessage() is not \"test.\"");
            stat++;
        }
        return as;
    }
    
    private Throwable testAssertionError3() {
        boolean b = true;
        AssertionError as = new AssertionError(b);
        if (!(as instanceof AssertionError)) {
            fail("FAIL: In testAssertionError3() is not expected class");
            stat++;
        }
        if (!as.getMessage().trim().equals("true")) {
            fail("FAIL: In testAssertionError3() getMessage() is not \"true\"");
            stat++;
        }
        return as;
    }
    
    private Throwable testAssertionError4() {
        char c = 'q';
        AssertionError as = new AssertionError(c);
        if (!(as instanceof AssertionError)) {
            fail("FAIL: In testAssertionError4() is not expected class");
            stat++;
        }
        if (!as.getMessage().trim().equals("q")) {
            fail("FAIL: In testAssertionError4() getMessage() is not \"q\"");
            stat++;
        }
        return as;
    }
    
    private Throwable testAssertionError5() {
        double d = 3.256d;
        AssertionError as = new AssertionError(d);
        if (!(as instanceof AssertionError)) {
            fail("FAIL: In testAssertionError5() is not expected class");
            stat++;
        }
        if (!as.getMessage().trim().equals("3.256")) {
            fail("FAIL: In testAssertionError5() getMessage() is not \"3.256\"");
            stat++;
        }
        return as;
    }
    
    private Throwable testAssertionError6() {
        float f = 10.4566f;
        AssertionError as = new AssertionError(f);
        if (!(as instanceof AssertionError)) {
            fail("FAIL: In testAssertionError6() is not expected class");
            stat++;
        }
        if (!as.getMessage().trim().equals("10.4566")) {
            fail("FAIL: In testAssertionError6() getMessage() is not \"10.4566\"");
            stat++;
        }
        return as;
    }

    private Throwable testAssertionError7() {
        AssertionError as = new AssertionError(2467);
        if (!(as instanceof AssertionError)) {
            fail("FAIL: In testAssertionError7() is not expected class");
            stat++;
        }
        if (!as.getMessage().trim().equals("2467")) {
            fail("FAIL: In testAssertionError7() getMessage() is not \"2467\"");
            stat++;
        }
        return as;
    }
    
    private Throwable testAssertionError8() {
        AssertionError as = new AssertionError(457475979l);
        if (!(as instanceof AssertionError)) {
            fail("FAIL: In testAssertionError3() is not expected class");
            stat++;
        }
        if (!as.getMessage().trim().equals("457475979")) {
            fail("FAIL: In testAssertionError3() getMessage() is not \"457475979\"");
            stat++;
        }
        return as;
    }
   
    private Throwable testUnsupportedClassVersionError1() {
        UnsupportedClassVersionError as = new UnsupportedClassVersionError();
        if (!(as instanceof UnsupportedClassVersionError)) {
            fail("FAIL: In testUnsupportedClassVersionError1() is not expected class");
            stat++;
        }
        if (as.getMessage() != null) {
            fail("FAIL: In testUnsupportedClassVersionError1() getMessage() is not null");
            stat++;
        }
        return as;
    }
    
    private Throwable testUnsupportedClassVersionError2() {
        UnsupportedClassVersionError as = new UnsupportedClassVersionError("this is test.");
        if (!(as instanceof UnsupportedClassVersionError)) {
            fail("FAIL: In testUnsupportedClassVersionError2() is not expected class");
            stat++;
        }
        if (!as.getMessage().trim().equals("this is test.")) {
            fail("FAIL: In testUnsupportedClassVersionError2() getMessage() is not \"this is test.\"");
            stat++;
        }
        return as;
    }
    
    private Throwable testVirtualMachineError1() {
        MyVirtualMachineError as = new MyVirtualMachineError();
        if (!(as instanceof VirtualMachineError)) {
            fail("FAIL: In VirtualMachineError1() is not expected class");
            stat++;
        }
        if (as.getMessage() != null) {
            fail("FAIL: In VirtualMachineError1() getMessage() is not null");
            stat++;
        }
        return as;
    }
    
    private Throwable testVirtualMachineError2() {
        MyVirtualMachineError as = new MyVirtualMachineError("this is test2.");
        if (!(as instanceof VirtualMachineError)) {
            fail("FAIL: In testVirtualMachineError2() is not expected class");
            stat++;
        }
        if (!as.getMessage().trim().equals("this is test2.")) {
            fail("FAIL: In testVirtualMachineError2() getMessage() is not \"this is test2.\"");
            stat++;
        }
        return as;
    }
    
    private Throwable testUnsupportedOperationException1() {
        UnsupportedOperationException as = new UnsupportedOperationException();
        if (!(as instanceof UnsupportedOperationException)) {
            fail("FAIL: In UnsupportedOperationException1() is not expected class");
            stat++;
        }
        if (as.getMessage() != null) {
            fail("FAIL: In UnsupportedOperationException1() getMessage() is not null");
            stat++;
        }
        return as;
    }
    
    private Throwable testUnsupportedOperationException2() {
        UnsupportedOperationException as = new UnsupportedOperationException("this is test3.");
        if (!(as instanceof UnsupportedOperationException)) {
            fail("FAIL: In testUnsupportedOperationException2() is not expected class");
            stat++;
        }
        if (!as.getMessage().trim().equals("this is test3.")) {
            fail("FAIL: In testUnsupportedOperationException2() getMessage() is not \"this is test3.\"");
            stat++;
        }
        return as;
    }
    
    private Throwable testError1() {
        Exception ex = new Exception();
        Error as = new Error(ex);
        if (!(as instanceof Error)) {
            fail("FAIL: In testError1() is not expected class");
            stat++;
        }
        if (!as.getMessage().equals(ex.toString())) {
            fail("FAIL: In testError1() getMessage() is not equal to couse.toString.");
            stat++;
        }
        if (!as.getCause().equals(ex)) {
            fail("FAIL: In testError1() Error.getCause() is unexpected");
            stat++;
        }
        return as;
    }
    
    private Throwable testError2() {
        Exception ex = new Exception();
        Error as = new Error("this is test4.", ex);
        if (!(as instanceof Error)) {
            fail("FAIL: In testError2() is not expected class");
            stat++;
        }
        if (!as.getMessage().trim().equals("this is test4.")) {
            fail("FAIL: In testError2() getMessage() is not \"this is test4.\"");
            stat++;
        }
        if (!as.getCause().equals(ex)) {
            fail("FAIL: In testError2() Error.getCause() is unexpected");
            stat++;
        }
        return as;
    }
    
    private Throwable testRuntimeException1() {
        Exception ex = new Exception("t");
        RuntimeException as = new RuntimeException(ex);
        if (!(as instanceof RuntimeException)) {
            fail("FAIL: In testRuntimeException1() is not expected class");
            stat++;
        }
        if (!as.getMessage().equals(ex.toString())) {
            fail("FAIL: In testRuntimeException1() getMessage() is not equal to couse.toString.");
            stat++;
        }
        if (!as.getCause().equals(ex)) {
            fail("FAIL: In testRuntimeException1() Error.getCause() is unexpected");
            stat++;
        }
        return as;
    }
    
    private Throwable testRuntimeException2() {
        Exception ex = new Exception();
        RuntimeException as = new RuntimeException("this is test5.", ex);
        if (!(as instanceof RuntimeException)) {
            fail("FAIL: In testRuntimeException2() is not expected class");
            stat++;
        }
        if (!as.getMessage().trim().equals("this is test5.")) {
            fail("FAIL: In testRuntimeException2() getMessage() is not \"this is test5.\"");
            stat++;
        }
        if (!as.getCause().equals(ex)) {
            fail("FAIL: In testRuntimeException2() Error.getCause() is unexpected");
            stat++;
        }
        return as;
    }
}

