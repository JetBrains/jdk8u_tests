/*
    Copyright 2005-2006 The Apache Software Foundation or its licensors, as applicable

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

    See the License for the specific language governing permissions and
    limitations under the License.
*/
/**
 * @author Aleksey V Golubitsky
 * @author Vladimir A. Ivanov
 * @version $Revision: 1.13 $
 */

package org.apache.harmony.share;

import java.text.DecimalFormat;
import java.util.List;

import org.apache.harmony.share.framework.performance.TimeMeasurer;

public abstract class Test extends Base {

    public abstract int test();

    public int test(String[] args) {
        log.setLevel(getLogLevel(args));
        removeLogLevel(args);
        try {
            setPerformance(args);
        } catch (IllegalArgumentException iae) {
            return error("Invalid performance parameters: " + iae.getMessage());
        }
        int result;
        if (performance) {
            TimeMeasurer tm = new TimeMeasurer();
            try {
                before();
                tm.startTotal(duration, minTestsCount);
                do {
                    result = test();
                    if (result != Result.PASS) {
                        break;
                    }
                } while (tm.isActive());
                after();
            } catch (Exception e) {
                return error(e.getMessage());
            }
            // All data in System.out should be in xml 1.0 format. The default
            // separation: out - framework data (like performance), err - test
            // output data
            float rate = 1f;
            if (tm.getActualTestsCount() != 0) {
                rate = (float)tm.getActualDuration()
                    / (float)tm.getActualTestsCount();
            }
            //all perf times store in the seconds with format 0.0001
            DecimalFormat decFormat = new DecimalFormat("#######0.0000");
            System.out.println("<perfdata>\n"
                + "<established_duration data=\""
                + decFormat.format(duration / 1000.0)
                + "\"/>\n"
                + "<actual_duration  data=\""
                + decFormat.format(tm.getActualDuration() / 1000.0)
                + "\"/>\n"
                + "<overdue data=\""
                + decFormat
                    .format((tm.getActualDuration() - duration) / 1000.0)
                + "\"/>\n" + "<tests_executed data=\""
                + tm.getActualTestsCount() + "\"/>\n" + "<rate data=\""
                + decFormat.format(rate / 1000.0) + "\"/>\n");
            List times = tm.getRunTimes();
            for (int i = 0; i < times.size(); i++) {
                System.out.println("<exec_time iteration=\""
                    + i
                    + "\" time=\""
                    + decFormat
                        .format(((Long)times.get(i)).doubleValue() / 1000)
                    + "\"/>");
            }
            System.out.println("</perfdata>");
        } else {
            try {
                before();
                result = test();
                after();
            } catch (Exception e) {
                return error(e.getMessage());
            }
        }
        return result;
    }

    public int test(String[] args, DRLLogging testLog) {
        log = testLog;
        return test(args);
    }

    public int pass() {
        return pass("PASSED");
    }

    public int pass(String msg) {
        log.info(msg);
        return Result.PASS;
    }

    public int fail(String msg) {
        log.warning(msg);
        return Result.FAIL;
    }

    public int error(String msg) {
        log.severe(msg);
        return Result.ERROR;
    }
}