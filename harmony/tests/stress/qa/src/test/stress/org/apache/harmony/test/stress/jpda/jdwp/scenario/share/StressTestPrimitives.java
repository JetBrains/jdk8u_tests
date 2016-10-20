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
 * @author Anatoly F. Bondarenko
 * @version $Revision: 1.2 $
 */

/**
 * Created on 14.09.2005 
 */

package org.apache.harmony.test.stress.jpda.jdwp.scenario.share;

import org.apache.harmony.share.framework.jpda.jdwp.CommandPacket;
import org.apache.harmony.share.framework.jpda.jdwp.JDWPCommands;
import org.apache.harmony.share.framework.jpda.jdwp.JDWPConstants;
import org.apache.harmony.share.framework.jpda.jdwp.ReplyPacket;

public class StressTestPrimitives {
    
    
    public static void fillArrayOfSignatures
        (String[] array, int sourceClasses, int generatedClasses) {
        for ( int i=0; i < sourceClasses; i++ ) {
            String signature = array[i];
            int startIndex = sourceClasses + (i * generatedClasses);
            for (int j=0; j < generatedClasses; j++) {
                signature = "[" + signature;
                array[startIndex + j] = signature;
            }
        }
    }

    public static void checkThreadStatus(StressTestCase stressTestCase, long threadID, String marker) {
        CommandPacket commandPacket = new CommandPacket(
                JDWPCommands.ThreadReferenceCommandSet.CommandSetID,
                JDWPCommands.ThreadReferenceCommandSet.StatusCommand);
        commandPacket.setNextValueAsThreadID(threadID);
        ReplyPacket reply = stressTestCase.debuggeeWrapper.vmMirror.performCommand(commandPacket);
        String infoHeader = "CHECK_THREAD_STATUS(" + marker + "): ";
        short errorCode = reply.getErrorCode();
        if ( errorCode == JDWPConstants.Error.NONE ) {
            int threadStatus = reply.getNextValueAsInt();
            stressTestCase.printlnForDebug(infoHeader + "threadStatus = " + threadStatus +
                    "(" + JDWPConstants.ThreadStatus.getName(threadStatus) + ")");
            int suspendStatus = reply.getNextValueAsInt();    
            stressTestCase.printlnForDebug(infoHeader + "suspendStatus = " + suspendStatus);
        } else {
            stressTestCase.checkReplyForError(reply, JDWPConstants.Error.NONE, 
                    infoHeader + "ThreadReference.Status command");
        }
    }


}    
