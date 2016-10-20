/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */    

/**
 * @author Alexander D. Shipilov
 * @version $Revision: 1.4 $
 */

package org.apache.harmony.test.stress.misc;

import junit.framework.TestCase;

import org.apache.harmony.test.stress.misc.MiscTestError;
import org.apache.harmony.test.share.stress.ReliabilityRunner;

public class ExceptionInitStackTrace49 extends TestCase {
    public static int h = 1;

    public void test() {
        StackTraceElement ste[];

        ExceptionInitStackTrace49 eist = new ExceptionInitStackTrace49();

        try {
            ExceptionInitStackTrace49.h = Eist1.i;
            ReliabilityRunner.debug("Test fail: error has not been thrown");
        } catch (ExceptionInInitializerError er) {
            ste = er.getStackTrace();
            for (int k = 0; k < ste.length - 1; k++) {
                if (!ste[k].getMethodName().equals("<clinit>")) {
                    ReliabilityRunner.debug("Wrong method name.  Instead of <clinit> "
                                    + ste[k].getMethodName());
                    ReliabilityRunner.debug("Test fail");
                    ReliabilityRunner.mainTest.addError(this, new MiscTestError());
                }
            }
            if (!ste[ste.length - 1].getMethodName().equals("main")) {
                ReliabilityRunner.debug("Wrong method name. Instead of main "
                                + ste[ste.length - 1].getMethodName());
                ReliabilityRunner.debug("Test fail");
                ReliabilityRunner.mainTest.addError(this, new MiscTestError());
            }
        }
    }
}

class Eist100 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = 1 / 0;
    }
}

class Eist1 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist2.i;
    }
}

class Eist2 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist3.i;
    }
}

class Eist3 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist4.i;
    }
}

class Eist4 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist5.i;
    }
}

class Eist5 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist6.i;
    }
}

class Eist6 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist7.i;
    }
}

class Eist7 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist8.i;
    }
}

class Eist8 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist9.i;
    }
}

class Eist9 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist10.i;
    }
}

class Eist10 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist11.i;
    }
}

class Eist11 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist12.i;
    }
}

class Eist12 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist13.i;
    }
}

class Eist13 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist14.i;
    }
}

class Eist14 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist15.i;
    }
}

class Eist15 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist16.i;
    }
}

class Eist16 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist17.i;
    }
}

class Eist17 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist18.i;
    }
}

class Eist18 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist19.i;
    }
}

class Eist19 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist20.i;
    }
}

class Eist20 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist21.i;
    }
}

class Eist21 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist22.i;
    }
}

class Eist22 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist23.i;
    }
}

class Eist23 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist24.i;
    }
}

class Eist24 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist25.i;
    }
}

class Eist25 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist26.i;
    }
}

class Eist26 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist27.i;
    }
}

class Eist27 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist28.i;
    }
}

class Eist28 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist29.i;
    }
}

class Eist29 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist30.i;
    }
}

class Eist30 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist31.i;
    }
}

class Eist31 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist32.i;
    }
}

class Eist32 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist33.i;
    }
}

class Eist33 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist34.i;
    }
}

class Eist34 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist35.i;
    }
}

class Eist35 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist36.i;
    }
}

class Eist36 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist37.i;
    }
}

class Eist37 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist38.i;
    }
}

class Eist38 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist39.i;
    }
}

class Eist39 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist40.i;
    }
}

class Eist40 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist41.i;
    }
}

class Eist41 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist42.i;
    }
}

class Eist42 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist43.i;
    }
}

class Eist43 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist44.i;
    }
}

class Eist44 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist45.i;
    }
}

class Eist45 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist46.i;
    }
}

class Eist46 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist47.i;
    }
}

class Eist47 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist48.i;
    }
}

class Eist48 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist49.i;
    }
}

class Eist49 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist50.i;
    }
}

class Eist50 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist51.i;
    }
}

class Eist51 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist52.i;
    }
}

class Eist52 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist53.i;
    }
}

class Eist53 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist54.i;
    }
}

class Eist54 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist55.i;
    }
}

class Eist55 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist56.i;
    }
}

class Eist56 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist57.i;
    }
}

class Eist57 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist58.i;
    }
}

class Eist58 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist59.i;
    }
}

class Eist59 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist60.i;
    }
}

class Eist60 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist61.i;
    }
}

class Eist61 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist62.i;
    }
}

class Eist62 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist63.i;
    }
}

class Eist63 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist64.i;
    }
}

class Eist64 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist65.i;
    }
}

class Eist65 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist66.i;
    }
}

class Eist66 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist67.i;
    }
}

class Eist67 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist68.i;
    }
}

class Eist68 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist69.i;
    }
}

class Eist69 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist70.i;
    }
}

class Eist70 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist71.i;
    }
}

class Eist71 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist72.i;
    }
}

class Eist72 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist73.i;
    }
}

class Eist73 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist74.i;
    }
}

class Eist74 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist75.i;
    }
}

class Eist75 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist76.i;
    }
}

class Eist76 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist77.i;
    }
}

class Eist77 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist78.i;
    }
}

class Eist78 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist79.i;
    }
}

class Eist79 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist80.i;
    }
}

class Eist80 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist81.i;
    }
}

class Eist81 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist82.i;
    }
}

class Eist82 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist83.i;
    }
}

class Eist83 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist84.i;
    }
}

class Eist84 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist85.i;
    }
}

class Eist85 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist86.i;
    }
}

class Eist86 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist87.i;
    }
}

class Eist87 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist88.i;
    }
}

class Eist88 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist89.i;
    }
}

class Eist89 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist90.i;
    }
}

class Eist90 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist91.i;
    }
}

class Eist91 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist92.i;
    }
}

class Eist92 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist93.i;
    }
}

class Eist93 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist94.i;
    }
}

class Eist94 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist95.i;
    }
}

class Eist95 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist96.i;
    }
}

class Eist96 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist97.i;
    }
}

class Eist97 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist98.i;
    }
}

class Eist98 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist99.i;
    }
}

class Eist99 {
    static int i = 1;
    static {
        ExceptionInitStackTrace49.h = Eist100.i;
    }
}
