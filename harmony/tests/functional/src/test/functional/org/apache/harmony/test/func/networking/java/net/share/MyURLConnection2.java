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
/*
 * Created on 25.11.2004
 *
 */
package org.apache.harmony.test.func.networking.java.net.share;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;

public class MyURLConnection2 extends URLConnection {

    protected MyURLConnection2(URL arg0) {
        super(arg0);
    }

    public InputStream getInputStream() throws IOException {
        String s = "abcd";
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(stream);
        //ObjectOutputStream oos = new ObjectOutputStream(stream);
        
        //oos.writeObject(s);
        
        //oos.close();
        ps.print(s);
        ps.close();
        return new ByteArrayInputStream(stream.toByteArray());
    }

    public void connect() throws IOException {
        
    }
    

    public String getContentType() {
        return "text/html";
    }
}