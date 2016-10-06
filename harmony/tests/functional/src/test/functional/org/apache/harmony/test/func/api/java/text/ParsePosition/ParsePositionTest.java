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

package org.apache.harmony.test.func.api.java.text.ParsePosition;

import java.text.ParsePosition;

import org.apache.harmony.test.func.api.java.share.PropertyTest;
import org.apache.harmony.share.Test;

/*
 * 17.10.2005
 */
public class ParsePositionTest extends Test {

    public int test() {
        ParsePosition pp = new ParsePosition(2);
        
        Class cls = ParsePosition.class;
        PropertyTest.Data [] simple = {
                new PropertyTest.Data(cls, "ErrorIndex", new Integer(2), (new Integer(-1))),
                new PropertyTest.Data(cls, "Index", new Integer(3), (new Integer(2))),
        };
        //PropertyTest.CodeGen cg = new PropertyTest.CodeGen();
        //cg.printCode(pp, System.out);
        
        int failCount = 0;
        for (int i = 0; i < simple.length; i++) {
            if (!simple[i].test(pp)) {
                this.error(simple[i].name + " property have invalid behaviour");
                failCount++;
            }
        }
        if (failCount > 0) {
            return fail(failCount + " properties failed");
        } else {
            return pass();
        }
    }

    public static void main(String[] args) {
        PropertyTest.Data.setLogger(log);
        System.exit(new ParsePositionTest().test(args));
    }

}
