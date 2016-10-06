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
 * Created on 27.11.2004
 *
 */
package org.apache.harmony.test.func.networking.java.net.URL.Init08;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import org.apache.harmony.test.func.networking.java.net.share.MyURLStreamHandler2;
import org.apache.harmony.test.func.networking.java.net.share.URLTestFramework;

/**
 *  
 */

public class init08 extends URLTestFramework {

    public int test() {
        URL url = null;
        String urlFile = getValidFile();
        String urlContextStr = "ftp://user:password@www.example.com/test";

        URLStreamHandler st = new MyURLStreamHandler2();
        URLConnection con = null;
        URL urlContext = null;

        try {
            urlContext = new URL(urlContextStr);
            url = new URL(urlContext, urlFile, st);

            ByteArrayInputStream result = (ByteArrayInputStream) (url
                    .getContent());
            //ByteArrayInputStream result =
            // (ByteArrayInputStream)(url.openConnection().getInputStream());

            byte[] bufferArray = new byte[result.available()];
            result.read(bufferArray);
            String s = new String(bufferArray);

            if (s.equals("abcd")) {
                return (pass());
            } else {
                return fail("Result: " + s + ", should be: " + "abcd");
            }

        } catch (Exception e) {
            return fail("Exception: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        System.exit(new init08().test(args));
    }
}