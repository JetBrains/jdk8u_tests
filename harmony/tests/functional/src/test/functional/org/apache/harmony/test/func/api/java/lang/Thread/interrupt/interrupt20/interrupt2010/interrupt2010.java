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

package org.apache.harmony.test.func.api.java.lang.Thread.interrupt.interrupt20.interrupt2010;

import org.apache.harmony.share.Test;

import java.security.Permission;

public class interrupt2010  extends Test {
    public static final String TESTED_THREAD_NAME = "SecurityManagerTestThread";

    volatile static boolean tStop = false;

    class T extends Thread {
        public T( String str){
            super( str );
        }
        public void run () {
            log.info( "go run" );
            while ( !tStop ) {
                try {
                    Thread.currentThread().sleep(2000);
                }catch( InterruptedException ie){
                    ie.printStackTrace();
                    interrupt2010.tStop = true;
                }catch( Exception e ){
                    e.printStackTrace();
                    interrupt2010.tStop = true;
                }
            }
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
            if( t.getName().equals( prohibitedThreadName ))
                throw new SecurityException( "Acces denied to thread "+ t.getName() +
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

    public static void main(String args[]) {
        System.exit(new interrupt2010().test());
    }

    public int test() {
        log.info( "New generation "+getClass().getName() );
        if( ! checkJavaVersionGreateOrEqual(1.5)  ){
            return fail("Test has not been started.");
        }
        SecurityManager ts = new locSecurityManager( TESTED_THREAD_NAME );
        SecurityManager sSM = System.getSecurityManager();
        T t1;
        t1 = new T( TESTED_THREAD_NAME );        
        try{
            t1.interrupt();
            tStop = true;
            return fail( "Expected NullPointerException has NOT been thrown" );
        }catch( NullPointerException npe ){
            tStop = true;
            return pass( "Expected NullPointerException has been thrown" );
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
