/*
    Copyright 2005-2006 The Apache Software Foundation or its licensors, as applicable

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

    See the License for the specific language governing permissions and
    limitations under the License.
*/
/**
 * @author Nikolay Y. Amosov
 * @version $Revision: 1.3 $
 */

package org.apache.harmony.vts.test.vm.jvms.classFile.limitations.interfaces.interfaces01;

import org.apache.harmony.share.Result;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

public class Interfaces01 {
//  Interface template
    static byte[] interfaceTemplate = {
        -54, -2, -70, -66, 0, 0, 0, 46,
        0, 5, 7, 0, 3, 7, 0, 4,
        1, 0, 6,
        105, 0, 0, 0, 0, 0, // interface name
        1, 0, 16, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116,
        6, 1, 0, 1, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0};


    static String testedClassFilename = 
      "org.apache.harmony.vts.test.vm.jvms.classFile.limitations.interfaces.interfaces01.ManyInterfacesImplementer";
    //"ManyInterfacesImplementer";

    public static void main(String[] args) throws Exception {
        System.exit((new Interfaces01()).test(args));
    }

    int generatedInterfaces = 0;

    public int test(String[] args) throws Exception {
        testCL cl = new testCL();
        cl.loadClass(testedClassFilename);
        return Result.PASS;
    }

    class testCL extends ClassLoader {

        public Class loadClass(ByteArrayOutputStream classInStream)
                throws ClassNotFoundException {
            byte[] dt = classInStream.toByteArray();
            return defineClass(testedClassFilename, dt, 0, dt.length);
        }

        boolean isExpectedInterfaceName(String name) {
            if (name.length() == 6 && name.charAt(0) == 'i') {
                try {
                    int interfaceNumber = Integer.parseInt(name.substring(1), 10);
                    if (interfaceNumber > 0)
                        return true;
                } catch (NumberFormatException nfe) {
                    return false;
                }
            }
            return false;
        }

        public Class loadClass(String name) throws ClassNotFoundException {
            if (isExpectedInterfaceName(name)) {
                generatedInterfaces++;
                System.arraycopy(name.substring(1).getBytes(), 0, interfaceTemplate, 20, 5);
                return defineClass(name, interfaceTemplate, 0, interfaceTemplate.length);
            } else if (name.equals(testedClassFilename)) {
                return loadClassFromFile(name);
            } else {
                return super.loadClass(name);
            }
        }

        /**
         * @param classFileName - class name to load
         * @return
         * @throws ClassNotFoundException - when class file  not fount in classpath's paths
         */

        private Class loadClassFromFile(String classFileName) throws ClassNotFoundException {
            String classpath = System.getProperty("java.class.path");
            String[] paths = classpath.split(System.getProperty("path.separator"));
            String pathdelimiter = System.getProperty("file.separator");

            // search class file in classpath's folders
            java.io.File fl = null;
            String classFileNameWithPath =   classFileName.replace(".", pathdelimiter);

            for (int i = 0; i < paths.length; ++i)
                if ((new java.io.File(paths[i] + pathdelimiter + classFileNameWithPath + ".class").exists())) {
                    fl = new java.io.File(paths[i] + pathdelimiter + classFileNameWithPath + ".class");
                    break;
                }

            if (fl != null) {
                try {
                    FileInputStream fis = new FileInputStream(fl);
                    byte[] clsFromFile = new byte[(int) fl.length()];
                    fis.read(clsFromFile);
                    return defineClass(classFileName, clsFromFile, 0, clsFromFile.length);
                } catch (Exception e) {
                    throw new ClassNotFoundException(e.getMessage());
                }
            } else
                throw new ClassNotFoundException("file " + classFileName + ".class not found in classpath's folders");
        }
    }
}