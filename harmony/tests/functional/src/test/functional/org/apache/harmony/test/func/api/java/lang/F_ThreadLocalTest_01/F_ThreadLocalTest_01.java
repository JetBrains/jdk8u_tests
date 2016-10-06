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
 * Created on 12.11.2004
 */
package org.apache.harmony.test.func.api.java.lang.F_ThreadLocalTest_01;

import org.apache.harmony.test.func.api.java.lang.F_ThreadLocalTest_01.auxiliary.ThreadLocalClass_01;
import org.apache.harmony.test.func.api.java.lang.F_ThreadLocalTest_01.auxiliary.ThreadLocalClass_02;
import org.apache.harmony.test.func.share.ScenarioTest;
import org.apache.harmony.share.Result;

/**
 * 
 *    Usage: 
 *        java.lang.ThreadLocal
 *
 **/

public class F_ThreadLocalTest_01 extends ScenarioTest {

    public int task1()
    {
        try 
        {
            ThreadLocalClass_01 myThreadLocal = new ThreadLocalClass_01(log);
            myThreadLocal.start();
        } 
        catch (Exception e) 
        {
            return error("test failed - " + e.getMessage());
        }
        return pass("task1");
    }        
    public int task2()
    {
        try 
        {
            ThreadLocalClass_02 myThreadLocal = new ThreadLocalClass_02(log);
            myThreadLocal.start();
        } 
        catch (Exception e) 
        {
            return error("test failed - " + e.getMessage());
        }
        return pass("task2");
    }        
    public int test()
    {
        try {
            if (task1() != Result.PASS) 
                return fail("test NOT passed");
            if (task2() != Result.PASS) 
                return fail("test NOT passed");
        } catch (Exception e) {
            return error("test failed - " + e.getMessage());
        }
        return pass();
    }        


    public static void main(String[] args)
    {
        System.exit(new F_ThreadLocalTest_01().test(args));
    }
}
