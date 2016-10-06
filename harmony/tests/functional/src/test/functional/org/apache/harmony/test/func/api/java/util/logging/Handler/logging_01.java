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

package org.apache.harmony.test.func.api.java.util.logging.Handler;


import java.io.UnsupportedEncodingException;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;


class myHandler extends Handler {
    public myHandler () {
        super();
    }
    public void close () {}
    public void flush() {}

    public void publish(LogRecord lr) { }
}

public class logging_01 extends MultiCase{
    public static void main(String[] args) {
        System.exit(new logging_01().test(args));
    }

    public Result testSetGetEncoding() throws SecurityException, UnsupportedEncodingException {

        Handler handler = new myHandler();
        

        String encodings[] = {
                "koi8-r", 
                "UTF-8",  
                "cp1251", 
                "cp1255", 
                "cp1256",
                "cp1252",
                "ISO8859-1",
                };
        
        for (int i=0; i< encodings.length; i++) {
            handler.setEncoding(encodings[i]);
            String enc = handler.getEncoding();
            //System.out.println(enc);
            if (!enc.equals(encodings[i])) {
                return failed ("wrong encoding: " + enc + ", should be " + encodings[i]);
            }
        }
        return passed();
    }
        
}