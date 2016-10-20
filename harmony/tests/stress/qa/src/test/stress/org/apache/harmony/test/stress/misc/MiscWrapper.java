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
 * @author Alexander D. Shipilov, Vladimir Nenashev
 * @version $Revision: 1.4 $
 */

package org.apache.harmony.test.stress.misc;

import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.StreamTokenizer;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.lang.reflect.*;

public class MiscWrapper extends Thread {

    public int test(String[] args) throws IOException {
        int numClasses = 0;
        List classes[] = new LinkedList[1000];
        StreamTokenizer st = null;

        // reading from file and excluding comments.
        Reader reader;
        if (args.length == 0) {
            System.err
                    .println("File name with list of classes to run expected");
            return -1;
        }
        try {
            reader = new FileReader(args[0]);
        } catch (FileNotFoundException e) {
            System.err.println("Cannot open file " + args[0]);
            return -1;
        }

        st = new StreamTokenizer(reader);
        st.commentChar(';');
        st.eolIsSignificant(true);
        st.slashSlashComments(false);
        st.slashStarComments(false);
        st.ordinaryChars('0', '9');
        st.ordinaryChar('-');
        st.ordinaryChar('.');
        st.wordChars('0', '9');
        st.wordChars('.', '.');
        st.wordChars('-', '-');
        classes[0] = null;
        while (st.nextToken() != StreamTokenizer.TT_EOF) {
            if (st.ttype == StreamTokenizer.TT_WORD) {
                if (classes[numClasses] == null)
                    classes[numClasses] = new LinkedList();
                classes[numClasses].add(st.sval);
            } else if (st.ttype == StreamTokenizer.TT_EOL) {
                if ((classes[numClasses]) != null
                        && (classes[numClasses].size() != 0))
                    classes[++numClasses] = null;
            }
        }
        if (classes[numClasses] != null)
            numClasses++;
        reader.close();

        new MiscWrapper().start();
        // run classes

        System.out.println(numClasses + " classes total to run");

        for (int i = 0; i < numClasses; i++) {
            String className = (String) classes[i].get(0);
            Class newThread;
            try {
                newThread = Class.forName(className);
            } catch (ClassNotFoundException e) {
                System.err.println("Class not found: " + className);
                continue;
            }
            if (Thread.class.isAssignableFrom(newThread)) { // run thread
                                                            // instance
                int params_number = classes[i].size() - 1;
                Object[] params = new Object[params_number];
                Class[] fParams = new Class[params_number];
                if (params_number != 0) {
                    int j = 0;
                    Iterator it = classes[i].iterator();
                    it.next();
                    while (it.hasNext()) {
                        String param = (String) it.next();
                        // try to parse as integer, then as float, then leave as
                        // String
                        Object objParam = null;
                        try {
                            objParam = Integer.valueOf(param);
                        } catch (NumberFormatException e) {
                        }
                        if (objParam != null) {
                            params[j] = objParam;
                            fParams[j++] = int.class;
                            continue;
                        }

                        try {
                            objParam = Float.valueOf(param);
                        } catch (NumberFormatException e) {
                        }
                        if (objParam != null) {
                            params[j] = objParam;
                            fParams[j++] = float.class;
                            continue;
                        }

                        params[j] = param;
                        fParams[j++] = String.class;

                    }
                }

                Constructor<?> c = null;
                Thread th = null;
                try {
                    c = newThread.getConstructor(fParams);
                } catch (NoSuchMethodException e) {
                    System.err.println("Cannot find a proper constructor for "
                            + newThread.getName());
                    continue;
                }
                try {
                    th = (Thread) c.newInstance(params);
                } catch (IllegalAccessException e) {
                    System.err.println("Cannot create an instance of "
                            + newThread.getName());
                    continue;
                } catch (InvocationTargetException e) {
                    System.err.println("Cannot create an instance of "
                            + newThread.getName());
                    continue;
                } catch (InstantiationException e) {
                    System.err.println("Cannot create an instance of "
                            + newThread.getName());
                    continue;
                }

                th.start();
                System.out.println("Started " + newThread.getName());
            } else { // run non-thread class
                Method m;
                int j = 0;
                int params_number = classes[i].size() - 2;
                Object[] params = new Object[params_number];
                Class[] fParams = new Class[params_number];
                String mName = (String) classes[i].get(1);
                Iterator it = classes[i].iterator();
                it.next();
                it.next();
                while (it.hasNext()) {
                    String param = (String) it.next();
                    // try to parse as integer, then as float, then leave as
                    // String
                    Object objParam = null;
                    try {
                        objParam = Integer.valueOf(param);
                    } catch (NumberFormatException e) {
                    }
                    if (objParam != null) {
                        params[j] = objParam;
                        fParams[j++] = int.class;
                        continue;
                    }

                    try {
                        objParam = Float.valueOf(param);
                    } catch (NumberFormatException e) {
                    }
                    if (objParam != null) {
                        params[j] = objParam;
                        fParams[j++] = float.class;
                        continue;
                    }

                    params[j] = param;
                    fParams[j++] = String.class;

                }

                try {
                    m = newThread.getMethod(mName, fParams);
                } catch (NoSuchMethodException e) {
                    System.err.print("Cannot find method " + mName + "(");
                    for (int k = 0; k < fParams.length; k++) {
                        System.err.print(fParams[k].getName());
                        if (k < fParams.length - 1)
                            System.err.print(",");
                    }
                    System.err.println(") in " + newThread.getName());
                    continue;
                }
                Thread th;
                try {
                    th = new StressLoadsThread(m, newThread.newInstance(),
                            params);
                } catch (InstantiationException e) {
                    System.err.println("Cannot create instance of "
                            + newThread.getName());
                    continue;
                } catch (IllegalAccessException e) {
                    System.err.println("Cannot create instance of "
                            + newThread.getName());
                    continue;
                }

                th.start();
            }
        }
        System.out.println("All classes started");
        return 0;
    }

    public static void main(String[] args) throws IOException {
        new MiscWrapper().test(args);
    }

    public void run() {
        try {
            while (true) {
                Thread.sleep(2000);
                // System.out.println(Thread.activeCount());
                System.out.print(".");
                System.out.flush();
            }
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }

    }
}

class StressLoadsThread extends Thread {
    Method toInvoke;

    Object toRun;

    Object paramArray[];

    public void run() {
        try {
            toInvoke.invoke(toRun, paramArray);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    public StressLoadsThread(Method toInvoke, Object toRun, Object paramArray[]) {
        this.toInvoke = toInvoke;
        this.toRun = toRun;
        this.paramArray = paramArray;
    }
}
