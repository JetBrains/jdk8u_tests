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
package org.apache.harmony.test.func.api.java.security.F_AccessControllerTest_02;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.AccessControlException;

import org.apache.harmony.test.func.share.ScenarioTest;
import auxiliary.FullAccessClass;

/**
 * Created on 16.11.2004
 */

public class F_AccessControllerTest_02 extends ScenarioTest 
{

    public int test() 
    {
        System.setSecurityManager(new SecurityManager());
        //String path = testArgs[0] + File.separator; 
        FullAccessClass faClass = new FullAccessClass(testArgs[0]);
        try
        {
            log.info("Trying to read a file...");
            byte[] inBuf = faClass.ReadFile();
            log.info("OK");
            
        }
        catch (FileNotFoundException fnfe)
        {              
            return fail(fnfe.getMessage());
        }
        catch (IOException ioe)
        {        
            return fail(ioe.getMessage());
        }
        catch (AccessControlException ace)
        {
            ace.printStackTrace();
            return fail("Test failed");
        }        
        
        try
        {
            log.info("Trying to write into a file without having a required permission...");
            byte[] outBuf = new String("If one path of destiny is blocked, another will appear.").getBytes();
            faClass.WriteFile(outBuf);
            return fail("Test failed");
        }
        catch (FileNotFoundException fnfe)
        {
            return fail(fnfe.getMessage());
        }
        catch (IOException ioe)
        {
            return fail(ioe.getMessage());
        }
        catch (AccessControlException ace)
        {        
            log.info("OK. Attempt to write failed.");
        }
        
        return pass("Test passed");
    }

    public static void main(String[] args) 
    {
        System.exit(new F_AccessControllerTest_02().test(args));
    }
}
