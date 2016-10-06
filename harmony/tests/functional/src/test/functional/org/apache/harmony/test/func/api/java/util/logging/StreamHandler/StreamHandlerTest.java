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
 * Created on 06.09.2005
 *
 */

package org.apache.harmony.test.func.api.java.util.logging.StreamHandler;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

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

class MyStreamHandler extends StreamHandler {
    public MyStreamHandler (){
        super();
    }
    public void setOutputStream (OutputStream out) throws SecurityException {
        super.setOutputStream(out);
    }
}

public class StreamHandlerTest extends MultiCase{
    public final Level[] levels = {
            Level.SEVERE,
            Level.WARNING, 
            Level.INFO,
            Level.CONFIG, 
            Level.FINE, 
            Level.FINER, 
            Level.FINEST 
    };
    
//     Charsets: jre/lib/charsets/sun/io/charset.jar
    public final String encodings[] = {
            "koi8-r", 
            "UTF-8",  
            "cp1251", 
            "cp1255", 
            "cp1256",
            "cp1252",
            "ISO8859-1",
             };
    
    public static void main(String[] args) {
        System.exit(new StreamHandlerTest().test(args));
    }

    public Result testSetGetEncoding() throws SecurityException, UnsupportedEncodingException {

        StreamHandler handler = new StreamHandler();
        
        for (int i=0; i< encodings.length; i++) {
            handler.setEncoding(encodings[i]);
            String enc = handler.getEncoding();
            if (!enc.equals(encodings[i])) {
                return failed ("wrong encoding: " + enc + ", should be " + encodings[i]);
            }
        }
        
        return passed();
    }
    
    public Result testSetOutputStream() {
        MyStreamHandler handler = new MyStreamHandler();
        Logger logger = Logger.getLogger("name");
        logger.addHandler(handler);
        
        OutputStream stream = new ByteArrayOutputStream();
        
        handler.setOutputStream(stream);
        logger.setUseParentHandlers(false);
        
        logger.log(Level.INFO, "Test the output stream");
        handler.flush();
        
        byte[] buf = ((ByteArrayOutputStream)stream).toByteArray();
        String outS = new String(buf);
        if (outS.indexOf("Test the output stream") == -1) {
            return failed ("Message isn't logged into the new stream!");
        }

        return passed();
    }
    public Result testFlush () {
           MyStreamHandler handler = new MyStreamHandler();
        Logger logger = Logger.getLogger("name");
        logger.addHandler(handler);
        
        OutputStream stream = new ByteArrayOutputStream();
        
        handler.setOutputStream(stream);
        logger.setUseParentHandlers(false);
        
        logger.log(Level.INFO, "Test the output stream");
        //handler.flush();
        
        byte[] buf = ((ByteArrayOutputStream)stream).toByteArray();
        String outS = new String(buf);
        if (outS.indexOf("Test the output stream") != -1) {
            return failed ("Message shouldn't logged into the new stream becouse flush() method wasn't applied! ");
        }
        
        handler.flush();
        
        buf = ((ByteArrayOutputStream)stream).toByteArray();
        outS = new String(buf);
        if (outS.indexOf("Test the output stream") == -1) {
            return failed ("Message should logged into the new stream becouse flush() method was applied! ");
        }
        
        return passed();       
    }
    public Result testIsLoggable() {
        boolean t;
        
        Logger logger = Logger.getLogger("name");
        
        //if no output stream has been assigned yet. 
        MyStreamHandler handler = new MyStreamHandler();
        logger.addHandler(handler);
        
        LogRecord record = new LogRecord(Level.INFO, Level.INFO.getName());
        
        if (handler.isLoggable(record)) {
            return failed ("Stream is not available, isLoggable(record) should return false!");
        }
        
        OutputStream stream = new ByteArrayOutputStream();
        handler.setOutputStream(stream);
        
        t = handler.isLoggable(record);
        if (!handler.isLoggable(record)) {
            return failed ("Stream is available, isLoggable(record) should return true!");
        }
        // check if the LogRecord has an appropriate level
        
        handler.setLevel(Level.OFF);
        record.setLevel(Level.ALL);
        if (handler.isLoggable(record)) {
            return failed ("isLoggable returns wrong value: should return false");
        }
        
        handler.setLevel(Level.ALL);
        record.setLevel(Level.OFF);
        if (!handler.isLoggable(record)) {
            return failed ("isLoggable returns wrong value: should return true");
        }
            
        for (int i=0; i< levels.length; i++) {
            handler.setLevel(levels[i]);       
            
            if (i < levels.length-1) {
                record.setLevel(levels[i+1]);
                if (handler.isLoggable(record)) {
                    return failed ("Level " + levels[i+1] + " should not be logged!");
                }
            }
        }
        return passed();
    }
    public Result testPublish() throws IOException {
        LogRecord record = new LogRecord (Level.ALL, "INFO");    
        Logger logger = Logger.getLogger("name");
        
        OutputStream stream = new ByteArrayOutputStream();
        StreamHandler handler = new StreamHandler(stream, new MyFormatter());
        logger.addHandler(handler);
        
        handler.publish(record);
        handler.flush();
         
        byte[] buf = ((ByteArrayOutputStream)stream).toByteArray();
        String outS = new String(buf);
        if (!outS.equals("")) {
            return failed ("Unexpected logged message published: " + outS);
        }
        record.setLevel(Level.INFO);
        
        handler.publish(record);
        handler.flush();
        
        buf = ((ByteArrayOutputStream)stream).toByteArray();
        outS = new String(buf);
        if (!outS.equals("RESULT is INFO")) {
            return failed ("Unexpected logged message published: " + outS);
        }
        
        
        return passed();
        
    }
}