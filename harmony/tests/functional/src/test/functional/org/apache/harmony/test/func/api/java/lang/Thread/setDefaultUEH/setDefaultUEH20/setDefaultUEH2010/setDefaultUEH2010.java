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
 *          Date: Dec 13, 2005
 *          Time: 12:33:48
 */

package org.apache.harmony.test.func.api.java.lang.Thread.setDefaultUEH.setDefaultUEH20.setDefaultUEH2010;

import org.apache.harmony.share.Test;
import java.security.Permission;

public class setDefaultUEH2010 extends Test {
    public static final String TESTED_THREAD_NAME = "SecurityManagerTestThread";

    volatile static boolean tStop = false;

    class T extends Thread {
        public T( String str){
            super( str );
        }

        public void run () {
            while ( !tStop ) {
                try {
                    Thread.currentThread().sleep(2000);
                }catch( InterruptedException ie){

                }catch( Exception e ){
                    e.printStackTrace();
                    setDefaultUEH2010.tStop = true;
                }
            }
        }
    }

    class ueHandler implements Thread.UncaughtExceptionHandler{
        public ueHandler(){};
        public void uncaughtException(Thread t, Throwable e){
            info( "uncaughtException");
            info("Thread: \t"+  t + " Throwabl: \t"+ e);

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
            info(" started checkPermission( " + perm +" )from Security Manager " + getClass().getName());
            if( perm.equals( new RuntimePermission("setSecurityManager") ) ) {
                log.info("checking RuntimePermission( " + perm.getName()+ " ): PERMITTED");
            } else if( perm.equals( new RuntimePermission("setDefaultUncaughtExceptionHandler") ) ) {
                log.info("checking RuntimePermission( " + perm.getName()+ " ): PROHIBITED");
                throw new SecurityException();
            } else {
                //super.checkPermission( perm );
              return;
            }
        }
    }

    public static void main(String args[]) {
        System.out.println( " public static void main(String args[]) " ); System.out.flush();
        System.exit(new setDefaultUEH2010().test());
    }

    public int test() {
        info( getClass().getName() );
        if( ! checkJavaVersionGreateOrEqual(1.5)  ){
            return fail("Test has not been started.");
        }
        SecurityManager ts = new locSecurityManager( TESTED_THREAD_NAME );
        SecurityManager sSM = System.getSecurityManager();
        info( "System Security Manager is: " + sSM );
        T t1;
        t1 = new T( TESTED_THREAD_NAME );

        t1.start();
        System.setSecurityManager( ts );

        info( "System Security Manager was changed to: " + System.getSecurityManager());

        try{
            Thread.setDefaultUncaughtExceptionHandler( new ueHandler() );
            tStop = true;
            System.setSecurityManager( sSM );
            info( "System Security Manager has been changed to: " + System.getSecurityManager());
            return fail( "Expected SecurityException has NOT been thrown" );
        }catch( SecurityException se ){
            tStop = true;
            System.setSecurityManager( sSM );
            info( "System Security Manager has been changed to: " + System.getSecurityManager());
            return pass( "Expected SecurityException has been thrown" );
        }
    }


    void info(String text){
        log.info( text );
//        System.out.println(text); System.out.flush();
    }

    public boolean checkJavaVersionGreateOrEqual( double checkedVer ){
        double ver = Float.parseFloat( System.getProperties().getProperty("java.version").substring(0,3) );
        if( ver >= checkedVer )
            return true;
        else {
            info("Java version:\t"+ System.getProperties().getProperty("java.version"));
            info("Test supports java version " +checkedVer+ " and higher");
            return false;
        }
    }

}
