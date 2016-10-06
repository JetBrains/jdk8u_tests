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
 * Created on 31.08.2005
 *
 */

package org.apache.harmony.test.func.api.java.util.logging.Level;

import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.Enumeration;


import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;


class myLevel extends Level {
    public myLevel (String name, int value) {
        super(name, value);
    }
    public myLevel (String name, int value, String resourceBundleName) {
        super(name, value, resourceBundleName);
    }

}

public class logging_01 extends MultiCase{
    
    public final Level[] levels = {
                Level.OFF,
                Level.ALL,
                Level.SEVERE,
                Level.WARNING, 
                Level.INFO,
                Level.CONFIG, 
                Level.FINE, 
                Level.FINER, 
                Level.FINEST 
        };
    
    public final String[] strings = {
                "OFF",
                "ALL",
                "SEVERE",
                "WARNING",
                "INFO",
                "CONFIG",
                "FINE",
                "FINER",
                "FINEST"
        };    
    
    public final int[] values = {
                Integer.MAX_VALUE,
                Integer.MIN_VALUE,
                1000,
                900,
                800,
                700,
                500,
                400,
                300
        };
    
    //public final String bundleName = "org.apache.harmony.test.func.api.java.util.logging.Logger.logging_02_en";
    public final String bundleName = "org.apache.harmony.test.func.api.java.util.logging.Logger.someResources";
    
    public static void main(String[] args) {
        System.exit(new logging_01().test(args));
    }

    public Result testLevel1 () {
        
        Level myLevel1 = new myLevel("anyString", 992);
        Level myLevel2 = new myLevel("anyString", 992, null);
        if (!myLevel1.equals(myLevel2)) {
            return failed ("2 created levels should be equal");
        }
        return passed();
    }
    
    public Result testLevel2 () {
        Level myLevel1 = new myLevel("someString", 229, bundleName);
        if (!myLevel1.getName().equals("someString")) {
            return failed("Level(String, int, String) create a Level with wrong name");
        }
        if (myLevel1.intValue() != 229) {
            return failed("Level(String, int, String) create a Level with wrong value");
        }
        if (!myLevel1.getResourceBundleName().equals(bundleName)) {
            return failed("Level(String, int, String) create a Level with wrong bundle name");
        }
        return passed();
    }
    
    public Result testIntValue () {
        
        for (int i=0; i< levels.length; i++) {
            if (levels[i].intValue() != values[i]) {
                return failed ("intValue() returns " 
                        + levels[i].intValue() 
                        + ", expected " 
                        + values[i]);
            } 
        }
        return passed();
    }
    
    public Result testParse () {
        
        for (int i=0; i< levels.length; i++) {
            Level l = Level.parse(strings[i]);
            if (!l.toString().equals(strings[i])) {
                return failed ("parse(String) returns " 
                        + l.toString() 
                        + ", expected " 
                        + strings[i]);
            }
        }
        
        return passed();
    }
    
    public Result testToString () {
        
        for (int i=0; i< levels.length; i++) {
            String s = levels[i].toString();
            if (!s.equals(strings[i])) {
                return failed ("toString() returns " 
                        + s 
                        + ", expected " 
                        + strings[i]);
            }
        }
        return passed();
    }
        
    public Result testGetLocalizedName () {
        // non-localized values are the same as returned with toString()
        
        for (int i=0; i< levels.length; i++) {
            String s = levels[i].getLocalizedName();
            
            if (!s.equals(strings[i])) {
                return failed ("getLocalizedName() returns " 
                        + s 
                        + ", expected " 
                        + strings[i]);
            }    
        }
        
        // Localized values

        String levelName = "De_error";
        Level newLevel = new myLevel(levelName, 600, bundleName);
        
        //getLocalizedName() should return the same value as 
        //resourceBundle.getString(levelName)
        
        if (!newLevel.getLocalizedName().equals(ResourceBundle
                .getBundle(bundleName).getString(levelName))) {
            return failed ("getLocalizedName() returns "
                    + newLevel.getLocalizedName()
                    + " but should return the same value as " 
                    + "resourceBundle.getString(levelName)." );
        }
        
        //The result should be "fehler"
        
        if (!newLevel.getLocalizedName().equals("fehler")) {
            return failed ("getLocalizedName() returns "
                    + newLevel.getLocalizedName()
                    + " but should return: fahler. " );
                    
        }
        
        Enumeration e = ResourceBundle.getBundle(bundleName).getKeys();
        while (e.hasMoreElements()) {
            String currentLevelName = e.nextElement().toString();
            Level currentLevel = new myLevel(currentLevelName, 1, bundleName);
            
            if (!currentLevel.getLocalizedName().equals(ResourceBundle
                    .getBundle(bundleName).getString(currentLevelName))) {
                return failed ("getLocalizedName() returns "
                        + currentLevel.getLocalizedName()
                        + " but should return the same value as " 
                        + " resourceBundle.getString(levelName)." );
            }
        }
        
        return passed();
    }
        
}