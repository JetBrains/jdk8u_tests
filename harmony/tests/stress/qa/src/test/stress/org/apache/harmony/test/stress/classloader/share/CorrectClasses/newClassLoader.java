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
 * @author: Vera Y.Petrashkova
 * @version: $Revision: 1.4 $
 */

package org.apache.harmony.test.stress.classloader.share.CorrectClasses;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class newClassLoader extends ClassLoader {

    private static final char[] sepCh = { File.separatorChar };

    private static final String sep = new String(sepCh, 0, sepCh.length);

    private String path = null;

    public Class defineKlass(String name, byte[] ar, int st, int len) {
        return super.defineClass(name, ar, st, len);
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Class loadClass(String name) throws ClassNotFoundException {
        if (name.indexOf("/") != -1) {
            throw new ClassNotFoundException("Wrong name: " + name);
        }
        if (name.endsWith(".class")) {
            throw new ClassNotFoundException("Wrong name: " + name);
        }

        if (name.startsWith("java")) {
            return super.loadClass(name);
        }

        File ff = null;
        FileInputStream fis = null;
        String name1 = name.concat(".class");
        while (true) {
            try {
                ff = new File(name1);
                fis = new FileInputStream(ff);
                break;
            } catch (FileNotFoundException e) {
                if (path == null) {
                    throw new ClassNotFoundException("There is no file: "
                            + name1);
                }
                name1 = path.concat(sep).concat(name).concat(".class");
            }
        }
        if (fis != null) {
            try {
                byte[] byteCl = new byte[512];

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                while (true) {
                    int n = fis.read(byteCl, 0, byteCl.length);
                    if (n == -1) {
                        break;
                    }
                    if (n > 0) {
                        bos.write(byteCl, 0, n);
                    }
                }

                byteCl = bos.toByteArray();

                return super.defineClass(name, byteCl, 0, byteCl.length);
            } catch (Throwable e) {
                e.printStackTrace(System.out);
                return null;
            } finally {
                try {
                    fis.close();
                } catch (IOException e1) {
                    e1.printStackTrace(System.out);
                    return null;
                }
            }
        } else {
            return null;
        }

    }

    private byte[] firstBytes = null;

    private byte[] lastBytes = null;

    private int intF = 0;

    private int intL = 0;

    public Class loadClass(String name, String nameCl)
            throws ClassNotFoundException {

        if (nameCl.indexOf("/") != -1) {
            throw new ClassNotFoundException("Wrong name: " + nameCl);
        }
        if (nameCl.endsWith(".class")) {
            throw new ClassNotFoundException("Wrong name: " + nameCl);
        }
        if (name.indexOf("/") != -1) {
            throw new ClassNotFoundException("Wrong name: " + name);
        }
        if (name.endsWith(".class")) {
            throw new ClassNotFoundException("Wrong name: " + name);
        }

        if (nameCl.startsWith("java")) {
            return super.loadClass(name);
        }

        if ((firstBytes == null) || (lastBytes == null) || (intF == 0)
                || (intL == 0)) {
            File ff = null;
            FileInputStream fis = null;
            String name1 = name.concat(".class");
            while (true) {
                try {
                    ff = new File(name1);
                    fis = new FileInputStream(ff);
                    break;
                } catch (FileNotFoundException e) {
                    if (path == null) {
                        throw new ClassNotFoundException("There is no file: "
                                + name1);
                    }
                    name1 = path.concat(sep).concat(name).concat(".class");
                }
            }

            if (fis != null) {
                try {
                    DataInputStream dd1 = new DataInputStream(fis);
                    firstBytes = new byte[11];
                    intF = dd1.read(firstBytes, 0, 11);
                    dd1.readUTF();
                    lastBytes = new byte[512];
                    intL = dd1.read(lastBytes, 0, lastBytes.length);
                } catch (Throwable e) {
                    e.printStackTrace(System.out);
                    return null;
                } finally {
                    try {
                        fis.close();
                    } catch (IOException e1) {
                        e1.printStackTrace(System.out);
                        return null;
                    }
                }
            } else {
                return null;
            }
        }
        ByteArrayOutputStream bos = null;
        DataOutputStream dd2 = null;
        try {
            bos = new ByteArrayOutputStream();
            dd2 = new DataOutputStream(bos);
            dd2.write(firstBytes, 0, intF);
            dd2.writeUTF(nameCl);
            dd2.write(lastBytes, 0, intL);
            dd2.close();
            byte[] bbb = bos.toByteArray();
            return defineKlass(nameCl, bbb, 0, bbb.length);

        } catch (Throwable e) {
            e.printStackTrace(System.out);
            if (dd2 != null) {
                try {
                    dd2.close();
                } catch (IOException e1) {
                    e1.printStackTrace(System.out);
                    return null;
                }
            }
            return null;

        }
    }
}
