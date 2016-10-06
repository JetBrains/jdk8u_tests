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

import java.util.List;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.relation.InvalidRoleValueException;
import javax.management.relation.RelationNotFoundException;
import javax.management.relation.RelationServiceNotRegisteredException;
import javax.management.relation.RelationSupportMBean;
import javax.management.relation.RelationTypeNotFoundException;
import javax.management.relation.RoleNotFoundException;

/**
 * The MBean interface for the external relation.
 * 
 */

public interface ERelationMBean extends RelationSupportMBean {

    String getRelationTypeName();

    void unregisterMBean(ObjectName mBeanName, String roleName)
            throws RoleNotFoundException, InvalidRoleValueException,
            RelationServiceNotRegisteredException,
            RelationTypeNotFoundException, RelationNotFoundException,
            IllegalArgumentException;

    void unregisterMBean(ObjectName mBeanName)
            throws InstanceNotFoundException, MBeanRegistrationException;

    String getRelationServiceObjName();

    ObjectName getRelationON() throws MalformedObjectNameException,
            NullPointerException;

    List retrieveRoleList();
}