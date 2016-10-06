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

package org.apache.harmony.test.func.api.java.util.logging.Handler;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;


class formatterHandler extends Handler {
    public formatterHandler () {
        super();
    }
    public void close () {}
    public void flush() {}

    public void publish(LogRecord lr) {
        String s = this.getFormatter().format(lr);
        System.out.println(s);
    }
}

class myFormatter extends Formatter {

    public String format(LogRecord arg0) {
        String s = arg0.getMessage();
        s = s.replaceAll(" ", "");
        s = s.replaceAll("\n", "");
        s = s.replaceAll("\r", "");
        return s;
    }
    
}

public class HandlerGetSetFormatterTest extends MultiCase{
    
    public static void main(String[] args) {
        System.exit(new HandlerGetSetFormatterTest().test(args));
    }

    public Result testSetGetFormatter() throws IOException {
        PrintStream stdout = System.out;
        Result result = failed ("Can not happen.");
        try {
            result = runSetGetFormatter();
        }
        finally {
            System.setErr(stdout);
        }
        return result;
    }
    public Result runSetGetFormatter() throws IOException  {
        LogRecord record;
        
        Formatter formatter = new myFormatter();
        Handler handler = new formatterHandler();
        handler.setFormatter(formatter);
        
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(stream);

        System.setOut(ps);
        
        record = new LogRecord(Level.INFO, "Some separate\nwords");
        
        handler.publish(record);
        handler.flush();
        
        InputStream in = new ByteArrayInputStream(stream.toByteArray());      
        byte[] bufferArray = new byte[in.available()];
        in.read(bufferArray);
        String inS = new String(bufferArray);
        
        if (inS.indexOf("Someseparatewords")<0) {
                      
            return failed("1: Wrong formatted message: " + inS);
        }
        
        if (!handler.getFormatter().equals(formatter)) {
            return failed ("1: getFormatter return wrong formatter!");
        }
        
        Formatter simpleFormatter = new SimpleFormatter();
        handler.setFormatter(simpleFormatter);
        
        handler.publish(record);
        handler.flush();
        
        in = new ByteArrayInputStream(stream.toByteArray());      
        bufferArray = new byte[in.available()];
        in.read(bufferArray);
        inS = new String(bufferArray);

        if (inS.indexOf("Some separate\nwords")<0) {
            return failed("2: Wrong formatted message: " + inS);
        }
        
        if (!handler.getFormatter().equals(simpleFormatter)) {
            return failed ("2: getFormatter return wrong formatter!");
        }
        
        return passed();
    }
        
}