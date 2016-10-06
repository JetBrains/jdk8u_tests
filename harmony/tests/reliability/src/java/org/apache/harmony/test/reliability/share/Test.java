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
 * @author Oleg Oleinik
 * @version $Revision: 1.1 $
 */

package org.apache.harmony.test.reliability.share;

public abstract class Test {

    public static Log log = new Log();

    public int pass() {
        return pass("PASSED");
    }

    public int pass(String msg) {
        log.add(msg);
        return Result.PASS;
    }

    public int fail(String msg) {
        log.add(msg);
        return Result.FAIL;
    }

    public int error(String msg) {
        log.add(msg);
        return Result.ERROR;
    }

    public int test(String[] args) {
        return Result.PASS;
    }
}
    