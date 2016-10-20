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


package org.apache.harmony.test.stress.eclipse.emulation.java.lang.ClassLoader.S_definePackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.URL;
import java.util.Vector;

import org.apache.harmony.test.stress.eclipse.emulation.java.lang.ClassLoader.share.TestClassLoader;

import org.apache.harmony.share.Test;

public class S_definePackage extends Test {

	static int THREAD_COUNT = 20;
	
	public static void main(String[] args) {
		System.exit(new S_definePackage().test(args));
	}

	public int test() {
		String auxilaryDir = testArgs[0];
		String eclipsePath = testArgs[1];
		try {
			THREAD_COUNT = Integer.decode(testArgs[2]).intValue();
		} catch(Throwable e) {
			log.info("THREAD_COUNT parameter for test is invalid - use default");
		}
		log.info("Eclipse path: " + eclipsePath);
		File eclipsePluginDir = new File(eclipsePath+"/plugins");
		if (eclipsePluginDir.exists()) {
			File auxilaryFile = new File(auxilaryDir + File.separator
					+ "definePackage_parameters.txt");
			if (THREAD_COUNT !=0 ) log.info("THREAD_COUNT = " + THREAD_COUNT);
			String[] pack = getPackages(auxilaryFile);
			if (pack == null) return fail("Test failed: unable to get package list");
			log.info("Package to define count: " + pack.length);
			log.info("Test start...");
			if (THREAD_COUNT == 0) return test1T(0, pack, eclipsePluginDir);
			else return testST(pack, eclipsePluginDir);
		} else {
			return fail("Test failed: Eclipse plugin path is incorrect: "
					+ eclipsePath + "/plugins");
		}
	}
	
	int test1T(int threadNumber, String[] parameters, File pluginDir) {
		int count = 0;
		String packageName = null;
		try {
			URL eclipsePluginURL = pluginDir.toURL();
			if (parameters == null) fail("Unable to get Packages list");
			Package[] newPackage = new Package[parameters.length];
			String[] paramArr;
			URL url;				
			for (int i=0; i<parameters.length; i++) {
				paramArr = parameters[i].split(" ");
				packageName = paramArr[0]; 
				if (paramArr[7].equals("null")) {
					url = null;
				} else {
					url = new URL(eclipsePluginURL.toString().concat(
							paramArr[7]));
				}
				TestClassLoader testClassLoader = new TestClassLoader();
				newPackage[i] = testClassLoader.test_definePackage(paramArr[0],
						paramArr[1], paramArr[2], paramArr[3],
						paramArr[4], paramArr[5], paramArr[6], url);
				testClassLoader.test_getPackage(newPackage[i].getName());
				count++;
			}
			if (THREAD_COUNT != 0) log.info("Thread " + threadNumber + ":");
			log.info("Defined packages count: " + count);
			if (THREAD_COUNT  != 0) return 104;
			else return pass();
		} catch (Throwable e) {
			log.add(e);
			log.add("Fail to define package: " + packageName + " on thread "
					+ threadNumber);
			return fail("Test failed: " + e.toString());
		}
	}

	int testST(String[] packages, File pluginDir) {
		TestThread threads[] = new TestThread[THREAD_COUNT];
		boolean passed = true;
			
	    for (int i=0; i<THREAD_COUNT; i++){
			threads[i] = new TestThread(this, i, packages, pluginDir);
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
		
	private String[] getPackages(File file) {
		try {
			FileInputStream defineClassParam = new FileInputStream(file);
			LineNumberReader reader = new LineNumberReader(
				new InputStreamReader(defineClassParam));
			String str;
			Vector paramValues = new Vector();
			while( (str = reader.readLine()) != null ) {
				paramValues.add(str);
			}
			return ((String[]) paramValues.toArray(new String[paramValues.size()]));
		} catch (FileNotFoundException fileNotFoundException) {	
			log.info("Resource file " + file + " wasn't found");
			return null;
		} catch (Throwable e) {
			log.add(e);
			return null;
		}	
	}
	
	class TestThread extends Thread {
        int status = -1;
		int currentThread;
		String[] packages;
		S_definePackage instance;
		File pluginDir;
        
        public TestThread(S_definePackage instance, int i, String[] arg,
				File dir) {
            super();
			currentThread = i;
			packages = arg;
			this.instance = instance;
			pluginDir = dir;
        }
		
        public void run() {        
			status = instance.test1T(currentThread, packages, pluginDir);
        }
    }
	
}
