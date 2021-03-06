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
 * Created on 16.11.2004
 *
 */
package org.apache.harmony.test.func.networking.java.net.URL.Init02;

import java.net.*;

import org.apache.harmony.test.func.networking.java.net.share.URLTestFramework;

/**
 * 
 */

public class init02 extends URLTestFramework {

    public static void main(String[] args) {
        System.exit(new init02().test(args));
    }
    
    public int test () {
        URL url = null;
        String urlStr = getInvalidURLString();
        try {
            url = new URL (urlStr);
            return (fail("URL " + urlStr + " should have not been created!"));
        }    
        catch (MalformedURLException e) {
            return (pass("Exception correctly thrown: " + e.getMessage()));
        }
    }
}
