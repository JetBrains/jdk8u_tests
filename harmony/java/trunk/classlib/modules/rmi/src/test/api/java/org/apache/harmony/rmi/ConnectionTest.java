/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author  Mikhail A. Markov, Vasily Zakharov
 */
package org.apache.harmony.rmi;

import java.lang.reflect.Proxy;

import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.ServerError;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.rmi.server.RemoteObject;

import org.apache.harmony.rmi.test.MyException;
import org.apache.harmony.rmi.test.MyInvocationHandler;
import org.apache.harmony.rmi.test.MyRemoteInterface;
import org.apache.harmony.rmi.test.MyRemoteInterface1;
import org.apache.harmony.rmi.test.MyRemoteObject;
import org.apache.harmony.rmi.test.MyRemoteObject1;
import org.apache.harmony.rmi.test.MyRemoteObject3;
import org.apache.harmony.rmi.test.MyRemoteObject4;

import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * Unit test for RMI.
 *
 * @author  Mikhail A. Markov, Vasily Zakharov
 */
public class ConnectionTest extends RMITestBase {

    /**
     * String to identify that a child test process must be started.
     */
    private static final String CHILD_ID = "child";

    /**
     * No-arg constructor to enable serialization.
     */
    public ConnectionTest() {
        super();
    }

    /**
     * Constructs this test case with the given name.
     *
     * @param   name
     *          Name for this test case.
     */
    public ConnectionTest(String name) {
        super(name);
    }

    /**
     * Creates RMI server part.
     *
     * @throws  Exception
     *          If some error occurs.
     */
    private void initServer() throws Exception {
        MyRemoteObject obj = new MyRemoteObject();
        System.err.println("Object: " + obj + " created and exported.");
        Registry reg = LocateRegistry.createRegistry(REGISTRY_PORT);
        exportedObjects.add(reg);
        System.err.println("Registry created.");
        reg.bind("MyRemoteObject", RemoteObject.toStub(obj));
        exportedObjects.add(obj);
        System.err.println("Object bound in the registry.");
    }

    /**
     * Performs various data exchange tests.
     *
     * @throws  Exception
     *          If some error occurs.
     */
    private void mainTestBody() throws Exception {
        Registry reg = LocateRegistry.getRegistry(REGISTRY_HOST);
        System.err.println("Registry located.");
        MyRemoteInterface mri = (MyRemoteInterface) reg.lookup("MyRemoteObject");
        System.err.println("Lookup object is: " + mri);
        Remote obj;

        // test_String_Void
        System.err.println("Testing test_String_Void...");
        mri.test_String_Void("Main for test_String_Void.");
        System.err.println("Done.\n");

        // test_Void_String
        System.err.println("Testing test_Void_String...");
        System.err.println("Returned: " + mri.test_Void_String());
        System.err.println("Done.\n");

        // test_Int_Void
        System.err.println("Testing test_Int_Void...");
        mri.test_Int_Void(1234567890);
        System.err.println("Done.\n");

        // test_Void_Int
        System.err.println("Testing test_Void_Int...");
        System.err.println("Returned: " + mri.test_Void_Int());
        System.err.println("Done.\n");

        //test_Remote_Void
        System.err.println("Testing test_Remote_Void...");
        obj = new MyRemoteObject1("Main for test_Remote_Void.");
        exportedObjects.add(obj);
        mri.test_Remote_Void(obj);
        System.err.println("Done.\n");

        //tests remote Object extending another remote object
        System.err.println("Testing test_Remote_Void with remote object "
                + "extending another remote object...");
        obj = new MyRemoteObject4("Main for test_Remote_Void 1.");
        exportedObjects.add(obj);
        mri.test_Remote_Void(obj);
        System.err.println("Done.\n");

        // test_Void_Remote
        System.err.println("Testing test_Void_Remote...");
        System.err.println("Returned: " + mri.test_Void_Remote());
        System.err.println("Done.\n");

        // test_Long_Long
        System.err.println("Testing test_Long_Long...");
        System.err.println("Returned: "
                + mri.test_Long_Long(112233445566778899L));
        System.err.println("Done.\n");

        // test_String_String
        System.err.println("Testing test_String_String...");
        System.err.println("Returned: "
                + mri.test_String_String("Main for test_String_String."));
        System.err.println("Done.\n");

        // test_Remote_Remote
        System.err.println("Testing test_Remote_Remote...");
        obj = new MyRemoteObject1("Main for test_Remote_Remote.");
        exportedObjects.add(obj);
        System.err.println("Returned: " + mri.test_Remote_Remote(obj));
        System.err.println("Done.\n");

        // test_RemoteString_Void
        System.err.println("Testing test_RemoteString_Void...");
        obj = new MyRemoteObject1("Main for test_RemoteString_Void.");
        exportedObjects.add(obj);
        mri.test_RemoteString_Void(obj,
                "Main for test_RemoteString_Void (2).");
        System.err.println("Done.\n");

        // test_RemoteRemote_Remote
        System.err.println("Testing test_RemoteRemote_Remote...");
        obj = new MyRemoteObject1("Main for test_RemoteRemote_Remote.");
        Remote obj1 = new MyRemoteObject3(
                "Main for test_RemoteRemote_Remote (2).");
        exportedObjects.add(obj);
        exportedObjects.add(obj1);
        System.err.println("Returned: "
                + mri.test_RemoteRemote_Remote(obj, obj1));
        System.err.println("Done.\n");

        // test_BooleanStringRemote_Void
        System.err.println("Testing test_BooleanStringRemote_Void...");
        obj = new MyRemoteObject1(
                "Main for test_BooleanStringRemote_Void (2).");
        exportedObjects.add(obj);
        mri.test_BooleanStringRemote_Void(false,
            "Main for test_BooleanStringRemote_Void.", obj);
        System.err.println("Done.\n");

        // test_Proxy_Void
        System.err.println("Testing test_Proxy_Void...");
        Object proxy = Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class[] { MyRemoteInterface1.class },
                new MyInvocationHandler());
        mri.test_Proxy_Void(proxy);
        System.err.println("Done.\n");

        // test_Array_Void
        System.err.println("Testing test_Array_Void...");
        mri.test_Array_Void(new String[] { "Main for test_Array_Void 1.",
               "Main for test_Array_Void 2." });
        System.err.println("Done.\n");

        // test_Void_Array
        System.err.println("Testing test_Void_Array...");
        printArray(mri.test_Void_Array());
        System.err.println("Done.\n");

        // test_RemoteArray_Void
        System.err.println("Testing test_RemoteArray_Void...");
        obj = new MyRemoteObject1("Main for test_RemoteArray_Void 1.");
        obj1 = new MyRemoteObject1("Main for test_RemoteArray_Void 2.");
        exportedObjects.add(obj);
        exportedObjects.add(obj1);
        mri.test_RemoteArray_Void(new Remote[] { obj, obj1 });
        System.err.println("Done.\n");

        // test_Void_RemoteArray
        System.err.println("Testing test_Void_RemoteArray...");
        printArray(mri.test_Void_RemoteArray());
        System.err.println("Done.\n");

        // test_Exception
        System.err.println("Testing test_Exception...");
        try {
            mri.test_Exception();
            fail("test_Exception() should have thrown MyException");
        } catch (MyException e) {
            System.err.println(e.toString());
        }
        System.err.println("Done.\n");

        // test_Error
        System.err.println("Testing test_Error...");
        try {
            mri.test_Error();
            fail("test_Error() should have thrown ServerError");
        } catch (ServerError e) {
            System.err.println(e.toString());
        }
        System.err.println("Done.\n");

        // test_RuntimeException
        System.err.println("Testing test_RuntimeException...");
        try {
            mri.test_RuntimeException();
            fail("test_RuntimeException() should have thrown RuntimeException");
        } catch (RuntimeException e) {
            System.err.println(e.toString());
        }
        System.err.println("Done.\n");

        // test_RemoteException
        System.err.println("Testing test_RemoteException...");
        try {
            mri.test_RemoteException();
            fail("test_RemoteException() should have thrown RemoteException");
        } catch (RemoteException e) {
            System.err.println(e.toString());
        }
        System.err.println("Done.\n");
    }

    /**
     * Returns test suite for this class.
     *
     * @return  Test suite for this class.
     */
    public static Test suite() {
        return new TestSuite(ConnectionTest.class);
    }

}
