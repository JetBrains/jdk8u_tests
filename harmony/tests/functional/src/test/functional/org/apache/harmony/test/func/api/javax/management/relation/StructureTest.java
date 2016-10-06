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
package org.apache.harmony.test.func.api.javax.management.relation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.Notification;
import javax.management.NotificationFilterSupport;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.relation.InvalidRelationIdException;
import javax.management.relation.InvalidRelationServiceException;
import javax.management.relation.InvalidRoleValueException;
import javax.management.relation.RelationNotFoundException;
import javax.management.relation.RelationNotification;
import javax.management.relation.RelationService;
import javax.management.relation.RelationServiceNotRegisteredException;
import javax.management.relation.RelationTypeNotFoundException;
import javax.management.relation.RelationTypeSupport;
import javax.management.relation.Role;
import javax.management.relation.RoleInfo;
import javax.management.relation.RoleList;
import javax.management.relation.RoleNotFoundException;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 * Purpose: Verify Relation Pattern, Check Quantitative and Qualitative Changes
 * Are Valid, Check Relation Pattern Consistency.
 * <p>
 * <br>
 * Under test:RelationService; RelationSupport; RelationTypeSupport; Role;
 * RoleInfo; RoleList;
 * <ul>
 * <br>
 * <br>
 * testNotifications() <br>
 * Step by step for verification that Notification are sent in all common cases:
 * <ul>
 * <li>a. Create external and internal relations. Check that notifications are
 * sent and correct.
 * <li>b. Update relations. Check that notifications are sent and correct.
 * <li>c. Remove relations.Check that notifications are sent and correct.
 * </ul>
 * 
 * testSendNotifications<br>
 * Step by step for verification that Notification are sent in all common cases:
 * <ul>
 * <li>a. Create relation. After creation we can send notification
 * <li>b. Send creation, updating and removal notifications. Check that
 * Notifications are correct.
 * <li>c. After removal notification send creation notification.
 * <li>d. Try to send incorrect notifications.
 * </ul>
 * 
 * testRemovalNotification<br>
 * This testcase verifies MBeanToUnregister field in notifications.<br>
 * <br>
 * 
 * testRemoveRelationType<br>
 * Step by step for verification that relation is removed as soon as its
 * relation type is removed.
 * <ul>
 * <li>a. Create relation
 * <li>b. Remove relation type
 * <li>c. Check that relation isn't present in relation service
 * </ul>
 * 
 * testIsActive<br>
 * This testCase verifies IsActive method.<br>
 * <br>
 * 
 * testTheSameRelationType<br>
 * Test creates 2 relation types with equals names and RoleInfos, but referred
 * to different relation services. Test checks that when first relation type
 * creates relation, second relation type is not affected <br>
 * <br>
 * 
 * testRelationServiseConsistency<br>
 * Test Checks That:
 * <ul>
 * <li>Relation Service Consistency When Referenced MBeans Are Unregistered
 * From MBean Server;
 * <li>Influence The Purge Flag To The Relation Consistency Support;
 * </ul>
 * 
 * testMBeansDoNotHaveTo<br>
 * This test verifies that MBeans don't have to be registered before creation of
 * role. <br>
 * <br>
 * 
 * testMBeansMap<br>
 * This test verifies getReferencedMBeans and findAssociatedMBeans methods. <br>
 * <br>
 * 
 * testPurgeNotifications<br>
 * Test verifies that notifications are sent when relation service updates and
 * removes relations by itself <br>
 * <br>
 * 
 * testManyNames<br>
 * This test tries change relation service via register/unregister MBean. <br>
 * <br>
 * 
 * testSelfUnregistration<br>
 * In this test External relation unregistered MBeans by itself. After this
 * verified relation service consistency. <br>
 * <br>
 * 
 * testChangeMBeanObject<br>
 * This test tries to change MBean object via register/unregister. <br>
 * <br>
 * 
 * testRelationIdMap<br>
 * This test verifies findReferencingRelations method <br>
 * <br>
 * 
 * 
 * Some description, if necessary.
 * 
 */

public class StructureTest extends MultiCase implements NotificationListener {

    /**
     * Class Path For Unit MBeans
     */
    private final String classPath = Unit.class.getName();

    /**
     * Object For Check Notification Correctness
     */
    private RelationNotificationForCompare relationNotification;

    /**
     * Flag signals that notification is incorrect (used for pass/fail criteria)
     */
    private boolean incorrectNotificationFlag = false;

    /**
     * Flag signals that notifications can be verified (used for code
     * compression - RelationNotificationForCompare objects created and
     * notification verified only in tested area)
     */
    private boolean checkNotifications = true;

    /**
     * handleNotification method compares sent notification with explain.
     * Explain notification creates as RelationNotificationForCompareObject
     * directly in source code
     */
    public void handleNotification(Notification notification, Object handback) {
        if (checkNotifications
                && !relationNotification
                        .equals((RelationNotification) notification)) {
            log.info("Notification is incorrect: "
                    + relationNotification.getDestinction() + " ("
                    + notification.getMessage() + ")");
            incorrectNotificationFlag = true;
        }
    }

    /**
     * Step by step for verification that Notification are sent in all common
     * cases:
     * <ul>
     * <li>a. Create external and internal relations. Check that notifications
     * are sent and correct.
     * <li>b. Update relations. Check that notifications are sent and correct.
     * <li>c. Remove relations.Check that notifications are sent and correct.
     * </ul>
     */

    public Result testNotifications() throws Exception {

        incorrectNotificationFlag = false;

        MBeanServer mBeanServer = MBeanServerFactory.createMBeanServer();

        RelationService relationService = new RelationService(true);
        ObjectName relationServiceName = new ObjectName(
                "mBeanServer:type=RelationService,name=rs");
        mBeanServer.registerMBean(relationService, relationServiceName);

        NotificationFilterSupport filter = new NotificationFilterSupport();
        filter.enableType("jmx.relation");
        relationService.addNotificationListener(this, filter, null);
        checkNotifications = true;

        RoleInfo[] roleInfo = new RoleInfo[1];
        roleInfo[0] = new RoleInfo("unitRole", classPath, true, true, 1, 2,
                "Unit role");

        // Creating an Internal Relation Type
        relationService.createRelationType("internalRelationType", roleInfo);

        // Creating an External Relation Type
        ERelationType externalRelationType = new ERelationType(
                "externalRelationType", roleInfo);
        relationService.addRelationType(externalRelationType);

        // 2 MBean objects to be associate
        UnitMBean unit1 = new Unit();
        ObjectName unit1Name = new ObjectName(
                "mBeanServer:type=Unit,name=Unit1");
        mBeanServer.registerMBean(unit1, unit1Name);

        UnitMBean unit2 = new Unit();
        ObjectName unit2Name = new ObjectName(
                "mBeanServer:type=Unit,name=Unit2");
        mBeanServer.registerMBean(unit2, unit2Name);

        ArrayList unitList = new ArrayList();
        unitList.add(unit1Name);
        unitList.add(unit2Name);

        Role unitRole = new Role("unitRole", unitList);
        RoleList unitRoles = new RoleList();
        unitRoles.add(unitRole);

        // Now Create relations between unit MBeans

        // Notification Data
        relationNotification = new RelationNotificationForCompare(
                "jmx.relation.creation.basic", relationService, 1, 10,
                "Creation of relation internalRelation", "internalRelation",
                "internalRelationType", null, null);

        relationService.createRelation("internalRelation",
                "internalRelationType", unitRoles);

        ERelation externalRelation = new ERelation("externalRelation",
                relationServiceName, mBeanServer, externalRelationType
                        .getRelationTypeName(), unitRoles);
        mBeanServer.registerMBean(externalRelation, externalRelation
                .getRelationON());

        // Notification Data
        relationNotification = new RelationNotificationForCompare(
                "jmx.relation.creation.mbean", relationService, 2, 10,
                "Creation of relation externalRelation", "externalRelation",
                "externalRelationType", externalRelation.getRelationON(), null);

        relationService.addRelation(externalRelation.getRelationON());

        // Update Relations: Remove one unit.

        // Change unitRole
        ArrayList oldUnitList = (ArrayList) unitList.clone();
        unitList.remove(0);
        unitRole.setRoleValue(unitList);

        // Notification Data
        relationNotification = new RelationNotificationForCompare(
                "jmx.relation.update.mbean", relationService, 3, 10,
                "Value of role unitRole has changed\n"
                        + "Old value:\nmBeanServer:type=Unit,name=Unit1"
                        + "\nmBeanServer:type=Unit,name=Unit2\n"
                        + "New value:\nmBeanServer:type=Unit,name=Unit1",
                "externalRelation", "externalRelationType", externalRelation
                        .getRelationON(), "unitRole", unitList, oldUnitList);

        externalRelation.setRole(unitRole);

        // Notification Data
        relationNotification = new RelationNotificationForCompare(
                "jmx.relation.update.basic", relationService, 4, 10,
                "Value of role unitRole has changed\n"
                        + "Old value:\nmBeanServer:type=Unit,name=Unit1\n"
                        + "New value:\nmBeanServer:type=Unit,name=Unit1\n"
                        + "mBeanServer:type=Unit,name=Unit2",
                "internalRelation", "internalRelationType", null, "unitRole",
                unitList, oldUnitList);

        // Updating
        relationService.setRole("internalRelation", unitRole);

        // Remove Relations

        // Notification Data
        relationNotification = new RelationNotificationForCompare(
                "jmx.relation.removal.basic", relationService, 5, 10,
                "Removal of relation internalRelation", "internalRelation",
                "internalRelationType", null, new ArrayList());

        relationService.removeRelation("internalRelation");

        // Expected Notification Data
        relationNotification = new RelationNotificationForCompare(
                "jmx.relation.removal.mbean", relationService, 6, 10,
                "Removal of relation externalRelation", "externalRelation",
                "externalRelationType", externalRelation.getRelationON(),
                new ArrayList());

        try {
            mBeanServer.unregisterMBean(externalRelation.getRelationON());
        } catch (Throwable t) {
            return failed("Unhanded Exception: " + t);
        }

        if (incorrectNotificationFlag) {
            return failed("IncorrectNotifications (see log for more information)");
        }
        return passed();

    }

    /**
     * Step by step for verification that Notification are sent in all common
     * cases:
     * <ul>
     * <li>a. Create relation. After creation we can send notification
     * <li>b. Send creation, updating and removal notifications. Check that
     * Notifications are correct.
     * <li>c. After removal notification send creation notification.
     * <li>d. Try to send incorrect notifications.
     * </ul>
     */

    public Result testSendNotifications() throws Exception {

        incorrectNotificationFlag = false;

        MBeanServer mBeanServer = MBeanServerFactory.createMBeanServer();

        RelationService relationService = new RelationService(true);
        ObjectName relationServiceName = new ObjectName(
                "mBeanServer:type=RelationService,name=rs");
        mBeanServer.registerMBean(relationService, relationServiceName);

        NotificationFilterSupport filter = new NotificationFilterSupport();
        filter.enableType("jmx.relation");
        relationService.addNotificationListener(this, filter, null);
        checkNotifications = false;

        RoleInfo[] roleInfo = new RoleInfo[1];
        roleInfo[0] = new RoleInfo("unitRole", classPath, true, true, 1, 2,
                "Unit role");

        relationService.createRelationType("internalRelationType", roleInfo);

        UnitMBean unit1 = new Unit();
        ObjectName unit1Name = new ObjectName(
                "mBeanServer:type=Unit,name=Unit1");
        mBeanServer.registerMBean(unit1, unit1Name);

        ArrayList unitList = new ArrayList();
        unitList.add(unit1Name);

        Role unitRole = new Role("unitRole", unitList);
        RoleList unitRoles = new RoleList();
        unitRoles.add(unitRole);

        relationService.createRelation("internalRelation",
                "internalRelationType", unitRoles);

        /*
         * Now Relation is created and we send removal, updating and creation
         * notifications
         */

        checkNotifications = true;

        // Removal notification
        relationNotification = new RelationNotificationForCompare(
                "jmx.relation.removal.basic", relationService, 2, 10,
                "Removal of relation internalRelation", "internalRelation",
                "internalRelationType", null, null);
        relationService.sendRelationRemovalNotification("internalRelation",
                null);

        // Updating Notification
        ArrayList oldUnitList = (ArrayList) unitList.clone();

        relationNotification = new RelationNotificationForCompare(
                "jmx.relation.update.basic", relationService, 3, 10,
                "Value of role unitRole has changed\n"
                        + "Old value:\nmBeanServer:type=Unit,name=Unit1\n"
                        + "New value:\nmBeanServer:type=Unit,name=Unit1\n"
                        + "mBeanServer:type=Unit,name=Unit2",
                "internalRelation", "internalRelationType", null, "unitRole",
                unitList, oldUnitList);

        relationService.sendRoleUpdateNotification("internalRelation",
                unitRole, unitRole.getRoleValue());

        // Creating Notification
        relationNotification = new RelationNotificationForCompare(
                "jmx.relation.creation.basic", relationService, 4, 10,
                "Creation of relation internalRelation", "internalRelation",
                "internalRelationType", null, null);

        relationService.sendRelationCreationNotification("internalRelation");

        /*
         * Now we send Incorrect Notifications. Any sent notification differs
         * from current RelationNotificationForCompare object - so any sent
         * notification caused test to fail
         */

        try {
            relationService.sendRelationRemovalNotification(null, null);
        } catch (IllegalArgumentException e) {
            /*Correct state*/
        }

        try {
            relationService.sendRelationRemovalNotification("wrongRelation",
                    null);
        } catch (RelationNotFoundException e) {
            /*Correct state*/
        }

        try {
            relationService.sendRoleUpdateNotification(null, unitRole, unitRole
                    .getRoleValue());
        } catch (IllegalArgumentException e) {
            /*Correct state*/
        }

        try {
            relationService.sendRoleUpdateNotification("internalRelation",
                    null, unitRole.getRoleValue());
        } catch (IllegalArgumentException e) {
            /*Correct state*/
        }

        try {
            relationService.sendRoleUpdateNotification("internalRelation",
                    unitRole, null);
        } catch (IllegalArgumentException e) {
            /*Correct state*/
        }

        try {
            relationService.sendRoleUpdateNotification("wrongRelation",
                    unitRole, unitRole.getRoleValue());
        } catch (RelationNotFoundException e) {
            /*Correct state*/
        }

        try {
            relationService.sendRelationCreationNotification(null);
        } catch (IllegalArgumentException e) {
            /*Correct state*/
        }

        try {
            relationService.sendRelationCreationNotification("wrongRelation");
        } catch (RelationNotFoundException e) {
            /*Correct state*/
        }

        if (incorrectNotificationFlag) {
            return failed("IncorrectNotifications (see log for more information)");
        }
        return passed();
    }

    /**
     * This testcase verifies MBeanToUnregister field in notifications.
     */

    public Result testRemovalNotification() throws Exception {

        incorrectNotificationFlag = false;

        MBeanServer mBeanServer = MBeanServerFactory.createMBeanServer();

        RelationService relationService = new RelationService(true);
        ObjectName relationServiceName = new ObjectName(
                "mBeanServer:type=RelationService,name=rs");
        mBeanServer.registerMBean(relationService, relationServiceName);

        NotificationFilterSupport filter = new NotificationFilterSupport();
        filter.enableType("jmx.relation");
        relationService.addNotificationListener(this, filter, null);
        checkNotifications = true;

        RoleInfo[] roleInfo = new RoleInfo[1];
        roleInfo[0] = new RoleInfo("unitRole", classPath, true, true, 1, 2,
                "Unit role");

        // Creating an Internal Relation Type
        relationService.createRelationType("internalRelationType", roleInfo);

        UnitMBean unit = new Unit();
        ObjectName unitName = new ObjectName("mBeanServer:type=Unit,name=Unit");
        mBeanServer.registerMBean(unit, unitName);

        ArrayList unitList = new ArrayList();
        unitList.add(unitName);

        Role unitRole = new Role("unitRole", unitList);
        RoleList unitRoles = new RoleList();
        unitRoles.add(unitRole);

        // Now Create relations between unit MBeans

        // Notification Data
        relationNotification = new RelationNotificationForCompare(
                "jmx.relation.creation.basic", relationService, 1, 10,
                "Creation of relation internalRelation", "internalRelation",
                "internalRelationType", null, null);

        relationService.createRelation("internalRelation",
                "internalRelationType", unitRoles);

        // Notification Data
        relationNotification = new RelationNotificationForCompare(
                "jmx.relation.removal.basic", relationService, 2, 10,
                "Removal of relation internalRelation", "internalRelation",
                "internalRelationType", null, new ArrayList());

        relationService.removeRelation("internalRelation");

        if (incorrectNotificationFlag) {
            return failed("IncorrectNotifications (see log for more information)");
        }
        return passed();
    }

    /**
     * Step by step for verification that relation is removed as soon as its
     * relation type is removed.
     * <ul>
     * <li>a. Create relation
     * <li>b. Remove relation type
     * <li>c. Check that relation isn't present in relation service
     * </ul>
     */
    public Result testRemoveRelationType() throws Exception {

        MBeanServer mBeanServer = MBeanServerFactory.createMBeanServer();

        RelationService relationService = new RelationService(true);
        ObjectName relationServiceName = new ObjectName(
                "mBeanServer:type=RelationService,name=rs");
        mBeanServer.registerMBean(relationService, relationServiceName);

        RoleInfo[] roleInfo = new RoleInfo[1];
        roleInfo[0] = new RoleInfo("unitRole", classPath, true, true, 1, 2,
                "Unit role");

        /* Creating an Internal Relation Type */
        relationService.createRelationType("internalRelationType", roleInfo);

        /* 2 MBean objects to be associate */
        UnitMBean unit1 = new Unit();
        ObjectName unit1Name = new ObjectName(
                "mBeanServer:type=Unit,name=Unit1");
        mBeanServer.registerMBean(unit1, unit1Name);

        /* Create Role */
        ArrayList unitList = new ArrayList();
        unitList.add(unit1Name);

        Role unitRole = new Role("unitRole", unitList);
        RoleList unitRoles = new RoleList();
        unitRoles.add(unitRole);

        /* Now Create relations between unit MBeans */
        relationService.createRelation("internalRelation",
                "internalRelationType", unitRoles);

        ERelation externalRelation = new ERelation("externalRelation",
                relationServiceName, mBeanServer, "internalRelationType",
                unitRoles);
        mBeanServer.registerMBean(externalRelation, externalRelation
                .getRelationON());

        /* Relations created. Now Remove Relation type */
        relationService.removeRelationType("internalRelationType");

        /* Check relations are removed */
        if (relationService.hasRelation("internalRelation").booleanValue()
                && relationService.hasRelation("externalRelation")
                        .booleanValue()) {
            return failed("Relation type had been removed, but relation is not");
        }

        return passed();
    }

    /**
     * This testCase verifies IsActive method.
     */
    public Result testIsActive() throws Exception {

        MBeanServer mBeanServer = MBeanServerFactory.createMBeanServer();

        RelationService relationService = new RelationService(true);
        ObjectName relationServiceName = new ObjectName(
                "mBeanServer:type=RelationService,name=rs");

        mBeanServer.registerMBean(relationService, relationServiceName);
        mBeanServer.unregisterMBean(relationServiceName);

        try {
            relationService.isActive();
            return failed("isActive method incorrect");
        } catch (RelationServiceNotRegisteredException e) {
            return passed();
        }
    }

    /**
     * Test creates 2 relation types with equals names and RoleInfos, but
     * referred to different relation services. Test checks that when first
     * relation type creates relation, second relation type is not affected
     */
    public Result testTheSameRelationType() throws Exception {

        MBeanServer mBeanServer = MBeanServerFactory.createMBeanServer();

        RelationService firstRelationService = new RelationService(true);
        RelationService secondRelationService = new RelationService(true);

        ObjectName firstRelationServiceName = new ObjectName(
                "mBeanServer:type=RelationService,name=first");
        ObjectName secondRelationServiceName = new ObjectName(
                "mBeanServer:type=RelationService,name=second");

        mBeanServer.registerMBean(firstRelationService,
                firstRelationServiceName);
        mBeanServer.registerMBean(secondRelationService,
                secondRelationServiceName);

        RoleInfo roleInfo = new RoleInfo("usualRole", classPath, true, true, 1,
                1, "usual role");

        ERelationType eRelationType = new ERelationType("relationType",
                roleInfo);

        firstRelationService.addRelationType(eRelationType);
        secondRelationService.addRelationType(eRelationType);

        UnitMBean unit1 = new Unit();

        ObjectName unit1Name = new ObjectName(
                "mBeanServer:type=Unit,name=Unit1");
        mBeanServer.registerMBean(unit1, unit1Name);

        ArrayList unitList = new ArrayList();
        unitList.add(unit1Name);

        Role singleUnitRole = new Role("usualRole", unitList);

        RoleList singleunitRoles = new RoleList();
        singleunitRoles.add(singleUnitRole);

        firstRelationService.createRelation("relation", "relationType",
                singleunitRoles);

        if (secondRelationService.hasRelation("relation").booleanValue()) {
            return failed("RelationType Mismatch");
        }
        return passed();
    }

    /**
     * Test Checks That:
     * <ul>
     * <li>Relation Service Consistency When Referenced MBeans Are Unregistered
     * From MBean Server;
     * <li>Influence The Purge Flag To The Relation Consistency Support;
     * </ul>
     */
    public Result testRelationServiseConsistency() throws Exception {

        // Create MBean Server
        MBeanServer mBeanServer = MBeanServerFactory.createMBeanServer();

        // Register Relation Service
        RelationService relationService = new RelationService(true);
        ObjectName relationServiceName = new ObjectName(
                "mBeanServer:type=RelationService,name=rs");
        mBeanServer.registerMBean(relationService, relationServiceName);

        // Create Role Information Array
        RoleInfo[] roleInfo = new RoleInfo[1];
        roleInfo[0] = new RoleInfo("unitRole", classPath, true, true, 1, 2,
                "Unit role");

        // Creating an Internal Relation Type
        relationService.createRelationType("internalRelationType", roleInfo);

        // Creating an External Relation Type
        RelationTypeSupport externalRelationType = new RelationTypeSupport(
                "externalRelationType", roleInfo);
        relationService.addRelationType(externalRelationType);

        // Registering 2 MBean objects
        UnitMBean unit1 = new Unit();

        ObjectName unit2Name = new ObjectName(
                "mBeanServer:type=Unit,name=Unit2");
        mBeanServer.registerMBean(unit1, unit2Name);

        ObjectName unit1Name = new ObjectName(
                "mBeanServer:type=Unit,name=Unit1");
        mBeanServer.registerMBean(unit1, unit1Name);

        // Create RoleLists for single unit and double unit relations
        ArrayList unitList = new ArrayList();

        unitList.add(unit1Name);
        Role singleUnitRole = new Role("unitRole", unitList);

        unitList.add(unit2Name);
        Role doubleUnitRole = new Role("unitRole", unitList);

        RoleList singleunitRoles = new RoleList();
        singleunitRoles.add(singleUnitRole);

        RoleList doubleunitRoles = new RoleList();
        doubleunitRoles.add(doubleUnitRole);

        // Create 2 Internal Relations
        relationService.createRelation("internalRelationSingle",
                "internalRelationType", singleunitRoles);
        relationService.createRelation("internalRelationDouble",
                "internalRelationType", doubleunitRoles);

        mBeanServer.unregisterMBean(unit1Name);

        if (relationService.hasRelation("internalRelationSingle")
                .booleanValue()
                & relationService.hasRelation("externalRelationSingle")
                        .booleanValue()) {
            return failed("Relation Is Not Deleted: Purge Flag=true; "
                    + "Referenced MBeans Are Absent");
        }

        if (!relationService.hasRelation("internalRelationDouble")
                .booleanValue()
                & !relationService.hasRelation("internalRelationDouble")
                        .booleanValue()) {
            return failed("Relation Is Deleted: Purge Flag=true; "
                    + "Referenced MBeans Are Present");
        }

        relationService.setPurgeFlag(false);

        mBeanServer.unregisterMBean(unit2Name);

        if (!relationService.hasRelation("internalRelationDouble")
                .booleanValue()
                & !relationService.hasRelation("internalRelationDouble")
                        .booleanValue()) {
            return failed("Relation Is Deleted: Purge Flag=false; "
                    + "Referenced MBeans Are Absent, "
                    + "PurgeRelations Method hadn't been Called");
        }

        relationService.purgeRelations();

        if (relationService.hasRelation("internalRelationDouble")
                .booleanValue()
                & relationService.hasRelation("internalRelationDouble")
                        .booleanValue()) {
            return failed("Relation Is Not Deleted: Purge Flag=false; "
                    + "Referenced MBeans Are Absent, "
                    + "PurgeRelations Method had been Called");
        }

        return passed();
    }

    /**
     * This test verifies that MBeans don't have to be registered before
     * creation of role.
     */

    public Result testMBeansDoNotHaveTo() throws Exception {

        MBeanServer mBeanServer = MBeanServerFactory.createMBeanServer();

        // Register Relation Service
        RelationService relationService = new RelationService(true);
        ObjectName relationServiceName = new ObjectName(
                "mBeanServer:type=RelationService,name=rs");
        mBeanServer.registerMBean(relationService, relationServiceName);

        // Create Role Information Array
        RoleInfo[] roleInfo = new RoleInfo[1];
        roleInfo[0] = new RoleInfo("unitRole", classPath, true, true, 1, 2,
                "Unit role");

        // Creating an Internal Relation Type
        relationService.createRelationType("internalRelationType", roleInfo);

        ObjectName unit1Name = new ObjectName(
                "mBeanServer:type=Unit,name=Unit1");

        // Create RoleLists for single unit and double unit relations
        ArrayList unitList = new ArrayList();
        unitList.add(unit1Name);
        Role unitRole = new Role("unitRole", unitList);

        return !unitRole.getRoleValue().contains(unit1Name) ? failed("")
                : passed();
    }

    /**
     * This test verifies getReferencedMBeans and findAssociatedMBeans methods.
     */

    public Result testMBeansMap() throws Exception {

        MBeanServer mBeanServer = MBeanServerFactory.createMBeanServer();

        // Register Relation Service
        RelationService relationService = new RelationService(true);
        ObjectName relationServiceName = new ObjectName(
                "mBeanServer:type=RelationService,name=rs");
        mBeanServer.registerMBean(relationService, relationServiceName);

        // Create Role Information Array
        RoleInfo[] roleInfo = new RoleInfo[1];
        roleInfo[0] = new RoleInfo("unitRole", classPath, true, true, 1, 2,
                "Unit role");

        // Creating an Internal Relation Type
        relationService.createRelationType("internalRelationType", roleInfo);

        ERelationType externalRelationType = new ERelationType(
                "externalRelationType", roleInfo);
        relationService.addRelationType(externalRelationType);

        // Registering 2 MBean objects
        UnitMBean unit1 = new Unit();

        ObjectName unit2Name = new ObjectName(
                "mBeanServer:type=Unit,name=Unit2");
        mBeanServer.registerMBean(unit1, unit2Name);

        ObjectName unit1Name = new ObjectName(
                "mBeanServer:type=Unit,name=Unit1");
        mBeanServer.registerMBean(unit1, unit1Name);

        // Create RoleLists for single unit and double unit relations
        ArrayList unitList = new ArrayList();

        unitList.add(unit1Name);
        Role singleUnitRole = new Role("unitRole", unitList);

        unitList.add(unit2Name);
        Role doubleUnitRole = new Role("unitRole", unitList);

        RoleList singleunitRoles = new RoleList();
        singleunitRoles.add(singleUnitRole);

        RoleList doubleunitRoles = new RoleList();
        doubleunitRoles.add(doubleUnitRole);

        // Create 2 Internal Relations
        relationService.createRelation("internalRelationSingle",
                "internalRelationType", singleunitRoles);
        relationService.createRelation("internalRelationDouble",
                "internalRelationType", doubleunitRoles);
        ERelation externalRelation = new ERelation("externalRelation",
                relationServiceName, mBeanServer, "externalRelationType",
                singleunitRoles);
        mBeanServer.registerMBean(externalRelation, externalRelation
                .getRelationON());
        relationService.addRelation(externalRelation.getRelationON());

        if ((!relationService.findAssociatedMBeans(unit2Name,
                "internalRelationType", "unitRole").containsKey(unit1Name))
                || (!relationService.findAssociatedMBeans(unit1Name,
                        "externalRelationType", "unitRole").isEmpty())
                || (!relationService.getReferencedMBeans(
                        "internalRelationDouble").containsKey(unit2Name))
                || (!relationService.getReferencedMBeans(
                        "internalRelationDouble").containsKey(unit1Name))
                || (relationService
                        .getReferencedMBeans("internalRelationSingle")
                        .containsKey(unit2Name))) {
            if (!relationService.findAssociatedMBeans(unit2Name,
                    "internalRelationType", "unitRole").containsKey(unit1Name)) {
                log.info(relationService.findAssociatedMBeans(unit2Name,
                        "internalRelationType", "unitRole").toString()
                        + " - must contain unit1Name");
            }
            if (!relationService.findAssociatedMBeans(unit1Name,
                    "externalRelationType", "unitRole").isEmpty()) {
                log.info(relationService.findAssociatedMBeans(unit1Name,
                        "externalRelationType", "unitRole").toString()
                        + " must be null");
            }
            if (!(relationService.getReferencedMBeans("internalRelationDouble")
                    .containsKey(unit2Name))
                    && (relationService
                            .getReferencedMBeans("internalRelationDouble")
                            .containsKey(unit1Name))) {
                log.info(relationService.getReferencedMBeans(
                        "internalRelationDouble").toString()
                        + " must contain unit1Name and unit2Name");
            }
            if (relationService.getReferencedMBeans("internalRelationSingle")
                    .containsKey(unit2Name)) {
                log.info(relationService.getReferencedMBeans(
                        "internalRelationSingle").toString()
                        + " - unit2Name must be absent");
            }
            return failed("MBeansMap methods is incorrect");
        }
        return passed();
    }

    /**
     * Test verifies that notifications are sent when relation service updates
     * and removes relations by itself
     */
    public Result testPurgeNotifications() throws Exception {

        incorrectNotificationFlag = false;
        checkNotifications = false;

        MBeanServer mBeanServer = MBeanServerFactory.createMBeanServer();

        RelationService relationService = new RelationService(true);
        ObjectName relationServiceName = new ObjectName(
                "mBeanServer:type=RelationService,name=rs");
        mBeanServer.registerMBean(relationService, relationServiceName);

        NotificationFilterSupport filter = new NotificationFilterSupport();
        filter.enableType("jmx.relation");
        relationService.addNotificationListener(this, filter, null);
        checkNotifications = true;

        RoleInfo[] roleInfo = new RoleInfo[1];
        roleInfo[0] = new RoleInfo("unitRole", classPath, true, true, 1, 2,
                "Unit role");

        // Creating an Internal Relation Type
        relationService.createRelationType("internalRelationType", roleInfo);

        // Creating an External Relation Type
        ERelationType externalRelationType = new ERelationType(
                "externalRelationType", roleInfo);
        relationService.addRelationType(externalRelationType);

        // 2 MBean objects to be associate
        UnitMBean unit1 = new Unit();
        ObjectName unit1Name = new ObjectName(
                "mBeanServer:type=Unit,name=Unit1");
        mBeanServer.registerMBean(unit1, unit1Name);
        ObjectName unit2Name = new ObjectName(
                "mBeanServer:type=Unit,name=Unit2");
        mBeanServer.registerMBean(unit1, unit2Name);

        ArrayList unitList = new ArrayList();
        unitList.add(unit1Name);
        unitList.add(unit2Name);

        Role unitRole = new Role("unitRole", unitList);
        RoleList unitRoles = new RoleList();
        unitRoles.add(unitRole);

        // Change unitRole
        ArrayList oldUnitList = (ArrayList) unitList.clone();
        oldUnitList.remove(0);

        checkNotifications = true;

        // Notification Data
        relationNotification = new RelationNotificationForCompare(
                "jmx.relation.update.basic", relationService, 4, 10,
                "Value of role unitRole has changed\n"
                        + "Old value:\nmBeanServer:type=Unit,name=Unit1\n"
                        + "New value:\nmBeanServer:type=Unit,name=Unit1\n"
                        + "mBeanServer:type=Unit,name=Unit2",
                "internalRelation", "internalRelationType", null, "unitRole",
                unitList, oldUnitList);

        // Updating
        mBeanServer.unregisterMBean(unit1Name);

        // Remove Relations

        // Notification Data
        relationNotification = new RelationNotificationForCompare(
                "jmx.relation.removal.basic", relationService, 5, 10,
                "Removal of relation internalRelation", "internalRelation",
                "internalRelationType", null, null);

        mBeanServer.unregisterMBean(unit2Name);

        if (incorrectNotificationFlag) {
            return failed("IncorrectNotifications (see log for more information)");
        }
        return passed();
    }

    /**
     * This test tries to change relation service via register/unregister MBean.
     */
    public Result testManyNames() throws Exception {

        MBeanServer mBeanServer = MBeanServerFactory.createMBeanServer();

        RelationService relationService = new RelationService(true);

        ObjectName firstRelationServiceName = new ObjectName(
                "mBeanServer:type=RelationService,name=first");
        ObjectName secondRelationServiceName = new ObjectName(
                "mBeanServer:type=RelationService,name=second");

        mBeanServer.registerMBean(relationService, firstRelationServiceName);

        RoleInfo[] roleInfo = new RoleInfo[1];
        roleInfo[0] = new RoleInfo("usualRole", classPath, true, true, 1, 1,
                "usual role");

        relationService.createRelationType("relationType", roleInfo);

        UnitMBean unit1 = new Unit();

        ObjectName unit1Name = new ObjectName(
                "mBeanServer:type=Unit,name=Unit1");
        mBeanServer.registerMBean(unit1, unit1Name);

        ArrayList unitList = new ArrayList();
        unitList.add(unit1Name);

        Role singleUnitRole = new Role("usualRole", unitList);

        RoleList singleunitRoles = new RoleList();
        singleunitRoles.add(singleUnitRole);

        // Note that externalRelationSingle refers to firstRelationServiceName
        ERelation externalRelationSingle = new ERelation(
                "externalRelationSingle", firstRelationServiceName,
                mBeanServer, "relationType", singleunitRoles);
        mBeanServer.registerMBean(externalRelationSingle,
                externalRelationSingle.getRelationON());

        // Deleting RelationService with firstRelationServiceName
        mBeanServer.unregisterMBean(firstRelationServiceName);

        mBeanServer.registerMBean(relationService, secondRelationServiceName);

        try {
            relationService.addRelation(externalRelationSingle.getRelationON());
            return failed("the Relation Service name in the MBean is not the one of the current Relation Service");
        } catch (InvalidRelationServiceException e) {
            return passed();
        } catch (Exception e) {
            return failed("Wrong type of Exception");
        }

    }

    public Result testUnregiserMBean() throws Exception {

        MBeanServer mBeanServer = MBeanServerFactory.createMBeanServer();

        RelationService relationService = new RelationService(true);
        ObjectName relationServiceName = new ObjectName(
                "mBeanServer:type=RelationService,name=rs");

        UnitMBean unit1 = new Unit();
        ObjectName unit1Name = new ObjectName(
                "mBeanServer:type=Unit,name=Unit1");
        mBeanServer.registerMBean(unit1, unit1Name);

        RoleInfo[] roleInfo = new RoleInfo[1];
        roleInfo[0] = new RoleInfo("unitRole", classPath, true, true, 0, 2,
                "usual role");

        relationService.createRelationType("internalRelationType", roleInfo);

        ERelationType externalRelationType = new ERelationType(
                "externalRelationType", roleInfo);

        relationService.addRelationType(externalRelationType);

        ArrayList unitList = new ArrayList();
        unitList.add(unit1Name);

        Role unitRole = new Role("unitRole", unitList);
        RoleList unitRoles = new RoleList();
        unitRoles.add(unitRole);

        ERelation externalRelation = new ERelation("externalRelation",
                relationServiceName, mBeanServer, externalRelationType
                        .getRelationTypeName(), unitRoles);
        mBeanServer.registerMBean(externalRelation, externalRelation
                .getRelationON());

        try {
            externalRelation.handleMBeanUnregistration(unit1Name, "unitRole");
            return failed("handleMBeanUnregistration hasn't trow "
                    + "RelationServiceNotRegisteredException");
        } catch (RelationServiceNotRegisteredException e) {
            return passed();
        } catch (Exception e) {
            return failed(e.toString());
        }
    }

    /**
     * In this test External relation unregistered MBeans by itself. After this
     * verified relation service consistency.
     */

    public Result testSelfUnregistration() throws Exception {

        MBeanServer mBeanServer = MBeanServerFactory.createMBeanServer();

        RelationService relationService = new RelationService(true);
        ObjectName relationServiceName = new ObjectName(
                "mBeanServer:type=RelationService,name=rs");
        mBeanServer.registerMBean(relationService, relationServiceName);

        RoleInfo[] roleInfo = new RoleInfo[1];
        roleInfo[0] = new RoleInfo("unitRole", classPath, true, true, 1, 2,
                "Unit role");

        // Creating an Internal Relation Type
        relationService.createRelationType("internalRelationType", roleInfo);

        // Registering 2 MBean objects
        UnitMBean unit1 = new Unit();
        ObjectName unit1Name = new ObjectName(
                "mBeanServer:type=Unit,name=Unit1");
        mBeanServer.registerMBean(unit1, unit1Name);

        ArrayList unitList = new ArrayList();

        unitList.add(unit1Name);
        Role unitRole = new Role("unitRole", unitList);
        RoleList unitRoles = new RoleList();
        unitRoles.add(unitRole);

        ERelation externalRelation = new ERelation("externalRelation",
                relationServiceName, mBeanServer, "internalRelationType",
                unitRoles);
        mBeanServer.registerMBean(externalRelation, externalRelation
                .getRelationON());
        relationService.addRelation(externalRelation.getRelationON());

        externalRelation.unregisterMBean(unit1Name);

        if (relationService.hasRelation("externalRelation").booleanValue()) {
            return failed("Consistency Failed");
        }
        return passed();
    }

    /**
     * This test verifies findReferencingRelations method
     */

    public Result testRelationIdMap() throws Exception {

        MBeanServer mBeanServer = MBeanServerFactory.createMBeanServer();

        // Register Relation Service
        RelationService relationService = new RelationService(true);
        ObjectName relationServiceName = new ObjectName(
                "mBeanServer:type=RelationService,name=rs");
        mBeanServer.registerMBean(relationService, relationServiceName);

        // Create Role Information Array
        RoleInfo[] roleInfo = new RoleInfo[1];
        roleInfo[0] = new RoleInfo("unitRole", classPath, true, true, 1, 2,
                "Unit role");

        // Creating an Internal Relation Type
        relationService.createRelationType("internalRelationType", roleInfo);

        // Registering 2 MBean objects
        UnitMBean unit1 = new Unit();

        ObjectName unit2Name = new ObjectName(
                "mBeanServer:type=Unit,name=Unit2");
        mBeanServer.registerMBean(unit1, unit2Name);

        ObjectName unit1Name = new ObjectName(
                "mBeanServer:type=Unit,name=Unit1");
        mBeanServer.registerMBean(unit1, unit1Name);

        // Create RoleLists for single unit and double unit relations
        ArrayList unitList = new ArrayList();

        unitList.add(unit1Name);
        Role singleUnitRole = new Role("unitRole", unitList);

        unitList.add(unit2Name);
        Role doubleUnitRole = new Role("unitRole", unitList);

        RoleList singleunitRoles = new RoleList();
        singleunitRoles.add(singleUnitRole);

        RoleList doubleunitRoles = new RoleList();
        doubleunitRoles.add(doubleUnitRole);

        // Create 2 Internal Relations
        relationService.createRelation("internalRelationSingle",
                "internalRelationType", singleunitRoles);
        relationService.createRelation("internalRelationDouble",
                "internalRelationType", doubleunitRoles);
        ERelation externalRelation = new ERelation("externalRelation",
                relationServiceName, mBeanServer, "internalRelationType",
                doubleunitRoles);
        mBeanServer.registerMBean(externalRelation, externalRelation
                .getRelationON());
        relationService.addRelation(externalRelation.getRelationON());

        if ((!relationService.findReferencingRelations(unit2Name,
                "internalRelationType", "unitRole").containsKey(
                "internalRelationDouble"))
                || (!relationService.findReferencingRelations(unit1Name,
                        "internalRelationType", "unitRole").containsKey(
                        "internalRelationDouble"))
                || (!relationService.findReferencingRelations(unit1Name,
                        "internalRelationType", "unitRole").containsKey(
                        "internalRelationSingle"))
                || (!relationService.findReferencingRelations(unit1Name,
                        "internalRelationType", "unitRole").containsKey(
                        "externalRelation"))
                || (!relationService.findReferencingRelations(unit2Name,
                        "internalRelationType", "unitRole").containsKey(
                        "externalRelation"))) {
            log.info(relationService.findReferencingRelations(unit1Name,
                    "internalRelationType", "unitRole").toString());
            return failed("findReferencingRelations method is incorrect");
        }
        return passed();
    }

    public Result testNotRegisteredException() throws Exception {

        /* Initialisation without registering */
        MBeanServer server = MBeanServerFactory.createMBeanServer();

        ObjectName unitName = new ObjectName("a:b=c");
        server.registerMBean(new Unit(), unitName);

        RelationService rService = new RelationService(true);
        RoleInfo[] rInfo = { new RoleInfo("role", classPath) };
        rService.createRelationType("relationType", rInfo);

        RoleList unitRoles = new RoleList();
        unitRoles.add(new Role("role", Collections.singletonList(unitName)));

        /* catch RelationServiceNotRegisteredException */
        try {
            rService.createRelation("relation", "relationType", unitRoles);
            return failed("");
        } catch (RelationServiceNotRegisteredException e) {
            return passed();
        } catch (Throwable t) {
            return failed(t.toString());
        }
    }

    public Result testNullRelationName() throws Exception {

        MBeanServer server = MBeanServerFactory.createMBeanServer();

        ObjectName unitName = new ObjectName("a:b=c");
        server.registerMBean(new Unit(), unitName);

        RelationService rService = new RelationService(true);
        server.registerMBean(rService, new ObjectName("a:b=d"));

        RoleInfo[] rInfo = { new RoleInfo("role", classPath) };
        rService.createRelationType("relationType", rInfo);

        RoleList unitRoles = new RoleList();
        unitRoles.add(new Role("role", Collections.singletonList(unitName)));

        /* set null relation name */
        try {
            rService.createRelation(null, "relationType", unitRoles);
            return failed("");
        } catch (IllegalArgumentException e) {
            return passed();
        } catch (Throwable t) {
            return failed(t.toString());
        }
    }

    public Result testNullRelationType() throws Exception {

        MBeanServer server = MBeanServerFactory.createMBeanServer();

        ObjectName unitName = new ObjectName("a:b=c");
        server.registerMBean(new Unit(), unitName);

        RelationService rService = new RelationService(true);
        server.registerMBean(rService, new ObjectName("a:b=d"));

        RoleList unitRoles = new RoleList();
        unitRoles.add(new Role("role", Collections.singletonList(unitName)));

        /* set null relation type */
        try {
            rService.createRelation("relation", null, unitRoles);
            return failed("");
        } catch (IllegalArgumentException e) {
            return passed();
        } catch (Throwable t) {
            return failed(t.toString());
        }
    }

    public Result testRoleNotFound() throws Exception {

        MBeanServer server = MBeanServerFactory.createMBeanServer();

        ObjectName unitName = new ObjectName("a:b=c");
        server.registerMBean(new Unit(), unitName);

        RelationService rService = new RelationService(true);
        server.registerMBean(rService, new ObjectName("a:b=d"));

        RoleInfo[] rInfo = { new RoleInfo("role", classPath) };
        rService.createRelationType("relationType", rInfo);

        RoleList unitRoles = new RoleList();
        /* role name differs from role info contains */
        unitRoles.add(new Role("another role", Collections
                .singletonList(unitName)));

        /* catch RoleNotFoundException */
        try {
            rService.createRelation("relation", "relationType", unitRoles);
            return failed("");
        } catch (RoleNotFoundException e) {
            return passed();
        } catch (Throwable t) {
            return failed(t.toString());
        }
    }

    public Result testIdAlreadyUsed() throws Exception {

        MBeanServer server = MBeanServerFactory.createMBeanServer();

        ObjectName unitName = new ObjectName("a:b=c");
        server.registerMBean(new Unit(), unitName);

        RelationService rService = new RelationService(true);
        server.registerMBean(rService, new ObjectName("a:b=d"));

        RoleInfo[] rInfo = { new RoleInfo("role", classPath) };
        rService.createRelationType("relationType", rInfo);

        RoleList unitRoles = new RoleList();
        unitRoles.add(new Role("role", Collections.singletonList(unitName)));

        rService.createRelation("relation", "relationType", unitRoles);

        /* now try to create another relation with the same name */
        /* create another relation type for clearance */
        rService.createRelationType("another relationType", rInfo);

        try {
            rService.createRelation("relation", "another relationType",
                    unitRoles);
            return failed("");
        } catch (InvalidRelationIdException e) {
            return passed();
        } catch (Throwable t) {
            return failed(t.toString());
        }
    }

    public Result testUnknownRelationType() throws Exception {

        MBeanServer server = MBeanServerFactory.createMBeanServer();

        ObjectName unitName = new ObjectName("a:b=c");
        server.registerMBean(new Unit(), unitName);

        RelationService rService = new RelationService(true);
        server.registerMBean(rService, new ObjectName("a:b=d"));

        RoleList unitRoles = new RoleList();
        unitRoles.add(new Role("role", Collections.singletonList(unitName)));

        /* Try to create relation with unknown relation type */
        try {
            rService.createRelation("relation", "relationType", unitRoles);
            return failed("");
        } catch (RelationTypeNotFoundException e) {
            return passed();
        } catch (Throwable t) {
            return failed(t.toString());
        }
    }

    public Result testTheSameRoleName() throws Exception {

        MBeanServer server = MBeanServerFactory.createMBeanServer();

        ObjectName unitName = new ObjectName("a:b=c");
        ObjectName unitName2 = new ObjectName("a:b=c2");
        server.registerMBean(new Unit(), unitName);
        server.registerMBean(new Unit(), unitName2);

        RelationService rService = new RelationService(true);
        server.registerMBean(rService, new ObjectName("a:b=d"));

        RoleInfo[] rInfo = { new RoleInfo("role", classPath) };
        rService.createRelationType("relationType", rInfo);

        RoleList unitRoles = new RoleList();

        /* Add 2 roles with the same name */
        unitRoles.add(new Role("role", Collections.singletonList(unitName)));
        unitRoles.add(new Role("role", Collections.singletonList(unitName2)));

        try {
            rService.createRelation("relation", "relationType", unitRoles);
            return failed("");
        } catch (InvalidRoleValueException e) {
            return passed();
        } catch (Throwable t) {
            return failed(t.toString());
        }
    }

    public Result testNumberOfMBeanLessThanNeed() throws Exception {

        MBeanServer server = MBeanServerFactory.createMBeanServer();

        ObjectName unitName = new ObjectName("a:b=c");
        server.registerMBean(new Unit(), unitName);

        RelationService rService = new RelationService(true);
        server.registerMBean(rService, new ObjectName("a:b=d"));

        RoleInfo[] rInfo = { new RoleInfo("role", classPath) };
        rService.createRelationType("relationType", rInfo);

        RoleList unitRoles = new RoleList();
        /* The Number of MBeans is less than default (1) */
        unitRoles.add(new Role("role", new ArrayList()));
        try {
            rService.createRelation("relation", "relationType", unitRoles);
            return failed("");
        } catch (InvalidRoleValueException e) {
            return passed();
        } catch (Throwable t) {
            return failed(t.toString());
        }
    }

    public Result testNumberOfMBeanMoreThanNeed() throws Exception {

        MBeanServer server = MBeanServerFactory.createMBeanServer();

        ObjectName unitName = new ObjectName("a:b=c");
        ObjectName unitName2 = new ObjectName("a:b=c2");
        server.registerMBean(new Unit(), unitName);
        server.registerMBean(new Unit(), unitName2);

        RelationService rService = new RelationService(true);
        server.registerMBean(rService, new ObjectName("a:b=d"));

        RoleInfo[] rInfo = { new RoleInfo("role", classPath) };
        rService.createRelationType("relationType", rInfo);

        RoleList unitRoles = new RoleList();
        /* The Number of MBeans is more than default (1) */
        ArrayList units = new ArrayList();
        units.add(unitName);
        units.add(unitName2);

        unitRoles.add(new Role("role", units));

        try {
            rService.createRelation("relation", "relationType", unitRoles);
            return failed("");
        } catch (InvalidRoleValueException e) {
            return passed();
        } catch (Throwable t) {
            return failed(t.toString());
        }
    }

    public Result testMBeanNotRegistered() throws Exception {

        MBeanServer server = MBeanServerFactory.createMBeanServer();

        ObjectName unitName = new ObjectName("a:b=c");

        RelationService rService = new RelationService(true);
        server.registerMBean(rService, new ObjectName("a:b=d"));

        RoleInfo[] rInfo = { new RoleInfo("role", classPath) };
        rService.createRelationType("relationType", rInfo);

        RoleList unitRoles = new RoleList();

        /* Referenced MBean hasn't registered */
        unitRoles.add(new Role("role", Collections.singletonList(unitName)));

        try {
            rService.createRelation("relation", "relationType", unitRoles);
            return failed("");
        } catch (InvalidRoleValueException e) {
            return passed();
        } catch (Throwable t) {
            return failed(t.toString());
        }
    }

    public Result testDifferentTypeMBean() throws Exception {

        MBeanServer server = MBeanServerFactory.createMBeanServer();

        ObjectName unitName = new ObjectName("a:b=c");
        /* MBean class is NotUnit */
        server.registerMBean(new NotUnit(), unitName);

        RelationService rService = new RelationService(true);
        server.registerMBean(rService, new ObjectName("a:b=d"));

        RoleInfo[] rInfo = { new RoleInfo("role", classPath) };
        rService.createRelationType("relationType", rInfo);

        RoleList unitRoles = new RoleList();

        /* Referenced MBean hasn't registered */
        unitRoles.add(new Role("role", Collections.singletonList(unitName)));

        try {
            rService.createRelation("relation", "relationType", unitRoles);
            return failed("");
        } catch (InvalidRoleValueException e) {
            return passed();
        } catch (Throwable t) {
            return failed(t.toString());
        }
    }

    public Result testExistenceAfterException() throws Exception {

        MBeanServer server = MBeanServerFactory.createMBeanServer();

        ObjectName unitName = new ObjectName("a:b=c");
        server.registerMBean(new Unit(), unitName);

        RelationService rService = new RelationService(true);
        server.registerMBean(rService, new ObjectName("a:b=d"));

        RoleInfo[] rInfo = { new RoleInfo("role", classPath),
                new RoleInfo("another role", classPath) };
        rService.createRelationType("relationType", rInfo);

        RoleList unitRoles = new RoleList();
        /*
         * "anotherRole is incorrect - the number of MBeans is less than
         * default"
         */
        unitRoles.add(new Role("role", Collections.singletonList(unitName)));
        unitRoles.add(new Role("another role", new ArrayList()));

        try {
            rService.createRelation("relation", "relationType", unitRoles);
            System.out.println("existenceAfterExceptions test failed");
        } catch (InvalidRoleValueException e) {
            /* this exception should be caught */
        } catch (Throwable t) {
            System.out.println("existenceAfterExceptions test failed: "
                    + t.toString());
        }

        /* now verify is relation exists */
        if (!rService.getAllRelationIds().contains("relation")) {
            return passed();
        }
        return failed("");
    }

    public Result testNotReadable() throws Exception {

        MBeanServer server = MBeanServerFactory.createMBeanServer();

        ObjectName unitName = new ObjectName("a:b=c");
        server.registerMBean(new Unit(), unitName);

        RelationService rService = new RelationService(true);
        server.registerMBean(rService, new ObjectName("a:b=d"));

        /* Role is not readable */
        RoleInfo[] rInfo = { new RoleInfo("role", classPath, false, true) };
        rService.createRelationType("relationType", rInfo);

        RoleList unitRoles = new RoleList();
        unitRoles.add(new Role("role", Collections.singletonList(unitName)));

        try {
            rService.createRelation("relation", "relationType", unitRoles);
            return passed();
        } catch (Throwable t) {
            return failed(t.toString());
        }
    }

    public Result testNotWritable() throws Exception {

        MBeanServer server = MBeanServerFactory.createMBeanServer();

        ObjectName unitName = new ObjectName("a:b=c");
        server.registerMBean(new Unit(), unitName);

        RelationService rService = new RelationService(true);
        server.registerMBean(rService, new ObjectName("a:b=d"));

        /* Role is not readable */
        RoleInfo[] rInfo = { new RoleInfo("role", classPath, true, false) };
        rService.createRelationType("relationType", rInfo);

        RoleList unitRoles = new RoleList();
        unitRoles.add(new Role("role", Collections.singletonList(unitName)));

        try {
            rService.createRelation("relation", "relationType", unitRoles);
            return passed();
        } catch (Throwable t) {
            return failed(t.toString());
        }
    }

    public Result testPurgeRemains() throws Exception {

        MBeanServer server = MBeanServerFactory.createMBeanServer();

        ObjectName[] unitNames = { new ObjectName("a:b=c"),
                new ObjectName("a:b=c2") };

        server.registerMBean(new Unit(), unitNames[0]);
        server.registerMBean(new Unit(), unitNames[1]);

        RelationService rService = new RelationService(true);
        server.registerMBean(rService, new ObjectName("a:b=d"));

        /* This role remains after unregister one of unit */
        RoleInfo[] rInfo = { new RoleInfo("role", classPath, true, true, 1, 2,
                "role") };
        rService.createRelationType("relationType", rInfo);

        RoleList unitRoles = new RoleList();
        Role role = new Role("role", Arrays.asList(unitNames));
        unitRoles.add(role);

        rService.createRelation("relation", "relationType", unitRoles);

        /* now remove one of MBeans */
        server.unregisterMBean(unitNames[0]);

        /* verify relation is exist */
        if (!rService.getAllRelationIds().contains("relation")) {
            return failed("");
        }
        return passed();
    }

    public Result testPurgeRemoves() throws Exception {

        MBeanServer server = MBeanServerFactory.createMBeanServer();

        ObjectName[] unitNames = { new ObjectName("a:b=c"),
                new ObjectName("a:b=c2") };

        server.registerMBean(new Unit(), unitNames[0]);
        server.registerMBean(new Unit(), unitNames[1]);

        RelationService rService = new RelationService(true);
        server.registerMBean(rService, new ObjectName("a:b=d"));

        /* This role remains after unregister one of unit */
        RoleInfo[] rInfo = { new RoleInfo("role", classPath, true, true, 2, 2,
                "role") };
        rService.createRelationType("relationType", rInfo);

        RoleList unitRoles = new RoleList();
        Role role = new Role("role", Arrays.asList(unitNames));
        unitRoles.add(role);

        rService.createRelation("relation", "relationType", unitRoles);

        /* now remove one of MBeans */
        server.unregisterMBean(unitNames[0]);

        /* verify relation is exist */
        if (rService.getAllRelationIds().contains("relation")) {
            return failed("");
        }
        return passed();
    }

    public static void main(String[] args) {
        System.exit(new StructureTest().test(args));
    }

}