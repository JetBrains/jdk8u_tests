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
 * Created on 30.08.2005
 *
 */

package org.apache.harmony.test.func.api.java.util.logging.SimpleFormatter;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;



public class SimpleFormatterTest extends MultiCase{
    
    public static void main(String[] args) {
        System.exit(new SimpleFormatterTest().test(args));
    }

    public Result testSimpleFormatter() throws IOException  {
        LogRecord record;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        SimpleFormatter formatter = new SimpleFormatter();
        Handler handler = new StreamHandler(stream, formatter);
     
        record = new LogRecord(Level.INFO, "Message");
        
        handler.publish(record);
        handler.flush();
        
        InputStream in = new ByteArrayInputStream(stream.toByteArray());      
        byte[] bufferArray = new byte[in.available()];
        in.read(bufferArray);
        String inS = new String(bufferArray);
        
        if (inS.indexOf("INFO: Message")<0) {
            return failed ("1: Formatted message doesn't include Level or Message of LogRecord: " +
                    inS);
        }
        
        
        String formatted = formatter.format(record);
        
        if (formatted.indexOf("INFO: Message")<0) {
            return failed ("2: Formatted message doesn't include Level or Message of LogRecord: " +
                    formatted);
        }
        return passed();
    }
        
}