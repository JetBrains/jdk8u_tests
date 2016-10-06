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

package org.apache.harmony.test.func.api.java.util.logging.Formatter;

import java.util.ResourceBundle;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;


class myFormatter extends Formatter {
    public myFormatter (){
        super();
    }
    public String format(LogRecord arg0) {
        return null;
    }

}

public class logging_01 extends MultiCase{

    //public final String bundleName = "org.apache.harmony.test.func.api.java.util.logging.Logger.logging_02_en";
    public final String bundleName = "org.apache.harmony.test.func.api.java.util.logging.Logger.someResources";
    public final ResourceBundle bundle = ResourceBundle.getBundle(bundleName);
    
    public Formatter formatter;
    public Logger logger;
    public LogRecord record;
    public Handler h;
    
    public static void main(String[] args) {
        System.exit(new logging_01().test(args));
    }

    public Result testFormatter() {
        // Non-localized message    
    
        formatter = new myFormatter();
        logger = Logger.getLogger("test");
        record = new LogRecord(Level.INFO, "Non-localized message");
        if (!formatter.formatMessage(record).equals("Non-localized message")) {
            return failed ("Non-localized format error: " + formatter.formatMessage(record));
        }
        record.setMessage("{0} {1} {2}");
        if (!formatter.formatMessage(record).equals("{0} {1} {2}")) {
            return failed ("There are no parameters, java.text.MessageFormat should not work!");
        }
        
        // Localized message
        logger = Logger.getLogger("test", bundleName);
        record = new LogRecord(Level.INFO, "Localized");
        record.setResourceBundle(bundle);
        
        if (!formatter.formatMessage(record)
                    .equals(record.getResourceBundle().getString(record.getMessage()))) {
            return failed ("Localized format error: " + formatter.formatMessage(record));
        }
        record.getResourceBundleName();
        
        //Message with parameters
        Object parameters[] = {
                new String(" two "),
                new String(" 1230387592999 "),
                new String("\n...\n"),
                new Integer(18),
        };
        record = new LogRecord (Level.INFO, "With {0} {1} {2} {3} params");
        record.setParameters(parameters);
        
        if(!formatter.formatMessage(record).equals(
                java.text.MessageFormat.format(record.getMessage(), parameters))) {
            return failed ("Format with parameters works incorrect!" + formatter.formatMessage(record));
        }
        if (!formatter.formatMessage(record).equals("With  two   1230387592999  \n...\n 18 params")) {
            return failed ("Format with parameters works incorrect!" + formatter.formatMessage(record));
        }
        
        
        // getHead should return empty string
        h = new StreamHandler ();
        logger.addHandler(h);    
        if (!formatter.getHead(h).equals("")) {
            return failed ("Formatter.getHead() should return an empty string!");
        }
      

        return passed();
    }
        
}