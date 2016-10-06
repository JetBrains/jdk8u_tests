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
package org.apache.harmony.test.func.api.javax.management.share.framework;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 */
public class Utils {

    public static List getCommaSeparatedTokens(String str) {
        return getTokens(str, ",");
    }

    public static List getTokens(String str, String delim) {
        Vector v = new Vector();
        if (str != null) {
            StringTokenizer st = new StringTokenizer(str, delim);
            while (st.hasMoreTokens()) {
                v.add(st.nextToken().trim());
            }
        }
        return v;
    }

    public static String arrayToString(Object[] array, String delim) {
        String str = "";
        for (int i = 0; i < array.length; i++) {
            str += array[i];

            if (i != array.length - 1) {
                str += delim;
            }
        }
        return str;
    }

    public static void cpFile(File file, File destDir) throws IOException {
        if (file.isDirectory()) {
            File[] f = file.listFiles();
            File dir = new File(destDir, file.getName());
            if (!dir.exists()) {
                dir.mkdir();
            }
            for (int i = 0; i < f.length; i++) {
                cpFile(f[i], dir);
            }
        } else {
            long lm = file.lastModified();
            File outFile = new File(destDir, file.getName());

            if (outFile.exists() && (outFile.lastModified() == lm)) {
                // System.out.println("File exist: " + file);
                return;
            } else {
                outFile.createNewFile();
            }

            FileInputStream fis = new FileInputStream(file);
            FileOutputStream fos = new FileOutputStream(outFile);
            byte[] buf = new byte[4096];
            int i = -1;
            while ((i = fis.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }

            fis.close();
            fos.flush();
            fos.close();
            outFile.setLastModified(lm);
        }
    }

    /**
     * Returns all classes from the specified package and its subpackages.
     * 
     * @param packageName package name.
     * @return all classes from the specified package and its subpackages.
     */
    public static Class[] getClasses(String packageName) {
        String cp = System.getProperty("java.class.path");
        cp = cp.replace('/', File.separatorChar);
        String pkg = packageName.replace('.', File.separatorChar);
        Vector classes = new Vector();
        List resources = getTokens(cp, File.pathSeparator);
        int size = resources.size();
        for (int i = 0; i < size; i++) {
            String res = (String) resources.get(i);
            File rfile = new File(res);
            if (!rfile.exists()) {
                continue;
            }

            if (rfile.isDirectory()) {
                File f = new File(rfile, pkg);
                if (!f.exists()) {
                    continue;
                }
                findDirClasses(rfile.getPath(), f, classes);
            } else {
                findJARClasses(packageName, rfile, classes);
            }
        }
        Class[] c = new Class[classes.size()];
        classes.toArray(c);
        return c;
    }

    private static void findDirClasses(String prefix, File dir, Vector classes) {
        File[] f = dir.listFiles();
        for (int i = 0; i < f.length; i++) {
            if (f[i].isDirectory()) {
                findDirClasses(prefix, f[i], classes);
            } else {
                String name = f[i].getPath();
                int ind = name.lastIndexOf(".class");
                if (ind == -1) {
                    continue;
                }

                try {
                    name = name.substring(0, ind);
                    name = name.substring(prefix.length());
                    if (name.startsWith(File.separator)) {
                        name = name.substring(1);
                    }
                    classes.add(Class.forName(name.replace(File.separatorChar,
                        '.')));
                } catch (Throwable ex) {
                }
            }
        }
    }

    private static void findJARClasses(String prefix, File f, Vector classes) {
        try {
            InputStream is = new BufferedInputStream(new FileInputStream(f));
            JarInputStream jis = new JarInputStream(is);
            JarEntry entry;
            while ((entry = jis.getNextJarEntry()) != null) {
                try {
                    String name = entry.getName().replace('/', '.');
                    if (!name.startsWith(prefix)) {
                        continue;
                    }

                    int ind = name.lastIndexOf(".class");
                    if (ind == -1) {
                        continue;
                    }

                    name = name.substring(0, ind);
                    classes.add(Class.forName(name));
                } catch (Throwable ex) {
                }
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    public static String getArg(String name, String[] args) {
        if (name == null) {
            return null;
        }
        for (int i = 0; i < args.length - 1; i++) {
            try {
                if (name.equals(args[i])) {
                    return args[i + 1];
                }
            } catch (Exception e) {
                break;
            }
        }
        return null;
    }

    /**
     * Returns comma separated list of class names.
     * 
     * @param c
     * @return
     */
    public static String classesToString(Class[] c) {
        String s = "";
        for (int i = 0; i < c.length; i++) {
            if (c[i].isArray()) {
                String str = c[i].getName();
                int n = 0;
                for (int ind = -1; (ind = str.indexOf('[')) != -1; n++) {
                    str = str.substring(ind + 1);
                }

                if (str.startsWith("L") && str.endsWith(";")) {
                    s += str.substring(1, str.length() - 1);
                } else {
                    s += str.equals("Z") ? "boolean" : str.equals("B") ? "byte"
                        : str.equals("C") ? "char" : str.equals("D") ? "double"
                            : str.equals("F") ? "float" : str.equals("I")
                                ? "int" : str.equals("J") ? "long" : "short";
                }

                for (; n > 0; n--) {
                    s += "[]";
                }
            } else {
                s += c[i].getName();
            }

            if (i != c.length - 1) {
                s += ", ";
            }
        }
        return s;
    }
}
