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
 */

package org.apache.harmony.test.func.reg.vm.btest5940;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */
public class Btest5940 extends MultiCase {

    public static native void foo();

    public static native void foo1();

    static {
        System.loadLibrary("Btest5940");
    }

    public static void main(String[] argv) {
        System.exit(new Btest5940().test(argv));
    }

    public Result test0001() {
        try {
            foo();
            return failed("FAILED: Error should be thrown ");
        } catch (java.lang.ClassCircularityError e) {
            return passed();
        } catch (Throwable e) {
            return failed("FAILED: Unexpected " + e);
        }
    }

    public Result test0002() {
        try {
            foo1();
            return failed("FAILED: Error should be thrown ");
        } catch (java.lang.ClassCircularityError e) {
            return passed();
        } catch (Throwable e) {
            return failed("FAILED: Unexpected " + e);
        }

    }

}
