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

package org.apache.harmony.test.func.api.java.util.logging.Handler;


import java.util.logging.Filter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;


class filterHandler extends Handler {
    public filterHandler () {
        super();
    }
    public void close () {}
    public void flush() {}

    public void publish(LogRecord lr) { }
}

class myFilter implements Filter {

    public boolean isLoggable(LogRecord rec) {
        String message = rec.getMessage();
        Level level = rec.getLevel();
        if (level.equals(Level.FINE) & message.indexOf("filter") > 0) {
            return true;
        }
        return false;
    }
    
}

public class HandlerGetSetFilterTest extends MultiCase{
    public static void main(String[] args) {
        System.exit(new HandlerGetSetFilterTest().test(args));
    }

    public Result testGetSetFilter() {
        LogRecord record;
        
        Handler handler = new filterHandler();
        Filter filter = new myFilter();
        handler.setFilter(filter);
        if (!handler.getFilter().equals(filter)) {
            return failed ("GetFilter returns wrong filter!");
        }
        record = new LogRecord (Level.INFO, "Test");
        if (handler.isLoggable(record)) {
            return failed("1: isLoggable should return false.");
        }
        
        record = new LogRecord (Level.FINE, "Test");
        if (handler.isLoggable(record)) {
            return failed("2: isLoggable should return false.");    
        }
        
        record = new LogRecord (Level.FINE, "Somefilter");
        if (!handler.isLoggable(record)) {
            return failed("3: isLoggable should return true.");
        }
        
        record = new LogRecord (Level.WARNING, "Last filter");
        if (handler.isLoggable(record)) {
            return failed("4: isLoggable should return false.");    
        }
        
        handler.setFilter(null);
        
        record = new LogRecord (Level.INFO, "Test");
        if (!handler.isLoggable(record)) {
            return failed("5: isLoggable should return true.");
        }
        
        if (handler.getFilter() != null) {
            return failed ("GetFilter returns wrong filter!");
        }
        
        return passed();
    }
        
}