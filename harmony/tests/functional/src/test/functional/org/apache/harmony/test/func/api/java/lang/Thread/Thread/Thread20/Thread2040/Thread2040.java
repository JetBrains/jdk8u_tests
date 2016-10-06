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
 *          Date: Dec 14, 2005
 *          Time: 13:33:48
 */

package org.apache.harmony.test.func.api.java.lang.Thread.Thread.Thread20.Thread2040;

import org.apache.harmony.share.Test;

import java.security.Permission;


public class Thread2040  extends Test {
//    public static final String TESTED_THREAD_NAME = "SecurityManagerTestThread";
    public static final String TESTED_RUNNABLE_NAME = "SecurityManagerTestRunnable";

    volatile static boolean tStop = false;
/*  tested representation of Thread */
    class T extends Thread {
        public T(){
            super();
        }
        public T( String str){
            super( str );
        }
        public T(Runnable target){
            super( target );
        }

    }

    class R implements Runnable{
        String name;
        public R( String str ){
            name = str;
        }
        public void run () {
            while ( !tStop ) {
                try {
                    Thread.currentThread().sleep(2000);
                    Thread.currentThread().yield();
                }catch( InterruptedException ie){

                }catch( Exception e ){
                    e.printStackTrace();
                    Thread2040.tStop = true;
                }
            }
        }

        public String getName(){
            return name;
        }
    }
/* This Security Manager blocks any access to any thtread   */
    class locSecurityManager extends SecurityManager{


        public locSecurityManager( ){
            super();

        }

        public void checkAccess(Thread t){
            throw new SecurityException( "Acces denied to thread \""+ t.getName() +
                    "\" by SecurityManager \"" + getClass().getName()+"\"" );
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
        System.exit(new Thread2040().test());
    }

    public int test() {
        log.info( getClass().getName() );
        SecurityManager ts = new locSecurityManager( );
        SecurityManager sSM = System.getSecurityManager();
        Thread t1;

        Runnable r1;
        r1 = new R( TESTED_RUNNABLE_NAME );
        System.setSecurityManager( ts );
        try{
//test Thread(Runnable target)
            t1 = new T( r1 );
            tStop = true;
            System.setSecurityManager( sSM );
            return fail( "Expected SecurityException has NOT been thrown" );
        }catch( SecurityException se ){
            tStop = true;
            System.setSecurityManager( sSM );
            return pass( "Expected SecurityException has been thrown: \t"+se );
        }
    }
    void info(String text){
        log.info( text );
//        System.out.println(text); System.out.flush();
    }
}
