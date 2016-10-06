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
package org.apache.harmony.test.func.networking.java.net.URL.SameFile01;

import java.net.*;
import org.apache.harmony.test.func.networking.java.net.share.URLTestFramework;

/**
 */

public class sameFile01 extends URLTestFramework {

    public int test () {
        URL url1 = null;
        URL url2 = null;
        URL url3 = null;
        
        String urlProtocol = getValidProtocol();
        String urlHost = getValidHost();
        String urlFile = getValidFile();
        
        String urlPath = null;
        String urlFile2 = null;
        
        try {
            url1 = new URL (urlProtocol, urlHost, urlFile);
            urlPath = url1.getFile();
            
            url2 = new URL (urlProtocol, urlHost, urlPath);
            
            String urlRef = url1.getRef();
            String urlQuery = url1.getQuery() ;
            urlFile2 = urlRef == null ? urlPath : urlPath + "#" + urlRef;
            
            url3 = new URL (urlProtocol, urlHost, urlFile2);
        }    
        catch (MalformedURLException e) {
            return (fail("can't create URL: " + e.getMessage()));
        }
        
        if (url1.sameFile(url2) && url1.sameFile(url3)) {
            return (pass(url1.toString()));
        }
        else {
            return (fail ("Files are different: " + url1.toString() + ", " + url2.toString()
                    + ", " + url3.toString()));
        }
    }
    
    public static void main (String[] args) {
        System.exit(new sameFile01().test(args));
    }
}
