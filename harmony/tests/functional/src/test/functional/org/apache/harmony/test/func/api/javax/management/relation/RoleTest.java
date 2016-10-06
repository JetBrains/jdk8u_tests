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
import java.util.Map;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.relation.InvalidRelationIdException;
import javax.management.relation.InvalidRelationTypeException;
import javax.management.relation.InvalidRoleInfoException;
import javax.management.relation.RelationService;
import javax.management.relation.RelationServiceNotRegisteredException;
import javax.management.relation.RelationSupport;
import javax.management.relation.RelationTypeSupport;
import javax.management.relation.Role;
import javax.management.relation.RoleInfo;
import javax.management.relation.RoleList;
import javax.management.relation.RoleNotFoundException;
import javax.management.relation.RoleResult;
import javax.management.relation.RoleUnresolved;
import javax.management.relation.RoleUnresolvedList;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 * Purpose: Verify Role Performance, Check All Changes Of Role Value Are Valid.
 * 
 * <p>
 * <br>
 * Under test:RelationService; Role; RoleInfo; RoleList; RoleResult;
 * RoleUnresolved; RoleUnresolvedList; <br>
 * <br>
 * </p>
 * testGetSet <br>
 * Step by step for Verification Read and Write Properties For Single Role:
 * <ul>
 * <li>a. Create a relation
 * <li>b. Create a not readable and not writable role
 * <li>c. Check that we cant read and write role value
 * </ul>
 * 
 * testRoleLists <br>
 * Step by step for verification RoleResult List:
 * <ul>
 * <li>a. Create a relation
 * <li>b. Create 8 roles
 * <li>c. Change name for the first role
 * <li>d. For the second role set a role value with more ObjectNames than the
 * maximum expected cardinality
 * <li>f. For the third role set a role value with less ObjectNames than the
 * minimum expected cardinality
 * <li>g. For the fourth role set a role value including the ObjectName of a
 * MBean not registered in the MBean Server.
 * <li>h. Fifth role is not readable
 * <li>i. Sixth role is not writable
 * <li>j. For the seventh role set a role value including the ObjectName of a
 * MBean not of the class expected for that role.
 * <li>k. Sets the roles in the relation.
 * <li>l. Verify RoleResult list is correct
 * </ul>
 * 
 * testUpdate<br>
 * This test verifies relation update mechanism.
 * 
 * testWrongRoleInfo<br>
 * Test verifies that roleinfo minimum value can't be more than maximum value<br>
 * <br>
 * 
 * testNoRoleInfo<br>
 * Test verifies that external relation type cannot be registered without
 * roleinfo<br>
 * <br>
 * 
 * testRoleInfoInfinity<br>
 * This test verifies that minimum role value also can be infinite <br>
 * <br>
 * 
 * testRoleInfoClassPathMBean<br>
 * This test tries to create roleinfo with wrong MBean classpath <br>
 * <br>
 * 
 * testRoleListNull<br>
 * RoleList can't add null <br>
 * <br>
 * 
 * testRoleListCanBeNull<br>
 * This test verifies that rolelist can be null in relation creation <br>
 * <br>
 * 
 * testInfluenceMBeanUnregToRole<br>
 * Step by step for verification that then MBean is unregistered Role becomes
 * incorrect
 * <ul>
 * <li>a. Create relation
 * <li>b. Unregister referenced MBean
 * <li>c. Verify that relation becomes incorrect
 * </ul>
 * 
 * testUnknownRole<br>
 * This test verifies that Role with unknown RoleName cannot be used for
 * relation registering <br>
 * <br>
 * 
 * testUpdateRoleMap<br>
 * Test verifies that updateRoleMap throws RelationServiceNotRegisteredException
 * <br>
 * <br>
 * 
 * testGetRoles<br>
 * This test verifies getRoles method <br>
 * <br>
 * 
 * testGetAllRelationTypeNames<br>
 * This test verifies getAllRelationTypeNames method <br>
 * <br>
 * 
 * 
 */

public class RoleTest extends MultiCase {

    /**
     * Class Path For Unit MBeans
     */
    private static final String classPath = Unit.class.getName();

    /**
     * roleCompare methods are to compare Roles and Unresolved Roles.
     * roleCompare(Role, Role) and roleCompare(RoleUnresolved, RoleUnresolved)
     * verifies than all fields are equals.<br>
     * roleCompare(Role, RoleUnresolved, int) verifies coincident fields between
     * Role and RoleUnresolved and equalizes RoleUnresolved problem type with
     * expected value
     */
    private boolean roleCompare(Role role1, Role role2) {
        return (role1.getRoleName().equals(role2.getRoleName()))
                && (role1.getRoleValue().equals(role2.getRoleValue()));
    }

    private boolean roleCompare(Role role, RoleUnresolved roleUnresolved,
            int problem) {
        return (role.getRoleName().equals(roleUnresolved.getRoleName()))
                && (role.getRoleValue().equals(roleUnresolved.getRoleValue()))
                && (problem == roleUnresolved.getProblemType());
    }

    private boolean roleCompare(RoleUnresolved roleUnresolved1,
            RoleUnresolved roleUnresolved2) {
        return (roleUnresolved1.getRoleName().equals(roleUnresolved2
                .getRoleName()))
                && (roleUnresolved1.getRoleValue().equals(roleUnresolved2
                        .getRoleValue()))
                && (roleUnresolved1.getProblemType() == roleUnresolved2
                        .getProblemType());
    }

    /**
     * CheckMBeansInTheMap compares MBeans in the Role (as Map of referenced
     * MBeans in the Relation) and Expected ArrayList of MBeans
     */
    private boolean checkMBeansInTheMap(String roleName, ArrayList unitList,
            Map roleMap) {
        boolean res = true;
        if (roleMap.size() != unitList.size()) {
            return false;
        }
        for (int i = 0; i < unitList.size(); i++) {
            res &= ((ArrayList) roleMap.get(unitList.get(i)))
                    .contains(roleName);
        }
        return res;
    }

    /**
     * Step by step for verification RoleResult List:
     * <ul>
     * <li>a. Create a relation
     * <li>b. Create 8 roles
     * <li>c. Change name for the first role
     * <li>d. For the second role set a role value with more ObjectNames than
     * the maximum expected cardinality
     * <li>f. For the third role set a role value with less ObjectNames than
     * the minimum expected cardinality
     * <li>g. For the fourth role set a role value including the ObjectName of
     * a MBean not registered in the MBean Server.
     * <li>h. Fifth role is not readable
     * <li>i. Sixth role is not writable
     * <li>j. For the seventh role set a role value including the ObjectName of
     * a MBean not of the class expected for that role.
     * <li>k. Sets the roles in the relation.
     * <li>l. Verify RoleResult list is correct
     * </ul>
     */

    public Result testRoleLists() throws Exception {

        // Get the Platform MBean Server
        MBeanServer mBeanServer = MBeanServerFactory.createMBeanServer();

        // Register Relation service
        RelationService relationService = new RelationService(true);
        ObjectName relationServiceName = new ObjectName(
                "mBeanServer:type=RelationService,name=rs");
        mBeanServer.registerMBean(relationService, relationServiceName);

        // Create MBeans
        UnitMBean unit1 = new Unit();
        ObjectName unit1Name = new ObjectName(
                "mBeanServer:type=Unit,name=mbean1");
        mBeanServer.registerMBean(unit1, unit1Name);

        UnitMBean unit2 = new Unit();
        ObjectName unit2Name = new ObjectName(
                "mBeanServer:type=Unit,name=mbean2");
        mBeanServer.registerMBean(unit2, unit2Name);

        NotUnit notUnit = new NotUnit();
        ObjectName notUnitName = new ObjectName(
                "mBeanServer:type=NotUnit,name=notUnit");
        mBeanServer.registerMBean(notUnit, notUnitName);

        // Create RoleInfos And Relation Type
        RoleInfo[] roleInfo = new RoleInfo[8];

        roleInfo[0] = new RoleInfo("correctRole", classPath);
        roleInfo[1] = new RoleInfo("wrongnameRole", classPath, true, true, 1,
                1, "1");
        roleInfo[2] = new RoleInfo("notreadableRole", classPath, false, true,
                0, 1, "2");
        roleInfo[3] = new RoleInfo("notwritableRole", classPath, true, false,
                1, 1, "3");
        roleInfo[4] = new RoleInfo("lessthanminRole", classPath, true, true, 1,
                1, "4");
        roleInfo[5] = new RoleInfo("morethanmaxRole", classPath, true, true, 1,
                1, "5");
        roleInfo[6] = new RoleInfo("incorrectclassRole", classPath, true, true,
                1, 1, "6");
        roleInfo[7] = new RoleInfo("notregisteredmbeanRole", classPath, true,
                true, 1, 1, "7");

        relationService.createRelationType("internalRelationType", roleInfo);

        // Create Relation
        ArrayList unitList = new ArrayList();
        unitList.add(unit1Name);

        RoleList roleList = new RoleList();
        Role correctRole = new Role("correctRole", unitList);
        Role wrongnameRole = new Role("wrongnameRole", unitList);
        Role notreadableRole = new Role("notreadableRole", unitList);
        Role notwritableRole = new Role("notwritableRole", unitList);
        Role lessthanminRole = new Role("lessthanminRole", unitList);
        Role morethanmaxRole = new Role("morethanmaxRole", unitList);
        Role incorrectclassRole = new Role("incorrectclassRole", unitList);
        Role notregisteredmbeanRole = new Role("notregisteredmbeanRole",
                unitList);

        roleList.add(correctRole);
        roleList.add(wrongnameRole);
        roleList.add(notreadableRole);
        roleList.add(notwritableRole);
        roleList.add(lessthanminRole);
        roleList.add(morethanmaxRole);
        roleList.add(incorrectclassRole);
        roleList.add(0, notregisteredmbeanRole);

        relationService.createRelation("internalRelation",
                "internalRelationType", roleList);

        // Change name for the wrongnameRole
        wrongnameRole.setRoleName(wrongnameRole.getRoleName() + "!");

        // Add MBean to unitList and describe it to the morethanmaxRole
        unitList.add(unit2Name);
        morethanmaxRole.setRoleValue(unitList);

        // Clear unitList and describe it to the lessthanminRole
        unitList.clear();
        lessthanminRole.setRoleValue(unitList);

        // Add Reference to MBean of different class to incorrectclassRole
        unitList.add(notUnitName);
        incorrectclassRole.setRoleValue(unitList);

        // Add not registered MBean to the unitList and describe it to the
        // notregisteredmbeanRole
        unitList.clear();
        unitList.add(new ObjectName("a:b=c"));
        notregisteredmbeanRole.setRoleValue(unitList);

        RoleUnresolvedList roleUnresolvedList = new RoleUnresolvedList();
        RoleList roleResolvedList = new RoleList();

        // add unresolved role manually
        RoleUnresolved manuallyaddedRole = new RoleUnresolved(
                "manuallyaddedRole", unitList, 1);
        roleUnresolvedList.add(manuallyaddedRole);

        RoleResult roleResult = relationService.setRoles("internalRelation",
                roleList);
        roleUnresolvedList.addAll(roleResult.getRolesUnresolved());
        roleResolvedList.addAll(roleResult.getRoles());

        for (int i = 0; i < roleUnresolvedList.size(); i++) {
            if (roleCompare((RoleUnresolved) roleUnresolvedList.get(i),
                    manuallyaddedRole)) {
                continue;
            }
            if (roleCompare(wrongnameRole, (RoleUnresolved) roleUnresolvedList
                    .get(i), 1)) {
                continue;
            }
            if (roleCompare(morethanmaxRole,
                    (RoleUnresolved) roleUnresolvedList.get(i), 5)) {
                continue;
            }
            if (roleCompare(notwritableRole,
                    (RoleUnresolved) roleUnresolvedList.get(i), 3)) {
                continue;
            }
            if (roleCompare(lessthanminRole,
                    (RoleUnresolved) roleUnresolvedList.get(i), 4)) {
                continue;
            }
            if (roleCompare(incorrectclassRole,
                    (RoleUnresolved) roleUnresolvedList.get(i), 6)) {
                continue;
            }
            if (roleCompare(notregisteredmbeanRole,
                    (RoleUnresolved) roleUnresolvedList.get(i), 7)) {
                continue;
            }

            return failed("RoleUnresolvedList Is Not Valid");
        }

        for (int i = 0; i < roleResolvedList.size(); i++) {
            if (roleCompare((Role) roleResolvedList.get(i), correctRole)) {
                continue;
            }
            if (roleCompare((Role) roleResolvedList.get(i), notreadableRole)) {
                continue;
            }
            System.err.println(roleUnresolvedList);
            System.err.println(roleResolvedList);
            return failed("RoleList Is Not Valid");
        }

        roleUnresolvedList.clear();
        roleResolvedList.clear();

        roleResult = relationService.getAllRoles("internalRelation");
        roleUnresolvedList.addAll(roleResult.getRolesUnresolved());
        roleResolvedList.addAll(roleResult.getRoles());

        // Modification And Addition manuallyaddedRole to roleUnresolvedList
        manuallyaddedRole.setRoleName(manuallyaddedRole.getRoleName() + "!");
        manuallyaddedRole.setRoleValue(correctRole.getRoleValue());
        manuallyaddedRole.setProblemType(7);

        roleUnresolvedList.add(0, manuallyaddedRole);
        for (int i = 1; i < roleUnresolvedList.size(); i++) {
            if (roleCompare((RoleUnresolved) roleUnresolvedList.get(i),
                    manuallyaddedRole)) {
                continue;
            }
            if (((RoleUnresolved) roleUnresolvedList.get(i)).getRoleName()
                    .equals(notreadableRole.getRoleName())
                    && (((RoleUnresolved) roleUnresolvedList.get(i))
                            .getProblemType() == 2)) {
                continue;
            }
            return failed("RoleUnresolvedList Is Not Valid");
        }

        for (int i = 0; i < roleResolvedList.size(); i++) {
            if (roleCompare((Role) roleResolvedList.get(i), correctRole)) {
                continue;
            }
            if (roleCompare((Role) roleResolvedList.get(i), new Role(
                    "wrongnameRole", correctRole.getRoleValue()))) {
                continue;
            }
            if (roleCompare((Role) roleResolvedList.get(i), new Role(
                    "morethanmaxRole", correctRole.getRoleValue()))) {
                continue;
            }
            if (roleCompare((Role) roleResolvedList.get(i), notwritableRole)) {
                continue;
            }
            if (roleCompare((Role) roleResolvedList.get(i), new Role(
                    "lessthanminRole", correctRole.getRoleValue()))) {
                continue;
            }
            if (roleCompare((Role) roleResolvedList.get(i), new Role(
                    "incorrectclassRole", correctRole.getRoleValue()))) {
                continue;
            }
            if (roleCompare((Role) roleResolvedList.get(i), new Role(
                    "notregisteredmbeanRole", correctRole.getRoleValue()))) {
                continue;
            }
            System.err.println(roleResolvedList);
            return failed("RoleList Is Not Valid");
        }

        return passed();
    }

    /**
     * Step by step for Verification Read and Write Properties For Single Role:
     * 
     * <li>a. Create a relation
     * <li>b. Create a not readable and not writable role
     * <li>c. Check that we cant read and write role value
     * </ul>
     */

    public Result testGetSet() throws Exception {

        // Create MBean Server
        MBeanServer mBeanServer = MBeanServerFactory.createMBeanServer();

        // Register Relation Service
        RelationService relationService = new RelationService(true);

        ObjectName relationServiceName = new ObjectName(
                "mBeanServer:type=RelationService,name=rs");
        mBeanServer.registerMBean(relationService, relationServiceName);

        // Register MBean

        UnitMBean unit1 = new Unit();
        // Construct ObjectName for MBean
        ObjectName unitName = new ObjectName("mBeanServer:type=Unit,name=Unit1");
        // Register the MBean
        mBeanServer.registerMBean(unit1, unitName);

        // Create RoleInfos and relation type
        RoleInfo[] roleInfo = new RoleInfo[4];

        roleInfo[0] = new RoleInfo("notreadableRole", classPath, false, true,
                1, 1, "not readable role");
        roleInfo[1] = new RoleInfo("notwritableRole", classPath, true, false,
                1, 1, "not writable role");
        roleInfo[2] = new RoleInfo("unaccessedRole", classPath, false, false,
                1, 1, "not writable and not readable role");
        roleInfo[3] = new RoleInfo("usualRole", classPath, true, true, 1, 1,
                "writable and readable role");

        relationService.createRelationType("internalRelationType", roleInfo);

        // Creating an External Relation Type
        RelationTypeSupport externalRelationType = new RelationTypeSupport(
                "externalRelationType", roleInfo);
        relationService.addRelationType(externalRelationType);

        // Create Relation
        ArrayList unitList = new ArrayList();
        unitList.add(unitName);

        RoleList roleList = new RoleList();
        Role notreadableRole = new Role("notreadableRole", unitList);
        Role notwritableRole = new Role("notwritableRole", unitList);
        Role unaccessedRole = new Role("unaccessedRole", unitList);
        Role usualRole = new Role("usualRole", unitList);

        roleList.add(notreadableRole);
        roleList.add(notwritableRole);
        roleList.add(unaccessedRole);
        roleList.add(usualRole);

        // Create External Relation

        RelationSupport externalRelation = new RelationSupport(
                "externalRelationSingle", relationServiceName,
                externalRelationType.getRelationTypeName(), roleList);
        ObjectName externalRelationName = new ObjectName(
                "mBeanServer:type=Relation,name=externalRelationSingle");
        mBeanServer.registerMBean(externalRelation, externalRelationName);
        relationService.addRelation(externalRelationName);

        try {
            relationService.createRelation("internalRelation",
                    "internalRelationType", roleList);
            relationService.createRelation("internalRelation",
                    "internalRelationType", roleList);
            return failed("Internal Relation With Non-Unique Id Registered Successfully");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return failed("ConcurrentModificationException while creating relation "
                    + "with more than one role");
        } catch (InvalidRelationIdException e) {
            /* Correct state */
        } catch (Exception e) {
            e.printStackTrace();
            return failed("Wrong Type Of Exception");
        }

        relationService.setRole("internalRelation", notreadableRole);
        relationService.setRole("internalRelation", usualRole);
        relationService.getRole("internalRelation", "notwritableRole");
        relationService.getRole("internalRelation", "usualRole");

        try {
            relationService.setRole("internalRelation", unaccessedRole);
            return failed("Can Write Not Writable Role");
        } catch (RoleNotFoundException e) {
            try {
                relationService.getRole("internalRelation", "unaccessedRole");
                return failed("Can Read Not Readable Role");
            } catch (RoleNotFoundException e1) {
                /* Correct state */
            }
        }

        try {
            relationService.getRole("internalRelation", "notreadableRole");
            return failed("Can Read Not Readable Role");
        } catch (RoleNotFoundException e) {
            /* Correct state */
        }

        try {
            relationService.setRole("internalRelation", notwritableRole);
            return failed("Can Write Not Writable Role");
        } catch (RoleNotFoundException e) {
            /* Correct state */
        }

        return passed();
    }

    /**
     * <b>This test verifies relation update mechanism.</b> <br>
     * Step by step:
     * <ul>
     * <li>a. Create relations (one external and one internal)
     * <li>b. Change role value
     * <li>c. Verify that relation haven't modify its referenced MBeans list.
     * <li>d. Set modified role in the relation
     * <li>e. Verify changes are correct
     * </ul>
     */

    public Result testUpdate() throws Exception {

        /* Initialization */
        MBeanServer mBeanServer = MBeanServerFactory.createMBeanServer();

        RelationService relationService = new RelationService(true);
        ObjectName relationServiceName = new ObjectName(
                "mBeanServer:type=RelationService,name=rs");
        mBeanServer.registerMBean(relationService, relationServiceName);

        RoleInfo[] roleInfo = { new RoleInfo("unitRole", Unit.class.getName(),
                true, true, 1, 3, "Unit role") };

        relationService.createRelationType("internalRelationType", roleInfo);

        ERelationType externalRelationType = new ERelationType(
                "externalRelationType", roleInfo);
        relationService.addRelationType(externalRelationType);

        ObjectName unit1Name = new ObjectName(
                "mBeanServer:type=Unit,name=Unit1");
        ObjectName unit2Name = new ObjectName(
                "mBeanServer:type=Unit,name=Unit2");
        ObjectName unit3Name = new ObjectName(
                "mBeanServer:type=Unit,name=Unit3");
        mBeanServer.registerMBean(new Unit(), unit1Name);
        mBeanServer.registerMBean(new Unit(), unit2Name);
        mBeanServer.registerMBean(new Unit(), unit3Name);

        ArrayList unitList = new ArrayList();
        unitList.add(unit1Name);
        unitList.add(unit2Name);
        unitList.add(unit3Name);

        Role unitRole = new Role("unitRole", unitList);
        RoleList unitRoles = new RoleList();
        unitRoles.add(unitRole);

        relationService.createRelation("internalRelation",
                "internalRelationType", unitRoles);

        ERelation externalRelation = new ERelation("externalRelation",
                relationServiceName, mBeanServer, "externalRelationType",
                unitRoles);
        mBeanServer.registerMBean(externalRelation, externalRelation
                .getRelationON());
        relationService.addRelation(externalRelation.getRelationON());

        /* Update relations */
        ArrayList unitListNew = new ArrayList();
        unitListNew.add(unit1Name);
        unitRole.setRoleValue(unitListNew);

        /* Check that role value changes, but relations is not. */
        if (!checkMBeansInTheMap("unitRole", unitList, relationService
                .getReferencedMBeans("internalRelation"))) {
            return failed("internalRelation is unstable");
        }

        relationService.setRole("internalRelation", unitRole);

        /* Check updates had been successful */
        if (!checkMBeansInTheMap("unitRole", unitListNew, relationService
                .getReferencedMBeans("internalRelation"))) {
            return failed("internalRelation update failed");
        }

        /* The same for external relation */
        if (!checkMBeansInTheMap("unitRole", unitList, relationService
                .getReferencedMBeans("externalRelation"))) {
            return failed("externalRelation is unstable");
        }

        relationService.setRole("externalRelation", unitRole);

        if (!checkMBeansInTheMap("unitRole", unitListNew, relationService
                .getReferencedMBeans("externalRelation"))) {
            return failed("externalRelation update failed");
        }

        return passed();
    }

    /**
     * Test verifies that roleinfo minimum value can't be more than maximum
     * value
     */
    public Result testWrongRoleInfo() throws Exception {

        RoleInfo[] roleInfo = new RoleInfo[1];
        try {
            roleInfo[0] = new RoleInfo("usualRole", classPath, true, true, 10,
                    9, "usual role");
            return failed("Wrong Role Info");
        } catch (InvalidRoleInfoException e) {
            return passed();
        } catch (Exception e) {
            return failed("Wrong type of Exception");
        }
    }

    /**
     * Test verifies that external relation type cannot be registered without
     * roleinfo
     */

    public Result testNoRoleInfo() throws Exception {

        MBeanServer mBeanServer = MBeanServerFactory.createMBeanServer();

        RelationService relationService = new RelationService(true);

        ObjectName relationServiceName = new ObjectName(
                "mBeanServer:type=RelationService,name=rs");
        mBeanServer.registerMBean(relationService, relationServiceName);

        ERelationType eRelationType = new ERelationType("firstERelationType");

        try {
            relationService.addRelationType(eRelationType);
            return failed("RelationType registered, but no RoleInfo provided");
        } catch (InvalidRelationTypeException e) {
            return passed();
        } catch (Exception e) {
            return failed("Wrong type of Exception");
        }

    }

    /**
     * This test verifies that minimum role value also can be infinite
     */

    public Result testRoleInfoInfinity() throws Exception {

        try {
            new RoleInfo("usualRole", classPath, true, true,
                    RoleInfo.ROLE_CARDINALITY_INFINITY, 4, "Unit role");
            new RoleInfo("usualRole", classPath, true, true,
                    RoleInfo.ROLE_CARDINALITY_INFINITY,
                    RoleInfo.ROLE_CARDINALITY_INFINITY, "Unit role");
            new RoleInfo("usualRole", classPath, true, true, 0,
                    RoleInfo.ROLE_CARDINALITY_INFINITY, "Unit role");
        } catch (Exception e) {
            return failed(e.toString());
        }

        return passed();
    }

    /**
     * RoleList can't add null
     */
    public Result testRoleListNull() {

        RoleList roleList = new RoleList();

        try {
            roleList.addAll(null);
        } catch (Exception e) {
            return failed("RoleList(null) is acceptable: " + e.toString());
        }
        return passed();
    }

    /**
     * This test verifies that rolelist can be null in relation creation
     */
    public Result testRoleListCanBeNull() throws Exception {

        // Create MBean Server
        MBeanServer mBeanServer = MBeanServerFactory.createMBeanServer();

        // Register Relation Service
        RelationService relationService = new RelationService(true);
        ObjectName relationServiceName = new ObjectName(
                "mBeanServer:type=RelationService,name=rs");
        mBeanServer.registerMBean(relationService, relationServiceName);

        UnitMBean unit1 = new Unit();
        ObjectName unitName = new ObjectName("mBeanServer:type=Unit,name=Unit1");
        mBeanServer.registerMBean(unit1, unitName);

        RoleInfo[] roleInfo = new RoleInfo[1];
        roleInfo[0] = new RoleInfo("Role", classPath, true, true, 1, 1,
                "The Role can contain any number of MBeans");

        relationService.createRelationType("internalRelationType", roleInfo);

        try {
            relationService.createRelation("relationId",
                    "internalRelationType", null);
        } catch (Exception e) {
            return failed("Role list can be null: " + e.toString());
        }

        return passed();
    }

    /**
     * Step by step for verification that then MBean is unregistered Role
     * becomes incorrect
     * <ul>
     * <li>a. Create relation
     * <li>b. Unregister referenced MBean
     * <li>c. Verify that relation becomes incorrect
     * </ul>
     * 
     */
    public Result testInfluenceMBeanUnregToRole() throws Exception {

        MBeanServer mBeanServer = MBeanServerFactory.createMBeanServer();

        RelationService relationService = new RelationService(true);
        ObjectName relationServiceName = new ObjectName(
                "mBeanServer:type=RelationService,name=rs");
        mBeanServer.registerMBean(relationService, relationServiceName);

        UnitMBean unit1 = new Unit();
        ObjectName unitName = new ObjectName("mBeanServer:type=Unit,name=Unit1");
        mBeanServer.registerMBean(unit1, unitName);

        RoleInfo[] roleInfo = new RoleInfo[1];
        roleInfo[0] = new RoleInfo("usualRole", classPath);

        relationService.createRelationType("internalRelationType", roleInfo);

        ArrayList unitList = new ArrayList();
        unitList.add(unitName);

        Role usualRole = new Role("usualRole", unitList);

        mBeanServer.unregisterMBean(unitName);

        if (relationService.checkRoleWriting(usualRole, "internalRelationType",
                Boolean.TRUE).byteValue() != 7) {
            return failed("unregisterMBean influence to Role incorrect");
        }
        return passed();
    }

    /**
     * This test verifies that Role with unknown RoleName cannot be used for
     * relation registering
     */
    public Result testUnknownRole() throws Exception {
        MBeanServer mBeanServer = MBeanServerFactory.createMBeanServer();

        RelationService relationService = new RelationService(true);
        ObjectName relationServiceName = new ObjectName(
                "mBeanServer:type=RelationService,name=rs");
        mBeanServer.registerMBean(relationService, relationServiceName);

        UnitMBean unit1 = new Unit();
        ObjectName unitName = new ObjectName("mBeanServer:type=Unit,name=Unit1");
        mBeanServer.registerMBean(unit1, unitName);

        RoleInfo[] roleInfo = new RoleInfo[1];
        roleInfo[0] = new RoleInfo("usualRole", classPath, true, true,
                RoleInfo.ROLE_CARDINALITY_INFINITY,
                RoleInfo.ROLE_CARDINALITY_INFINITY, "usual role");

        relationService.createRelationType("internalRelationType", roleInfo);

        ArrayList unitList = new ArrayList();
        unitList.add(unitName);

        RoleList roleList = new RoleList();
        Role unknownRole = new Role("unknown role", unitList);
        roleList.add(unknownRole);

        try {
            relationService.createRelation("invalidRelation",
                    "internalRelationType", roleList);
            return failed("Incorrect Relation Registered. "
                    + "Role doesn't Present in Relation Type");
        } catch (RoleNotFoundException e) {
            /* Correct state */
        } catch (Exception e) {
            return failed(e.toString());
        }
        return passed();
    }

    /**
     * Test verifies that updateRoleMap throws
     * RelationServiceNotRegisteredException
     */

    public Result testUpdateRoleMap() throws Exception {

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

        try {
            relationService.updateRoleMap("externalRelation", unitRole,
                    unitList);
            return failed("updateRoleMap method hadn't trow "
                    + "RelationServiceNotRegisteredException");
        } catch (RelationServiceNotRegisteredException e) {
            /* Correct state */
        } catch (Exception e) {
            return failed(e.toString());
        }

        return passed();
    }

    /**
     * This test verifies getRoles method
     */

    public Result testGetRoles() throws Exception {

        MBeanServer mBeanServer = MBeanServerFactory.createMBeanServer();

        RelationService relationService = new RelationService(true);
        ObjectName relationServiceName = new ObjectName(
                "mBeanServer:type=RelationService,name=rs");
        mBeanServer.registerMBean(relationService, relationServiceName);

        UnitMBean unit1 = new Unit();
        ObjectName unit1Name = new ObjectName(
                "mBeanServer:type=Unit,name=Unit1");
        mBeanServer.registerMBean(unit1, unit1Name);

        RoleInfo[] roleInfo = new RoleInfo[1];
        roleInfo[0] = new RoleInfo("unitRole", classPath, true, true, 0, 2,
                "usual role");

        relationService.createRelationType("RelationType", roleInfo);

        ArrayList unitList = new ArrayList();
        unitList.add(unit1Name);

        Role unitRole = new Role("unitRole", unitList);
        RoleList unitRoles = new RoleList();
        unitRoles.add(unitRole);

        String[] roleNameArray = { "unitRole" };

        ERelation externalRelation = new ERelation("externalRelation",
                relationServiceName, mBeanServer, "RelationType", unitRoles);
        mBeanServer.registerMBean(externalRelation, externalRelation
                .getRelationON());
        relationService.addRelation(externalRelation.getRelationON());

        try {
            relationService.getRoles("externalRelation", roleNameArray);
        } catch (Exception e) {
            return failed(e.toString());
        }
        return passed();
    }

    /**
     * This test verifies getAllRelationTypeNames method
     */
    public Result testGetAllRelationTypeNames() throws Exception {

        MBeanServer mBeanServer = MBeanServerFactory.createMBeanServer();

        RelationService relationService = new RelationService(true);
        ObjectName relationServiceName = new ObjectName(
                "mBeanServer:type=RelationService,name=rs");
        mBeanServer.registerMBean(relationService, relationServiceName);

        RoleInfo[] roleInfo = { new RoleInfo("JustRole", classPath) };

        relationService.createRelationType("RelationType0", roleInfo);
        relationService.addRelationType(new RelationTypeSupport(
                "RelationType1", roleInfo));
        relationService.addRelationType(new ERelationType("RelationType2",
                roleInfo));
        try {
            relationService.addRelationType(new ERelationType("RelationType3"));
        } catch (Exception e) {
            /* Correct state */
        }

        for (int i = 0; i < 3; i++) {
            if (!relationService.getAllRelationTypeNames().contains(
                    "RelationType" + i)) {
                return failed("getAllRelationTypeNames missed data");
            }
        }
        if (relationService.getAllRelationTypeNames().contains(
                "RelationType" + 3)) {
            return failed("Incorrect Relation Type Added");
        }
        return passed();

    }

    public static void main(String[] args) {
        System.exit(new RoleTest().test(args));
    }
}