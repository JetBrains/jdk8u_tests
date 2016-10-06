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

package org.apache.harmony.test.func.api.java.net.URI;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

public class URITest extends MultiCase {
    public static void main(String[] args) {
        System.exit(new URITest().test(args));
    }

    public Result testToString() throws URISyntaxException {
        int port = 123;

        String[][] uris = new String[][] {
                {
                        "http",
                        "user:pass",
                        "example.com",
                        "/index.html",
                        "a=b",
                        "x",
                        "http://user:pass@example.com:" + port
                                + "/index.html?a=b#x",
                        "http://user:pass@example.com/index.html?a=b#x" },
                { "http", "user", "example.com", "/index.html", "a=b", "x",
                        "http://user@example.com:" + port + "/index.html?a=b#x",
                        "http://user@example.com/index.html?a=b#x" },
                { "ftp", null, "example.com", null, "a=b", "x",
                        "ftp://example.com:" + port + "?a=b#x",
                        "ftp://example.com?a=b#x" },
                { "ftp", null, "example.com", null, null, "x",
                        "ftp://example.com:" + port + "#x", "ftp://example.com#x" },
                { "ftp", null, "example.com", null, "a", null,
                        "ftp://example.com:" + port + "?a", "ftp://example.com?a" },
                { "ftp", null, "example.com", null, null, null,
                        "ftp://example.com:" + port, "ftp://example.com" },
                { null, null, "example.com", null, null, null,
                        "//example.com:" + port, "//example.com" },
                { "http", "user", "example.com", "/index.html ", "", null,
                        "http://user@example.com:" + port + "/index.html%20?",
                        "http://user@example.com/index.html%20?" },
                { "http", "user", "example.com", "/index.html\u0000", null, null,
                        "http://user@example.com:" + port + "/index.html\u0000",
                        "http://user@example.com/index.html\u0000" },

        };

        for (int i = 0; i < uris.length; ++i) {
            String[] uriData = uris[i];
            if (!new URI(uriData[0], uriData[1], uriData[2], port, uriData[3],
                    uriData[4], uriData[5]).toString().equals(uriData[6])) {
                return failed("expected new URI("
                        + uriData[0]
                        + " , "
                        + uriData[1]
                        + " , "
                        + uriData[2]
                        + ", "
                        + port
                        + ", "
                        + uriData[3]
                        + ", "
                        + uriData[4]
                        + ", "
                        + uriData[5]
                        + ").toString() to return "
                        + uriData[6]
                        + ", got "
                        + new URI(uriData[0], uriData[1], uriData[2], port,
                                uriData[3], uriData[4], uriData[5]).toString());
            }
            if (!new URI(uriData[0], uriData[1], uriData[2], -1, uriData[3],
                    uriData[4], uriData[5]).toString().equals(uriData[7])) {
                return failed("expected new URI("
                        + uriData[0]
                        + " , "
                        + uriData[1]
                        + " , "
                        + uriData[2]
                        + ", -1, "
                        + uriData[3]
                        + ", "
                        + uriData[4]
                        + ", "
                        + uriData[5]
                        + ").toString() to return "
                        + uriData[7]
                        + ", got "
                        + new URI(uriData[0], uriData[1], uriData[2], -1,
                                uriData[3], uriData[4], uriData[5]).toString());
            }
        }

        return passed();
    }
}