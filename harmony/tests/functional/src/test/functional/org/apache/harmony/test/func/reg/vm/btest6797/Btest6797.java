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
/**
 */
package org.apache.harmony.test.func.reg.vm.btest6797;

import org.apache.harmony.test.share.reg.RegressionTest;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.PhantomReference;
import java.util.logging.Logger;

/**
 */
public class Btest6797 extends RegressionTest {

    static final int MAX_REF = 10000;
    static final int FIN_SIZE = 4096;

    public int test(Logger logger, String[] args) {
        ReferenceQueue queue = new ReferenceQueue();
        PhantomReference [] phantoms = new PhantomReference[MAX_REF];

        FinalizedObject obj = null;
        for(int i= 0; i < phantoms.length; i++) {
            Reference reference = queue.poll();
            
            while( reference != null) {
               reference.clear();
               System.out.println("reference = " + reference);
               reference = queue.poll();
            }

            phantoms[i] = new PhantomReference(obj, queue);
            obj = new FinalizedObject(FIN_SIZE, i);
        }
        return pass();
    }

    // to run test from console
    public static void main(String[] args) {
        System.exit(new Btest6797().test(Logger.global, args));
    }

    static class FinalizedObject {
       byte [] bytes;
       int number = 0;
       
       FinalizedObject(int size, int number) {
          bytes = new byte[size];
          this.number = number;
       }
       
       protected void finalize() {
           System.out.println("Finalize: " + number);
       }
    }
}
