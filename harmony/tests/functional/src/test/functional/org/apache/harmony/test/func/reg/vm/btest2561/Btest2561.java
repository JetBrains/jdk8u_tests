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
package org.apache.harmony.test.func.reg.vm.btest2561;

import org.apache.harmony.share.Test;

import java.awt.Component;
import javax.swing.JLayeredPane;
import javax.swing.JButton;

public class Btest2561 extends Test {

    public static void main(String[] args) {
        System.exit(new Btest2561().test(args));
    }

    public int test() {
        try {
            JLayeredPane p = new JLayeredPane();
            JButton c = new JButton();
            JButton c1 = new JButton();
            JButton c2 = new JButton();
            p.setLayer((Component)c1, 2);
            p.add((Component)c1, 2);
            p.setLayer((Component)c, 2);
            p.add((Component)c, 2);
            p.setLayer((Component)c2, 2);
            p.add((Component)c2, 2);
        p.moveToFront(c);
        p.getPosition(c);
        } catch (Exception e) {
            return fail("unexpected exception " + e);
        }
    return pass();
    }
}

