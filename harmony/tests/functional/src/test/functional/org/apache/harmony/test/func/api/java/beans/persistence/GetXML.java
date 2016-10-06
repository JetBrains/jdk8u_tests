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
package org.apache.harmony.test.func.api.java.beans.persistence;

import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.harmony.test.func.api.java.beans.persistence.beans.GeneratingBeans;

/**
 * Use main method of this class to output XML's to console or save XML's to files.
 * 
 */
public class GetXML {
    private static ArrayList methods;

    static {
        methods = getMethods();
    }

    public static void main(String[] args) {
        try {
            saveXMLToFiles("Some directory");
        } catch (InvocationTargetException e) {
            e.getTargetException().printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getIndexOfMethod(String methodName) throws Exception {
        return methods.indexOf(GeneratingBeans.class.getDeclaredMethod(
            methodName, null));
    }

    // output XML's to console. Use this method in debugging.
    private static void outputXMLToConsole() throws Exception {
        for (int i = 0; i < getNumberOfMethods(); i++) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            GetXML.encodeObject(out, i);
            System.out.println(new String(out.toByteArray()));
        }
    }

    // save XML's to files
    private static void saveXMLToFiles(String dir) throws Exception {
        File file = new File(dir);
        file.mkdir();
        new File(dir).mkdir();
        for (int i = 0; i < getNumberOfMethods(); i++) {
            GetXML.encodeObject(new FileOutputStream(dir + getMethodName(i),
                false), i);
        }
    }

    public static int getNumberOfMethods() {
        return methods.size();
    }

    public static String getMethodName(int i) {
        return ((Method)methods.get(i)).getName();

    }

    private static ArrayList getMethods() {
        Method[] methods1 = GeneratingBeans.class.getDeclaredMethods();
        ArrayList methods2 = new ArrayList(methods1.length);
        for (int i = 0; i < methods1.length; i++) {
            if (!Modifier.isPublic(methods1[i].getModifiers()))
                continue;
            methods2.add(methods1[i]);
        }
        return methods2;
    }

    public static void encodeObject(OutputStream outputStream, int i)
        throws Exception {
        XMLEncoder encoder = new XMLEncoder(outputStream);
        Object obj = ((Method)methods.get(i)).invoke(null, null);
        encoder.writeObject(obj);
        encoder.close();
    }

    public static Object getBeanFromMethod(String methodName) throws Exception {
        return GeneratingBeans.class.getDeclaredMethod(methodName, null)
            .invoke(null, null);
    }

    public static Object decodeXMLwithReference(String cmd, String fileName)
        throws Exception {
        String className = DecoderReference.class.getName();
        Process process = null;
        Object object;
        try {
            process = Runtime.getRuntime().exec(
                cmd + className + " " + convertNameDependsOnOS(fileName));
            object = new ObjectInputStream(process.getInputStream())
                .readObject();
            process.waitFor();
        } finally {
            readErrors(process.getErrorStream());
        }
        return object;
    }

    private static String convertNameDependsOnOS(String string) {
        if (System.getProperty("os.name").startsWith("Windows")) {
            return "\"" + string + "\"";
        }
        return string;
    }

    public static InputStream encodeObjectWithReference(String cmd,
        String methodName) throws Exception {

        String className = EncoderReference.class.getName();
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(
                cmd + className + " " + convertNameDependsOnOS(methodName));
            process.waitFor();
        } finally {
            readErrors(process.getErrorStream());
        }
        return process.getInputStream();
    }

    private static void readErrors(InputStream errInput) throws IOException {
        int av = errInput.available();
        if (av > 0) {
            byte[] bytes = new byte[av];
            errInput.read(bytes);
            System.err.print(new String(bytes));
        }
    }

    /**
     * Compare two objects including arrays, null and standard objects.
     */
    public static void compareObjects(Object object1, Object object2)
        throws Exception {
        if (object1 == null) {
            if (object2 == null) {
                return;
            } else {
                throw new Exception("One object=null, but another!= null");
            }
        }
        if (object1.getClass().isArray()) {
            if (object1.getClass().equals(int[].class)) {
                if (Arrays.equals((int[])object1, (int[])object2)) {
                    return;
                } else {
                    throw new Exception();
                }
            }
            if (Arrays.equals((Object[])object1, (Object[])object2)) {
                return;
            } else {
                throw new Exception("Arrays.equals()=false");
            }
        }
        if (object1.equals(object2)) {
            return;
        } else {
            throw new Exception("object1.equals(object2)=false: " + object1
                + " " + object2);
        }
    }
}