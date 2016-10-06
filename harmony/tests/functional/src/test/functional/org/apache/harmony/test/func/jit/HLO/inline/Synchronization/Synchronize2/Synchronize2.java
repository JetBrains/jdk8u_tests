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
package org.apache.harmony.test.func.jit.HLO.inline.Synchronization.Synchronize2;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 16.11.2005
 * 
 * Jitrino HLO test
 * 
 */

public class Synchronize2 extends Test {
    
    public static void main(String[] args) {
        System.exit((new Synchronize2()).test());
    }

    public int test() {
        log.info("Start Synchronize2 test..."); 
        try {
            new TestThread().run();
            return fail("TEST FAILED: NullPointerException was expected");
        } catch (NullPointerException e) { 
            return pass();
        } catch (Throwable e) { 
            log.info(e.toString());
            log.add(e);
            return fail("TEST FAILED: unexpected " + e);
        }
    }
}
    
class TestThread implements Runnable {
    
    private AuxiliaryClass au;
    
    public void run() {
        synchronized (au) {
            au = new AuxiliaryClass();
        }
    }
}

class AuxiliaryClass  { 
    AuxiliaryClass() {    
    }
}
   
  




