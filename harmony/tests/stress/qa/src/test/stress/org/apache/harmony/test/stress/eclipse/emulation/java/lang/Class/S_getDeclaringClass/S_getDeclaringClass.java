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

package org.apache.harmony.test.stress.eclipse.emulation.java.lang.Class.S_getDeclaringClass;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.harmony.test.stress.eclipse.emulation.java.lang.share.UsefulMethods;

import org.apache.harmony.share.Test;

public class S_getDeclaringClass extends Test {

	public int test() {
		String auxilaryDir = testArgs[0];
		String eclipsePath = testArgs[1];
		int getDeclaringClass = 0;
		int nullCount = 0; 
		File auxilaryFile = new File(auxilaryDir + File.separator 
				+ "getDeclaringClass_parameters.txt");
		URL[] eclipsePlugins = UsefulMethods.get_plugin_path(eclipsePath);
		if (eclipsePlugins != null ) {
			URLClassLoader urlClassLoader = new URLClassLoader(eclipsePlugins);
			Class[] classes = UsefulMethods.getClassesArray(auxilaryFile, 
					                                        urlClassLoader);
			if (classes == null) return fail("Test failed: " +
					"unable to get class list");
			log.info("Number of classes to test: " + classes.length);
			String info = "";
			try {
				Class[] clazz = new Class[classes.length];
				for (int i=0; i<classes.length; i++) { 
					info = classes[i].getName();
					clazz[i] = classes[i].getDeclaringClass();
					getDeclaringClass++;
					if (clazz[i] == null) nullCount++;
				}
				log.info("getDeclaringClass() call count: "
						+ String.valueOf(getDeclaringClass));
				//log.info("Null value count:" + String.valueOf(nullCount));
				return pass();
			} catch (Throwable e) {
				log.add(e);
				log.add("Error on class: " + info);
				return fail("Test failed: " + e.toString());
			}
		} else {
			return fail("Test failed: Unable to get eclipse plugins list");
		}
	}
	
	public static void main(String[] args) {
		System.exit(new S_getDeclaringClass().test(args));
	}

}


