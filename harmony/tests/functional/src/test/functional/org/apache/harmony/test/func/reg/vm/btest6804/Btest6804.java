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
package org.apache.harmony.test.func.reg.vm.btest6804;

import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JScrollBar;

import org.apache.harmony.test.share.reg.HangTest;
/**
 * Some Swing constructors hang VM
 *
 */
public class Btest6804 extends HangTest {
    
    public static void main(String[] args) {
        System.exit(new Btest6804().test(Logger.global, args));
    }


    public int test(Logger logger, String[] args) {
        setTestName("org.apache.harmony.test.func.reg.vm.btest6804.Test6804");
        setTimeout(15000);
        setShouldCheckExitCode(true);
        setShouldContainCrashString(false);
        return run(getCommand(args));
    }
}

class Test6804 {
    public static void main(String[] args) {
        System.out.println("Start test");
        new JScrollBar();
        new JButton();
        System.out.println("Test PASSED");
    }
}
