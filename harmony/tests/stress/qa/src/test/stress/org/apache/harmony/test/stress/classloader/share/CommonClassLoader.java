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

package org.apache.harmony.test.stress.classloader.share;

import java.io.IOException;
import java.io.InputStream;

public class CommonClassLoader extends ClassLoader {
    public Class loadClass(String name, String name2) {
        byte[] data = new byte[0];
        byte[] piece = new byte[512];
        int len;
        try {
            InputStream is = getResourceAsStream(name);
            if (is == null) {
                return null;
            }
            while ((len = is.read(piece)) != -1) {
                byte[] tmp = data;
                data = new byte[tmp.length + len];
                System.arraycopy(tmp, 0, data, 0, tmp.length);
                System.arraycopy(piece, 0, data, tmp.length, len);
                tmp = null;
            }
            is.close();
        } catch (IOException ex) {
            throw new Error(ex);
        }
        return defineClass(name2, data, 0, data.length);
    }
}
