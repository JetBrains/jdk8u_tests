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
 * @author Olessia Salmina
 */

package org.apache.harmony.test.reliability.api.text;

import org.apache.harmony.test.reliability.share.Test;
import java.util.*;
import java.text.*;


/**
 * Goal: 
 *  find resource leaks (or intermittent failures, or cache problems), 
 *  connected with use of following java.text.BreakIterator methods:
 * - BreakIterator.getSentenceInstance(Locale)
 * - BreakIterator.setText(String)
 * - BreakIterator.next()
 * 
 * NOTE: test does not check, that sentence-breaks are set correctly.
 * 
 * The test does:
 * 
 * 1. Reads parameters, which are: 
 *         param[0] - number of threads to be run in parallel 
 *         param[1] - number of iterations in each thread
 * 
 * 2. Obtains array of sinrings with different sentances and different punctuation marks.
 * 3. Parse these strings in a cycle.
 * 4. Runs System.gc()
 */ 
 


public class getSentenceInstance  extends Test {

    public int callSystemGC = 1;

    public int NUMBER_OF_ITERATIONS = 100;

    public int numThreads = 10;
        
    public int[] statuses;

    public static void main(String[] args) {
        System.exit(new getSentenceInstance ().test(args));
    }


    public int test(String[] params) {
    
        parseParams(params);
                                
        // Start 'numThreads' threads each reading from file, inflating/deflating

        Thread[] t = new Thread[numThreads];

        statuses = new int[t.length];
                                
        for (int i = 0; i < t.length; i++) {
            t[i] = new Thread(new SentenceInstanceRunner(i, this));
            t[i].start();
            //log.add("Thread " + i + " started");
        }
                
        // Correctly wait for all threads to finish

        for (int i = 0; i < t.length; ++i){
            try {
                t[i].join();
                //log.add("Thread " + i + ": joined() ");

            } catch (InterruptedException ie){
                return fail("interruptedException while join() of thread #" + i);
            }
        }

        // For each thread check whether operations/checks PASSed in the thread

        for (int i = 0; i < statuses.length; ++i){
            if (statuses[i] != Status.PASS){
                return fail("thread #" + i + " returned not PASS status");
            }
            //log.add("Status of thread " + i + ": is PASS");
        }

        return pass("OK");
    }


    public void parseParams(String[] params) {

        if (params.length >= 1) {
            numThreads = Integer.parseInt(params[0]);
        }        

        if (params.length >= 2) {
            NUMBER_OF_ITERATIONS = Integer.parseInt(params[1]);
        }        

    }

}

class SentenceInstanceRunner implements Runnable {

    public int id;
    public getSentenceInstance base;

    public SentenceInstanceRunner(int id, getSentenceInstance base) {
        this.id = id;
        this.base = base;
    }



    public void run() {
        int k = 0;
        while (k++ < base.NUMBER_OF_ITERATIONS) {        

            Thread.yield();

            String[] toParse = new String[14];
            toParse[0] = "\"What\'s the matter?..\"";
            toParse[1] = "I was pretty shaken up!..";
            toParse[2] = "\"I was pretty shaken up!..\"";
            toParse[3] = "\"I was pretty shaken up,\" - he sad.";
            toParse[4] = "\"I was pretty shaken up.\" - he sad.";
            toParse[5] = "\"I was pretty shaken up...\" - he sad.";
            toParse[6] = "\"I was pretty shaken up!..\" - he sad.";
            toParse[7] = "W3C\'s easy-to-use HTML validation service, based on ... an SGML parser.";
            toParse[8] = "Published Wednesday 21st December 2006 11:58 GMT";
            toParse[9] = "I.e. is the abbreviation for a two-word Latin term, id est. "
                + "Translated word for word, id est means\"that is.\"";
            toParse[10] = "This specification defines the HyperText Markup Language (HTML),"
                + "version 4.0, the publishing language...";
            toParse[11] = "Published 2005-08-10";
            toParse[12] = "Number pi is approximately equal to 3.14.";
            toParse[13] = "Number pi is approximately equal to 3.14";

            for (int i = 0; i < toParse.length; i++) {

                SIterator_Marker SI = new SIterator_Marker(toParse[i]);

                Thread.yield();

                int boundary = SI.sentenceIterator.first();

                while (boundary != BreakIterator.DONE) {
                    SI.markers.setCharAt(boundary, '^');
                    boundary = SI.sentenceIterator.next();
                    Thread.yield();
                } //while

                //base.log.add(" " + SI.ScanStr + "\r\n"
                //        + "-----------------------" + SI.markers);
                //base.log.add(" "+SI.markers);

                Thread.yield();

            }//for

            if (base.callSystemGC != 0) {
                System.gc();
            }

        }//while
        base.statuses[id] = Status.PASS;
    }
}

    //class Status {
    //    public static final int FAIL = -10;
    //    public static final int PASS = 10;
    //}

class SIterator_Marker {
    public BreakIterator sentenceIterator;

    public StringBuffer markers;

    public String ScanStr;

    public SIterator_Marker(String toScan) {
        Locale currentLocale = new Locale("en", "US");
        sentenceIterator = BreakIterator.getSentenceInstance(currentLocale);

        markers = new StringBuffer();

        sentenceIterator.setText(toScan);

        markers.setLength(toScan.length() + 1);
        for (int j = 0; j < markers.length(); j++) {
            markers.setCharAt(j, ' ');

            ScanStr = new String(toScan);
        }

    }
}