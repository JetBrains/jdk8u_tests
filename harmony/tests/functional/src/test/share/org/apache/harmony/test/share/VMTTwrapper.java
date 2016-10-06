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

package org.apache.harmony.test.share;

import java.io.File;
import org.apache.harmony.vmtt.VMTT;

public class VMTTwrapper {
    public static void main(String [] args) {
        String ccode = args[args.length-1];

        String dirname = ccode.substring(0, ccode.lastIndexOf(File.separator));
        dirname = dirname.substring(dirname.lastIndexOf("org"+File.separator+"apache"));

        String d_opt = args[args.length-2];
        if ("-d.".equals(d_opt)) {
			args[args.length-2] = "-d"+dirname;
            File dir = new File(dirname);
            if (! dir.exists()) {
	    		dir.mkdirs();
	    	}
		}

        System.out.print("Process -");
        for (int i=0; i<args.length; i++) {
			System.out.print(" "+ args[i]);
		}
        System.out.println();

		org.apache.harmony.vmtt.VMTT.main(args);
    }
}