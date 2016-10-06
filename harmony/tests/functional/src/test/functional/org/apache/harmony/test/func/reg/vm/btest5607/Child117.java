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
package org.apache.harmony.test.func.reg.vm.btest5607;
import java.io.*;

public class Child117 extends Super117 {

    public Child117() {
        try {
            prln("point 1_");
            File file = new File(System.getProperty
("java.home"), "lib/mailcap");
            prln("point 2_");
            InputStream is1 = new FileInputStream(file);
            prln("point 3_");
        } catch (SecurityException e) {
            prln("catchblock 1_");
        } catch (IOException e) {
            prln("catchblock 2_");
        }
    }

    void prln(String s) {
        System.out.println(s);
    }
}



