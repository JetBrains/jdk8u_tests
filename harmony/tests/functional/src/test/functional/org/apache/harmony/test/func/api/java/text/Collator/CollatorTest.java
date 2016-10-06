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
package org.apache.harmony.test.func.api.java.text.Collator;

import java.text.Collator;
import java.util.Locale;

import org.apache.harmony.share.Test;

/**
 */
public class CollatorTest extends Test {

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.share.Test#test()
     */
    public int test() {
        Collator c;
        Collator def;
        try {
            c = Collator.getInstance();
            if (c == null) {
                throw new Throwable("Collator.getInstance() == null");
            }
        } catch (Throwable e) {
            log.add("Collator.getInstance() throws an exception:");
            log.add(e.getMessage());
            e.printStackTrace();
            return fail("Null reference");
        }
        try {
            def = Collator.getInstance(Locale.getDefault());
            if (def == null) {
                throw new Throwable("Collator.getInstance(Locale) == null");
            }
        } catch (Throwable e) {
            log.add("Collator.getInstance(Locale) throws an exception:");
            log.add(e.getMessage());
            e.printStackTrace();
            return fail("Null reference");
        }

        if (!c.equals(def)) {
            return fail("Collator.getInstance() != "
                    + "Collator.getInstance(Locale.getDefault())");
        }

        int[] dcmp = { Collator.NO_DECOMPOSITION,
                Collator.CANONICAL_DECOMPOSITION, Collator.FULL_DECOMPOSITION };

        for (int i = 0; i < dcmp.length; i++) {
            c.setDecomposition(dcmp[i]);
            if (c.getDecomposition() != dcmp[i]) {
                return fail("Collator.setDecomposition(int) != "
                        + "Collator.getDecomposition()");
            }
        }

        int[] strength = { Collator.PRIMARY, Collator.SECONDARY,
                Collator.TERTIARY };

        for (int i = 0; i < strength.length; i++) {
            c.setStrength(strength[i]);
            if (c.getStrength() != strength[i]) {
                return fail("Collator.setStrength(int) != Collator.getStrength()");
            }
        }

        return pass();
    }

    public static void main(String[] args) {
        System.exit(new CollatorTest().test(args));
    }
}