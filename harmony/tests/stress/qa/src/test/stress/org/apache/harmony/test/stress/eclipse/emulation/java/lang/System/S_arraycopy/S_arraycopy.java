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

/**
 * @author Aleksei Y. Semenov
 * @version $Revision: 1.3 $
 */

package org.apache.harmony.test.stress.eclipse.emulation.java.lang.System.S_arraycopy;

import java.util.Random;

import org.apache.harmony.share.Test;

public class S_arraycopy extends Test {

    public Random random;
    
	public int THREAD_COUNT = 20;
    public int ITERATIONS_COUNT = 50000;
    public int MAX_ARRAY_SIZE = 10000;
    
    public S_arraycopy() {
        super();
        random = new Random();
    }

    public Object getRandomArray(int length, boolean primitive, boolean filled) {
     
        if (primitive){
            // create int[]
            int result[] = new int[length];
            if (filled) {
                for (int i=0; i<result.length; i++){
                    result[i] = random.nextInt();
                }
            }
            return result;
        } else {
            //create Object[]
            Object result[] = new Object[length];
            if (filled) {
                for (int i=0; i<result.length; i++){
                    result[i] = new Long(random.nextInt());
                }
            }            
            return result;
        }                
    }
    
    public int test1T(int threadNumber) {	
        int ioobe = 0, ase = 0;
		
        for (int i=0; i<ITERATIONS_COUNT; i++) {
			
            int srcLength = Math.abs(random.nextInt(MAX_ARRAY_SIZE));
            boolean primitive1 = random.nextBoolean();
            
            Object arg0 = getRandomArray(srcLength, primitive1, true);
            int arg1 = random.nextInt(MAX_ARRAY_SIZE);
            int dstLength = Math.abs(random.nextInt(MAX_ARRAY_SIZE));
            
            boolean primitive2 = random.nextBoolean();            
            Object arg2 = getRandomArray(dstLength, primitive2, random.nextBoolean());
            int arg3 = random.nextInt(MAX_ARRAY_SIZE);
            int arg4 = random.nextInt(MAX_ARRAY_SIZE);
            try {
                System.arraycopy(arg0, arg1, arg2, arg3, arg4);
            } catch (IndexOutOfBoundsException e1){
                ioobe++;
                if (!(arg1 < 0 || arg1 >= srcLength || arg1 + arg4 >= srcLength)
						&& !(arg3 < 0 || arg3 >= dstLength || arg3 + arg4 >= dstLength)) {
					log.add("Indexes are in allowed ranges:");
					log.add("srcLentgh: " + srcLength);
					log.add("arg1: " + arg1);
					log.add("dstLentgh: " + dstLength);
					log.add("arg3: " + arg3);
					log.add("arg4: " + arg4);
                    return fail("Test failed: unexpected IndexOutOfBoundsException"
							+ " (thread + " + threadNumber + ")");
                }                                
            } catch (ArrayStoreException e2){
                ase++;
                if (primitive1==primitive2) {
                    return fail("Test failed: both arrays are either primitive or not"
							+ " (thread + " + threadNumber + ")");
                }
            }
        }
		if (threadNumber != -1) log.info("Thread " + threadNumber + ":");
		log.info("IndexOutOfBoundsException count: " + ioobe);
		log.info("ArrayStoreException count: " + ase);
		if (threadNumber != -1) return 104;
		else return pass();
    }
    
    public int testST() {
        TestThread t[] = new TestThread[THREAD_COUNT];
        boolean passed = true;
        
        for (int i=0; i<THREAD_COUNT; i++){
            t[i] = new TestThread(this, i);
            t[i].start();
        }
        for (int i=0; i<THREAD_COUNT; i++){
            try {
                t[i].join();
            } catch (InterruptedException e){
				return fail("Test failed: InterruptedException");
            }
            passed = passed && t[i].status==104;            
        }
        if (passed){
            return pass();
        } else {
            return fail("Test failed: unexpected exception occurred in some thread");
        }        
    }
    
	public int test() {
		try {
			THREAD_COUNT = Integer.decode(testArgs[0]).intValue();
			ITERATIONS_COUNT = Integer.decode(testArgs[1]).intValue();
			MAX_ARRAY_SIZE = Integer.decode(testArgs[2]).intValue();
		} catch(Throwable e) {
			log.info("Parameters for test is invalid - use default");
		}
		if (THREAD_COUNT !=0 ) log.info("THREAD_COUNT = " + THREAD_COUNT);
		log.info("ITERATIONS_COUNT = " + ITERATIONS_COUNT);
		log.info("MAX_ARRAY_SIZE = " + MAX_ARRAY_SIZE);
		log.info("Test start...");
		
		if (THREAD_COUNT == 0) return test1T(-1);
		else return testST();
	}
	
    public static void main(String[] args) {
        System.exit(new S_arraycopy().test(args));
    }
    
    class TestThread extends Thread {
        int status = -1;
        S_arraycopy ac;
		int currentThread;
        
        public TestThread(S_arraycopy ac, int i) {
            super();
            this.ac = ac;
			currentThread = i;
        }
        
        public void run() {        
            status = ac.test1T(currentThread);
        }
    }
	
	
}
