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
 * @version $Revision: 1.1 $
 */  

/**
 * Class that can interrupt InterruptibleTest after the specified timeout
 */
public class Interruptor extends Thread {
    InterruptibleTest t;
    int delay;

    public Interruptor(InterruptibleTest t, int delay) {
        this.t = t;
        this.delay = delay;
    }

    public void run() {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
        }
        t.interrupt();
    }
}