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
/*
 * Created on 21.02.2005
 *
 */
package org.apache.harmony.test.func.api.java.util.jar.Attributes.Name;

import java.util.jar.Attributes;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 *  
 */
public class NameTest extends MultiCase {
    public static void main(String[] args) {
        System.exit(new NameTest().test(args));
    }

    public Result testInit() {
        try {
            new Attributes.Name("\u0000");
            return failed("expected IllegalArgumentException on new Attributes.Name(\\u0000)");
        } catch (IllegalArgumentException e) {
        }

        try {
            new Attributes.Name(null);
            return failed("expected NPE on new Attributes.Name(null)");
        } catch (NullPointerException e) {
        }

        try {
            new Attributes.Name("a.b");
            return failed("expected IllegalArgumentException on new Attributes.Name(a.b)");
        } catch (IllegalArgumentException e) {
        }

        try {
            new Attributes.Name("");
            return failed("expected IllegalArgumentException on new Attributes.Name('')");
        } catch (IllegalArgumentException e) {
        }

        new Attributes.Name(
                "0123456789012345678901234567890123456789012345678901234567890123456789");

        try {
            new Attributes.Name(
                    "01234567890123456789012345678901234567890123456789012345678901234567890");
            return failed("expected IllegalArgumentException on new Attributes.Name(<71-char string>)");
        } catch (IllegalArgumentException e) {
        }

        return passed();
    }

    public Result testEquals() {
        if (!new Attributes.Name("A").equals(new Attributes.Name("a"))) {
            return failed("expected 'A' and 'a' to be equal");
        }

        if (new Attributes.Name("A").equals(new Attributes.Name("b"))) {
            return failed("expected 'A' and 'b' to be not equal");
        }

        if (!new Attributes.Name("Ab").equals(new Attributes.Name("aB"))) {
            return failed("expected 'Ab' and 'aB' to be equal");
        }

        return passed();
    }
    
    public Result testHashCode() {
        if (new Attributes.Name("A").hashCode() != new Attributes.Name("a").hashCode()) {
            return failed("expected hashCodes of 'A' and 'a' to be equal");
        }

        if (new Attributes.Name("A").hashCode() == new Attributes.Name("b").hashCode()) {
            return failed("expected hashCodes of 'A' and 'b' to be not equal");
        }

        if (new Attributes.Name("Ab").hashCode() != new Attributes.Name("aB").hashCode()) {
            return failed("expected hashCodes of 'Ab' and 'aB' to be equal");
        }

        return passed();
    }


    public Result testStaticFields() {
        if (Attributes.Name.CLASS_PATH == null
                || Attributes.Name.CONTENT_TYPE == null
                || Attributes.Name.EXTENSION_INSTALLATION == null
                || Attributes.Name.EXTENSION_LIST == null
                || Attributes.Name.EXTENSION_NAME == null
                || Attributes.Name.IMPLEMENTATION_TITLE == null
                || Attributes.Name.IMPLEMENTATION_URL == null
                || Attributes.Name.IMPLEMENTATION_VENDOR == null
                || Attributes.Name.IMPLEMENTATION_VENDOR_ID == null
                || Attributes.Name.IMPLEMENTATION_VERSION == null
                || Attributes.Name.MAIN_CLASS == null
                || Attributes.Name.MANIFEST_VERSION == null
                || Attributes.Name.SEALED == null
                || Attributes.Name.SIGNATURE_VERSION == null
                || Attributes.Name.SPECIFICATION_TITLE == null
                || Attributes.Name.SPECIFICATION_VENDOR == null
                || Attributes.Name.SPECIFICATION_VERSION == null) {
            return failed("one of the static fields is null");
        }

        return passed();
    }

    public Result testToString() {
        String[] sarr = new String[] { "1", "ABCD", "abcd", "x", "10g10" };

        for (int i = 0; i < sarr.length; ++i) {
            if (!sarr[i].equals(new Attributes.Name(sarr[i]).toString())) {
                return failed("new Attributes.Name(" + sarr[i]
                        + " ).toString() is "
                        + new Attributes.Name(sarr[i]).toString());
            }
        }

        return passed();
    }

}