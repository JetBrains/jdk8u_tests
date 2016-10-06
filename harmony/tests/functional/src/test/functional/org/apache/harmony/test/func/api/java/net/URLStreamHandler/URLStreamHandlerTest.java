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

package org.apache.harmony.test.func.api.java.net.URLStreamHandler;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.net.URL;
import java.net.URLConnection;
import java.net.MalformedURLException;

import java.io.IOException;

public class URLStreamHandlerTest extends MultiCase {
    public static void main(String[] args) {
        System.exit(new URLStreamHandlerTest().test(args));
    }

    static URLStreamHandler handler;
    String urlString1 = "http://somehost.org/";
    String urlString2 = "somepath/index.html#anchor1";
    URL url;

    {
        handler = new MyURLStreamHandler();
        URL.setURLStreamHandlerFactory(new URLStreamHandlerFactory() {
            public URLStreamHandler createURLStreamHandler(String protocol) {
                return handler;
            }
        });
    }

    void setup() throws MalformedURLException {
        url = new URL(urlString1);
    }

    /*
    * java.net.URLStreamHandler()
    */
    public Result testURLStreamHandler() {
        URLStreamHandler handler = new MyURLStreamHandler();
        return passed("OK");
    }

    /*
    * java.net.URLStreamHandler.parseURL(URL, String, int, int)
    */
    public Result testParseURL() {
        try {
            setup();
        } catch (MalformedURLException e) {
            return failed("Could not parse URL:" + urlString1);
        }
        ((MyURLStreamHandler) handler).parseURL(url, urlString2, 0,
            urlString2.length());
        if (!url.toString().equals(urlString1 + urlString2)) {
            return failed("Wrong parsing result: " + url.toString());
        }
        return passed("OK");
    }

    /*
    * java.net.URLStreamHandler.setURL(URL, String, String, int, String, String)
    */
    public Result testSetURL() {
        try {
            setup();
        } catch (MalformedURLException e) {
            return failed("Could not parse URL:" + urlString1);
        }
        ((MyURLStreamHandler) handler).setURL(url, "http", "somehost", 555,
            null, null);
        if (!url.getProtocol().equals("http")
                || !url.getHost().equals("somehost")
                || url.getPort() != 555
                || url.getFile() != null
                || url.getRef() != null) {
            return failed("Wrong url is set by setURL(short set of args)");
        }
        URLStreamHandler handler2 = new MyURLStreamHandler();
        try {
            ((MyURLStreamHandler) handler2).setURL(url,
                "my", "somehost", 80, null, null);
            return failed("No SecurityException was thrown when expected");
        } catch (SecurityException e) {
        }
        return passed("OK");
    }

    /*
    * java.net.URLStreamHandler.setURL(URL, String, String, int, String, String,
    *     String, String, String)
    */
    public Result testSetURL1() {
        try {
            setup();
        } catch (MalformedURLException e) {
            return failed("Could not parse URL:" + urlString1);
        }
        ((MyURLStreamHandler) handler).setURL(url, "http", "somehost.org", 80,
            "somehost.org", null, "", null, null);
        if (!url.getProtocol().equals("http")
                || !url.getHost().equals("somehost.org")
                || url.getPort() != 80
                || !url.getAuthority().equals("somehost.org")
                || url.getUserInfo() != null
                || !url.getPath().equals("")
                || url.getQuery() != null
                || url.getRef() != null) {
            return failed("Wrong url is set by setURL(long set of args)");
        }
        URLStreamHandler handler2 = new MyURLStreamHandler();
        try {
            ((MyURLStreamHandler) handler2).setURL(url, "http", "somehost.org",
                80, "somehost.org", null, "", null, null);
            return failed("No SecurityException was thrown when expected");
        } catch (SecurityException e) {
        }
        return passed("OK");
    }
}

/*
* MyURLConnection helper
*/
class MyURLConnection extends URLConnection {
    public MyURLConnection(URL u) {
        super(u);
    }

    public void connect() {
    }
}

/*
* MyURLStreamHandler helper
*/
class MyURLStreamHandler extends URLStreamHandler {
    protected MyURLStreamHandler() {
        super();
    }

    protected URLConnection openConnection(URL u) throws IOException {
        return new MyURLConnection(u);
    }

    protected void parseURL(URL u, String spec, int start, int limit) {
        super.parseURL(u, spec, start, limit);
    }

    protected void setURL(URL u, String protocol,
            String host, int port, String file, String ref) {
        super.setURL(u, protocol, host, port, file, ref);
    }

    protected void setURL(URL u, String protocol, String host, int port,
            String authority, String userInfo, String path,
            String query, String ref) {
        super.setURL(u, protocol, host, port, authority,
                userInfo, path, query, ref);
    }
}
