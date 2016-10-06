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
package org.apache.harmony.test.func.reg.vm.btest3010;

import org.apache.harmony.share.Test;

public class Btest3010 extends Test {
    
    static Btest3010 obj;

    public static void main(String[] args) {
        System.exit(new Btest3010().test()); 
    } 

    public int test() {
        if ("Linux".equals(System.getProperty("os.name"))) {
            System.err.println("It is test for Win32 only");
            return pass();
        }
        
        try {
            System.loadLibrary("Btest3010");
        } catch (Exception e) {
            System.err.println("Can not load library!");
            e.printStackTrace();
        }
        try {
            initCallBack();
            obj = new Btest3010();
            runCallBack();
            return pass();
        } catch (Throwable t) {
            return fail("Test falied with" + t);
        }
        
    }
    
    public long windowProc(long hwnd, int msg, long p2, long p3) {
        long result = (msg % 2 == 0) ? p2 : p3;
        System.err.println(" windowProc (" + hwnd + ", " + msg + ", " + p2 + ", " + p3 + ") returns " + result);
        return result;
    }

    private static long runCallback(long hwnd, int msg, long p2, long p3) {
        return obj.windowProc(hwnd, msg, p2, p3);
    }

    private static native long initCallBack();

    private static native int runCallBack();


}
