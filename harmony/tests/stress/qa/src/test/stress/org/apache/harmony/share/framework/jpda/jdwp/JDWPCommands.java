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
 * @author Aleksey V. Yantsen
 * @version $Revision: 1.2 $
 */

/**
 * Created on 12.03.2004
 */
package org.apache.harmony.share.framework.jpda.jdwp;

/**
 * This class defines constants for JDWP commands and command sets.
 */
public class JDWPCommands {
    public class VirtualMachineCommandSet {
        public static final byte CommandSetID                 = 1;
        public static final byte VersionCommand               = 1;
        public static final byte ClassesBySignatureCommand    = 2;
        public static final byte AllClassesCommand            = 3;
        public static final byte AllThreadsCommand            = 4;
        public static final byte TopLevelThreadGroupsCommand  = 5;
        public static final byte DisposeCommand               = 6;
        public static final byte IDSizesCommand               = 7;
        public static final byte SuspendCommand               = 8;
        public static final byte ResumeCommand                = 9;
        public static final byte ExitCommand                  = 10;
        public static final byte CreateStringCommand          = 11;
        public static final byte CapabilitiesCommand          = 12;
        public static final byte ClassPathsCommand            = 13;
        public static final byte DisposeObjectsCommand        = 14;
        public static final byte HoldEventsCommand            = 15;
        public static final byte ReleaseEventsCommand         = 16;
        public static final byte CapabilitiesNewCommand       = 17;
        public static final byte RedefineClassesCommand       = 18;
        public static final byte SetDefaultStratumCommand     = 19;
        public static final byte AllClassesWithGenericCommand = 20;
    }

    public class ReferenceTypeCommandSet {
        public static final byte CommandSetID                = 2;
        public static final byte SignatureCommand            = 1;
        public static final byte ClassLoaderCommand          = 2;
        public static final byte ModifiersCommand            = 3;
        public static final byte FieldsCommand               = 4;
        public static final byte MethodsCommand              = 5;
        public static final byte GetValuesCommand            = 6;
        public static final byte SourceFileCommand           = 7;
        public static final byte NestedTypesCommand          = 8;
        public static final byte StatusCommand               = 9;
        public static final byte InterfacesCommand           = 10;
        public static final byte ClassObjectCommand          = 11;
        public static final byte SourceDebugExtensionCommand = 12;
        public static final byte SignatureWithGenericCommand = 13;
        public static final byte FieldsWithGenericCommand    = 14;
        public static final byte MethodsWithGenericCommand   = 15;
    }

    public class ClassTypeCommandSet {
        public static final byte CommandSetID        = 3;
        public static final byte SuperclassCommand   = 1;
        public static final byte SetValuesCommand    = 2;
        public static final byte InvokeMethodCommand = 3;
        public static final byte NewInstanceCommand  = 4;
    }

    public class ArrayTypeCommandSet {
        public static final byte CommandSetID       = 4;
        public static final byte NewInstanceCommand = 1;
    }

    public class InterfaceTypeCommandSet {
        public static final byte CommandSetID = 5;
    }

    public class MethodCommandSet {
        public static final byte CommandSetID                    = 6;
        public static final byte LineTableCommand                = 1;
        public static final byte VariableTableCommand            = 2;
        public static final byte BytecodesCommand                = 3;
        public static final byte IsObsoleteCommand               = 4;
        public static final byte VariableTableWithGenericCommand = 5;
    }

    public class FieldCommandSet {
        public static final byte CommandSetID = 8;
    }

    public class ObjectReferenceCommandSet {
        public static final byte CommandSetID             = 9;
        public static final byte ReferenceTypeCommand     = 1;
        public static final byte GetValuesCommand         = 2;
        public static final byte SetValuesCommand         = 3;
        public static final byte MonitorInfoCommand       = 5;
        public static final byte InvokeMethodCommand      = 6;
        public static final byte DisableCollectionCommand = 7;
        public static final byte EnableCollectionCommand  = 8;
        public static final byte IsCollectedCommand       = 9;
    }

    public class StringReferenceCommandSet {
        public static final byte CommandSetID = 10;
        public static final byte ValueCommand = 1;
    }

    public class ThreadReferenceCommandSet {
        public static final byte CommandSetID                   = 11;
        public static final byte NameCommand                    = 1;
        public static final byte SuspendCommand                 = 2;
        public static final byte ResumeCommand                  = 3;
        public static final byte StatusCommand                  = 4;
        public static final byte ThreadGroupCommand             = 5;
        public static final byte FramesCommand                  = 6;
        public static final byte FrameCountCommand              = 7;
        public static final byte OwnedMonitorsCommand           = 8;
        public static final byte CurrentContendedMonitorCommand = 9;
        public static final byte StopCommand                    = 10;
        public static final byte InterruptCommand               = 11;
        public static final byte SuspendCountCommand            = 12;
    }

    public class ThreadGroupReferenceCommandSet {
        public static final byte CommandSetID    = 12;
        public static final byte NameCommand     = 1;
        public static final byte ParentCommand   = 2;
        public static final byte ChildrenCommand = 3;
    }

    public class ArrayReferenceCommandSet {
        public static final byte CommandSetID     = 13;
        public static final byte LengthCommand    = 1;
        public static final byte GetValuesCommand = 2;
        public static final byte SetValuesCommand = 3;
    }

    public class ClassLoaderReferenceCommandSet {
        public static final byte CommandSetID          = 14;
        public static final byte VisibleClassesCommand = 1;
    }

    public class EventRequestCommandSet {
        public static final byte CommandSetID               = 15;
        public static final byte SetCommand                 = 1;
        public static final byte ClearCommand               = 2;
        public static final byte ClearAllBreakpointsCommand = 3;
    }

    public class StackFrameCommandSet {
        public static final byte CommandSetID      = 16;
        public static final byte GetValuesCommand  = 1;
        public static final byte SetValuesCommand  = 2;
        public static final byte ThisObjectCommand = 3;
        public static final byte PopFramesCommand  = 4;
    }

    public class ClassObjectReferenceCommandSet {
        public static final byte CommandSetID         = 17;
        public static final byte ReflectedTypeCommand = 1;
    }

    public class EventCommandSet {
        public static final byte CommandSetID     = 64;
        public static final byte CompositeCommand = 100;
    }

}