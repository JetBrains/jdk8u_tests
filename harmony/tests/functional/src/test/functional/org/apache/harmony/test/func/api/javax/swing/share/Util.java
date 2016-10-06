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
/** 
 */  
/*
 * Created on 18.03.2005
 *
 */
package org.apache.harmony.test.func.api.javax.swing.share;

import javax.swing.SwingUtilities;

/**
 *
 */
public class Util {
    /**
     * blocks until all events that were in event queue are processed
     *
     */
    public static void waitQueueEventsProcess() {
        try {
            for(int i = 0; i < 5; ++i) {
                SwingUtilities.invokeAndWait(new Thread());
                Thread.sleep(20);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
