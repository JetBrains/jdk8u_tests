/*
 * Copyright 2006 The Apache Software Foundation or its licensors, as applicable
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author Oleg V. Oleinik
 * @version $Revision: 1.1 $
 */

package org.apache.harmony.test.reliability.api.nio.channels.filechannel;

import java.util.Random;
import java.io.File;


public class Utils {

    static Random r = new Random(10);

    public static File createWorkFile(String workFileName) throws Exception {
  
        File f = new File(workFileName);

        if (f.exists()){
            f.delete();
        }

        return f;
    }



    public static byte[] createRndBytes(int size) {
        return createRndBytes(size, null);
    }


    public static byte[] createRndBytes(int size, byte[] exclusion) {

        byte[] b = new byte[size];

        for (int i = 0; i < b.length; ++i){

            byte b_value = (byte) r.nextInt(Byte.MAX_VALUE);

            if (exclusion != null && (b_value >= exclusion[0] && b_value <= exclusion[1])) {
                continue;
            }

            b[i] = b_value;
        }

        return b;
    }

}

