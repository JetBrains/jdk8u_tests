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

package org.apache.harmony.test.func.api.java.awt.AWTPermission;

import java.awt.AWTPermission;
import java.io.IOException;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

public class AWTPermissionTest extends MultiCase {

    public static void main(String[] args) throws IOException {
        System.exit(new AWTPermissionTest().test(args));
    }

    public Result testConstructor() {
        try {
            AWTPermission awtp = new AWTPermission(null);
            return failed("expected NPE if name is null");
        } catch (NullPointerException e) {
        }
        
        String s = "abcd " + Math.random();
        AWTPermission awtp = new AWTPermission(s);
        if(!s.equals(awtp.getName())) {
            return failed("expected name to be equal to argument");
        }
        

        return passed();
    }
}