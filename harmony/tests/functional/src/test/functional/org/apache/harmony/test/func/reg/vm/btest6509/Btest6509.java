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
package org.apache.harmony.test.func.reg.vm.btest6509;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Vector;

import org.apache.harmony.test.share.reg.RegressionTest;

/**
 */
public class Btest6509 extends RegressionTest {

    public static void main(String[] args) {
        System.exit(new Btest6509().test(args));
    }

    public int test(final String[] args) {
        try {
            final ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(args[0]));
            final Object obj = ois.readObject();

            ois.close();

            return obj instanceof Vector ? passed() : failed();
        } catch (Exception ex) {
            ex.printStackTrace();
            return failed();
        }
    }
}
