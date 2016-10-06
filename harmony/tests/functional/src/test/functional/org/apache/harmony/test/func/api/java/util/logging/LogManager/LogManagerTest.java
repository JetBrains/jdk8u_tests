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

package org.apache.harmony.test.func.api.java.util.logging.LogManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Permission;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.LoggingPermission;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/*
 * 09.09.2005
 */
public class LogManagerTest extends MultiCase {

    private class InvalidPermission extends RuntimeException {

        private static final long serialVersionUID = -2063492733975489473L;

        public InvalidPermission(String message) {
            super(message);
        }
    }

    private class MySecurityManager extends SecurityManager {

        public void checkPermission(Permission p) {
            if (!(p instanceof LoggingPermission))
                super.checkPermission(p);
            LoggingPermission lp = (LoggingPermission) p;
            if (!lp.getName().equals("control")) {
                throw new InvalidPermission("Invalid permission check!");
            }
        }
    }

    public Result test_checkAccess() {
        boolean failed = false;
        SecurityManager security = System.getSecurityManager();
        LogManager manager = LogManager.getLogManager();
        if (security != null) {
            System.setSecurityManager(new MySecurityManager());
            try {
                manager.checkAccess();
            } catch (InvalidPermission e) {
                log.add(e.getMessage());
                failed = true;
            }
            System.setSecurityManager(security);
        }
        return failed ? failed("") : passed();
    }

    public Result test_addLogger() {
        String name = this.getClass().toString() + ":1";
        LogManager manager = LogManager.getLogManager();
        Logger myLogger = manager.getLogger(name);
        if (null != myLogger) {
            return failed("LogManager.getLogger(String) should return null,"
                    + " if none suitable found");
        }

        myLogger = Logger.getLogger(name);

        Logger same = manager.getLogger(name);

        if (!same.equals(myLogger)) {
            return failed("LogManager.getLogger() should return same"
                    + " logger as was added");
        }

        return passed();
    }

    private static class MyLogger extends Logger {
        private static int serial = 0;

        private int id;

        public MyLogger(String name) {
            super(name, null);
            id = serial++;
        }

        public boolean equals(Object o) {
            if (o instanceof MyLogger) {
                if (((MyLogger) o).id == id) {
                    return true;
                }
            }
            return false;
        }
    }

    public Result test_addLoggerKinked() {
        String name = this.getClass().toString() + ":2";
        LogManager manager = LogManager.getLogManager();
        Logger myLogger = manager.getLogger(name);
        if (null != myLogger) {
            return failed("LogManager.getLogger(String) should return null,"
                    + " if none suitable found");
        }

        myLogger = new MyLogger(name);

        if (null != manager.getLogger(name)) {
            return passed("This test is invalid!");
        }

        manager.addLogger(myLogger);

        Logger same = manager.getLogger(name);

        if (!same.equals(myLogger)) {
            return failed("LogManager.getLogger() should return same"
                    + " logger as was added");
        }

        return passed();
    }

    public Result test_getLoggerNames() {
        String[] loggerNames = { "Logger1", "Logger2", "Logger3" };

        LogManager manager = LogManager.getLogManager();
        for (int i = 0; i < loggerNames.length; i++) {
            manager.addLogger(Logger.getLogger(loggerNames[i]));
        }

        Enumeration enumeration = manager.getLoggerNames();

        int found = 0;
        while (enumeration.hasMoreElements()) {
            Object elem = enumeration.nextElement();
            for (int i = 0; i < loggerNames.length; i++) {
                if (loggerNames[i].equals(elem)) {
                    found++;
                }
            }
        }
        return found == loggerNames.length ? passed()
                : failed("Some loggers was misplaced");
    }

    private class MyHandler extends Handler {

        public void publish(LogRecord arg0) {
        }

        public void flush() {
        }

        private boolean closed = false;

        public void close() throws SecurityException {
            closed = true;
        }

        public boolean isClosed() {
            return closed;
        }
    }

    public Result test_reset() {
        LogManager manager = LogManager.getLogManager();
        Logger logger = Logger.getLogger("test_reset_Logger");
        Level level = logger.getLevel();
        if (level == null) {
            logger.setLevel(Level.WARNING);
            level = Level.WARNING;
        }

        Logger root = logger.getParent();
        root.setLevel(Level.WARNING);

        MyHandler handler = new MyHandler();
        logger.addHandler(handler);
        Handler[] handlers = logger.getHandlers();

        manager.reset();

        assertTrue("logger.getHandlers().length >= 1", handlers.length >= 1);
        assertTrue("logger.getHandlers().length == 0",
                logger.getHandlers().length == 0);
        assertEquals("root.getLevel(), Level.INFO", root.getLevel(), Level.INFO);
        assertTrue("handler.isClosed()", handler.isClosed());
        assertNull("logger.getLevel()", logger.getLevel());

        return result();
    }

    public static void main(String[] args) {
        System.exit(new LogManagerTest().test(args));
    }
}