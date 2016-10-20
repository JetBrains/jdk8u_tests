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
 * This tests SoftReference.
 * 1.Create Objecs in a loop. Put into SoftReference by invoking ctor with 1 argument.
 * 2.Create byte array and allocate a lost of memory in order to throw OutOfMemoryError.
 * 3.Check if all SoftReferences' referents are cleared after exception thrown.
 * 
 * scenario
 */
package org.apache.harmony.test.stress.api.java.lang.ref.S_SoftReferenceTest_03;

import org.apache.harmony.share.Test;
import java.lang.ref.*;

/**
 * This tests SoftReference.
 * 1.Create Objecs in a loop. Put into SoftReference by invoking ctor with 1 argument.
 * 2.Create byte array and allocate a lost of memory in order to throw OutOfMemoryError.
 * 3.Check if all SoftReferences' referents are cleared after exception thrown.
 */
public class S_SoftReferenceTest_03 extends Test {

    public static void main(String[] args) {
        System.exit(new S_SoftReferenceTest_03().test(args));
    }

    private static final int NUM = 1000;

    public int test() {
    	SoftReference [] sr = new SoftReference[NUM];
    	try {
	    	
	        for(int i=0; i<NUM;++i) {
	           Object obj = new String("The string " + (i+1));
	           sr[i] = new SoftReference(obj);
	           obj = null;
	        }
	        
	        
	        byte [][] garbage = new byte [1000000][];
	        for(int g=0; g<1000000; ++g) {
	        	 garbage[g]= new byte[2 << g];
	        }
	        
    	}
    	catch(OutOfMemoryError oome) {
    		log.info(oome.toString());
    		for(int j=0; j< NUM; ++j) {
            	if(sr[j].get()!=null) {
    	            return fail("Soft reference was not cleared");
    	        }
            }
    	}
    	

       return pass();
    }
}

