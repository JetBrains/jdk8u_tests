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

package org.apache.harmony.test.func.api.java.lang.Thread.getStackTrace.getStackTrace20.getStackTrace2010;

import org.apache.harmony.share.Test;

import java.security.Permission;
import java.security.*;

public class getStackTrace2010  extends Test {
    public static final String TESTED_THREAD_NAME = "SecurityManagerTestThread";
    SecurityManager sSM = System.getSecurityManager();
    volatile static boolean tStop = false;

    class T extends Thread {
        public T( String str){
            super( str );
        }
        public void run () {
//            Permission perm = new RuntimePermission("Main","getStackTrace");
            while ( !tStop ) {
                try {
                    Thread.currentThread().sleep(2000);
                }catch( InterruptedException ie){

                }catch( Exception e ){
                    e.printStackTrace();
                    getStackTrace2010.tStop = true;
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
                throw new SecurityException( "Acces denied to thread \""+ t.getName() +
                        "\" by SecurityManager \"" + getClass().getName()+"\"" );
            else
                super.checkAccess( t );
        }

        public void checkPermission( Permission perm ){
            info(" started checkPermission( " + perm +" ) from Security Manager: " + getClass().getName() );
            if( perm.equals( new RuntimePermission("getStackTrace") )){
                log.info("checking RuntimePermission( " + perm.getName()+ " ): PROHIBITED");
                throw new SecurityException();
            } else if( perm.equals( new RuntimePermission("setSecurityManager") ) ) {
                log.info("checking RuntimePermission( " + perm.getName()+ " ): PERMITTED");
            } else {
                if( sSM != null )
                    sSM.checkPermission( perm );
                return;
            }
        }
    }

    public static void main(String args[]) {
        System.exit(new getStackTrace2010().test());
    }

    public int test() {
        log.info( getClass().getName() );
        if( ! checkJavaVersionGreateOrEqual(1.5)  ){
            return fail("Test has not been started.");
        }
        SecurityManager ts = new locSecurityManager( TESTED_THREAD_NAME );
        Permission perm = new RuntimePermission(/*TESTED_THREAD_NAME,*/"getStackTrace");
        T t1;
        t1 = new T( TESTED_THREAD_NAME );
        t1.start();
        System.setSecurityManager( ts );
        try{
            t1.getStackTrace();
            tStop = true;
            System.setSecurityManager( sSM );
            return fail( "Expected SecurityException has NOT been thrown" );
        }catch( SecurityException se ){
            tStop = true;
            System.setSecurityManager( sSM );
            return pass( "Expected SecurityException has been thrown: \t" + se );
        }catch( Exception ex){
            tStop = true;
            System.setSecurityManager( sSM );
            return fail( "Unexcepted Exception has been thrown:\t" + ex);
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
