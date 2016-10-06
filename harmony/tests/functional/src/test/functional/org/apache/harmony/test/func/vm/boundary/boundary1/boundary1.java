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
package org.apache.harmony.test.func.vm.boundary.boundary1;

import org.apache.harmony.share.Test;
import java.util.Random;

/**
 */

public class boundary1 extends Test {

    public static void main(String[] args) {
        System.exit(new boundary1().test(args));
    }

    public int test() {

        try {
            Random rr = new Random(69846L);
            double[][][][][][][][][][] fff = new double[6][6][6][6][6][6][6][6][5][5];            
            for (int q = 0; q < 6; q++) {
                for (int w = 0; w < 6; w++) {
                    for (int e = 0; e < 6; e++) {
                        for (int r = 0; r < 6; r++) {
                            for (int t = 0; t < 6; t++) {
                                for (int y = 0; y < 6; y++) {
                                    for (int u = 0; u < 6; u++) {
                                        for (int i = 0; i < 6; i++) {
                                            for (int o = 0; o < 5; o++) {
                                                for (int p = 0; p < 5; p++) {

                                                    fff[q][w][e][r][t][y][u][i][o][p] = rr.nextDouble();
                                                    
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }

            return pass();

        } catch (OutOfMemoryError OME) {
            return pass();

        } catch (Exception ex) {

            return fail("FAILED");

        }
        
    }

}
