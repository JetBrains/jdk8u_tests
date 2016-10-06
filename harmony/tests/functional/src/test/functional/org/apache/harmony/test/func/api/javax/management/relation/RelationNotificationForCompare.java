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

import javax.management.ObjectName;
import javax.management.relation.RelationNotification;

/**
 * The only difference for this class from RelationNotification class is the
 * equals(Notification) method.
 * 
 */

public class RelationNotificationForCompare extends RelationNotification {

    private static final long serialVersionUID = 1L;

    private String Distinction = "";

    public RelationNotificationForCompare(String NType, Object arg1, long arg2,
            long arg3, String arg4, String arg5, String arg6, ObjectName arg7,
            List arg8) {
        super(NType, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
    }

    public RelationNotificationForCompare(String NType, Object theSrcObj,
            long TheSeqNbr, long theTimeStamp, String theMsg, String theRelId,
            String theRelTypeName, ObjectName theRelObjName,
            String theRoleName, List theNewRoleValue, List theOldRoleValue) {
        super(NType, theSrcObj, TheSeqNbr, theTimeStamp, theMsg, theRelId,
                theRelTypeName, theRelObjName, theRoleName, theNewRoleValue,
                theOldRoleValue);
    }

    public String getDestinction() {
        return Distinction;
    }

    private boolean isUpdate(String theType) {
        if (RELATION_BASIC_UPDATE.equals(theType)
                | RELATION_MBEAN_UPDATE.equals(theType)) {
            return true;
        }
        return false;
    }

    /**
     * This Method compares RelationNotificationForCompare object with
     * RelationNotification object.
     */

    private boolean compareObjectNames(RelationNotification N) {

        if ((this.getType()
                .equals(RelationNotification.RELATION_BASIC_CREATION))
                | (this.getType()
                        .equals(RelationNotification.RELATION_BASIC_REMOVAL))
                | (this.getType()
                        .equals(RelationNotification.RELATION_BASIC_UPDATE))) {
            if ((this.getObjectName() == null) && (N.getObjectName() == null)) {
                return true;
            } else {
                Distinction = Distinction
                        + "For Internal Relations can't be any ObjectNames";
                return false;
            }
        } else {
            if (this.getObjectName().equals(N.getObjectName())) {
                return true;
            } else {
                Distinction = Distinction + "\n" + "getObjectName "
                        + N.getObjectName() + " - " + this.getObjectName();
                return false;
            }
        }
    }

    private boolean checkNotificationCorrectness(RelationNotification N) {
        if ((N.getType() == null) && (N.getClass() == null)
                && (N.getSource() == null) && (N.getMessage() == null)
                && (N.getRelationId() == null)
                && (N.getRelationTypeName() == null)) {
            Distinction = Distinction + "\n"
                    + "Notification contains null fields" + N.toString();
            return false;
        }
        return true;
    }

    private boolean compareMBeansToUnreg(RelationNotification N) {

        boolean coincidence;

        if (this.getMBeansToUnregister() == null) {
            if (N.getMBeansToUnregister() == null) {
                return true;
            } else {
                Distinction = Distinction + "\n" + "getMBeansToUnregister "
                        + N.getMBeansToUnregister() + " - "
                        + this.getMBeansToUnregister();
                return false;
            }
        }

        if (this.getMBeansToUnregister().size() != N.getMBeansToUnregister()
                .size()) {
            Distinction = Distinction + "\n"
                    + "MBeansToUnregister size incorrect "
                    + this.getMBeansToUnregister().size() + " - "
                    + N.getMBeansToUnregister().size();
            return false;
        }
        for (int i = 0; i < this.getMBeansToUnregister().size(); i++) {
            coincidence = false;
            for (int j = 0; j < this.getMBeansToUnregister().size(); j++) {
                if (this.getMBeansToUnregister().get(i).equals(
                        N.getMBeansToUnregister().get(j))) {
                    coincidence = true;
                    break;
                }
            }
            if (!coincidence) {
                Distinction = Distinction + "\n" + "getMBeansToUnregister "
                        + N.getMBeansToUnregister() + " - "
                        + this.getMBeansToUnregister();
                return false;
            }
        }
        return true;
    }

    public boolean equals(RelationNotification N) {

        if (!checkNotificationCorrectness(N)
                || !checkNotificationCorrectness(this)) {
            return false;
        }

        if (isUpdate(this.getType())) {
            if ((this.getType().equals(N.getType()))
                    && (this.getSource().equals(N.getSource()))
                    && (this.getSequenceNumber() == N.getSequenceNumber())
                    && (this.getRelationId().equals(N.getRelationId()))
                    && (this.getRelationTypeName().equals(N
                            .getRelationTypeName()))
                    && (this.getRoleName().equals(N.getRoleName()))
                    && (this.getOldRoleValue().equals(N.getOldRoleValue()))
                    && (this.getNewRoleValue().equals(N.getNewRoleValue()))
                    && compareObjectNames(N))
                return true;
            else {
                if (!this.getType().equals(N.getType())) {
                    Distinction = Distinction + "\n Notification number "
                            + N.getSequenceNumber() + ": " + "getType "
                            + N.getType() + " - " + this.getType();
                }
                if (!(this.getSource().equals(N.getSource()))) {
                    Distinction = Distinction + "\n Notification number "
                            + N.getSequenceNumber() + ": " + "getSource "
                            + N.getSource() + " - " + this.getSource();
                }
                if (!(this.getSequenceNumber() == N.getSequenceNumber())) {
                    Distinction = Distinction + "\n Notification number "
                            + N.getSequenceNumber() + ": "
                            + "getSequenceNumber " + N.getSequenceNumber()
                            + " - " + this.getSequenceNumber();
                }
                if (!(this.getRelationId().equals(N.getRelationId()))) {
                    Distinction = Distinction + "\n Notification number "
                            + N.getSequenceNumber() + ": " + "getRelationId "
                            + N.getRelationId() + " - " + this.getRelationId();
                }
                if (!(this.getRelationTypeName()
                        .equals(N.getRelationTypeName()))) {
                    Distinction = Distinction + "\n Notification number "
                            + N.getSequenceNumber() + ": "
                            + "getRelationTypeName " + N.getRelationTypeName()
                            + " - " + this.getRelationTypeName();
                }

                if (!(this.getRoleName().equals(N.getRoleName()))) {
                    Distinction = Distinction + "\n Notification number "
                            + N.getSequenceNumber() + ": " + "getRoleName "
                            + N.getRoleName() + " - " + this.getRoleName();
                }
                if (!(this.getOldRoleValue().equals(N.getOldRoleValue()))) {
                    Distinction = Distinction + "\n Notification number "
                            + N.getSequenceNumber() + ": " + "getOldRoleValue "
                            + N.getOldRoleValue() + " - "
                            + this.getOldRoleValue();
                }
                if (!(this.getNewRoleValue().equals(N.getNewRoleValue()))) {
                    Distinction = Distinction + "\n Notification number "
                            + N.getSequenceNumber() + ": " + "getNewRoleValue "
                            + N.getNewRoleValue() + " - "
                            + this.getNewRoleValue();
                }
                return false;
            }
        } else {
            if ((this.getType().equals(N.getType()))
                    && (this.getSource().equals(N.getSource()))
                    && (this.getRelationId().equals(N.getRelationId()))
                    && (this.getRelationTypeName().equals(N
                            .getRelationTypeName()))
                    && (this.getSequenceNumber() == N.getSequenceNumber())
                    && compareMBeansToUnreg(N) && compareObjectNames(N))
                return true;
            else {
                if (!((this.getType() != null && N.getType() != null) || this
                        .getType().equals(N.getType()))) {
                    Distinction = Distinction + "\n Notification number "
                            + N.getSequenceNumber() + ": " + "getType "
                            + N.getType() + " - " + this.getType();
                }
                if (!((this.getSource() != null && N.getSource() != null) || this
                        .getSource().equals(N.getSource()))) {
                    Distinction = Distinction + "\n Notification number "
                            + N.getSequenceNumber() + ": " + "getSource "
                            + N.getSource() + " - " + this.getSource();
                }
                if (!(this.getSequenceNumber() == N.getSequenceNumber())) {
                    Distinction = Distinction + "\n Notification number "
                            + N.getSequenceNumber() + ": "
                            + "getSequenceNumber " + N.getSequenceNumber()
                            + " - " + this.getSequenceNumber();
                }
                if (!((this.getRelationId() != null && N.getRelationId() != null) || this
                        .getRelationId().equals(N.getRelationId()))) {
                    Distinction = Distinction + "\n Notification number "
                            + N.getSequenceNumber() + ": " + "getRelationId "
                            + N.getRelationId() + " - " + this.getRelationId();
                }
                if (!((this.getRelationTypeName() != null && N
                        .getRelationTypeName() != null) || this
                        .getRelationTypeName().equals(N.getRelationTypeName()))) {
                    Distinction = Distinction + "\n Notification number "
                            + N.getSequenceNumber() + ": "
                            + "getRelationTypeName " + N.getRelationTypeName()
                            + " - " + this.getRelationTypeName();
                }
                return false;
            }
        }
    }
}
