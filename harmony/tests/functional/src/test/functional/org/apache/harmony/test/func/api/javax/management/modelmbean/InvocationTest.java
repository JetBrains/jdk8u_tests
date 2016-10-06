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
//$Id$
package org.apache.harmony.test.func.api.javax.management.modelmbean;

import java.lang.reflect.Method;

import javax.management.Descriptor;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.modelmbean.ModelMBeanInfoSupport;
import javax.management.modelmbean.ModelMBeanOperationInfo;
import javax.management.modelmbean.RequiredModelMBean;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 * Purpose: verify that invoke method correctly invokes operation method and
 * caches returned value.
 * <p>
 * Under test: ModelMBeanInfoSupport, ModelMBeanOperationInfo,
 * RequiredModelMBean.
 * <p>
 * For more detail see descriptions for test cases.
 * 
 */
public class InvocationTest extends MultiCase {

    /**
     * To guarantee that after Thread.sleep(a) thread sleep more than `a`
     * milliseconds, the latency is added to sleep time. This parameter is used
     * in this and in RequiredModelMBeanTest classes.
     */
    static final int           latancy        = 500;
    private static boolean     isInvoked      = false;
    /**
     * simpleMethod method always returns this object.
     */
    private Integer            returnedObject = new Integer(6);
    private static final Class class1         = InvocationTest.class;

    public static void main(String[] args) {
        try {
            System.exit(new InvocationTest().test(args));
            // new InvocationTest().testNegative();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Verify that invoke method always retrieves value from cache if
     * currencyTimeLimit = 0.
     * <ul>
     * Step by step:
     * <li>Create operation method without parameters which always returns the
     * same value.
     * <li>Create ModelMBeanOperationInfo object for operation method.
     * <li>Set value currencyTimeLimit = 0 in descriptor for
     * ModelMBeanOperationInfo object.
     * <li>Create ModelMBeanInfoSupport object with default descriptor. All
     * ModelMBeanXXXInfo except ModelMBeanOperationInfo are default.
     * <li>Create RequiredModelMBean object.
     * <li>Instance of class created in 1st step sets managed resource for
     * RequiredModelMBean using setManagedResource method.
     * <li>Create ObjectName object.
     * <li>Register RequiredModelMBean object in MBeanServer with above
     * ObjectName.
     * <li>Invoke operation method thru invoke method of MBeanServer.
     * <li>Verify value which the invoke method returned is the same as value
     * which the operation method returned.
     * <li>Verify that operation method was invoked.
     * <li>Verify value of field `value` in descriptor for
     * ModelMBeanOperationInfo object.
     * <li>Invoke again operation method thru invoke method of MBeanServer.
     * <li>Verify value which the invoke method returned is the same as value
     * which the operation method returned.
     * <li>Verify that operation method wasn't invoked.
     * <li>Verify value of field `value` in descriptor for
     * ModelMBeanOperationInfo object is correct.
     * </ul>
     */
    public Result testZero() throws Exception {
        Method method = class1.getDeclaredMethod("simpleMethod", null);
        ModelMBeanOperationInfo operationInfo1 = new ModelMBeanOperationInfo(
            "description", method);
        Descriptor descriptor = operationInfo1.getDescriptor();
        descriptor.setField("currencyTimeLimit", "0");
        operationInfo1.setDescriptor(descriptor);
        ModelMBeanInfoSupport beanInfoSupport = new ModelMBeanInfoSupport(
            class1.getName(), "description", null, null,
            new ModelMBeanOperationInfo[] { operationInfo1 }, null);
        RequiredModelMBean requiredModelMBean = new RequiredModelMBean(
            beanInfoSupport);
        requiredModelMBean.setManagedResource(this, "ObjectReference");
        ObjectName objectName = new ObjectName("domain", "name", "simple name");
        MBeanServer server = MBeanServerFactory.createMBeanServer();
        server.registerMBean(requiredModelMBean, objectName);
        Object value = server.invoke(objectName, method.getName(), null, null);
        assertEquals(value, returnedObject);
        assertTrue(isInvokedMethod());
        ModelMBeanOperationInfo operationInfo2 = (ModelMBeanOperationInfo)server
            .getMBeanInfo(objectName).getOperations()[0];
        assertTrue(operationInfo1 == operationInfo2);
        value = operationInfo2.getDescriptor().getFieldValue("value");
        assertEquals(value, returnedObject);

        value = server.invoke(objectName, method.getName(), null, null);
        assertEquals(value, returnedObject);
        assertFalse(isInvokedMethod());
        operationInfo2 = (ModelMBeanOperationInfo)server.getMBeanInfo(
            objectName).getOperations()[0];
        assertTrue(operationInfo1 == operationInfo2);
        value = operationInfo2.getDescriptor().getFieldValue("value");
        assertEquals(value, returnedObject);
        return result();
    }

    /**
     * Verify that invoke method throws exception if target operation method
     * throws exception.
     * <ul>
     * Step by step:
     * <li>Create operation method with one string parameter which always
     * throws an exception with message=parameter of this method.
     * <li>Create ModelMBeanOperationInfo object for operation method.
     * <li>Set value currencyTimeLimit = 0 in descriptor for
     * ModelMBeanOperationInfo object.
     * <li>Create ModelMBeanInfoSupport object with default descriptor. All
     * ModelMBeanXXXInfo except ModelMBeanOperationInfo are default.
     * <li>Instance of class created in 1st step sets managed resource for
     * RequiredModelMBean using setManagedResource method.
     * <li>Create ObjectName object.
     * <li>Register RequiredModelMBean object in MBeanServer with above
     * ObjectName.
     * <li>Invoke operation methodThrowException method thru invoke method of
     * MBeanServer with specific msg.
     * <li>Verify that MBeanException was thrown with nested exception which
     * has message which specified in previous step.
     * </ul>
     */
    public Result testException() throws Exception {
        Method method = class1.getMethod("methodThrowException",
            new Class[] { String.class });
        ModelMBeanOperationInfo operationInfo1 = new ModelMBeanOperationInfo(
            "description", method);
        Descriptor descriptor = operationInfo1.getDescriptor();
        descriptor.setField("currencyTimeLimit", "0");
        operationInfo1.setDescriptor(descriptor);
        ModelMBeanInfoSupport beanInfoSupport = new ModelMBeanInfoSupport(
            class1.getName(), "description", null, null,
            new ModelMBeanOperationInfo[] { operationInfo1 }, null);
        RequiredModelMBean requiredModelMBean = new RequiredModelMBean(
            beanInfoSupport);
        requiredModelMBean.setManagedResource(this, "ObjectReference");
        ObjectName objectName = new ObjectName("domain", "name", "simple name");
        MBeanServer server = MBeanServerFactory.createMBeanServer();
        server.registerMBean(requiredModelMBean, objectName);
        try {
            server.invoke(objectName, method.getName(),
                new Object[] { "message" }, new String[] { String.class
                    .getName() });
            assertTrue(false);
        } catch (MBeanException e) {
            assertEquals(e.getCause().getMessage(), "message");
        }
        assertTrue(isInvokedMethod());
        return result();
    }

    /**
     * Verify that invoke method never caches returned value of method if
     * currencyTimeLimit < 0.
     * <ul>
     * Step by step:
     * <li>Create operation method without parameters which always returns the
     * same value.
     * <li>Create ModelMBeanOperationInfo object for operation method.
     * <li>Set value currencyTimeLimit <0 in descriptor for
     * ModelMBeanOperationInfo object.
     * <li>Create ModelMBeanInfoSupport object with default descriptor. All
     * ModelMBeanXXXInfo except ModelMBeanOperationInfo are default.
     * <li>Create RequiredModelMBean object.
     * <li>Instance of class created in 1st step sets managed resource for
     * RequiredModelMBean using setManagedResource method.
     * <li>Create ObjectName object.
     * <li>Register RequiredModelMBean object in MBeanServer with above
     * ObjectName.
     * <li>Invoke operation method thru invoke method of MBeanServer.
     * <li>Verify value which the invoke method returned is the same as value
     * which the operation method returned.
     * <li>Verify that operation method was invoked.
     * <li>Invoke again operation method thru invoke method of MBeanServer.
     * <li>Verify value which the invoke method returned is the same as value
     * which the operation method returned.
     * <li>Verify that operation method was invoked.
     * </ul>
     */
    public Result testNegative() throws Exception {
        Method method = class1.getDeclaredMethod("simpleMethod", null);
        ModelMBeanOperationInfo operationInfo1 = new ModelMBeanOperationInfo(
            "description", method);
        Descriptor descriptor = operationInfo1.getDescriptor();
        descriptor.setField("currencyTimeLimit", "-1");
        operationInfo1.setDescriptor(descriptor);
        ModelMBeanInfoSupport beanInfoSupport = new ModelMBeanInfoSupport(
            class1.getName(), "description", null, null,
            new ModelMBeanOperationInfo[] { operationInfo1 }, null);
        RequiredModelMBean requiredModelMBean = new RequiredModelMBean(
            beanInfoSupport);
        requiredModelMBean.setManagedResource(this, "ObjectReference");
        ObjectName objectName = new ObjectName("domain", "name", "simple name");
        MBeanServer server = MBeanServerFactory.createMBeanServer();
        server.registerMBean(requiredModelMBean, objectName);
        Object value = server.invoke(objectName, method.getName(), null, null);
        assertEquals(value, returnedObject);
        assertTrue(isInvokedMethod());
        ModelMBeanOperationInfo operationInfo2 = (ModelMBeanOperationInfo)server
            .getMBeanInfo(objectName).getOperations()[0];
        assertTrue(operationInfo1 == operationInfo2);

        value = server.invoke(objectName, method.getName(), null, null);
        assertEquals(value, returnedObject);
        assertTrue(isInvokedMethod());
        return result();
    }

    /**
     * Verify that returned value of invoked method never retrieves value from
     * cache if currencyTimeLimit is not defended in descriptor of
     * ModelMBeanOperationInfo.
     * <p>
     * Instructions are the same as in testNegative.
     */
    public Result testNotPresent() throws Exception {
        Method method = class1.getDeclaredMethod("simpleMethod", null);
        ModelMBeanOperationInfo operationInfo1 = new ModelMBeanOperationInfo(
            "description", method);
        ModelMBeanInfoSupport beanInfoSupport = new ModelMBeanInfoSupport(
            class1.getName(), "description", null, null,
            new ModelMBeanOperationInfo[] { operationInfo1 }, null);
        RequiredModelMBean requiredModelMBean = new RequiredModelMBean(
            beanInfoSupport);
        requiredModelMBean.setManagedResource(this, "ObjectReference");
        ObjectName objectName = new ObjectName("domain", "name", "simple name");
        MBeanServer server = MBeanServerFactory.createMBeanServer();
        server.registerMBean(requiredModelMBean, objectName);
        Object value = server.invoke(objectName, method.getName(), null, null);
        assertEquals(value, returnedObject);
        assertTrue(isInvokedMethod());
        ModelMBeanOperationInfo operationInfo2 = (ModelMBeanOperationInfo)server
            .getMBeanInfo(objectName).getOperations()[0];
        assertTrue(operationInfo1 == operationInfo2);
        value = server.invoke(objectName, method.getName(), null, null);
        assertEquals(value, returnedObject);
        assertTrue(isInvokedMethod());
        return result();
    }

    /**
     * Verify that invoke method retrieves returned value of method from cache
     * or invoke operation depends on currencyTimeLimit > 0 and
     * lastUpdatedTimeStamp.
     * <ul>
     * Step by step:
     * <li>Create operation method without parameters which always returns
     * value.
     * <li>Create ModelMBeanOperationInfo object for operation method.
     * <li>Set value currencyTimeLimit = > 0 in descriptor for
     * ModelMBeanOperationInfo object.
     * <li>Create ModelMBeanInfoSupport object with default descriptor. All
     * ModelMBeanXXXInfo except ModelMBeanOperationInfo are default.
     * <li>Instance of class created in 1st step sets managed resource for
     * RequiredModelMBean using setManagedResource method.
     * <li>Create ObjectName object.
     * <li>Register RequiredModelMBean object in MBeanServer with above
     * ObjectName.
     * <li>Invoke operation method thru invoke method of MBeanServer.
     * <li>Verify value which the invoke method returned is the same as value
     * which the operation method returned.
     * <li>Verify that operation method was invoked.
     * <li>Verify value of field `value` in descriptor for
     * ModelMBeanOperationInfo object.
     * <li>Invoke again operation method thru invoke method of MBeanServer.
     * <li>Verify returned value is not changed.
     * <li>Verify that operation method wasn't invoked.
     * <li>Verify value of field `value` in descriptor for
     * ModelMBeanOperationInfo object is not changed.
     * <li>Change returned value of operation method.
     * <li> Wait currencyTimeLimit seconds.
     * <li>Invoke again operation method thru invoke method of MBeanServer.
     * <li>Verify value which the invoke method returned is the same as value
     * which the operation method returned.
     * <li>Verify that operation method was invoked.
     * <li>Verify value of field `value` in descriptor for
     * ModelMBeanOperationInfo object is changed and verify this value.
     * </ul>
     */
    public Result testPositive() throws Exception {
        Method method = class1.getMethod("simpleMethod", null);
        ModelMBeanOperationInfo operationInfo1 = new ModelMBeanOperationInfo(
            "description", method);
        Descriptor descriptor = operationInfo1.getDescriptor();
        int currencyTimeLimit = 5;
        descriptor.setField("currencyTimeLimit", currencyTimeLimit + "");
        operationInfo1.setDescriptor(descriptor);
        ModelMBeanInfoSupport beanInfoSupport = new ModelMBeanInfoSupport(
            class1.getName(), "description", null, null,
            new ModelMBeanOperationInfo[] { operationInfo1 }, null);
        RequiredModelMBean requiredModelMBean = new RequiredModelMBean(
            beanInfoSupport);
        requiredModelMBean.setManagedResource(this, "ObjectReference");
        ObjectName objectName = new ObjectName("domain", "name", "simple name");
        MBeanServer server = MBeanServerFactory.createMBeanServer();
        server.registerMBean(requiredModelMBean, objectName);
        Object value = server.invoke(objectName, method.getName(), null, null);
        assertEquals(value, returnedObject);
        assertTrue(isInvokedMethod());
        printValue(server.getMBeanInfo(objectName));
        value = server.invoke(objectName, method.getName(), null, null);
        assertEquals(value, returnedObject);
        assertFalse(isInvokedMethod());
        printValue(server.getMBeanInfo(objectName));
        returnedObject = new Integer(10);
        Thread.sleep(1000 * currencyTimeLimit + latancy);
        value = server.invoke(objectName, method.getName(), null, null);
        assertEquals(value, returnedObject);
        printValue(server.getMBeanInfo(objectName));
        assertTrue(isInvokedMethod());
        return result();
    }

    /**
     * Verify that invoke method invokes correctly method with parameter and
     * without it.
     * <ul>
     * Step by step:
     * <li>Create operation method without parameters which always returns the
     * same value.
     * <li>Create operation method with name as in 1 step with parameter which
     * always returns the parameter.
     * <li>Create 2 ModelMBeanOperationInfo object for both operation method.
     * <li>Create ModelMBeanInfoSupport object with default descriptor. All
     * ModelMBeanXXXInfo except ModelMBeanOperationInfo are default.
     * <li>Create RequiredModelMBean object.
     * <li>Instance of class created in 1st step sets managed resource for
     * RequiredModelMBean using setManagedResource method.
     * <li>Create ObjectName object.
     * <li>Register RequiredModelMBean object in MBeanServer with above
     * ObjectName.
     * <li>Invoke 1st operation method thru invoke method of MBeanServer:
     * server.invoke(objectName, methodName, null, null).
     * <li>Verify value which the invoke method returned is the same as value
     * which the 1st operation method returned.
     * <li>Invoke 2nd operation method thru invoke method of MBeanServer:
     * server.invoke(objectName, methodName, argument, signature).
     * <li>Verify value which the invoke method returned is the same as
     * argument.
     * </ul>
     */
    public Result testMethodWithParameter() throws Exception {
        Method method1 = class1.getDeclaredMethod("simpleMethod",
            new Class[] { int.class });
        ModelMBeanOperationInfo operationInfo1 = new ModelMBeanOperationInfo(
            "description", method1);
        Method method2 = class1.getMethod("simpleMethod", null);
        ModelMBeanOperationInfo operationInfo2 = new ModelMBeanOperationInfo(
            "description", method2);
        ModelMBeanInfoSupport beanInfoSupport = new ModelMBeanInfoSupport(
            class1.getName(), "description", null, null,
            new ModelMBeanOperationInfo[] { operationInfo1, operationInfo2 },
            null);
        RequiredModelMBean requiredModelMBean = new RequiredModelMBean(
            beanInfoSupport);
        requiredModelMBean.setManagedResource(this, "ObjectReference");
        ObjectName objectName = new ObjectName("domain", "name", "simple name");
        MBeanServer server = MBeanServerFactory.createMBeanServer();
        server.registerMBean(requiredModelMBean, objectName);
        Object value = server.invoke(objectName, method2.getName(), null, null);
        assertEquals(value, returnedObject);
        int integer = 8;
        value = server.invoke(objectName, method1.getName(),
            new Object[] { new Integer(integer) }, new String[] { method1
                .getParameterTypes()[0].getName() });
        assertEquals(value, new Integer(integer));
        return result();
    }

    private void printValue(MBeanInfo beanInfo) {
        ModelMBeanOperationInfo operationInfo2 = (ModelMBeanOperationInfo)beanInfo
            .getOperations()[0];
        assertEquals(returnedObject, operationInfo2.getDescriptor()
            .getFieldValue("value"));
    }

    /**
     * @return always return arg.
     */
    public int simpleMethod(int arg) {
        return arg;
    }

    /**
     * This method always returns <code>returnObject</code> object.
     */
    public Integer simpleMethod() {
        isInvoked = true;
        return returnedObject;
    }

    /**
     * This method always throws exception with specified msg message.
     */
    public void methodThrowException(String msg) throws Exception {
        isInvoked = true;
        throw new Exception(msg);
    }

    private boolean isInvokedMethod() {
        boolean tmp = isInvoked;
        isInvoked = false;
        return tmp;
    }
}