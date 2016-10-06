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
/*
 * Created on 03.06.2005
 */
package org.apache.harmony.test.func.api.javax.swing.share.LookAndFeels;

import javax.swing.UIDefaults;

import org.apache.harmony.test.func.api.share.InteractiveTest;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */
public abstract class ConcreteLAFGenTest extends InteractiveTest {
    private ConcreteLAFPublic instance;
    protected abstract ConcreteLAFPublic getInstance();
    
    public final Result testGetDefaults() {
        instance = getInstance();
        if (instance.getDefaults() == null) {
            return failed("couldn't create the defaults table");
        }
            
        return passed();
    }
    
    public Result testInitClassDefaults() {
        instance = getInstance();
        instance.initClassDefaults(new UIDefaults());
        return passed();
    }
    
    public Result testInitComponentDefaults() {
        instance = getInstance();
        instance.initComponentDefaults(new UIDefaults());
        return passed();
    }
    
    public Result testInitSystemColorDefaults() {
        return passed();
    }
}
