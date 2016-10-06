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
import java.util.List;

import javax.management.*;
import javax.management.relation.*;

/**
 * This class demonstrates an external relation.
 * 
 */

public class ERelation extends RelationSupport implements ERelationMBean {

    List roleList = new ArrayList();

    MBeanServer mBeanServer;

    public String getRelationServiceObjName() {
        return super.getRelationServiceName().toString();
    }

    public List retrieveRoleList() {
        return roleList;
    }
    
    public ObjectName getRelationON() throws MalformedObjectNameException,
            NullPointerException {
        return new ObjectName("mBeanServer:type=Relation,name="
                + super.getRelationId());
    }

    public void unregisterMBean(ObjectName mBeanName, String roleName)
            throws RoleNotFoundException, InvalidRoleValueException,
            RelationServiceNotRegisteredException,
            RelationTypeNotFoundException, RelationNotFoundException,
            IllegalArgumentException {
        this.handleMBeanUnregistration(mBeanName, roleName);
    }

    public void unregisterMBean(ObjectName mBeanName)
            throws InstanceNotFoundException, MBeanRegistrationException {
        mBeanServer.unregisterMBean(mBeanName);
    }

    public ERelation(String name, ObjectName relationServiceObjName,
            MBeanServer mbeanServer, String relationTypeName, RoleList roleList)
            throws Exception {
        super(name, relationServiceObjName, mbeanServer, relationTypeName,
                roleList);
        mBeanServer = mbeanServer;
        roleList = super.retrieveAllRoles();

    }
}