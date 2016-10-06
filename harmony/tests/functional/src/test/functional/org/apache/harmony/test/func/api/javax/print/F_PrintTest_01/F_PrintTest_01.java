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
package org.apache.harmony.test.func.api.javax.print.F_PrintTest_01;

import java.io.*;
import javax.print.*; 
import javax.print.attribute.*;
import javax.print.attribute.standard.*;

import org.apache.harmony.test.func.share.ScenarioTest;
import org.apache.harmony.share.Result;

/**
 * Created on 06.11.2004
 * 
 *    Usage: 
 *        javax.print 
 *
 **/

public class F_PrintTest_01 extends ScenarioTest
{
    private String firstInputFileName;

    public int task1()
    {
        try
        {
            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            DocFlavor flavor = DocFlavor.INPUT_STREAM.POSTSCRIPT;
            PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
            if(printService.length!=0)
            {
                DocPrintJob job = printService[0].createPrintJob();
                FileInputStream fis = new FileInputStream(firstInputFileName);
                DocAttributeSet das = new HashDocAttributeSet(); 
                Doc doc = new SimpleDoc(fis, flavor, das); 
//                job.print(doc, pras);
            }
            else
            {
                return fail("No installed printers");
            }
        }
        catch(Exception e)
        {
            return error("test failed - " + e.getMessage());
        }
        return pass();
    }
    public int task2()
    {
        try
        {
            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            DocFlavor flavor = DocFlavor.INPUT_STREAM.POSTSCRIPT;
            PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
            if(printService.length!=0)
            {
                DocPrintJob job = printService[0].createPrintJob();
                FileInputStream fis = new FileInputStream(firstInputFileName);
                DocAttributeSet das = new HashDocAttributeSet();
                das.add(OrientationRequested.PORTRAIT);
                Doc doc = new SimpleDoc(fis, flavor, das); 
//                job.print(doc, pras);
            }
            else
            {
                return fail("No installed printers");
            }
        }
        catch(Exception e)
        {
            return error("test failed - " + e.getMessage());
        }
        return pass();
    }
    public int test()
    {
        firstInputFileName = testArgs[0];
        try 
        {
            if (task1() != Result.PASS || task2() != Result.PASS) 
                return fail("test NOT passed");
        } 
        catch (Exception e) 
        {
            return error("test failed - " + e.getMessage());
        }
        return pass();
    }        
    public static void main(String[] args) 
    {
        System.exit(new F_PrintTest_01().test(args));
    } 
}
