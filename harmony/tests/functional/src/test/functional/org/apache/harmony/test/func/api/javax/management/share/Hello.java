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
package org.apache.harmony.test.func.api.javax.management.share;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.management.AttributeChangeNotification;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.NotificationListener;

import org.apache.harmony.test.func.api.javax.management.share.framework.Logger;

/**
 */
public class Hello extends NotificationBroadcasterSupport implements
    HelloMBean, NotificationListener {

    public static final String SAY_HELLO_INVOKED     = "say.hello.invoked";

    public static final String SAY_HELLO_INVOKED_MSG = "The method sayHello()"
                                                         + " invoked";

    /**
     * MBean name.
     */
    private String             name                  = "HelloMBean";

    /**
     * Attribute1
     */
    private String             attribute1            = "attribute1";

    /**
     * Attribute2
     */
    private String             attribute2            = "attribute2";

    /**
     * Attribute3
     */
    private String             attribute3            = "attribute3";

    /**
     * Attribute3
     */
    private boolean            attribute4;

    private Integer            number;

    private BigDecimal         number1;

    private BigDecimal         number2;

    private int                intNumber1;

    private int                intNumber2;

    private long               longNumber1;

    private long               longNumber2;

    private double             doubleNumber1;

    private double             doubleNumber2;

    private float              floatNumber1;

    private float              floatNumber2;

    private boolean            boolean1;

    private boolean            boolean2;

    /**
     * Logger.
     */
    private Logger             log                   = Logger.getLogger(this
                                                         .getClass());

    /**
     * AttributeChangeNotification sequence number.
     */
    private long               attrSequenceNumber;

    /**
     * Notification sequence number.
     */
    public long                sequenceNumber;

    /**
     * Time stamp.
     */
    public long                timeStamp;

    /**
     * User data.
     */
    public Object              userData              = "User Data.";

    /**
     * Indicates whether the handleNotification() method invoked.
     */
    public boolean             isHandleNotifInvoked  = false;

    /**
     * Operation parameter.
     */
    private Object             operationParam;

    /**
     * Notification.
     */
    public Notification        notification;

    /**
     * handback object.
     */
    public Object              handback;

    public int                 point;

    public Hello() {
    }

    public Hello(String attribute1, String attribute2, String attribute3,
        boolean attribute4, Object data) {
        super();
        this.attribute1 = attribute1;
        this.attribute2 = attribute2;
        this.attribute3 = attribute3;
        this.attribute4 = attribute4;
        userData = data;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.test.func.api.javax.management.HelloMBean#getName()
     */
    public String getName() {
        return name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.test.func.api.javax.management.HelloMBean#setName(java.lang.String)
     */
    public void setName(String name) {
        fire("Name", this.name, name);
        this.name = name;
    }

    private void fire(String arrtName, Object oldVal, Object newVal) {
        AttributeChangeNotification n = new AttributeChangeNotification(this,
            attrSequenceNumber++, System.currentTimeMillis(), arrtName
                + " changed", arrtName, "java.lang.String", oldVal, newVal);
        sendNotification(n);
    }

    /**
     * Notification info.
     */
    public MBeanNotificationInfo[] getNotificationInfo() {
        MBeanNotificationInfo info1 = new MBeanNotificationInfo(
            new String[] { AttributeChangeNotification.ATTRIBUTE_CHANGE },
            AttributeChangeNotification.class.getName(),
            "Attribute change notification");

        MBeanNotificationInfo info2 = new MBeanNotificationInfo(
            new String[] { SAY_HELLO_INVOKED }, Notification.class.getName(),
            "sayHello() invoked notification");

        return new MBeanNotificationInfo[] { info1, info2 };
    }

    /**
     * Handle notification.
     */
    protected void handleNotification(NotificationListener listener,
        Notification notif, Object handback) {
        isHandleNotifInvoked = true;
        super.handleNotification(listener, notif, handback);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.test.func.api.javax.management.HelloMBean#sayHello()
     */
    public void sayHello() {
        log.info("Hello");
        sequenceNumber++;
        Notification n = new Notification(SAY_HELLO_INVOKED, this,
            sequenceNumber, timeStamp = System.currentTimeMillis(),
            SAY_HELLO_INVOKED_MSG);
        n.setUserData(userData);
        sendNotification(n);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.test.func.api.javax.management.HelloMBean#getAttribute1()
     */
    public String getAttribute1() {
        return attribute1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.test.func.api.javax.management.HelloMBean#setAttribute1(java.lang.String)
     */
    public void setAttribute1(String attribute1) {
        fire("Attribute1", this.attribute1, attribute1);
        this.attribute1 = attribute1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.test.func.api.javax.management.HelloMBean#getAttribute2()
     */
    public String getAttribute2() {
        return attribute2;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.test.func.api.javax.management.HelloMBean#setAttribute2(java.lang.String)
     */
    public void setAttribute2(String attribute2) {
        fire("Attribute2", this.attribute2, attribute2);
        this.attribute2 = attribute2;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.test.func.api.javax.management.HelloMBean#getAttribute3()
     */
    public String getAttribute3() {
        return attribute3;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.test.func.api.javax.management.HelloMBean#isAttribute4()
     */
    public boolean isAttribute4() {
        return attribute4;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.test.func.api.javax.management.HelloMBean#operation(java.lang.Object)
     */
    public Object operation(Object operationParam) {
        Object o = this.operationParam;
        this.operationParam = operationParam;
        return o;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public BigDecimal getNumber1() {
        return number1;
    }

    public void setNumber1(BigDecimal number1) {
        this.number1 = number1;
    }

    public BigDecimal getNumber2() {
        return number2;
    }

    public void setNumber2(BigDecimal number2) {
        this.number2 = number2;
    }

    public int getIntNumber1() {
        return intNumber1;
    }

    public void setIntNumber1(int intNumber1) {
        this.intNumber1 = intNumber1;
    }

    public int getIntNumber2() {
        return intNumber2;
    }

    public void setIntNumber2(int intNumber2) {
        this.intNumber2 = intNumber2;
    }

    public long getLongNumber1() {
        return longNumber1;
    }

    public void setLongNumber1(long longNumber1) {
        this.longNumber1 = longNumber1;
    }

    public long getLongNumber2() {
        return longNumber2;
    }

    public void setLongNumber2(long longNumber2) {
        this.longNumber2 = longNumber2;
    }

    public double getDoubleNumber1() {
        return doubleNumber1;
    }

    public void setDoubleNumber1(double doubleNumber1) {
        this.doubleNumber1 = doubleNumber1;
    }

    public double getDoubleNumber2() {
        return doubleNumber2;
    }

    public void setDoubleNumber2(double doubleNumber2) {
        this.doubleNumber2 = doubleNumber2;
    }

    public float getFloatNumber1() {
        return floatNumber1;
    }

    public void setFloatNumber1(float floatNumber1) {
        this.floatNumber1 = floatNumber1;
    }

    public float getFloatNumber2() {
        return floatNumber2;
    }

    public void setFloatNumber2(float floatNumber2) {
        this.floatNumber2 = floatNumber2;
    }

    public boolean isBoolean1() {
        return boolean1;
    }

    public void setBoolean1(boolean boolean1) {
        this.boolean1 = boolean1;
    }

    public boolean isBoolean2() {
        return boolean2;
    }

    public void setBoolean2(boolean boolean2) {
        this.boolean2 = boolean2;
    }

    public Number getNumberSubcl() {
        Number[] num = new Number[] { new BigDecimal("1"), new BigInteger("1"),
            new Byte("1"), new Double(1), new Float(1), new Integer(1),
            new Long(1), new Short("1") };
        return point > num.length ? null : num[point];
    } /*
         * (non-Javadoc)
         * 
         * @see javax.management.NotificationListener#handleNotification(javax.management.Notification,
         *      java.lang.Object)
         */

    public void handleNotification(Notification notification, Object handback) {
        this.notification = notification;
        this.handback = handback;
    }
}
