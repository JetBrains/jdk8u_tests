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
 * Created on 07.09.2005
 *
 */

package org.apache.harmony.test.func.api.java.util.logging.ConsoleHandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

class MyFormatter extends Formatter {
    public MyFormatter (){
        super();
    }
    public String format(LogRecord arg0) {
        return "RESULT is " + arg0.getMessage();
    }
}

public class ConsoleHandlerTest extends MultiCase{

    public static void main(String[] args) {
        System.exit(new ConsoleHandlerTest().test(args));
    }

    public Result testPublish() throws IOException {
        PrintStream stderr = System.err;
        Result result = failed("Can not happen.");
        try {
            result = runPublish();
        }
        finally {
            System.setErr(stderr);
        }
        return result;
    }
    
    public Result runPublish() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(stream);
        

        System.setErr(ps);
        
        ConsoleHandler handler = new ConsoleHandler();
        
        LogRecord record = new LogRecord (Level.INFO, "It is STDERR");    
        Logger logger = Logger.getLogger("ConsoleHandlerLogger");
        
        if (!handler.isLoggable(record)) {
            return failed ("handler should be loggable!");
        }
        handler.publish(record);
        handler.flush();
        
        InputStream in = new ByteArrayInputStream(stream.toByteArray());      
        byte[] bufferArray = new byte[in.available()];
        in.read(bufferArray);
        String inS = new String(bufferArray);
        
        if (inS.indexOf("It is STDERR") < 0) {
            return failed ("The message isn't logged into the stderr!");
        }
       
        return passed();
        
    }
    public Result testClose() throws IOException {
        PrintStream stderr = System.err;
        Result result = failed ("Can not happen.");
        try {
            result = runClose();
        }
        finally {
            System.setErr(stderr);
        }
        return result;
    }
    
    public Result runClose() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(stream);

        System.setErr(ps);
        LogRecord record = new LogRecord (Level.INFO, "The stream is not closed");    
        Logger logger = Logger.getLogger("ConsoleHandlerLogger");
        
        ConsoleHandler handler = new ConsoleHandler();

        //ConsoleHandler.close does a flush but does not close the output stream. 
        handler.close();
        if (!handler.isLoggable(record)) {
            return failed ("handler should be loggable!");
        }
        
        
        handler.publish(record);
        handler.close();
        if (!handler.isLoggable(record)) {
            return failed ("handler should be loggable!");
        }
        
        InputStream in = new ByteArrayInputStream(stream.toByteArray());      
        byte[] bufferArray = new byte[in.available()];
        in.read(bufferArray);
        String inS = new String(bufferArray);
        
        if (inS.indexOf("The stream is not closed") < 0) {
            return failed ("The message isn't logged into the stderr!");
        }
        
        handler.flush();
        handler.close();
        if (!handler.isLoggable(record)) {
            return failed ("handler should be loggable!");
        }
        
        return passed();
    }
}