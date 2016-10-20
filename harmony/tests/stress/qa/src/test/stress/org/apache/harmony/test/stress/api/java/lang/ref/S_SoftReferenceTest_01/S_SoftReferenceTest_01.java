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
 * This tests SoftReference object.
 * 1.Create Objects in a loop. Put into SoftReference by invoking ctor with 1 argument.
 * 2.Invoked gc().
 * 3. Check if at least one instance of SoftReference get() method returns null.
 * 
 * scenario
 */
package org.apache.harmony.test.stress.api.java.lang.ref.S_SoftReferenceTest_01;

import org.apache.harmony.share.Test;
import java.lang.ref.*;

/**
 * This tests SoftReference object.
 * 1.Create Objects in a loop. Put into SoftReference by invoking ctor with 1 argument.
 * 2.Invoked gc().
 * 3. Check if at least one instance of SoftReference get() method returns null.
 */
public class S_SoftReferenceTest_01 extends Test {

    public static void main(String[] args) {
        System.exit(new S_SoftReferenceTest_01().test(args));
    }

    private static final int NUM = 1000;

    public int test() {
        
         SoftReference [] sr = new SoftReference[NUM];
         for(int i=0; i<NUM;++i) {
            Object obj = (new StringBuffer(1000000)).append("The string buffer " + (i+1));
            sr[i] = new SoftReference(obj);
            obj = null;
            Thread.yield();
         }

         System.gc();
         try {
         	Thread.sleep(20);
         }
         catch(InterruptedException e) {
         	 return fail("interrupted excetipion thrown");
         }

         for(int j=0; j< NUM; ++j) {
         	if(sr[j].get()==null) {
	            return pass();
	        }
         }
     

        return fail("Soft reference was not cleared");
    }
}

