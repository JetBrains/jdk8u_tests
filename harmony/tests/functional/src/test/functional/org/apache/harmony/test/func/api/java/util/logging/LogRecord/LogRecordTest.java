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

package org.apache.harmony.test.func.api.java.util.logging.LogRecord;

import java.util.Enumeration;
import java.util.ListResourceBundle;
import java.util.ResourceBundle;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.apache.harmony.test.func.api.java.share.PropertyTest;
import org.apache.harmony.share.Test;

/*
 * 30.08.2005
 */
public class LogRecordTest extends Test {

    int result;

    public int test() {
        // We should penetrate into the logging framework
        // to allow LogRecord sucessfully parce call stack
        Logger logger = Logger.getLogger("Tested Logger");
        logger.setFilter(new MyFilter());
        logger.log(new LogRecord(Level.INFO,
                "She sells seashells on the seashore"));
        return result;
    }

    private class MyFilter implements Filter {

        public boolean isLoggable(LogRecord record) {
            result = LogRecordTest.this.test_impl(record);
            return false;
        }
    }

    public int test_impl(LogRecord cmp) {

        long millis = System.currentTimeMillis();
        Class cls = LogRecord.class;

        PropertyTest.Data[] simple = {
                new PropertyTest.Data(cls, "Level", Level.WARNING, Level.INFO),
                new PropertyTest.Data(cls, "LoggerName", "Another Logger",
                        "Tested Logger"),
                new PropertyTest.Data(cls, "Message",
                        "The shells that she sells are the seashells I'm sure",
                        "She sells seashells on the seashore"),
                new PropertyTest.Data(cls, "Millis", new Long(millis / 2),
                        new Long(millis)),
                new PropertyTest.Data(cls, "Parameters",
                        new Object[] { new Object[] { "a", "b", "c" } }, null),
                new PropertyTest.Data(cls, "ResourceBundle",
                        new ResourceBundle() {

                            protected Object handleGetObject(String arg0) {
                                return null;
                            }

                            public Enumeration getKeys() {
                                return null;
                            }
                        }, null),
                new PropertyTest.Data(cls, "ResourceBundleName", "name", null),
                new PropertyTest.Data(cls, "SequenceNumber", new Long(175)),
                new PropertyTest.Data(cls, "SourceClassName", this.getClass()
                        .getName()
                        + "$FakeInnerClass", this.getClass().getName()),
                new PropertyTest.Data(cls, "SourceMethodName", "fakeMethod",
                        "test"),
                new PropertyTest.Data(cls, "ThreadID", new Integer(100)),
                new PropertyTest.Data(cls, "Thrown", new Throwable("hello"),
                        null),

        };

        int failCount = 0;
        for (int i = 0; i < simple.length; i++) {
            if (!simple[i].test(cmp)) {
                this.error(simple[i].name + " property have invalid behaviour");
                failCount++;
            }
        }
        if (failCount > 0) {
            return fail(failCount + " properties failed");
        } else {
            return pass();
        }

    }

    public static void main(String[] args) {
        PropertyTest.Data.setLogger(log);
        System.exit(new LogRecordTest().test(args));
    }
}