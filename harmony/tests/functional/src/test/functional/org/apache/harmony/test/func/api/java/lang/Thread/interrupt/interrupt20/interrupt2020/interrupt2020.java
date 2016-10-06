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
 *          Date: Dec 9, 2005
 *          Time: 3:33:48 PM
 */

package org.apache.harmony.test.func.api.java.lang.Thread.interrupt.interrupt20.interrupt2020;

import org.apache.harmony.share.Test;

import java.security.Permission;

public class interrupt2020  extends Test {
    public static final String TESTED_THREAD_NAME = "SecurityManagerTestThread";

    volatile static boolean tStop = false;
    Object notification = new Object();

    class T extends Thread {
        public T( String str){
            super( str );
        }
        public void run () {
            info( "run started");
            while ( !tStop ) {
                try {
                    Thread.currentThread().sleep(2000);
                }catch( InterruptedException ie){
                    ie.printStackTrace();
                    interrupt2020.tStop = true;
                }catch( Exception e ){
                    e.printStackTrace();
                    interrupt2020.tStop = true;
                }
            }
            info( "go into notification" );
            notification();
            info( "run finished");
        }
        public void notification(){
            info( "notification started" );
            synchronized( notification ){
                notification.notifyAll();
            }
            info( "notification finished" );

        }
    }

    class locSecurityManager extends SecurityManager{
/* This Security Manager blocks any access to the thtread wint given name */
        private String prohibitedThreadName;
        public locSecurityManager( String prohibitedThreadName){
            super();
            this.prohibitedThreadName = prohibitedThreadName;
        }

        public void checkAccess(Thread t){
            if( t.getName().equals(prohibitedThreadName))
                throw new SecurityException( "Acces denied to thread "+ prohibitedThreadName +
                        " by SecurityManager " + getClass().getName() );
            else
                super.checkAccess( t );
        }
        public void checkPermission( Permission perm ){
            info(" started checkPermission( " + perm +" ) from Security Manager " + getClass().getName());
            //super.checkPermission(perm );
            if( perm.equals( new RuntimePermission("createSecurityManager") )){
                log.info("checking RuntimePermission( createSecurityManager ) ): PROHIBITED");
                throw new SecurityException();
            } else if( perm.equals( new RuntimePermission("setSecurityManager") ) ) {
                log.info("checking RuntimePermission( " + perm.getName()+ " ): PERMITTED");
            } else  {
                //super.checkPermission( perm );
                return;
            }
        }
    }

    void waiter(){
        info( "waiter started" );
        synchronized( notification ){
            try {
                notification.wait();
            } catch ( InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        info("waiter finished");
    }

    public static void main(String args[]) {
        System.exit(new interrupt2020().test());
    }

    public int test() {
        info( "New generation "+getClass().getName() );
        SecurityManager ts = new locSecurityManager( TESTED_THREAD_NAME );
        SecurityManager sSM = System.getSecurityManager();
        T t1;
        t1 = new T( TESTED_THREAD_NAME );
        t1.start();
        System.setSecurityManager( ts );
        try{
            info( "try ot interrupt tested thread");
            t1.interrupt();
            info( "tested thread interrupted");
            tStop = true;
            waiter();
            System.setSecurityManager( sSM );
            return fail( "Expected SecurityException has NOT been thrown" );
        }catch( SecurityException se ){
            tStop = true;
            waiter();
            System.setSecurityManager( sSM );
            return pass( "Expected SecurityException has been thrown" );
        }/*catch( Throwable th){
            tStop = true;
            waiter();
            System.setSecurityManager( sSM );
            return fail( "Unexpected Throwable" +th);
        }*/
    }


    void info(String text){
        log.info( text );
//        System.out.println(text); System.out.flush();
    }
}
