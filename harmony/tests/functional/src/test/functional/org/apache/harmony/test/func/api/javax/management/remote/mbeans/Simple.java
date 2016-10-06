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
package org.apache.harmony.test.func.api.javax.management.remote.mbeans;

import org.apache.harmony.share.Test;
import javax.management.NotificationBroadcasterSupport;

/**
 */
public class Simple  extends NotificationBroadcasterSupport implements SimpleMBean {
    
    private TMBParameter param;
    public String getState() {
            return state;
    }
    public void setState(String s) {
        this.state = s;   
        }   
    private String state = "initial";   
    
    public void echo(){
        Test.log.info("Invoke method echo success");
    }
    public TMBValue returnSomeClass() {
        return new TMBValue();
        }
    public void setTMBParameter(TMBParameter param){
        Test.log.info("Invoke method with parametr = " + param.getClass().getName());
        this.param = param;
    }        
}
