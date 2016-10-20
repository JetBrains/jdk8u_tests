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
 * @author Anton Luht
 * @version $Revision: 1.3 $
 */
/*
 * Created on 28.12.2004
 *
 */

package org.apache.harmony.test.stress.api.java.io.share;

import org.apache.harmony.share.Result;
import org.apache.harmony.share.MultiCase;

public class IOMultiCase extends MultiCase {
    public void parseArgs(String[] params) {
        if (params == null || params.length == 0) {
            return;
        }
        try {
            for (int i = 0; i < params.length; i++) {
                if ("-reference".equalsIgnoreCase(params[i])) {
                    Utils.REFERENCE_RUNTIME = params[++i];
                } else if ("-tested".equalsIgnoreCase(params[i])) {
                    Utils.TESTED_RUNTIME = params[++i];
                } else if ("-temp".equalsIgnoreCase(params[i])) {
                    Utils.TEMP_STORAGE = params[++i];
                } else if ("-threads".equalsIgnoreCase(params[i])) {
                    try {
                        Utils.THREADS = Integer.parseInt(params[++i]);
                    } catch (Throwable e) {
                        Utils.THREADS = 1;
                    }
                } else if ("-usedCP".equalsIgnoreCase(params[i])) {
                    Utils.REFERENCE_CLASSPATH = Utils.TESTED_CLASSPATH = params[++i];
                } else if ("-bootCP".equalsIgnoreCase(params[i])) {
                    if (params[i + 1].startsWith("-X")) {
                        Utils.TESTED_RUNTIME_PARAMS = params[++i];
                    }
                } else if ("-generalVMOption".equalsIgnoreCase(params[i])) {
                    if (params[i + 1].startsWith("-")) {
                        Utils.VM_OPTION = params[++i];
                    }
                } 
            }
        } catch (Throwable e) {
            e.printStackTrace();
            log.info("Unexpected exception in parseArgs " + e);
        }
        super.parseArgs(params);
    }

    public int test(String[] arg0) {
        try {
            parseArgs(arg0);
            return super.test(arg0);
        } catch (Throwable e) {
            e.printStackTrace();
            log.info("Unexpected exception in parseArgs " + e);
            return Result.FAIL;
        }
    }
}
