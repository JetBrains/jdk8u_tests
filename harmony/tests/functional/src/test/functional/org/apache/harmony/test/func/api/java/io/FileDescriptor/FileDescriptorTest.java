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
package org.apache.harmony.test.func.api.java.io.FileDescriptor;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.SyncFailedException;

import org.apache.harmony.test.func.api.java.io.share.IOMultiCase;
import org.apache.harmony.test.func.api.java.io.share.MultiThreadRunner;
import org.apache.harmony.share.Result;

public final class FileDescriptorTest extends IOMultiCase {
    public static void main(String[] args) {
        System.exit(MultiThreadRunner.run(new FileDescriptorTest(), args));
    }

    public Result testNewValid() {
        MultiThreadRunner.waitAtBarrier();
        if (new FileDescriptor().valid()) {
            return failed("expected created from scratch descriptor to be invalid");
        }
        return passed();
    }

    public Result testStaticDescriptors() {
        MultiThreadRunner.waitAtBarrier();
        if (!FileDescriptor.err.valid() || !FileDescriptor.in.valid()
                || !FileDescriptor.out.valid()) {
            return failed("expected created static FileDescriptor fields to be valid");
        }
        return passed();
    }

    public Result testSync() throws Exception {
        MultiThreadRunner.waitAtBarrier();
        FileDescriptor fd = new FileDescriptor();
        try {
            fd.sync();
            return failed("expected new FileDescriptor().sync() to throw exception");
        } catch (SyncFailedException e) {
        }

        File f = File.createTempFile("abcd", "xyz");
        f.deleteOnExit();

        FileOutputStream fos = new FileOutputStream(f);
        fd = fos.getFD();
        fos.write(new byte[] { 0 });
        fd.sync(); // no exception

        fos.close();
        try {
            fos.write(new byte[] { 0 });
            return failed("expected IOException writing to a closed stream");
        } catch (IOException e) {
        }

        try {
            fd.sync(); //no exception
            return failed("expected SyncFailedException syncing closed stream");
        } catch (SyncFailedException e) {
        }

        return passed();
    }

}