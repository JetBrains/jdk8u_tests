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
 * Created on 22.11.2004
 *
 */
package org.apache.harmony.test.func.networking.java.net.URL.OpenStream01;

import java.net.*;
import java.io.IOException;
import java.io.InputStream;

import org.apache.harmony.test.func.networking.java.net.share.URLTestFramework;

/**
 * 
 */

public class openStream01 extends URLTestFramework {
    
    public int test () {
        String urlStr = "http://www.apache.org";
        URL url = null;
        InputStream iStream = null;
        
        try {
            url = new URL (urlStr);
        }    
        catch (MalformedURLException e) {
            return (fail("Unexpected exception: can't create URL " + urlStr + ": "+ e.getMessage()));
        }
        
        try {
            iStream = url.openStream();
        }
        catch (IOException e) {
            return (fail("Unexpected exception: can't create Stream: " + e.getMessage()));
        }
        return (pass("Input Stream " + iStream.toString() + " correctly created."));
    }
    public static void main (String[] args) {
        System.exit(new openStream01().test(args));
    }
}
