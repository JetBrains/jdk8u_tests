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

package org.apache.harmony.test.func.api.java.text.FieldPosition;

import java.text.FieldPosition;
import java.text.Format;

import org.apache.harmony.test.func.api.java.share.PropertyTest;
import org.apache.harmony.share.Test;

/*
 * 17.10.2005
 */
public class FieldPositionTest extends Test {

    private class MyField extends Format.Field {

        private static final long serialVersionUID = 1398967240967857237L;

        public MyField(String name) {
            super(name);
        }
    }

    public int test() {
        MyField mf = new MyField("TestedField");
        FieldPosition fp = new FieldPosition(mf, 123);

        // new PropertyTest.CodeGen().printCode(fp, System.out);

        Class cls = FieldPosition.class;
        try {

            PropertyTest.Data[] data = {
                    new PropertyTest.Data(cls, "FieldAttribute", new MyField(
                            "OtherField"), mf),
                    new PropertyTest.Data(cls, "Field", new Integer(155),
                            (new Integer(123))),
                    new PropertyTest.Data(cls, "BeginIndex", new Integer(10),
                            (new Integer(0))),
                    new PropertyTest.Data(cls, "EndIndex", new Integer(12),
                            (new Integer(0))), };
            int failCount = 0;
            for (int i = 0; i < data.length; i++) {
                if (!data[i].test(fp)) {
                    this.error(data[i].name
                            + " property have invalid behaviour");
                    failCount++;
                }
            }
            if (failCount > 0) {
                return fail(failCount + " properties failed");
            } else {
                return pass();
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return error("Unexpected exception");
        }

    }

    public static void main(String[] args) {
        PropertyTest.Data.setLogger(log);
        System.exit(new FieldPositionTest().test(args));
    }

}
