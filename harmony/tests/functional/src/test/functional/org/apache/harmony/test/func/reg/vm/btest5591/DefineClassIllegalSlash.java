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
package org.apache.harmony.test.func.reg.vm.btest5591;

import java.io.InputStream;
import java.util.logging.Logger;

import org.apache.harmony.test.share.reg.RegressionTest;

/**
 * defineClass does not reject wrong class name
 * which contain symbol "/" instead of "." 
 * 
 */
public class DefineClassIllegalSlash extends RegressionTest {

    public int test(Logger logger, String[] args) {
        try {
            InputStream stream = ClassLoader.getSystemResource(
                        "org/apache/harmony/test/func/reg/vm/btest5591/DefineClassIllegalSlash.class").openStream();
            
            int size = stream.available();
            if (size == 0) {
                size = 2048; // class file used to be smaller than 2k
            }
            byte b[] = new byte[size];   
            size = stream.read(b);
            stream.close();           
            
            KlassLoader loader = new KlassLoader();
            loader.defineKlass("org/apache/harmony/test/func/reg/vm/btest5591/DefineClassIllegalSlash", b, 0, size);
            return FAILED;
        } catch (NoClassDefFoundError e) {
            return PASSED;
        } catch (Throwable e) {
            logger.warning("Unexpected error" + e);
            return ERROR;
        }
    }

    public static void main(String[] args) {
        System.exit(new DefineClassIllegalSlash().test(Logger.global, args));
    }
}

class KlassLoader extends ClassLoader {
    public Class defineKlass(String name, byte[] ar, int st, int len) {
        return super.defineClass(name, ar, st, len);
    }
}
