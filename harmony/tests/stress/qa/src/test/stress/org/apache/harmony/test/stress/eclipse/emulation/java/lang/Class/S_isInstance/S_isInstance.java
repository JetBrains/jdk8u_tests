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

package org.apache.harmony.test.stress.eclipse.emulation.java.lang.Class.S_isInstance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Vector;

import org.apache.harmony.test.stress.eclipse.emulation.java.lang.share.UsefulMethods;

import org.apache.harmony.share.Test;

public class S_isInstance extends Test {

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
										+ "isInstance_parameters.txt");
		Class classThis = null;
		Class classObj = null;
		int illegalAccessException = 0;
		int instantiationException = 0;
		int exceptionInInitializerError = 0;
		int nullPointerException = 0;
		int trueRes = 0;
		try {
			Class[][] classes = getClassesArray(auxilaryFile, urlClassLoader);
			if (classes == null) return fail("Unable to get classes list");
			Object[] obj = new Object[classes.length];
			for(int i=0; i<classes.length; i++) {
				try {
					classThis = classes[i][0];
					classObj =  classes[i][1];
					obj[i] = classThis.newInstance();
					if(classObj.isInstance(obj[i])) trueRes++;
					count++;
				} catch (IllegalAccessException e) {
					illegalAccessException++;
				} catch (InstantiationException e) {
					instantiationException++;
					if(classObj.isInstance(classThis)) trueRes++;
					count++;
				} catch (ExceptionInInitializerError e) {
					exceptionInInitializerError++;
				} catch (NullPointerException e) {
					nullPointerException++;
				}
				
			}
			log.info("isInstance(obj) call count: " + count);
			log.info("True result count: " + trueRes);
			log.info("IllegalAccessException count - " + illegalAccessException);
			log.info("InstantiationException count - " + instantiationException);
			log.info("Exceptions caused by calling " +
					"non-existing classes of Eclipse plugins:");
			log.info("\tExceptionInInitializerError - " + exceptionInInitializerError);
			log.info("\tNullPointerException count - " + nullPointerException);
			return pass();
		} catch (Throwable e) {
			log.add(e);
			log.info("New instances count: " + count);
			return fail("Test failed: " + e.toString() + " on " + classThis.toString());
		}
	}
	
	Class[][] getClassesArray(File file, ClassLoader loader) {
		String[] className = null;
		String str = null;
		try {
			FileInputStream defineClassParam = new FileInputStream(file);
			LineNumberReader reader = new LineNumberReader(
									new InputStreamReader(defineClassParam));
			Vector classVec = new Vector(31850);
			while( (str = reader.readLine()) != null ){
				className = str.split(" ");
				try {
						Class clazz1 = loader.loadClass(className[0]);
						Class clazz2 = loader.loadClass(className[1]);
						classVec.add(new Class[] {clazz1, clazz2});
				} catch (ClassNotFoundException e) {
					log.info(e.toString());
				}
			}
			return (Class[][]) classVec.toArray(new Class[classVec.size()][]);
		} catch (FileNotFoundException fileNotFoundException) {	
			log.add(fileNotFoundException);
			log.add("Test failed: resource file " + file + " was not found");
			return null;
		} catch (Throwable e) {	
			log.add(e);
			log.add("Call Class.forName(" + className + ") failed" );
			log.add("Unexpected " + e.toString());
			return null;
		} 
	}
	
	public static void main(String[] args) {
		System.exit(new S_isInstance().test(args));
	}

}
