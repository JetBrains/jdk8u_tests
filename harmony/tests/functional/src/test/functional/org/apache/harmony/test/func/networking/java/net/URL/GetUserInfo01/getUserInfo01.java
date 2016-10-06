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
package org.apache.harmony.test.func.networking.java.net.URL.GetUserInfo01;

import java.net.*;
import org.apache.harmony.test.func.networking.java.net.share.URLTestFramework;

/**
 * 
 */

public class getUserInfo01 extends URLTestFramework {
    
    public int test () {
        String urlStr = "http://user:password@www.netbsd.org/Releases/";
        String rightUserInfo = "user:password";
        
        URL url = null;
        String userInfo = null;
        String result = null;
        
        try {
            url = new URL (urlStr);
        }    
        catch (MalformedURLException e) {
            return (fail("Unexpected exception: can't create URL " + urlStr + ": "+ e.getMessage()));
        }
        
        userInfo = url.getUserInfo();
        result = userInfo == null ? null : userInfo.toString();
        
        if (userInfo.equals(rightUserInfo)) {
            return (pass("User Info is: " + result));
        }
        else {
            return fail ("Wrong User Info: " + result + ". Should be: " + rightUserInfo);
        }
    }
    public static void main (String[] args) {
        System.exit(new getUserInfo01().test(args));
    }
}
