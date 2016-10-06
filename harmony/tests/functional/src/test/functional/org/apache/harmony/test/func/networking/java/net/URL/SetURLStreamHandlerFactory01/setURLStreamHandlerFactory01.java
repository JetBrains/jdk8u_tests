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
 * Created on 25.11.2004
 *
 */
package org.apache.harmony.test.func.networking.java.net.URL.SetURLStreamHandlerFactory01;


import java.io.IOException;
import java.net.*;
import org.apache.harmony.test.func.networking.java.net.share.*;
import org.apache.harmony.test.func.networking.java.net.share.URLTestFramework;

/**
 *
 */
public class setURLStreamHandlerFactory01 extends URLTestFramework {
    String rightProcString = "net-test://www.example.com";
    String wrongProcString = "http://www.example.com";
    URL url1 = null;
    URL url2 = null;
    URLStreamHandler sh;
    URLStreamHandlerFactory fac;
    URLConnection con;
    
    public int test () {
        fac = new MyURLStreamHandlerFactory();
        sh = fac.createURLStreamHandler("net-test");
        URL.setURLStreamHandlerFactory(fac);
        
        try {
            url1 = new URL(rightProcString);
        }
        catch (MalformedURLException e) {
            return (fail("Unexpected exception: can't create URL: " + e.getMessage()));
        }
        
        if (url1 != null) {
            try {
                con = url1.openConnection();
                return (pass("URLConnection " + con.toString() + " correctly created."));
            }
            catch (IOException e) {
                return (fail("Unexpected exception: can't create URLConnection: " + e.getMessage()));
            }
        }
        else {
            return (fail("Can't open connection " + con.toString()));
        }
    }
    
    public static void main (String[] args) {
        System.exit(new setURLStreamHandlerFactory01().test(args));
    }

}
