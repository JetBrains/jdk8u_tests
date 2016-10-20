/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */    

/*
 * @author Alexander V. Esin
 * @version $Revision: 1.3 $
 * Created on 16.08.2005
 * 
 * This tests if WeakReference is "weaker" than SoftReference.
 * 1.Create Objecs in a loop. Put into SoftReference and WeakReferense by invoking ctor with 1 argument.
 * 2.Invoked gc().
 * 3.Check if a number of WeakReferences with cleared referent is greater thatn SoftReferences.
 * 
 * scenario
 */
package org.apache.harmony.test.stress.api.java.lang.ref.S_SoftReferenceTest_02;

import org.apache.harmony.share.Test;
import java.lang.ref.*;

/**
 * This tests if WeakReference is "weaker" than SoftReference.
 * 1.Create Objecs in a loop. Put into SoftReference and WeakReferense by invoking ctor with 1 argument.
 * 2.Invoked gc().
 * 3.Check if a number of WeakReferences with cleared referent is greater thatn SoftReferences.
 */
public class S_SoftReferenceTest_02 extends Test {

    public static void main(String[] args) {
        System.exit(new S_SoftReferenceTest_02().test(args));
    }

    private static final int NUM = 1000;

    public int test() {
        
         SoftReference [] sr = new SoftReference[NUM];
         WeakReference [] wr = new WeakReference[NUM];
         for(int i=0; i<NUM;++i) {
            Object obj1 = (new StringBuffer(1000)).append("The string buffer for Soft reference" + (i+1));
            Object obj2 = (new StringBuffer(1000)).append("The string buffer for Weak reference" + (i+1));
            sr[i] = new SoftReference(obj1);
            wr[i] = new WeakReference(obj2);
            obj1 = null;
            obj2 = null;
            Thread.yield();
         }

         System.gc();
         try {
         	Thread.sleep(20);
         }
         catch(InterruptedException e) {
         	 return fail("interrupted excetipion thrown");
         }
         int sr_num = NUM;
         int wr_num = NUM;
         for(int j=0; j< NUM; ++j) {
         	if(sr[j].get()==null) {
	            sr_num--;
	        }
         	if(wr[j].get()==null) {
	            wr_num--;
	        }
         }
         log.info("Number of Weak references = " + wr_num);
         log.info("Number of Soft references = " + sr_num);
         if(wr_num > sr_num) {   
         	return fail("Number of Weak references stayed in memory is greater than Soft ones: it seems weak reference is not weaker than soft reference");
         }
         return pass();
    }
}

