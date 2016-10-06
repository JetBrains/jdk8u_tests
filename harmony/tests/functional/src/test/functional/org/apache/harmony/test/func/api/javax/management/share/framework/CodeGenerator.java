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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Date;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

/**
 */
public class CodeGenerator {

    /**
     * Find tested methods.
     * 
     * @return tested methods.
     */
    private TreeMap testedClassMethods(Class c) {
        TreeMap map = new TreeMap();

        Member[] dc = c.getDeclaredConstructors();
        Member[] dm = c.getDeclaredMethods();
        Member[] m = new Member[dc.length + dm.length];
        System.arraycopy(dc, 0, m, 0, dc.length);
        System.arraycopy(dm, 0, m, dc.length, dm.length);
        for (int i = 0; i < m.length; i++) {
            int mod = m[i].getModifiers();
            if (Modifier.isPublic(mod) || Modifier.isProtected(mod)) {
                String mname = m[i].getName();
                // Check if there are more than 1 methods with the same name.
                int count = 0;
                for (int n = 0; n < m.length; n++) {
                    if (mname.equals(m[n].getName())) {
                        count++;
                    }
                    if (count > 1) {
                        break;
                    }
                }

                String prefix = "mtest";

                if (m[i] instanceof Constructor) {
                    prefix = "ctest";
                    int ind = mname.lastIndexOf('.');
                    if (ind != -1) {
                        mname = mname.substring(ind + 1);
                    }
                }

                mname = prefix + mname.substring(0, 1).toUpperCase()
                    + mname.substring(1);

                if (count > 1) {
                    Class[] t = null;
                    if (m[i] instanceof Method) {
                        t = ((Method) m[i]).getParameterTypes();
                    } else {
                        t = ((Constructor) m[i]).getParameterTypes();
                    }

                    for (int n = 0; n < t.length; n++) {
                        String s = t[n].getName();
                        if (t[n].isArray()) {
                            s = Utils.classesToString(new Class[] { t[n] })
                                .replaceAll("\\[]", "Array");
                        }
                        int ind = s.lastIndexOf('.');
                        if (ind != -1) {
                            s = s.substring(ind + 1);
                        }
                        mname += s;
                    }
                }

                map.put(mname, m[i]);
            }
        }

        return map;
    }

    public void print(String className, String pkg, String author,
        PrintStream out) throws ClassNotFoundException {
        Class tClass = Class.forName(className);
        TreeMap map = testedClassMethods(tClass);
        String tcpkg = "";
        String cname = className;
        String name = "";
        int ind = className.lastIndexOf('.');
        if (ind != 0) {
            tcpkg = className.substring(0, ind);
            pkg += "." + tcpkg;
            cname = cname.substring(ind + 1);
            name = cname + "Test";
        }

        out.println("package " + pkg + ";\n");
        out.println("import org.apache.harmony.test.func.api."
            + "javax.management.share.framework.NotImplementedException;");
        out.println("import org.apache.harmony.test.func.api."
            + "javax.management.share.framework.Test;");
        out.println("import org.apache.harmony.test.func.api."
            + "javax.management.share.framework.TestRunner;\n");
        out.println("/**\n * Test for the " + tClass + "\n *\n * author "
            + author + "\n */");
        out.println("public class " + name + " extends Test {\n");

        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            String mname = it.next().toString();
            Member m = (Member) map.get(mname);
            mname = mname.substring(1);
            Class[] t = null;
            String type = "constructor";
            String tmname = null;
            if (m instanceof Method) {
                tmname = m.getName() + "(";
                t = ((Method) m).getParameterTypes();
                type = "method";
            } else {
                tmname = m.getName();
                if (tmname.startsWith(tcpkg)) {
                    tmname = tmname.substring(tcpkg.length() + 1);
                }
                tmname += "(";
                t = ((Constructor) m).getParameterTypes();
            }

            tmname += Utils.classesToString(t);

            tmname += ")";
            out.println("    /**\n     * Test for the " + type + " " + tmname);
            out.println("     * ");
            out.println("     * @see " + tcpkg + "." + cname + "#" + tmname);
            out.println("     */");
            out.println("    public final void " + mname + "() {");
            out.println("        // TODO Implement this method");
            out
                .println("        throw new NotImplementedException(\"Not implemented.\");\n    }\n");
        }
        out.println("    /**\n" + "     * Run the test.\n" + "     * \n"
            + "     * @param args command line arguments.\n"
            + "     * @throws Exception \n" + "     */\n"
            + "    public static void main(String[] args) throws Exception {\n"
            + "        System.exit(TestRunner.run(" + name
            + ".class, args));\n" + "    }\n}");
    }

    public void printXML(String sig, String className, String testName,
        String testClass, String author, PrintStream out) throws IOException {
        out
            .println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
                + "<!DOCTYPE Test SYSTEM \"test.dtd\">\r\n"
                + "<Test ID=\"" + testName + "\"\r\n"
                + "   date-of-creation=\""
                + new Date(System.currentTimeMillis()).toString()
                + "\" timeout=\"1\">\r\n" + "\r\n"
                + "   <Author value=\"" + author + "\" />\r\n" + "\r\n"
                + "   <APITestDescription>\r\n" + "       "
                + getCoverage(sig, className).replaceAll("\n", "\r\n       ")
                + "\r\n" + "\r\n       <Description>\r\n"
                + "       </Description>\r\n" + "   </APITestDescription>\r\n"
                + "\r\n" + "   <Keyword name=\"functional\" />\r\n" + "\r\n"
                + "   <Source name=\"" + testName + ".java\" />\r\n" + "\r\n"
                + "   <Runner ID=\"Runtime\">\r\n"
                + "       <Param name=\"toRun\"\r\n" + "           value=\""
                + testClass + "\" />\r\n" + "   </Runner>\r\n" + "</Test>");
    }

    /**
     * Returns list containing tested methods signatures.
     * 
     * @param path
     * @param className
     * @return
     * @throws IOException
     */
    public String getCoverage(String path, String className) throws IOException {
        String cn = className;
        className = className.replace(".", "/") + "/";
        Vector v = new Vector();
        RandomAccessFile raf = new RandomAccessFile(path, "r");
        String s = null;
        while ((s = raf.readLine()) != null) {
            if (s.startsWith("non-testable") || !s.contains(className)) {
                continue;
            }

            s = s.replaceFirst(className, "");

            if (s.indexOf("/") == -1) {
                v.add(s);
            }
        }

        s = "<TestedClass name=\"" + cn + "\" />";
        int size = v.size();
        for (int i = 0; i < size; i++) {
            s += "\n<TestedMethod name=\"" + v.get(i) + "\" />";
        }

        return s;
    }

    public static void main(String[] args) throws Exception {
        String c = args[args.length - 1];
        String pkg = Utils.getArg("-pkg", args);
        String auth = Utils.getArg("-a", args);
        String sig = Utils.getArg("-s", args);
        String out = Utils.getArg("-out", args);
        pkg = pkg == null ? "org.apache.harmony.test.func.api" : pkg;

        if (sig != null && out != null) {
            String name = c + "Test";
            String tpkg = "";
            int ind = c.lastIndexOf('.');
            if (ind != 0) {
                tpkg = pkg + "." + c.substring(0, ind);
                name = name.substring(ind + 1);
            }

            CodeGenerator cg = new CodeGenerator();
            File outDir = new File(out + File.separator
                + tpkg.replace('.', File.separatorChar));
            if (!outDir.exists()) {
                outDir.mkdirs();
            }
            // cg.print(c, pkg, auth, System.out);
            File test = new File(outDir, name + ".java");
            if (!test.exists()) {
                System.out.println("Creating test: " + test);
                FileOutputStream fis = new FileOutputStream(test);
                cg.print(c, pkg, auth, new PrintStream(fis));
                fis.flush();
                fis.close();
            }

            File testXML = new File(outDir, name + ".xml");
            if (!testXML.exists()) {
                System.out.println("Creating test XML: " + testXML);
                FileOutputStream fis = new FileOutputStream(testXML);
                cg.printXML(sig, c, name, tpkg + "." + name, auth,
                    new PrintStream(fis));
                fis.flush();
                fis.close();
            }
        } else {
            new CodeGenerator().print(c, pkg, auth, System.out);
        }
    }
}
