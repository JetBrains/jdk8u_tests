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

package org.apache.harmony.test.func.api.java.util.Locale;

import java.util.Locale;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/*
 * Mar 16, 2006
 */
public class GetVariantTest extends MultiCase {

    public Result testGetVariant() {
        StringBuffer builder = new StringBuffer();
        builder.append('V');
        builder.append("ARIANT");
        
        Locale loc = new Locale("en", "US", "VARIANT");
        assertEquals(loc.getVariant(), builder.toString());
        
        assertFalse(builder.toString() == "VARIANT");
        
        String s = "VARIANT";
        assertTrue(s == "VARIANT");
        
        return result();
    }

    public static void main(String[] args) {
        System.exit(new GetVariantTest().test(args));
    }

}
