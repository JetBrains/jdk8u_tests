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
package org.apache.harmony.vts.test.vm.jvms.threads.threads212.threads21202;

/** 
 * @author Maxim N. Kurzenev
 * @version $Revision: 1.1 $
 */  
public class threads21202p {

    public int test(String[] args) {
        Thread t = new Thread() {
            public void run() {
                while (!Thread.interrupted()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                }
            } 
        };
        
        t.start();
        int res = t.isAlive() ? 104 : 105;
        t.interrupt();
        return res;
    }

    public static void main(String[] args) {
        System.exit(new threads21202p().test(args));
    }
}
