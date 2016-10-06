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
 * Created on 30.08.2005
 *
 */

package org.apache.harmony.test.func.api.java.util.logging.Logger;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

public class logging_04 extends MultiCase{
    public static void main(String[] args) {
        System.exit(new logging_04().test(args));
    }

    public Result testLogp () throws IOException {
        String className = "org.apache.harmony.test.func.api.java.util.logging.Logger.logging_00";
        String methodName = "testLogp1";
        Logger logger = Logger.getLogger("org.apache.harmony.test.func.api.java.util.logging.Logger.logging_04");
   
        OutputStream stream = new ByteArrayOutputStream();
        //OutputStream stream = System.out;
        StreamHandler handler = new StreamHandler(stream, new SimpleFormatter());
        logger.addHandler(handler);
        logger.setUseParentHandlers(false);
        
        logger.logp(Level.INFO,  className, methodName, "Hello");
        handler.flush();
        

        byte[] buf = ((ByteArrayOutputStream)stream).toByteArray();
        String outS = new String(buf);
        
        // String from output stream
        // String outS = new String(buf);
        
        InputStream in = new ByteArrayInputStream(buf);      
        byte[] bufferArray = new byte[in.available()];
        in.read(bufferArray);
        String inS = new String(bufferArray);
        
        String[] parts = inS.split("[ \r\n]");
        
        boolean rightClassName = false;
        boolean rightMethodName = false;
        for (int i=0; i<parts.length; i++) {
            
            if (parts[i].equals(className)) {
                rightClassName = true;
            }
            if (parts[i].equals(methodName)) {
                rightMethodName = true;
            }
        }
        if (!rightClassName) {
            return failed ("Logp logs the wrong class name: " + inS);
        }
        if (!rightMethodName) {
            return failed ("Logp logs the wrong method name: " + inS);
        }
        
        
        
        return passed();    
    }
}
