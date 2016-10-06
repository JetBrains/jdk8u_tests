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

package org.apache.harmony.test.func.api.java.lang.ThreadGroup.suspend.suspend20.suspend2010;

import org.apache.harmony.share.Test;

import java.security.Permission;

public class suspend2010  extends Test {
    private static final String TESTED_THREAD_NAME           = "SecurityManagerTestThread";
    private static final String TESTED_THREADGROUP_NAME      = "SecurityManagerTestThreadGroup";
    private static final String TESTED_THREADSUBGROUP_NAME   = "SecurityManagerTestThreadSubGroup";

    private static final String TESTED_THREADGROUP_CAUSE     = "SecurityManagerTestThreadGroupCause";

    private volatile static boolean tStop = false;
    private boolean tgCause = false;

    class T extends Thread {
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
                    suspend2010.tStop = true;
                }
            }
        }
    }

    class locSecurityManager extends SecurityManager{
/* This Security Manager blocks any access to the ThreadGroup wint given name
   and any access to the any threads */
        private String prohibitedThreadGroupName;
        public locSecurityManager( String prohibitedThreadGroupName){
            super();
            this.prohibitedThreadGroupName = prohibitedThreadGroupName;
        }

        public void checkAccess(Thread t){
            throw new SecurityException( "Acces denied to thread "+ t.getName() +
                     " by SecurityManager " + getClass().getName() );
        }

        public void checkAccess(ThreadGroup tg){
            if( tg.getName().equals( prohibitedThreadGroupName)) {
                tgCause = true;
                throw new SecurityException( "Acces denied to ThreadGroup "+ tg.getName() +
                        " by SecurityManager " + getClass().getName());
            }else
                super.checkAccess( tg );
        }
        public void checkPermission( Permission perm ){
            //log.info(" started checkPermission( " + perm +" )from Security Manager " + getClass().getName());
            if( perm.equals( new RuntimePermission("setSecurityManager") ) ) {
                log.info("Security Manager " + getClass().getName()+ "\n\tchecking RuntimePermission( " + perm.getName()+ " ): PERMITTED");
            } else {
                //super.checkPermission( perm );
              return;
            }
        }
    }

    public static void main(String args[]) {
        System.exit(new suspend2010().test());
    }

    public int test() {
        log.info( getClass().getName() );
        SecurityManager ts = new locSecurityManager( TESTED_THREADGROUP_NAME );
        SecurityManager sSM = System.getSecurityManager();
//--------- Create Thread Group and its subgroupe
        ThreadGroup rootTG = new ThreadGroup(TESTED_THREADGROUP_NAME);
        ThreadGroup subTG1  = new ThreadGroup(rootTG, TESTED_THREADSUBGROUP_NAME + "_1");
        ThreadGroup subTG2  = new ThreadGroup(rootTG, TESTED_THREADSUBGROUP_NAME + "_2");
// Create and start threads
        T t1 = new T( rootTG,  TESTED_THREAD_NAME + "_1" );
        T t2 = new T( subTG1,  TESTED_THREAD_NAME + "_2" );
        T t3 = new T( subTG1,  TESTED_THREAD_NAME + "_3" );
        T t4 = new T( subTG2,  TESTED_THREAD_NAME + "_4" );

        t1.start();
        t2.start();
        t3.start();
        t4.start();

// ----------------
        System.setSecurityManager( ts );
        try{
            rootTG.suspend();
            tStop = true;
            System.setSecurityManager( sSM );
            return fail( "Expected SecurityException has NOT been thrown" );
        }catch( SecurityException se ){
            tStop = true;
            System.setSecurityManager( sSM );
            if(  tgCause )
                return pass( "Expected SecurityException has been thrown: \t" + se );
            else
                return fail( "Unxpected SecurityException has been thrown: \t" + se );
        }
    }
}
