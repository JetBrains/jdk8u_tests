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

package org.apache.harmony.test.func.api.java.text.DateFormat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/*
 * 07.10.2005
 */
public class TestParse extends MultiCase {

    private boolean isEquals(Date a, Date b)
    {
        return (a.getTime() / 1000) == (b.getTime() / 1000);
    }

    private Result verifyPositive(DateFormat df, Date d, String t)
    {
        return verifyPositive(df, d, t, null);
    }
    
    private Result verifyPositive(DateFormat df, Date d, String t, ParsePosition pos)
    {
        try {
            Date p = ((pos == null) ? df.parse(t) : df.parse(t, pos));
            if (!isEquals(p, d)) {
                return failed("Expected '" + d + "' but got '" + p
                        + "' while parsing '" + t);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return failed("Unexpected exception of type "
                    + e.getClass().getName());
        }
        return passed();        
    }
    
    public Result testComplicated() {

        DateFormat df = DateFormat.getDateInstance();
        
        Calendar c = GregorianCalendar.getInstance();
        c.set(2007, 9, 7, 0, 0, 0);
        Date d = c.getTime();
        
        return verifyPositive(df, d, "October 07, 2007");
    }
    
    public Result testComplicatedNeg() {

        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, Locale.FRANCE);

        String t = "7 brumaire 215";

        try {
            df.parse(t);
            return failed("This calendar was dismissed in 1806");
        } catch (ParseException e) {
            return passed();
        }
    }
    
    public Result testLocalized()
    {
        DateFormat df = DateFormat.getDateInstance(DateFormat.FULL, Locale.FRENCH);
        
        String t = "mercredi 7 novembre 2007";
        Calendar c = GregorianCalendar.getInstance();
        c.set(2007, 10, 07, 0, 0, 0);
        Date d = c.getTime();
        
        return verifyPositive(df, d, t);
    }
    
    public Result testSimple()
    {
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
        String t = "07/07/07";
        Calendar c = GregorianCalendar.getInstance();
        c.set(2007, 06, 07, 0, 0, 0);
        Date d = c.getTime();
        return verifyPositive(df, d, t);
    }
    
    public Result testLenient()
    {
        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
        df.setLenient(true);        
        String t = "October 19, 2008";
        Calendar c = GregorianCalendar.getInstance();
        c.set(2008, 9, 19, 0, 0, 0);
        Date d = c.getTime();
        return verifyPositive(df, d, t);
    }
    
    public Result testLenientNeg()
    {
        DateFormat df = DateFormat.getDateInstance(DateFormat.FULL);
        df.setLenient(false);
        
        String t = "07/07/2007";
        try {
            df.parse(t);
            return failed("07/07/2007 is not in FULL form, so should not be parsed");
        } catch (ParseException e) {
            return passed();
        }
    }
    
    public Result testDateTimeInstance () {
        DateFormat df = DateFormat.getDateTimeInstance();

        String t = "Oct 19, 2008 3:38:22 PM";
        Calendar c = GregorianCalendar.getInstance();
        c.set(2008, 9, 19, 15, 38, 22);
        Date d = c.getTime();

        return verifyPositive(df, d, t);
    }
    
    public Result testSimpleDateFormat()
    {
        String pattern = "EEEE 'week' FF 'in' MMMM, MM/dd/yyyy";

        DateFormat df = new SimpleDateFormat(pattern);

        String t = "Defer it till sunday week 03 in october, 10/19/2008";
        ParsePosition pos = new ParsePosition(14);
        Calendar c = GregorianCalendar.getInstance();
        c.set(2008, 9, 19, 0, 0, 0);
        Date d = c.getTime();
        
        return verifyPositive(df, d, t, pos);
        
    }
    
    public static void main(String[] args) {
        System.exit(new TestParse().test(args));
    }

}
