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
/*
 * Created on 02.11.2005
 *  
 */

package org.apache.harmony.test.func.api.java.util.ListResourceBundle;


import java.util.Enumeration;
import java.util.ListResourceBundle;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

class MyResources extends ListResourceBundle {

    public Object[][] getContents() {
            return contents;
    }
    static final Object[][] contents = {
            {"1", "One"},
            {"2", "Two"},
            {"3", "Three"},
    };
    
}

public class ListResourceBundleTest extends MultiCase {

    public static void main(String[] args) {
        System.exit(new ListResourceBundleTest().test(args));
    }

    public Result testListResourceBundle() {
        Object[] keys = {"1", "2", "3"};
        Object[] values = {"One", "Two", "Three"};
        
        ListResourceBundle res = new MyResources();
        if (res instanceof ListResourceBundle) {
        } else {
            return failed ("Wrong object is created: "
                    + res.getClass().getName());
        }
        
        Enumeration en = res.getKeys();
        int sum = 0;
        int checkSum = 0;
        
        while (en.hasMoreElements()) {
            sum += new Integer(en.nextElement().toString()).intValue();
        }
        for (int i = 0; i < keys.length; i++) {
            checkSum += new Integer(keys[i].toString()).intValue();
        }
        if (sum != checkSum) {
            return failed ("Some key is wrong. Keys sum is " + sum);
        }
        for (int i = 0; i < keys.length; i++) {
            if (!res.getString(keys[i].toString()).equals(values[i]))
            return failed ("Some value is wrong: " + res.getString(keys[i].toString()) 
                    + " is found, but should be " + values[i]);
        }
                
        return passed();
    }

}