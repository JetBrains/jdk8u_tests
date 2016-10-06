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
 * Created on 29.08.2005
 *
 */

package org.apache.harmony.test.func.api.java.util.logging.Logger;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;



public class logging_01 extends MultiCase {
    public static void main(String[] args) {
        System.exit(new logging_01().test(args));
    }
    public Result testLoggerHandlers() {
        Logger plogger = Logger.getLogger("org.apache.harmony.test.func.api.java.util.logging");
        setHandlers(plogger);
        Logger logger = Logger.getLogger("org.apache.harmony.test.func.api.java.util.logging.Logger");
        setHandler(logger);
        logger.setParent(plogger);
        int i = countHandlers(plogger);
        if (i != 2) {
            return failed ("1: getHandlers should return 2 handlers");
        }
        boolean parent = logger.getUseParentHandlers();
        if (parent) {
            int j = countHandlers(logger);
            if (j != 1) {
                return failed ("2: getHandlers should return 1 handlers");
            }
        }
        else {
            return failed ("getUseParentHandlers should return true");
        }
        if (i >= 0) {
            removeOneHandler(plogger);
            i = countHandlers(plogger);
            if (i != 1) {
                return failed ("3: getHandlers should return 1 handlers");
            }
            i = countHandlers(logger);
            if (i != 1) {
                return failed ("4: getHandlers should return 1 handlers");
            }
        }
        removeOneHandler(logger);
        
        logger.setUseParentHandlers(false);
        
        logger.log(Level.INFO, "test", new RuntimeException("Parent handlers are disabled"));

        if (logger.getUseParentHandlers()) {
            return failed ("getUseParentHandlers should return false");
        }
        logger.setUseParentHandlers(true);
        if (!logger.getUseParentHandlers()) {
            return failed ("getUseParentHandlers should return true");
        }
        if (!logger.getParent().equals(plogger)) {
            return failed ("getParent() returns wrong parent logger");
        }
        return passed();
    }

    protected void setHandler (Logger logger) {
        StreamHandler handler = new StreamHandler();
        handler.setLevel(Level.INFO);    
        logger.addHandler(handler);
    }    
    protected void setHandlers (Logger logger) {
        StreamHandler handler = new StreamHandler();
        handler.setLevel(Level.INFO);
        ConsoleHandler errHandler = new ConsoleHandler();
        errHandler.setLevel(Level.WARNING);

        logger.addHandler(handler);
        logger.addHandler(errHandler);
    }
    protected int countHandlers(Logger logger) {
        Handler[] h = logger.getHandlers();
        return h.length;
    }
    protected void removeOneHandler (Logger logger) {
        Handler[] h = logger.getHandlers();
        if (h.length >= 0) {
            logger.removeHandler(h[0]);
        }
    }
}
