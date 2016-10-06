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
 * @author Gregory Shimansky, Petr Ivanov
 * @version $Revision: 1.3 $
 */  
/*
 * Created on 18.11.2004
 */
package org.apache.harmony.vts.test.vm.jni.monitors;

import org.apache.harmony.vts.test.vm.jni.share.JNITest;

/**
 * @author Gregory Shimansky
 *
 * Test for MonitorEnter and MonitorExit functions
 */
public class MonitorsTest extends JNITest {

    /**
     * @see org.apache.harmony.vts.test.vm.jni.share.JNITest#execute()
     */
    public boolean execute() throws Exception {
        SyncObject so = new SyncObject();
        long sleep = 10000;
        NativeThread nt = new NativeThread(so, sleep);
        nt.start();
        nt.join();
        long time = nt.getExecutionTime();

        if (time < sleep / 2 || time > sleep * 1.5)
            return false;

        return nt.isSuccess() && so.isFlagSet();
    }
    public static void main(String[] args){
        System.exit(new MonitorsTest().test());
    }
}
class NativeThread extends Thread {
    private SyncObject syncObj;
    private long sleepTime;
    private long executionTime;
    private boolean success;
    private native boolean nativeExecute(SyncObject so, Thread t, long sleep);

    public NativeThread(SyncObject syncObj, long sleepTime) {
        this.syncObj = syncObj;
        this.sleepTime = sleepTime;
        success = false;
    }

    public void run() {
        long t = System.currentTimeMillis();
        JavaThread jt = new JavaThread(syncObj);

        success = nativeExecute(syncObj, jt, sleepTime);
// Native code performs the following returning exception flag:
//        synchronized (syncObj) {
//            jt.start();
//
//            try {
//                sleep(sleepTime);
//            } catch (InterruptedException e) {
//                exception = true;
//            }
//        }

        executionTime = System.currentTimeMillis() - t;

        try {
            jt.join();
        } catch (InterruptedException e) {
            success = false;
        }
    }
    /**
     * @return Returns the success.
     */
    public boolean isSuccess() {
        return success;
    }
    /**
     * @return Returns the executionTime.
     */
    public long getExecutionTime() {
        return executionTime;
    }
}

class JavaThread extends Thread {
    private SyncObject syncObj;

    public JavaThread(SyncObject syncObj) {
        this.syncObj = syncObj;
    }

    public void run() {
        synchronized (syncObj) {
            syncObj.setFlag();
        }
    }
}

class SyncObject {
    private boolean flag;

    SyncObject() {
        flag = false;
    }

    public boolean isFlagSet() {
        return flag;
    }

    public void setFlag() {
        flag = true;
    }
}