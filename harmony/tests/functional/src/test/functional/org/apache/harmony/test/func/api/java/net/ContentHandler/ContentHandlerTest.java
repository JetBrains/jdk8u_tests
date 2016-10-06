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

package org.apache.harmony.test.func.api.java.net.ContentHandler;

import java.io.IOException;
import java.net.ContentHandler;
import java.net.URISyntaxException;
import java.net.URLConnection;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

public class ContentHandlerTest extends MultiCase {
    public static void main(String[] args) {
        System.exit(new ContentHandlerTest().test(args));
    }

    public Result testConstructor() throws URISyntaxException {
        new CH();
        return passed();
    }
}

class CH extends ContentHandler {
    public CH() {
        super();

    }
    public Object getContent(URLConnection arg0) throws IOException {
        return "hello";
    }
}