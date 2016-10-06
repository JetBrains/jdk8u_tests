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

import org.apache.harmony.share.Test;

public class MemTest extends Test {


    public static void main(String[] args) {
        System.exit(new MemTest().test());
    }

    public int test() {

        Runtime r = Runtime.getRuntime();
        long maxMem = r.maxMemory();
        log.info("max memory: " + maxMem);// /1024/1024);

        long freeMem = r.freeMemory();
        log.info("free memory:  " + freeMem); // /1024/1024);


        long totalMem = r.totalMemory();
        log.info("total memory:  " + totalMem); // /1024/1024);

        return pass();
    }
}
