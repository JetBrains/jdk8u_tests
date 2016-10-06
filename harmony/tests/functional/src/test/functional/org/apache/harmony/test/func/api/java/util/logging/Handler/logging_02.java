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
 * Created on 31.08.2005
 *
 */

package org.apache.harmony.test.func.api.java.util.logging.Handler;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;


class myHandler_02 extends Handler {
    public myHandler_02 () {
        super();
    }
    public void close () {}
    public void flush() {}

    public void publish(LogRecord lr) { }
}

public class logging_02 extends MultiCase{
    public final Level[] levels = {
                Level.SEVERE,
                Level.WARNING, 
                Level.INFO,
                Level.CONFIG, 
                Level.FINE, 
                Level.FINER, 
                Level.FINEST 
    };
    
    public static void main(String[] args) {
        System.exit(new logging_02().test(args));
    }

    public Result testSetGetLevel () {
        LogRecord record = new LogRecord(Level.INFO, "test");
        
        Handler handler = new myHandler_02();
        Logger logger = Logger.getLogger("org.apache.harmony.test.func.api.java.util.logging.Handler.logging_02");
        logger.addHandler(handler);
        logger.setUseParentHandlers(false);
        
        handler.setLevel(Level.OFF);
        logger.log(Level.FINEST, "Log level is currently OFF", new RuntimeException("Log level is OFF, this message should not be logged! "));
        if (!handler.getLevel().equals(Level.OFF)) {
            return failed ("getLevel should return Level.OFF");
        }
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
                
                logger.log(levels[i+1], levels[i].toString(), new RuntimeException("Level " + levels[i].toString() + " should not be logged! "));
            }
            
            Level l = handler.getLevel();
            
            if (!l.equals(levels[i])) {
                return failed ("getLevel returns wrong level: " + l + ", should be " + levels[i]);
            }
        }
        handler.setLevel(Level.INFO);
       
        return passed();
    }
        
}