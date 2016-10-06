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

import org.apache.harmony.share.Test;

public class ThreadTest extends Test { 



    static public void main(String args[]) { 
        System.exit(new ThreadTest().test());
    }
    
    public int test() {

     Thread t1, t2;                      
  
     final Object obj = new Object();    
    
     new Thread("thread_1") {
            public void run() {
                synchronized(this) {
                    try {
                        obj.wait(5000);
                    } catch(Throwable tex) { }
                }
            }
        }.start();

     new Thread("thread_2") {
            public void run() {
                synchronized(this) {
                    try {
                        obj.wait(5000);
                    } catch(Throwable tex) { 
                    }
                }
            }
        }.start(); 

       return pass();
    }
}