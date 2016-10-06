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
 * Goal: find resource leaks (or intermittent failures, or cache problems), 
 * connected with use of following java.text.BreakIterator methods:
 *  - BreakIterator.getAvailableLocales()
 *  - BreakIterator.getCharacterInstance()
 *  - BreakIterator.getCharacterInstance(Locale)
 *  - BreakIterator.getWordInstance()
 *  - BreakIterator.getWordInstance(Locale)
 *  - BreakIterator.getSentenceInstance()
 *  - BreakIterator.getSentenceInstance(Locale)
 *  - BreakIterator.getLineInstance()
 *  - BreakIterator.getLineInstance(Locale)
 *  - BreakIterator.setText(String)
 *  - BreakIterator.first()
 *  - BreakIterator.next()
 *  
 * The test does:
 * 
 * 1. Reads parameters, which are: 
 *       param[0] - number of iterations in each thread. 
 * 
 * 2. a. Obtains SomeText to parse
 *       b. NUMBER_OF_ITERATIONS times in a cycle marks out characters, words,
 *          sentances and lines for tis text. This maring is made for default Locale
 *          and for all available Locales, obtainted by BreakIterator.getAvailableLocales().
 *       c. Runs System.gc()
 */


public class BreakIterator_AllLocales extends Test {


    public static int callSystemGC = 1;

    public static int NUMBER_OF_ITERATIONS = 100;

    public static void main(String[] args) {
        System.exit(new BreakIterator_AllLocales().test(args));
    }


    public int test(String[] params) {
    
        boolean failed = false;

        parseParams(params);

        String SomeText = " Some text ... by BreakIterator.getWordInstance()" +
            " some text some text some text some text some text \' t" + 
            " some text some text some text some text some text" +
            " some text some text some text some text some text" +
            " some text some text some text some text some text."+
            " some text some text some text, some text some text some text some text" + 
            " some text some text: Some text some text some text some text" +
            " some text (some text some text," +
            " a CJK some text, a Hangul syllable, a Kana character, etc.), then the text" +
            " some text some text some text; some text, it\'s the material between words.)";
    
        for (int k = 0; k < NUMBER_OF_ITERATIONS; k++) {

            parseCharacter(SomeText);
            parseWord(SomeText);
            parseSentence(SomeText);
            parseLine(SomeText);
                                         
            Locale[] allLocals = BreakIterator.getAvailableLocales();

            for(int i = 0; i < allLocals.length; i++){

                parseCharacter(SomeText, allLocals[i]);
                parseWord(SomeText, allLocals[i]);
                parseSentence(SomeText, allLocals[i]);
                parseLine(SomeText, allLocals[i]);

            }

            if (callSystemGC != 0){
                System.gc();
            }
        }


        return failed == true ? fail("failed") : pass("OK");
    }


    public void parseParams(String[] params) {

        if (params.length >= 1) {
            NUMBER_OF_ITERATIONS = Integer.parseInt(params[0]);
        }        

    }



    public void parseCharacter(String toParse) {        

        //log.add("Default locale" + (Locale.getDefault()).toString());

        BreakIterator CharIterator =
            BreakIterator.getCharacterInstance();    

        CharIterator.setText(toParse);
        
        int boundary = CharIterator.first();

        //log.add("Character boundaries are");
        
        while (boundary != BreakIterator.DONE) {
            //log.add(" "+boundary);
            boundary = CharIterator.next();
        }

    }    


    public void parseCharacter(String toParse, Locale currentLocale) {        

        //log.add("Used locale" + (currentLocale.toString()));

        BreakIterator CharIterator =
            BreakIterator.getCharacterInstance(currentLocale);    

        CharIterator.setText(toParse);
        
        int boundary = CharIterator.first();

        //log.add("Character boundaries are");
        
        while (boundary != BreakIterator.DONE) {
            //log.add(" "+boundary);
            boundary = CharIterator.next();
        }
    }

    public void parseWord(String toParse) {

        //log.add("Default locale" + (Locale.getDefault()).toString());        
        BreakIterator wordIterator = 
            BreakIterator.getWordInstance();

        wordIterator.setText(toParse);

        int start = wordIterator.first();
        int finish = wordIterator.next();

        while (finish != BreakIterator.DONE) {
            String word = toParse.substring(start,finish);
            if (Character.isLetterOrDigit(word.charAt(0))) {
                //log.add(word);
            }
            start = finish;
            finish = wordIterator.next();
        }

    }    


    public void parseWord(String toParse, Locale currentLocale) {

        //log.add("Used locale" + (currentLocale.toString()));
        BreakIterator wordIterator = 
            BreakIterator.getWordInstance(currentLocale);

        wordIterator.setText(toParse);

        int start = wordIterator.first();
        int finish = wordIterator.next();

        while (finish != BreakIterator.DONE) {
            String word = toParse.substring(start,finish);
            if (Character.isLetterOrDigit(word.charAt(0))) {
                //log.add(word);
            }
            start = finish;
            finish = wordIterator.next();
        }
    }

    public void parseSentence(String toParse) {        
        //log.add("Default locale" + (Locale.getDefault()).toString());        

        BreakIterator sentenceIterator = 
            BreakIterator.getSentenceInstance();

        sentenceIterator.setText(toParse);

        StringBuffer markers = new StringBuffer();
        markers.setLength(toParse.length() + 1);
        for (int k = 0; k < markers.length(); k++) {
            markers.setCharAt(k,' ');
        }

        int boundary = sentenceIterator.first();
            
        while (boundary != BreakIterator.DONE) {
            markers.setCharAt(boundary,'^');
            boundary = sentenceIterator.next();
        }

        //log.add(" "+toParse);
        //log.add(" "+markers);
    }    


    public void parseSentence(String toParse, Locale currentLocale) {        

        //log.add("Used locale" + (currentLocale.toString()));

        BreakIterator sentenceIterator = 
            BreakIterator.getSentenceInstance(currentLocale);

        sentenceIterator.setText(toParse);

        StringBuffer markers = new StringBuffer();
        markers.setLength(toParse.length() + 1);
        for (int k = 0; k < markers.length(); k++) {
            markers.setCharAt(k,' ');
        }

        int boundary = sentenceIterator.first();
            
        while (boundary != BreakIterator.DONE) {
            markers.setCharAt(boundary,'^');
            boundary = sentenceIterator.next();
        }

        //log.add(" "+toParse);
        //log.add(" "+markers);


    }

    public void parseLine(String toParse) {        

        //log.add("Default locale" + (Locale.getDefault()).toString());        
        BreakIterator lineIterator = 
            BreakIterator.getLineInstance();

        lineIterator.setText(toParse);

        StringBuffer markers = new StringBuffer();
        markers.setLength(toParse.length() + 1);
        for (int k = 0; k < markers.length(); k++) {
            markers.setCharAt(k,' ');
        }

        int boundary = lineIterator.first();
            
        while (boundary != BreakIterator.DONE) {
            markers.setCharAt(boundary,'^');
            boundary = lineIterator.next();
        }

        //log.add(" "+toParse);
        //log.add(" "+markers);

    }    


    public void parseLine(String toParse, Locale currentLocale) {
        //log.add("Used locale" + (currentLocale.toString()));        
        BreakIterator lineIterator = 
            BreakIterator.getLineInstance(currentLocale);

        lineIterator.setText(toParse);

        StringBuffer markers = new StringBuffer();
        markers.setLength(toParse.length() + 1);
        for (int k = 0; k < markers.length(); k++) {
            markers.setCharAt(k,' ');
        }

        int boundary = lineIterator.first();
            
        while (boundary != BreakIterator.DONE) {
            markers.setCharAt(boundary,'^');
            boundary = lineIterator.next();
        }

        //log.add(" "+toParse);
        //log.add(" "+markers);
    }

}
           
