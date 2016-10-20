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

package org.apache.harmony.test.stress.eclipse.emulation.java.lang.ClassLoader.S_loadClass.S_loadClass1T;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.harmony.test.stress.eclipse.emulation.java.lang.share.UsefulMethods;

import org.apache.harmony.share.Test;

public class S_loadClass1T extends Test {
	
	public int test() {
		String auxilaryDir = testArgs[0];
		String eclipsePath = testArgs[1];
		URL[] eclipsePlugins = UsefulMethods.get_plugin_path(eclipsePath);
		if (eclipsePlugins != null ) {
			URLClassLoader urlClassLoader = new URLClassLoader(eclipsePlugins);
			int loadedClassCount = 0;
			int loadBySystemClassLoader = 0;
			int loadByUrlClassLoader = 0;
			String ResourceFileName;
			if (System.getProperty("os.name").
					toLowerCase().indexOf("windows") >= 0) {
				ResourceFileName = "loadClass_param_win.txt";
			} else { 
				ResourceFileName = "loadClass_param_lnx.txt";
			}
			File auxilaryFile = new File(auxilaryDir + File.separator
						+ ResourceFileName);
			try {
				FileInputStream loadClassParam = new FileInputStream(auxilaryFile);
				LineNumberReader reader = new LineNumberReader(
					new InputStreamReader(loadClassParam));
				String className;
				while( (className = reader.readLine()) != null ){
					try {
						ClassLoader.getSystemClassLoader().loadClass(className);
						loadedClassCount++;
						loadBySystemClassLoader++;
					} catch (ClassNotFoundException classNotFoundException) {
						urlClassLoader.loadClass(className);
						loadByUrlClassLoader++;
						loadedClassCount++;
					}
				}
				log.info("Loaded class count: " + loadedClassCount);
				log.info("\t by systemClassLoader - " + loadBySystemClassLoader);
				log.info("\t by urlClassLoader - " + loadByUrlClassLoader);
				return pass();
			} catch (FileNotFoundException fileNotFoundException) {
				log.add(fileNotFoundException);
				return fail("Test failed: Resource file " 
						+ auxilaryDir+File.separator + ResourceFileName
						+ " was not found");
			} catch (Throwable e) {
				log.add(e);
				return fail("Test failed: unexpected " + e + " occurred");
			}
		} else {
			return fail("Test failed: Unable to get eclipse plugins list");
		}
	}
			
	public static void main(String[] args) {
		System.exit(new S_loadClass1T().test(args));
	}

}
