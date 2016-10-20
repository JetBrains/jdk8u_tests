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

package org.apache.harmony.test.stress.eclipse.emulation.java.lang.share;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import org.apache.harmony.share.Test;

public abstract class UsefulMethods extends Test {
	
	public static URL[] get_plugin_path(String eclipsePath) {
		log.info("Eclipse path: " + eclipsePath);
		String eclipsePluginPath = eclipsePath + File.separator + "plugins";
		File pluginDir = new File(eclipsePluginPath);
		Vector jarList = new Vector(180);
		String javaHome = new String(System.getProperty("java.home"));
		if (pluginDir.exists()) {
			try {
				jarList.add(new URL("file:///" + javaHome + 
						"/lib/org.apache.harmony.eclipse.jdt.launching_1.0.0.jar"));
				jarList.add(new URL("file:///" + eclipsePath + "/startup.jar"));
			} catch (MalformedURLException e) {
				log.add(e); 
				return null;
			}
			return get_jars_from_this_dir(pluginDir, jarList);
		} else {
			log.info("Eclipse plugin path is incorrect: " + pluginDir);
			return null;
		}
	}
	
	private static URL[] get_jars_from_this_dir(File dir, Vector jars) {
		File[] subdirs = dir.listFiles();
		for (int i=0; i<subdirs.length; i++) {
			if (subdirs[i].isFile()) {
				if (subdirs[i].getPath().endsWith(".jar")) {
					try {
						jars.add(subdirs[i].toURI().toURL());
					} catch (MalformedURLException e) {
						log.add(e);
						return null;
					}
				}
			} else {
				get_jars_from_this_dir(subdirs[i], jars);
			}
		}
		return (URL[]) jars.toArray(new URL[jars.size()]);
	}
	
	public static Class[] getClassesArray(File file, ClassLoader loader) {
		String className = null;
		try {
			FileInputStream defineClassParam = new FileInputStream(file);
			LineNumberReader reader = new LineNumberReader(
									new InputStreamReader(defineClassParam));
			Class clazz;
			Vector classVec = new Vector();
			while( (className = reader.readLine()) != null ){
				className = className.replace('/', '.');
				try {
					clazz = loader.loadClass(className); 
					classVec.add(clazz);
				} catch (ClassNotFoundException e) {
					log.info(className);
					log.info(e.toString());
				}
			}
			return (Class[]) classVec.toArray(new Class[classVec.size()]);
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
	
}
