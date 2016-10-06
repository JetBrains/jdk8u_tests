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
package org.apache.harmony.test.func.networking.java.net.share;

import java.util.Random;
import java.util.Hashtable;

import org.apache.harmony.share.Test;

/**
 *
 */
public abstract class URLTestFramework extends Test {

    private static final String[] VALID_URL = {
        "http://archive.ncsa.uiuc.edu/SDG/Software/Mosaic/Demo/url-primer.html",
        "file://ftp.yoyodyne.com/pub/files/foobar.txt",
        "file://ftp.yoyodyne.com/",
        "file://ftp.yoyodyne.com/pub",
        //"gopher://gopher.yoyodyne.com/",
        //"gopher://gopher.banzai.edu:1234/",
        // "news:rec.gardening",
        "http://www.yoyodyne.com/pub/files/foobar.html",
        "http://www.yoyodyne.com:1234/pub/files/foobar.html",
        //"mailto:marca@ncsa.uiuc.edu"
    };
    private static final String[] INVALID_URL = {
            "htp:kjdhk:-1\\\\"    
    };
    
    // from RFC http://rfc.net/rfc1738.html
    // "nntp",
    // "wais",
    // "news",
    // "telnet"
    // "prospero"
    // "mailto",
    // "gopher",

    private static final String[] VALID_PROTOCOL = {
        "http",
        "file",
        "ftp"
    };
    
    private static String myURL = "http://www.example.com";
    
    private static final String[] VALID_HOST = {
        "host.com",
        "111.111.111.111",
        "GOOGLE.COM"
    };
    
    private static String[] VALID_FILE = {
        "",
        "/pub/files/foobar.html",
        "/a",
        "/cgi-bin/games?user=first",
        "/book/book.html#chapter1"
    };
    
    private static Hashtable DEFAULT_PORT = new Hashtable();
    static {
        DEFAULT_PORT.put("http",   new Integer(80));
        DEFAULT_PORT.put("ftp",    new Integer(21));
        DEFAULT_PORT.put("file",   new Integer(-1));
        DEFAULT_PORT.put("mailto", new Integer(-1));
        DEFAULT_PORT.put("gopher", new Integer(70));
    }
    


    
    // //<user>:<password>@<host>:<port>/<url-path>

    
    private static Random rand;
    
    
    
    public static void random_init () {
        rand = new Random();
    }
    public static int random (int low, int high) {
        return ((rand.nextInt() & 0x7fffffff) % (high-low+1)) + low;    
    }
    
    public static String getValidURLString () {
        random_init();
        return VALID_URL[random(0, VALID_URL.length-1)];
    }
    
    public static String getInvalidURLString () {
        random_init();
        return INVALID_URL[random(0, INVALID_URL.length-1)];
    }
    
    public static String getValidProtocol () {
        random_init();
        return VALID_PROTOCOL[random(0, VALID_PROTOCOL.length-1)];
    }
    public static String getValidHost () {
        random_init();
        return VALID_HOST[random(0, VALID_HOST.length-1)];
    }
    public static String getValidFile () {
        random_init();
        return VALID_FILE[random(0, VALID_FILE.length-1)];
    }
    public static int getValidPort () {
        random_init();
        return rand.nextInt(0xffff); // 2^16 - 1 (max port)
    }
    public static int getDefPort (String Prt) {
        Integer n = (Integer)DEFAULT_PORT.get(Prt);
        return n.intValue();
    }
    public static String getMyURL () {
        return myURL;
    }
}
