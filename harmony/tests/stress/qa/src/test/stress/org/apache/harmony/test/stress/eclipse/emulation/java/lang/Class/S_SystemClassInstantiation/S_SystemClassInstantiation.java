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
 * @author Elena V. Sayapina
 * @version $Revision: 1.3 $
 */

package org.apache.harmony.test.stress.eclipse.emulation.java.lang.Class.S_SystemClassInstantiation;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Random;
import java.util.Vector;

import org.apache.harmony.share.Test;

public class S_SystemClassInstantiation extends Test {

	int THREAD_COUNT = 16;
	Random random;
	
	public S_SystemClassInstantiation() {
        super();
        random = new Random();
    }
	
	public int test1T(int threadNumber, Class[] classes) {
		
		Class classThis = null;
		Class classObj = null;
		Object obj = null;
		int newInstanceCount = 0;
		try {
			int isInstanceCall = 0;
			int trueRes = 0;
			int illegalAccessException = 0;
			int instantiationException = 0;
			int exceptionInInitializerError = 0;
			for (int i=0; i<classes.length; i++) {
				classObj = classes[i];
				boolean insExc = false;
				boolean exception = false;
				try {
					obj = classObj.newInstance();
					newInstanceCount++;
				} catch (InstantiationException e) {
					insExc = true;
					instantiationException++;
				} catch (IllegalAccessException e) {
					illegalAccessException++;
					exception = true;
				} catch (ExceptionInInitializerError e) {
					exceptionInInitializerError++;
					exception = true;
				}
				if (!exception) {
					int lowBound = random.nextInt(classes.length);
					int upBound = random.nextInt(classes.length);
					if (lowBound > upBound) {
						int num = lowBound;
						lowBound = upBound;
						upBound = num;
					}	
					for (int k=lowBound; k<upBound; k++) {
						classThis = classes[k];
						if (insExc) {
							if (classThis.isInstance(classObj)) trueRes++;
						} else {
							if (classThis.isInstance(obj)) trueRes++;
						}
						isInstanceCall++;
					}
				}
			} 
			if (THREAD_COUNT != 0) log.info("Thread " + threadNumber + ":");
			log.info("New instances count: " + newInstanceCount);
			log.info("isInstance() method call count: " + isInstanceCall);
			log.info("True result count for isInstance() method: " + trueRes);
			log.info("\tIllegalAccessException count - " + illegalAccessException);
			log.info("\tInstantiationException count - " + instantiationException);
			log.info("\tExceptionInInitializerError count - " + exceptionInInitializerError);
			if (THREAD_COUNT  != 0) return 104;
			else return pass();
		} catch (Throwable e) {
			log.add(e);
			log.info("Error on class: " + classObj + " in thread " + threadNumber);
			log.info("New instances count: " + newInstanceCount);
			return fail("Test failed: " + e.toString());
		}
	}
	
	Class[] getClassesArray(File file) {
		String className = null;
		int classNotFoundException = 0;
		try {
			FileInputStream defineClassParam = new FileInputStream(file);
			LineNumberReader reader = new LineNumberReader(
									new InputStreamReader(defineClassParam));
			Vector classVec = new Vector();
			Class clazz;
			while( (className = reader.readLine()) != null ){
				className = className.replace('/', '.');
				try {
					clazz = Class.forName(className);
					classVec.add(clazz);
				} catch (ClassNotFoundException e) {
					classNotFoundException++;
				}
			}
			log.info("ClassNotFoundException count = " + classNotFoundException);
			return (Class[]) classVec.toArray(new Class[classVec.size()]);
		} catch (Throwable e) {	
			log.add(e);
			log.add("Call Class.forName(" + className + ") failed" );
			log.add("Unexpected " + e.toString());
			return null;
		} 
	}
	
	public int testST(Class[] classes) {
        TestThread threads[] = new TestThread[THREAD_COUNT];
        boolean passed = true;
		
        for (int i=0; i<THREAD_COUNT; i++){
			threads[i] = new TestThread(this, i, classes);
			threads[i].start();
        }
        for (int i=0; i<THREAD_COUNT; i++){
            try {
				threads[i].join();
            } catch (InterruptedException e){
				return fail("Test failed: InterruptedException");
            }
            passed = passed && threads[i].status==104;            
        }
        if (passed){
            return pass();
        } else {
            return fail("Test failed: unexpected exception occurred in some thread");
        }        
    }
	
	public int test() {
		try {
			THREAD_COUNT = Integer.decode(testArgs[1]).intValue();
		} catch(Throwable e) {
			log.info("THREAD_COUNT parameter for test is invalid - use default");
		}
		if (THREAD_COUNT !=0 ) log.info("THREAD_COUNT = " + THREAD_COUNT);
		String auxilaryDir = testArgs[0];
		File auxilaryFile = new File(auxilaryDir + File.separator + "classesList.txt");
		Class[] classes = getClassesArray(auxilaryFile);
		if (classes == null) return fail("Test failed: unable to get class list");
		log.info("Classes to instance count = " + classes.length);
		log.info("Test start...");
		if (THREAD_COUNT == 0) return test1T(0, classes);
		else return testST(classes);
	}
	
	public static void main(String[] args) {
		System.exit(new S_SystemClassInstantiation().test(args));
	}

	class TestThread extends Thread {
        int status = -1;
		S_SystemClassInstantiation instance;
		int currentThread;
		Class[] classes;
        
        public TestThread(S_SystemClassInstantiation instance, int i, Class[] classList) {
            super();
            this.instance = instance;
			currentThread = i;
			classes = classList;
        }
		
        public void run() {        
			status = instance.test1T(currentThread, classes);
        }
    }
	
}
