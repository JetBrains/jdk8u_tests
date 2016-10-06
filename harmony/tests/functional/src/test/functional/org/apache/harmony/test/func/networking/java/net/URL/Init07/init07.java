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
package org.apache.harmony.test.func.networking.java.net.URL.Init07;

import java.io.IOException;
import java.net.*;

import org.apache.harmony.test.func.networking.java.net.share.MyURLStreamHandler;
import org.apache.harmony.test.func.networking.java.net.share.URLTestFramework;

/**
 * 
 */

public class init07 extends URLTestFramework {
    
    public int test () {
        URL url = null;
        String urlFile = getValidFile();
        String urlContextStr = "ftp://user:password@www.example.com/test";
        
        URLStreamHandler st = new MyURLStreamHandler();    
        URLConnection con = null;
        URL urlContext = null;
        
        try {
            urlContext = new URL (urlContextStr);
        }    
        catch (MalformedURLException e) {
            return (fail("can't create URL " + e.getMessage()));
        }    
        try {
            url = new URL (urlContext, urlFile, st);
        }    
        catch (MalformedURLException e) {
            return (fail("can't create URL " + e.getMessage()));
        }        
        try {
            con = url.openConnection();
        }
        catch (IOException e) {
            return fail("Can't create URLConnection: " + e.getMessage());
        } 
        String result = con.getURL().toString();
        String rightResult = "http://www.cisco.com";
        
        if (result.equals(rightResult)) {
            return (pass("File: " + urlFile + "; Context: " + urlContext.toString() + "; URL: " + url.toString()+ ", URL set with Stream Handler: " + result));
        }
        else {
            return fail ("Result: " + result + ", should be: " + rightResult);
        }
    }
    
    public static void main (String[] args) {
        System.exit(new init07().test(args));
    }
}
