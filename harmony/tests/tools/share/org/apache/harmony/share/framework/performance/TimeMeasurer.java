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
 * @version $Revision: 1.9 $
 */

package org.apache.harmony.share.framework.performance;

import java.util.List;
import java.util.Vector;

public class TimeMeasurer {

    protected long    startTime      = 0;
    protected long    actualDuration = 0;
    protected long    tcount         = 0;
    protected long    duration       = -1;
    protected long    mtcount        = -1;

    private final int type_duration  = 1;
    private final int type_count     = 1 << 1;
    private int       type           = 0;

    private long      prevRunTime    = 0;
    private Vector    runTimes       = new Vector();

    public void startDuration(long d) {
        duration = d;
        start(type_duration);
    }

    public void startCount(long mtc) {
        mtcount = mtc;
        start(type_count);
    }

    public void startTotal(long d, long mtc) {
        duration = d;
        mtcount = mtc;
        start(type_count | type_duration);
    }

    private void start(int t) {
        type = t;
        tcount = 0;
        startTime = System.currentTimeMillis();
        prevRunTime = startTime;
    }

    public boolean isActive() {
        long time = System.currentTimeMillis();
        actualDuration = (time - startTime);
        runTimes.add(new Long(time - prevRunTime));
        prevRunTime = time;
        tcount++;
        if ((type & type_duration) != 0 && (type & type_count) != 0) {
            if (actualDuration < duration && tcount < mtcount) {
                return true;
            }
        } else if ((type & type_duration) != 0) {
            if (actualDuration < duration) {
                return true;
            }
        } else if ((type & type_count) != 0) {
            if (tcount < mtcount) {
                return true;
            }
        }
        return false;
    }

    public long getActualDuration() {
        return actualDuration;
    }

    public long getActualTestsCount() {
        return tcount;
    }

    public double getAverageTestDuration() {
        return (double)actualDuration / tcount;
    }

    public List getRunTimes() {
        return (List)runTimes.clone();
    }
}