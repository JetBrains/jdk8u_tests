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
 * @author Anton Luht
 * @version $Revision: 1.2 $
 */  
/*
 * Created on 27.10.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.apache.harmony.test.stress.api.java.io.ObjectOutputStream.share;

import java.io.ObjectInputStream;

public abstract class SerializationOnlyTest extends SerializationTestFramework {
    final boolean isSerializationOnlyTest() {
        return true;
    }
    
    protected final int testIn(ObjectInputStream ois) {
        return fail("this method should never be called");
    }
}
