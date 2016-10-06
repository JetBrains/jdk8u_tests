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
 * Created on 06.09.2005
 * 
 * This tests if WeakHashMap.
 * 1.Create WeakHashMap.
 * 2.Fill it with key-value pairs, assigning key to null.
 * 3.Check if the put value is the same with which obtained by get method.
 * 4.Invoked gc().
 * 5.Check if not null values become less.
 * 
 */
package org.apache.harmony.test.func.api.java.util.WeakHashMap.F_WeakHashMapTest_01; 


import java.util.*;
import org.apache.harmony.test.func.share.ScenarioTest;
/**
 * This tests if WeakHashMap.
 * 1.Create WeakHashMap.
 * 2.Fill it with key-value pairs, assigning key to null.
 * 3.Check if the put value is the same with which obtained by get method.
 * 4.Invoked gc().
 * 5.Check if not null values become less. 
 */
public class F_WeakHashMapTest_01 extends ScenarioTest {
  private static int NUM = 1000;
  
  public static void main(String [] args) {
      System.exit(new F_WeakHashMapTest_01().test(args));
  }
  
  public int test() {
      WeakHashMap whm = new WeakHashMap();
     for(int i=0;i<NUM;++i) {
        String key = "key" + i; 
        String value = "value" + i;
        whm.put(key, value);
        key = null;
     }
     int GCed = 0;
     for(int j=0;j<NUM;++j) {
        String key = "key" + j; 
        
        String value = (String) whm.get(key);
        if(value == null) {
           GCed++;
           continue;
        }
        if(!("value" + j).equals(value)) {
           return fail("incorrect value " + value  + " for key key" + j);
        }
     }
     log.info("GCed = " + GCed);
     System.gc();
     try {
       Thread.sleep(200);
     }
     catch(InterruptedException e) { return fail("InterruptedException thrown"); }
     log.info("GCing!!!");
     int GCed2 = 0;

     for(int k=0;k<NUM;++k) {
        String key = "key" + k; 
        String value = (String) whm.get(key);
        if(value == null) {
           GCed2++;
           continue;
        }
        if(!("value" + k).equals(value)) {
           return fail("incorrect value " + value  + " for key key" + k);
        }

     }
     log.info("GCed2 = " + GCed2);
     if(GCed2 == 0)
           return fail("No values were erased");

     return pass();
  }
}