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

package org.apache.harmony.test.stress.eclipse.emulation.java.lang.Class.S_newInstance;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.harmony.test.stress.eclipse.emulation.java.lang.share.UsefulMethods;

import org.apache.harmony.share.Test;

public class S_newInstance extends Test {

	public int test() {
		String auxilaryDir = testArgs[0];
		String eclipsePath = testArgs[1];
		int count = 0;
		URLClassLoader urlClassLoader;
		URL[] eclipsePlugins = UsefulMethods.get_plugin_path(eclipsePath);
		if (eclipsePlugins != null ) {
			urlClassLoader = new URLClassLoader(eclipsePlugins);
		} else {
			return fail("Test failed: Unable to get eclipse plugins list");
		}
		File auxilaryFile = new File(auxilaryDir + File.separator
										+ "newInstance_parameters.txt");
		int illegalAccessException = 0;
		int nullPointerException = 0;
		int exceptionInInitializerError = 0;
		int noClassDefFoundError= 0;
		int instantiationException = 0;
		Class[] classes = UsefulMethods.getClassesArray(auxilaryFile, urlClassLoader);
		if (classes == null) return fail("Test failed: unable to get class list");
		Object[] obj = new Object[classes.length];
		log.info("Number of classes to test: " + classes.length);
		String info = "";
		try {
			for (int i=0; i<classes.length; i++) { 
				try {
					info = classes[i].getName();
					obj[i] = classes[i].newInstance();
					count++;
				} catch (IllegalAccessException e) {
					illegalAccessException++;
				} catch (InstantiationException e) {
					instantiationException++;
				} catch (ExceptionInInitializerError e) {
					exceptionInInitializerError++;
				} catch (NullPointerException e) {
					nullPointerException++;
				} catch (NoClassDefFoundError e) {
					noClassDefFoundError++;
				}
			}
			log.info("New instances count: " + count);
			log.info("IllegalAccessException count - " + illegalAccessException);
			log.info("InstantiationException count - " + instantiationException);
			log.info("Exceptions caused by calling " +
						"non-existing classes of Eclipse plugins:");
			log.info("\tExceptionInInitializerError count - " + exceptionInInitializerError);
			log.info("\tNoClassDefFoundError count - " + noClassDefFoundError);
			log.info("\tNullPointerException count - " + nullPointerException);
			
			return pass();
		} catch (Throwable e) {
			log.add(e);
			log.info("Error on class: " + info);
			log.info("New instances count: " + count);
			return fail("Test failed: " + e.toString());
		}
	}
	
	public static void main(String[] args) {
		System.exit(new S_newInstance().test(args));
	}

}
