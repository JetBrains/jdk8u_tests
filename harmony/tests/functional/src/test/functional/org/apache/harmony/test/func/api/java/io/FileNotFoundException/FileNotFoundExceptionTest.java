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
/*
 * Created on 01.12.2004
 *  
 */
package org.apache.harmony.test.func.api.java.io.FileNotFoundException;

import java.io.FileNotFoundException;

import org.apache.harmony.test.func.api.java.io.share.IOMultiCase;
import org.apache.harmony.test.func.api.java.io.share.MultiThreadRunner;
import org.apache.harmony.share.Result;

public final class FileNotFoundExceptionTest extends IOMultiCase {
    public static void main(String[] args) {
        System.exit(MultiThreadRunner.run(new FileNotFoundExceptionTest(), args));
    }

    public Result testConstructor0001() {
        MultiThreadRunner.waitAtBarrier();
        if(new FileNotFoundException().getMessage() != null) {
            return failed("expected new FileNotFoundException().message() to be null");
        }
        return passed();
    }

    public Result testConstructor0002() {
        MultiThreadRunner.waitAtBarrier();
        if(!new FileNotFoundException("foobar").getMessage().equals("foobar")) {
            return failed("expected new FileNotFoundException().message(<string>) to be <string>");
        }
        return passed();
    }

}
