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
 * This is an additional class which is used to re-direct standart error and
 * output streams for System.exec(...) calls.
 */

package org.apache.harmony.test.share.reg;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class StreamRedirector extends Thread {
    private InputStream inStream;
    private OutputStream outStream;
    
    public StreamRedirector(InputStream stream) {
        this(stream, null);
    }
    
    public StreamRedirector(InputStream stream1, OutputStream stream2) {
        inStream = stream1;
        outStream = stream2;
    }
    
    public void run() {
        try {
            String str;
            PrintWriter writer = (outStream == null) 
                    ? null : new PrintWriter(outStream);
            InputStreamReader reader = new InputStreamReader(inStream);
            BufferedReader bReader = new BufferedReader(reader);
            while ((str = bReader.readLine()) != null) {
                if(writer != null) {
                    writer.println(str);
                }
            }
            if (writer != null) {
                writer.flush();
            }
        } catch(IOException e) {}
    }
}
