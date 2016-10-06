/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.harmony.test.func.reg.vm.btest6142;

import java.io.File;
import java.io.IOException;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.harmony.test.share.reg.CrashTest;
import java.util.logging.Logger;

public class Btest6142 extends CrashTest {

    private static final String JAR_SUFFIX = ".jar";

    private static final String CLASS_SUFFIX = ".class";

    private static final int CLASS_SUFFIX_LENGTH = CLASS_SUFFIX.length();

    private static final String BOOT_CLASS_PATH_PROPERTY = "sun.boot.class.path";

    /**
     * Stores found class names.
     */
    private Set classNames = new TreeSet();

    /**
     *
     */
    private void setSecurityManager() {
        System.out.println("Setting security manager");
        System.setSecurityManager(new SecurityManager() {
            public void checkPermission(java.security.Permission perm)
                    throws SecurityException {
                if (perm.equals(new RuntimePermission("exitVM"))) {
                    throw new SecurityException("Attempt to call System.exit()");
                }
            }
        });
    }

    /**
     * Reads all class names from the specified Jar.
     */
    private void readJar(String fileName) throws IOException {
        JarFile jarFile = new JarFile(fileName);
        int num = 0;

        System.out.print("Reading " + fileName + ": ");

        for (Enumeration e = jarFile.entries(); e.hasMoreElements(); ) {
            JarEntry jarEntry = (JarEntry) e.nextElement();

            if (jarEntry.isDirectory()) {
                continue;
            }
            String entryName = jarEntry.getName();

            if (!entryName.endsWith(CLASS_SUFFIX)) {
                continue;
            }
            classNames.add(entryName.substring(0, entryName.length()
                    - CLASS_SUFFIX_LENGTH).replace('/', '.'));
            num++;
        }
        System.out.println(num + " class files");
    }

    /**
     * Reads all class names from all jars listed in the specified property.
     */
    private void readProperty(String propertyName) throws IOException {
        System.out.println("Reading from property: " + propertyName);

        String propertyValue = System.getProperty(propertyName);

        if (propertyValue == null) {
            throw new IOException("Property not found: " + propertyName);
        }

        StringTokenizer tokenizer = new StringTokenizer(
                propertyValue, File.pathSeparator);

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();

            if (!token.endsWith(JAR_SUFFIX)) {
                System.out.println("Ignoring " + token);
                continue;
            }
            File file = new File(token);

            if (!file.isFile()) {
                System.out.println("Missing " + token);
                continue;
            }
            readJar(token);
        }
    }

    /**
     * Tries to load all known classes.
     */
    private void load(Logger logger) throws IOException {

        System.out.println("Loading classes: ");
        int num = 0;

        for (Iterator i = classNames.iterator(); i.hasNext(); ) {
            String className = (String) i.next();
            System.out.println(++num + ": " + className);

            try {
                Class.forName(className);
            } catch (Throwable e) {
                logger.severe("ERROR at class " + className);
                e.printStackTrace();
            }
        }
    }

    /**
     * Main.
     */
    public static void main(String[] args) {
        new Btest6142().test(Logger.global, args);
    }

    public int test(Logger logger, String[] args) {
        try {
            readProperty(BOOT_CLASS_PATH_PROPERTY);
            setSecurityManager();
            load(logger);
        } catch (Throwable e) {
            logger.severe("Unexpected exception: " + e);
            return fail();
        }
        return pass();
    }
}
