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
package org.apache.harmony.test.func.api.java.nio.channels.FileLock;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

public final class FileLockTest extends MultiCase {
    public static void main(String[] args) {
        System.exit(new FileLockTest().test(args));
    }
    
    public Result testConstructor() throws IOException {
        File f = File.createTempFile("xxx", "yyy");

        f.deleteOnExit();
        FileInputStream fis = new FileInputStream(f);

        long position = (long) Math.abs(Math.random());
        long size = (long) Math.abs(Math.random());
        boolean shared = Math.random() > 0;
        
        FileLock fl = new FileLockImpl(fis.getChannel(), position, size, shared);
        if(fl.position() != position || fl.size() != size || fl.isShared() != shared) {
            return failed("failed at " + position + ", " + size + ", " + shared);
        }
        
        return passed();
    }
}

class FileLockImpl extends FileLock {
    protected FileLockImpl(FileChannel arg0, long arg1, long arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }

    public void release() throws IOException {
    }

    public boolean isValid() {
        return false;
    }
}
 