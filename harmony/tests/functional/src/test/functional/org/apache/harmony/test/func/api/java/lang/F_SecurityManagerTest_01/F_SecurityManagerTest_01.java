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
/*
 */
package org.apache.harmony.test.func.api.java.lang.F_SecurityManagerTest_01;

import auxiliary.*;
import org.apache.harmony.test.func.share.ScenarioTest;
import java.io.*;
import java.security.AccessControlException;
import java.security.SecurityPermission;

/**
 * 
 * 
 */
public class F_SecurityManagerTest_01 extends ScenarioTest {
    static String name = System.getProperty("qe.test.dir").replace('\\','/') + "/file.txt";;
    SecurityManager sm;
    ThreadGroup tg;
    Object obj = new Object();
    volatile int stat = 0;

    public static void main(String[] args) {
        name = System.getProperty("qe.test.dir").replace('\\','/') + "/file.txt";
        System.exit(new F_SecurityManagerTest_01().test(args));
    }
    

    class MyThread extends Thread {
        ParentObject tested;
        String name;
                
        public MyThread(ThreadGroup group, String tname, ParentObject obj, String name) {
            super(group, tname);
            tested = obj;
            this.name = name;
        }
        
        public void run() {
            synchronized(obj) {
                obj.notifyAll();
            }
            tested.setConext();
            try {
                tested.checkDelete(name);
                if (getName().equals("t2")) {
                    getThreadGroup().uncaughtException(this, new Exception("denied.checkDelete() doesn't throw AccessControlException."));
                }
            } catch (AccessControlException e) {
                if (getName().equals("t1")) {
                    throw e;
                }
            }
            try {
                tested.checkPermission(new RuntimePermission("getProtectionDomain"));  
                if (getName().equals("t2")) {
                    getThreadGroup().uncaughtException(this, new Exception("denied.checkPermission() doesn't throw AccessControlException."));
                }
            } catch (AccessControlException e) {
                if (getName().equals("t1")) {
                    throw e;
                }
            }                      
        }
    }
    
    class MyTreadGroup extends ThreadGroup {

        public MyTreadGroup(String arg0) {
            super(arg0);
        }
        
        public void uncaughtException(Thread t, Throwable e) {
            if (t.getName().equals("t1")) {
                stat++;
            }
            super.uncaughtException(t, e);
        }
    }

    public int test() {
//        File f = new File(name);
//                      
//        try {
//            log.info(""+f.createNewFile());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        
//        System.out.println(getClass().getProtectionDomain().getPermissions());

        sm = new SecurityManager();
        System.setSecurityManager(sm);  
        
        tg = new MyTreadGroup("my group");
        
        
        if (!task1()) {
            return fail("FAIL in task1.");
        }
        if (!task2()) {
            return fail("FAIL in task2.");
        }
        if (stat ==0) {
            return pass();
        } else {
            return fail("FAIL in child thread.");
        }
    }
    
    private boolean task1() {
        Trusted trust = new Trusted();
        Granted tested = new Granted(trust);
        try {
            trust.checkRead(name);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        Thread t1 = new MyThread(tg, "t1", tested, name);        
        synchronized(obj) {
            t1.start();
            try {
                obj.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            trust.checkRead(name);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        try {
            trust.checkConnect("localhost", 12345);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        try {
            trust.checkConnect("www.example.com", 8080);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        try {
            trust.checkPermission(new RuntimePermission("getProtectionDomain"));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    private boolean task2() {
        Trusted trust = new Trusted();
        Denied tested = new Denied(trust);
        try {
            trust.checkPermission(new SecurityPermission("getPolicy"));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        Thread t1 = new MyThread(tg, "t2", tested, name);        
        synchronized(obj) {
            t1.start();
            try {
                obj.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            trust.checkRead(name);
            return false;
        } catch (AccessControlException e) {
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        try {
            trust.checkConnect("www.example.com", 8080);
            return false;
        } catch (AccessControlException e) {
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        try {
            trust.checkPermission(new SecurityPermission("getPolicy"));
            return false;
        } catch (AccessControlException e) {
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}