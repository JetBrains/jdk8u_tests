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

package org.apache.harmony.test.stress.eclipse.emulation.java.lang.ClassLoader.S_loadClass.S_loadClassST;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Random;

import org.apache.harmony.test.stress.eclipse.emulation.java.lang.share.UsefulMethods;

import org.apache.harmony.share.Test;

public class S_loadClassST extends Test {

	public int test() {
		String auxilaryDir = testArgs[0];
		String eclipsePath = testArgs[1];
		int threadsCount = Integer.decode(testArgs[2]).intValue();
		boolean difPriority = false;
		if (testArgs[3].equals("true")) difPriority = true;
		Random random = new Random();
		int[] priorities = new int [threadsCount];
		String ResourceFileName;
		if (System.getProperty("os.name").
				toLowerCase().indexOf("windows") >= 0) {
			ResourceFileName = "loadClass_param_win.txt";
		} else { 
			ResourceFileName = "loadClass_param_lnx.txt";
		}
		File auxilaryFile = new File(auxilaryDir + File.separator
					+ ResourceFileName);
		FileInputStream loadClassParam;
		LineNumberReader reader;
		try {
			reader = new LineNumberReader(new InputStreamReader(
					new FileInputStream(auxilaryFile)));
		} catch (FileNotFoundException fileNotFoundException) {
			log.add(fileNotFoundException);
			return fail("Test failed: Resource file " 
					+ auxilaryDir+File.separator + ResourceFileName
					+ " was not found");
		}
		URL[] eclipsePlugins = UsefulMethods.get_plugin_path(eclipsePath);
		if (eclipsePlugins != null ) {
			log.add("Threads count = " + threadsCount);
			log.add("Different priorities = " + difPriority);
			URLClassLoader urlClassLoader = new URLClassLoader(eclipsePlugins);
			TestThread[] threads = new TestThread[threadsCount];
			int priority;
			for (int k = 0; k < threadsCount; k++) {
				if (difPriority) {
					priority = random.nextInt(Thread.MAX_PRIORITY);
					if (priority  == 0) priority = 5;
					threads[k] = new TestThread(k, urlClassLoader, reader, 
							                    priority);
					priorities[k] = priority;
				} else {
					threads[k] = new TestThread(k, urlClassLoader, reader);
				}
			}
			for (int i = 0; i < threadsCount; i++) {
				threads[i].start();
			}
			if (wait_all_threads(threads)) {
				int loadedClassCount = 0;
				int loadBySystemClassLoader = 0;
				int loadByUrlClassLoader = 0;
				int errorCount = 0;
				for (int i = 0; i < threadsCount; i++) {
					loadedClassCount+=threads[i].loadedClassCount;
					loadBySystemClassLoader+=threads[i].loadBySystemClassLoader;
					loadByUrlClassLoader+=threads[i].loadByUrlClassLoader;
					errorCount+=threads[i].errorCount;
				}
				log.info("Loaded class count: " + loadedClassCount);
				log.info("By systemClassLoader - " + loadBySystemClassLoader);
				log.info("By urlClassLoader - " + loadByUrlClassLoader);
				for (int i = 0; i < threadsCount; i++) {
					if (difPriority) {
					log.info("\t Thread " + i + "[priority=" + priorities[i] 
				        + "]" + " loaded " + threads[i].loadedClassCount 
				        + " classes");
					} else {
						log.info("\t Thread " + i + " loaded "
								+ threads[i].loadedClassCount + " classes");
					}
				}
				if (errorCount == 0) {
					return pass();
				} else {
					return fail("Test failed: Error occurred in some thread\n" +
							"Error count " +  errorCount); 
				}
			} else {
				return fail("Test failed: Some exception occurred in " +
						"wait_all_threads function");
			}
		} else {
			return fail("Test failed: Unable to get eclipse plugins list");
		}
	}
	
	private boolean wait_all_threads(Thread[] threads) {
	    for (int i = 0; i < threads.length; i++) {
	            try {
					threads[i].join();
	            } catch(Throwable e) {
	                log.add(e);
	                return false;
	            }
	    }
	    return true;
	}
	
	public static void main(String[] args) {
		System.exit(new S_loadClassST().test(args));
	}


	class TestThread extends Thread {
		
		final ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
		int currentThread;
		URLClassLoader classLoader;
		LineNumberReader reader;
		String className = "";
		int loadedClassCount = 0;
		int loadBySystemClassLoader = 0;
		int loadByUrlClassLoader = 0;
		int errorCount = 0;
		
		public TestThread(int i, URLClassLoader loader, LineNumberReader lnr) {
			super();
       	 	currentThread = i;
			classLoader = loader;
			reader = lnr;
		}
		
		TestThread(int i, URLClassLoader loader, LineNumberReader lnr,
				   int priority) {
		    this(i, loader, lnr);
		    setPriority(priority);
		}
		
		public void run() {
			try {
				while (className != null) {
					synchronized(reader) {
						className = reader.readLine();
					}
					try {
						if (className != null) {
							systemClassLoader.loadClass(className);
							loadBySystemClassLoader++;
							loadedClassCount++;
						}
					} catch (ClassNotFoundException classNotFoundException) {
						classLoader.loadClass(className);
						loadByUrlClassLoader++;
						loadedClassCount++;	
					}
				}
			} catch (Throwable e) {
				log.add(e);
				log.add("Fail to load class: " + className);
				log.info("Unexpected " + e.toString() + " in thread "
						+ currentThread);
				errorCount++;
			}
		}
	}

}
