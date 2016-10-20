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

package org.apache.harmony.test.stress.exceptions.thrower;

/**
 * Throws UserDefinedError in implicit mode.
 * 
 * @author Alexander D. Shipilov
 * @version $Revision: 1.4 $
 * 
 */
public class UserDefinedErrorThrower extends Thrower {

    protected void doThrow() throws Throwable {
        throw new UserDefinedError();
    }

    /**
     * Registers as a thrower to use.
     */
    public void testUserDefinedError() {
        registerThrower(this, UserDefinedError.class);
    }

    private static class UserDefinedError extends Exception {
        static final long serialVersionUID = -8738025638090174457L;
    }

}
