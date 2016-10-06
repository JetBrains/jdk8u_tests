/*
    Copyright 2005-2006 The Apache Software Foundation or its licensors, as applicable

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

    See the License for the specific language governing permissions and
    limitations under the License.
*/
package org.apache.harmony.vts.test.vm.jvms.threads.share;

/** 
 * @author Maxim N. Kurzenev
 * @version $Revision: 1.2 $
 */  

/**
 * Base class for tests that need internal timeout value.
 */
public abstract class timedTest {
    private int delay;
    
    void parseArgs(String[] args) {
        if (args == null || args.length < 2) {
            throw new IllegalArgumentException("parameters should be defined");
        }
        if (!"delay".equalsIgnoreCase(args[0])) {
            throw new IllegalArgumentException("Should provide delay parameter");
        }
        try {
            delay = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Delay should be an integer");
        }
    }

    public int test(String[] args) throws InterruptedException {
         parseArgs(args);
         return testTimed(delay);
    }

    public abstract int testTimed(int delay) throws InterruptedException;
}