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
package org.apache.harmony.test.func.api.javax.management.monitor.share;

import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.monitor.MonitorNotification;

import org.apache.harmony.share.Base;

/**
 * Notification Listener verifies received value according to received data from
 * setParameters method. After handles of notification, it notifies object,
 * given in constructor. It also can return Boolean flag notification is correct
 * through getRes method
 */

public class NotifListener implements NotificationListener {

    /**
     * String, signalized that type verification should be missed.
     */
    public static final String UNKNOWN_TYPE = "unknown type";

    /** Allows notificationListener to use Counter increment for predictions */
    private boolean isPredict = false;

    /** Object, used to predict derived gauge */
    private Predictable objToPredict = null;

    /**
     * Expected observed Attribute (Sets in testcase and verifies in
     * notifications handler)
     */
    private String verifyedAttribute = null;

    /**
     * Expected observed ObjectName (Sets in testcase and verifies in
     * notifications handler)
     */
    private ObjectName verifyedObjectName = null;

    /**
     * Flag to skip verification
     */
    private boolean isSkip = false;

    /**
     * Expected notification type (Sets in testcase and verifies in
     * notifications handler)
     */
    private String verifyedType = null;

    /** Object for synchronization */
    private final Object sync;

    /** Test result */
    private boolean res = true;

    private MonitorNotification lastNotification;

    public NotifListener(Object objToSync) {
        this.sync = objToSync;
    }

    public void setPredictMode(boolean prps, Predictable pr) {
        if (prps) {
            this.objToPredict = pr;
            this.isPredict = true;
        } else {
            this.objToPredict = null;
            this.isPredict = false;
        }
    }

    public void setParameters(String attr, ObjectName objN, String type) {
        this.verifyedAttribute = attr;
        this.verifyedObjectName = objN;
        this.verifyedType = type;
    }

    public String getType() {
        return this.verifyedType;
    }

    public boolean getRes() {
        return this.res;
    }

    public String getDescription() {
        String dskr = "Number: " + lastNotification.getSequenceNumber()
                + "\nType: " + lastNotification.getType()
                + "\nObservedObject: " + lastNotification.getObservedObject()
                + "\nObservedAttribute: "
                + lastNotification.getObservedAttribute() + "\nDerivedGauge: "
                + lastNotification.getDerivedGauge();
        return dskr;
    }

    /** Skips the next notification */
    public void skipNext() {
        this.isSkip = true;
    }

    public void handleNotification(Notification notification, Object handback) {

        res = true;

        /* Skip verification */
        if (isSkip) {
            Base.log.add("NotificationListener: Notification skipped");
            isSkip = false;
            synchronized (this.sync) {
                this.sync.notify();
            }
            return;
        }

        Base.log.add("NotificationListener: Notification received");

        this.lastNotification = (MonitorNotification) notification;

        // System.out.println(notification.getType());
        // System.out.println(((MonitorNotification) notification)
        // .getObservedAttribute());
        // System.out.println(((MonitorNotification) notification)
        // .getObservedObject());
        // System.out.println(((MonitorNotification) notification)
        // .getDerivedGauge());

        if ((verifyedAttribute == null) | (verifyedObjectName == null)
                | (verifyedType == null)) {
            Base.log.info("NotificationListener: "
                    + "set all parameters to verify!");
            res = false;
            synchronized (sync) {
                sync.notify();
            }
            return;
        }

        /* Verifying */
        if (notification == null) {
            Base.log.info("FAIL: NotificationListener: notification is null");
            res = false;
        }

        if (!(notification instanceof MonitorNotification)) {
            Base.log.info("FAIL: NotificationListener: "
                    + "notification class is invalid");
            Base.log.info("javax.management.monitor."
                    + "MonitorNotification expected, "
                    + notification.getClass() + " received");
            res = false;
        }
        if (!("handback".equals(handback))) {
            Base.log.info("FAIL: NotificationListener: handback is invalid");
            res = false;
        }

        if (verifyedType.equals(UNKNOWN_TYPE)) {
            verifyedType = notification.getType();
            Base.log.info("NotificationListener: Unknown type proceeded ("
                    + verifyedType + ")");
        }

        if (!verifyedType.equals(notification.getType())) {
            Base.log.info(verifyedType + " expected, " + notification.getType()
                    + " received");
            res = false;
        }

        if (!((MonitorNotification) notification).getObservedObject().equals(
                verifyedObjectName)) {
            Base.log.info(verifyedObjectName + " expected, "
                    + ((MonitorNotification) notification).getObservedObject()
                    + " received");
            res = false;
        }

        if (!((MonitorNotification) notification).getObservedAttribute()
                .equals(verifyedAttribute)) {
            Base.log.info(verifyedAttribute
                    + " expected, "
                    + ((MonitorNotification) notification)
                            .getObservedAttribute() + " received");
            res = false;
        }

        if (isPredict
                && ((((MonitorNotification) notification).getDerivedGauge() == null) || (objToPredict
                        .getIncrement() == null))) {
            Base.log.info("NotificationListener: "
                    + "One of predicted values is null: notification="
                    + ((MonitorNotification) notification).getDerivedGauge()
                    + " counter=" + objToPredict.getIncrement());
            isPredict = false;
        }

        if (isPredict
                && !((MonitorNotification) notification).getDerivedGauge()
                        .equals(objToPredict.getIncrement())) {
            Base.log.info("FAIL: NotificationListener: " + "Predicted value ("
                    + objToPredict.getIncrement() + ") differs from received ("
                    + ((MonitorNotification) notification).getDerivedGauge()
                    + ")");
            res = false;
        }

        if (res) {
            Base.log.info("NotificationListener: Notification is correct");
        } else {
            Base.log.info("NotificationListener: Notification is incorrect");
        }

        /* notifying to test that notification is received */
        synchronized (this.sync) {
            this.sync.notify();
        }
    }
}
