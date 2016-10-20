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
 * @version $Revision: 1.4 $
 */

package org.apache.harmony.test.stress.eclipse.emulation.java.lang.ClassLoader.S_getResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.UnknownServiceException;
import java.util.Vector;

import org.apache.harmony.test.stress.eclipse.emulation.java.lang.share.UsefulMethods;

import org.apache.harmony.share.Test;

public class S_getResource extends Test {

	static int THREAD_COUNT = 20;
	
	public static void main(String[] args) {
		System.exit(new S_getResource().test(args));
	}
	
	public int test() {
		String auxilaryDir = testArgs[0];
		String eclipsePath = testArgs[1];
		try {
			THREAD_COUNT = Integer.decode(testArgs[2]).intValue();
		} catch(Throwable e) {
			log.info("THREAD_COUNT parameter for test is invalid - use default");
		}
		URL[] eclipsePlugins = UsefulMethods.get_plugin_path(eclipsePath);
		if (eclipsePlugins != null ) {
			URLClassLoader urlClassLoader = new URLClassLoader(eclipsePlugins);
			File auxilaryFile = new File(auxilaryDir + File.separator
					+ "getResource_parameters.txt");
			if (THREAD_COUNT !=0 ) log.info("THREAD_COUNT = " + THREAD_COUNT);
			String[] res = getResources(auxilaryFile);
			if (res == null) return fail("Test failed: unable to get resources list");
			log.info("Resources to get count: " + res.length);
			log.info("Test start...");
			if (THREAD_COUNT == 0) return test1T(0, res, urlClassLoader);
			else return testST(res, urlClassLoader);
		} else {
			return fail("Test failed: Unable to get eclipse plugins list");
		}
	}
	
	int test1T(int threadNumber, String[] resources, URLClassLoader loader) {
		int count = 0;
		int nullResource = 0;
		String auxilaryDir = testArgs[0];
		String eclipsePath = testArgs[1];
		File auxilaryFile = new File(auxilaryDir + File.separator
				+ "getResource_parameters.txt");
		String resourceName = "";
		try {
			resources = getResources(auxilaryFile);
			int length = resources.length;
			URL[] url = new URL[length];
			Object[] content = new Object[length];
			int unknownService = 0;
			for (int i=0; i<length; i++) {
				resourceName = resources[i];
				url[i] = loader.getResource(resources[i]);
				if (url[i] != null) {
					count++;
					try {
						content[i] = url[i].getContent(); 
					} catch (UnknownServiceException unknownServiceException) {
						unknownService++;
					}
				} else { 
					nullResource++; 
				}
			}
			if (THREAD_COUNT != 0) log.info("Thread " + threadNumber + ":");
			log.info("Founded resources count: " + count);
			log.info("Not Founded resources count: " + nullResource);
			log.info("UnknownServiceException count: " + unknownService);
			if (THREAD_COUNT  != 0) return 104;
			else return pass();
		} catch (Throwable e) {
			log.add(e);
			log.add("Fail to get following resource: " + resourceName
				+ " on thread " + threadNumber);
			return fail("Test failed: " + e.toString());
		}
	}
	
	int testST(String[] packages,  URLClassLoader loader) {
		TestThread threads[] = new TestThread[THREAD_COUNT];
		boolean passed = true;
			
	    for (int i=0; i<THREAD_COUNT; i++){
			threads[i] = new TestThread(this, i, packages, loader);
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
	
	private String[] getResources(File file) {
		try {
			FileInputStream defineClassParam = new FileInputStream(file);
			LineNumberReader reader = new LineNumberReader(
				new InputStreamReader(defineClassParam));
			String resourceName;
			Vector paramValues = new Vector();
			while( (resourceName = reader.readLine()) != null ) {
				paramValues.add(resourceName);
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
		S_getResource instance;
		URLClassLoader loader;
        
        public TestThread(S_getResource instance, int i, String[] arg,
				 URLClassLoader loader) {
            super();
			currentThread = i;
			packages = arg;
			this.instance = instance;
			this.loader = loader;
        }
		
        public void run() {        
			status = instance.test1T(currentThread, packages, loader);
        }
    }

}


