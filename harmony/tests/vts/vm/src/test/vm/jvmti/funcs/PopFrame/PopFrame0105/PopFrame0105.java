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
package org.apache.harmony.vts.test.vm.jvmti;

/** 
 * @author Valentin Al. Sitnick
 * @version $Revision: 1.1 $
 *
 */ 
public class PopFrame0105 {
    static boolean pause1 = true;
    static boolean pause2 = true;
    static boolean stop = false;

    static public void main(String args[]) {
        new Thread("agent") {
            public void run() {                
                special();            
                return;
            }
            public void special() {
                sub_special();
                return;
            }
            public void sub_special() {
                if (PopFrame0105.stop) return;
                System.err.print("\tjava: method (sub_special) is paused...\n");
                while (PopFrame0105.pause1) {
                    PopFrame0105.pause2 = false;
                    long x = 1;
                    for (int i = 0; i < 1000; i++) {
                        x *= i;
                    }
                }
                System.err.print("\tjava: method (sub_special) is UNpaused...\n");
                /*
                 * transfer control to native part.
                 */
                try {
                    throw new InterruptedException();
                } catch (Throwable tex) { }
                return;
            }            
        }.start();

        new Thread("control") {
            
            public void run() {
                special_method();
                return;
            }
            public void special_method() {
                boolean b = false;
                System.err.print("\tjava: method special_method waits...\n");
                while (PopFrame0105.pause2) {
                    try {
                        Thread.sleep(100);
                    } catch (Throwable tex) { }
                }
                /*
                 * transfer control to native part.
                 */
                try {
                    throw new InterruptedException();
                } catch (Throwable tex) { }

                PopFrame0105.pause1 = false;
                PopFrame0105.stop = true;

                System.err.print("\tjava: special_method finished...\n");

                return;
            }
        }.start();        
    }
}

        

