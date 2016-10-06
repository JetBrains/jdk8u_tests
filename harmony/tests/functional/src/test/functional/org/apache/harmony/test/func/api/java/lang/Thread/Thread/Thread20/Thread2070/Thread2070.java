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

package org.apache.harmony.test.func.api.java.lang.Thread.Thread.Thread20.Thread2070;

import org.apache.harmony.share.Test;

import java.security.Permission;



public class Thread2070  extends Test {
    public static final String TESTED_THREAD_NAME = "SecurityManagerTestThread";

    public static final String TESTED_THREADGROUP_NAME = "SecurityManagerTestThreadGroup";

    volatile static boolean tStop = false;
/*  tested representation of Thread */
    class T extends Thread {
        public T(){
            super();
        }
        public T( String str){
            super( str );
        }
        public T(ThreadGroup group, String name){
            super( group,  name );
        }
        public void run () {
            while ( !tStop ) {
                try {
                    Thread.currentThread().sleep(2000);
                }catch( InterruptedException ie){

                }catch( Exception e ){
                    e.printStackTrace();
                    Thread2070.tStop = true;
                }
            }
        }
    }


/* This Security Manager blocks any access to the thtread wint given name  */
    class locSecurityManager extends SecurityManager{

        private String prohibitedThreadName;
        public locSecurityManager( String prohibitedThreadName){
            super();
            this.prohibitedThreadName = prohibitedThreadName;
        }

        public void checkAccess(Thread t){
            if( t.getName().equals(prohibitedThreadName))
                throw new SecurityException( "Acces denied to thread \""+ t.getName() +
                        "\" by SecurityManager \"" + getClass().getName()+"\"" );
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
        System.exit(new Thread2070().test());
    }

    public int test() {
        log.info( getClass().getName() );
        SecurityManager ts = new locSecurityManager( TESTED_THREAD_NAME );
        SecurityManager sSM = System.getSecurityManager();
        ThreadGroup tg = new ThreadGroup( TESTED_THREADGROUP_NAME );
        Thread t1;
        System.setSecurityManager( ts );
        try{
//test Thread(ThreadGroup group,Runnable target, String name)
            t1 = new T(tg, TESTED_THREAD_NAME );
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
