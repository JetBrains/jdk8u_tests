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

package org.apache.harmony.test.func.api.java.text.AttributedCharacterIterator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.AttributedCharacterIterator;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/*
 * 11.10.2005
 */
public class AttributeTest extends MultiCase {

    protected void setUp() throws Exception {

    }

    public Result testHashCode() throws IOException, ClassNotFoundException {
        AttributedCharacterIterator.Attribute a = AttributedCharacterIterator.Attribute.LANGUAGE;
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bs);
        os.writeObject(a);
        os.flush();
        os.close();

        byte[] bytes = bs.toByteArray();
        bytes = (byte[]) bytes.clone();

        AttributedCharacterIterator.Attribute b = (AttributedCharacterIterator.Attribute) new ObjectInputStream(
                new ByteArrayInputStream(bytes)).readObject();
        System.out.println(b != a);
        for (int i = 0; i < bytes.length; i++) {
            System.out.print((char) bytes[i]);
        }
        System.out.println("\n");

        if (!b.equals(a)) {
            return failed("deserialized attribute is not equal to serialized");
        }

        if (b.hashCode() != a.hashCode()) {
            return failed("deserialized attribute has other hashcode");
        }

        if (a.hashCode() == 0) {
            return failed("could normal hashcode == 0?");
        }

        return passed();
    }

    class Attr extends AttributedCharacterIterator.Attribute {

        private static final long serialVersionUID = 873068336794789971L;

        public Attr(String name) {
            super(name);
        }

        public String getName() {
            return super.getName();
        }
    }

    public Result testGetName() {
        final String name = "Tested name";

        Attr attr = new Attr(name);

        return attr.getName().equals(name) ? passed("PASSED")
                : failed("getName returned wrong value");
    }

    public static void main(String[] args) {
        System.exit(new AttributeTest().test(args));
    }
}
