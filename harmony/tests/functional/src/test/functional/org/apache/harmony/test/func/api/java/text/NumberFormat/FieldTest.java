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

package org.apache.harmony.test.func.api.java.text.NumberFormat;

import java.text.DateFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.text.NumberFormat;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/*
 * 17.10.2005
 */
public class FieldTest extends MultiCase {

    private interface FieldIntruder {
        public String getName();
    }

    private final String name = "TestedField";

    private class NumberField extends NumberFormat.Field implements FieldIntruder {

        private static final long serialVersionUID = 4276251317308935871L;

        public NumberField(String name) {
            super(name);
        }

        public String getName() {
            return super.getName();
        }
    }

    private class MessageField extends MessageFormat.Field implements FieldIntruder {

        private static final long serialVersionUID = -6746640761122071313L;

        public MessageField(String name) {
            super(name);
        }

        public String getName() {
            return super.getName();
        }
    }

    private class DateField extends DateFormat.Field implements FieldIntruder {

        private static final long serialVersionUID = 6169273061876261210L;

        public DateField(String name) {
            super(name, -1);
        }

        public String getName() {
            return super.getName();
        }
    }

    private class FormatField extends Format.Field implements FieldIntruder {

        private static final long serialVersionUID = -7381518431061147343L;

        public FormatField(String name) {
            super(name);
        }

        public String getName() {
            return super.getName();
        }
    }
    private Result verifyField(FieldIntruder f) {

        if (f.getName().equals(name)) {
            return passed();
        } else {
            return failed(f.getName() + " != " + name);
        }
    }
    
    public Result testNumber()
    {
        return verifyField(new NumberField(name));
    }

    public Result testDate()
    {
        return verifyField(new DateField(name));
    }

    public Result testMessage()
    {
        return verifyField(new MessageField(name));
    }

    public Result testField()
    {
        return verifyField(new FormatField(name));
    }

    public static void main(String[] args) {
        System.exit(new FieldTest().test(args));
    }

}
