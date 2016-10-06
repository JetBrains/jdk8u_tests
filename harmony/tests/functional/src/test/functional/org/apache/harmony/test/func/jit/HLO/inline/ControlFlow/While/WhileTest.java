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
package org.apache.harmony.test.func.jit.HLO.inline.ControlFlow.While;

import java.util.Random;

import org.apache.harmony.share.Test;


/**
 */

/*
 * Created on 9.11.2005
 * 
 * Jitrino HLO test
 * 
 */

public class WhileTest extends Test {  
    
    public static void main(String[] args) {
        System.exit(new WhileTest().test(args));
    }
    
    int check = 0;
    boolean flag = new Random().nextBoolean();
    
    public int test() {
        log.info("Start WhileTest ...");
        log.info("Flag == " + flag);
        try {
            for (int i=0; i<10000; ) {
                do {
                    check+=inlineMethod(i++);
                    
                } while (false);
            }
            log.info("Check value == "+ check);
            if ((check==-49995000 && flag==true) ||  
                    (check==49995000 && flag==false)) return pass();
            else return fail("TEST FAILED: check value != 49995000");
        } catch (Throwable e) {
            log.add(e);
            return fail("TEST FAILED: unxpected " + e);
        }
    }

    final int inlineMethod(int i) {
        do {
            while (flag) {
                check = -1;
                do {
                    return i*=-1;
                } while (false);
            }
        } while (flag);
        return i;
    }

}
