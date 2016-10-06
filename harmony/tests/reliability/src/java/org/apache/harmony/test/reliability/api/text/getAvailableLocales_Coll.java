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
 * connected with use of following java.text.Collator methods:
 * - java.text.Collator.getAvailableLocales()
 * - java.text.Collator.getInstance()
 * - java.text.Collator.getInstance(Locale)
 * - java.text.Collator.setStrength(int)
 * - java.text.Collator.compare(...)
 * 
 * The test does:
 * 1. Reads parameters, which are: 
 *       param[0] - number of iterations in each thread. 
 * 
 * 2. Obtain array of strings to be sorted by method collator.compare(...)
 * 3. In a cycle sort this array for default Locale and for all available Locales, 
 *       obtained by Collator.getAvailableLocales().
 *       Comparison occurs in all possible modes: Collator.PRIMARY, Collator.SECONDARY, 
 *    Collator.TERTIARY, Collator.IDENTICAL.
 *    
 * 4. Runs System.gc()
*/

public class getAvailableLocales_Coll extends Test {

    public static int callSystemGC = 1;

    public static int NUMBER_OF_ITERATIONS = 100;

    public static void main(String[] args) {
        System.exit(new getAvailableLocales_Coll().test(args));
    }

    public int test(String[] params) {
    
        boolean failed = false;

        parseParams(params);

        String[] WordsToCompare = new String [7];

        String eWithCircumflex = new String("\u00EA");
        String eWithAcute = new String("\u00E9");
        String ash = new String("\u00E6");
              
        WordsToCompare[0] = "p" + eWithAcute + "ch" + eWithAcute;
        WordsToCompare[1] = "p" + eWithCircumflex + "ch" + ash;
        WordsToCompare[2] = "p" + ash + "che";
        WordsToCompare[3] = "peche";
        WordsToCompare[4] = "Peche";
        WordsToCompare[5] = "which";
        WordsToCompare[6] = "which";
     
        for (int k = 0; k < NUMBER_OF_ITERATIONS; k++) {
            
            compare(WordsToCompare);                                                     
            Locale[] allLocals = Collator.getAvailableLocales();

            for(int i = 0; i < allLocals.length; i++){                    
                compare(WordsToCompare, allLocals[i]);                
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

    public void compare(String[] Words) {

        //log.add("Default locale " + (Locale.getDefault()).toString());                
        Collator Col = 
            Collator.getInstance();

        Col.setStrength(Collator.PRIMARY);
        comp(Col, Words);
        Col.setStrength(Collator.SECONDARY);
        comp(Col, Words);
        Col.setStrength(Collator.TERTIARY);
        comp(Col, Words);
        Col.setStrength(Collator.IDENTICAL);
        comp(Col, Words);

    }
    

    public void compare(String[] Words, Locale currentLocale) {

        //log.add("Used locale " + (currentLocale.toString()));        
        Collator Col = 
            Collator.getInstance(currentLocale);

        Col.setStrength(Collator.PRIMARY);
        comp(Col, Words);
        Col.setStrength(Collator.SECONDARY);
        comp(Col, Words);
        Col.setStrength(Collator.TERTIARY);
        comp(Col, Words);
        Col.setStrength(Collator.IDENTICAL);
        comp(Col, Words);

    }    


    public void comp(Collator collator, String[] words) {

        String tmp;
        String[] WordsToSort = new String[words.length];
        for(int i = 0; i < WordsToSort.length; i++){
            WordsToSort[i] = new String(words[i]);
        }
        for (int i = 0; i < WordsToSort.length; i++) {
            for (int j = i + 1; j < WordsToSort.length; j++) {
              
                if (collator.compare(WordsToSort[i], WordsToSort[j] ) > 0 ) {
                  
                    tmp = WordsToSort[i];
                    WordsToSort[i] = WordsToSort[j];
                    WordsToSort[j] = tmp;
                }
            }
        }
        //for (int i = 0; i < WordsToSort.length; i++){
        //    log.add(" "+WordsToSort[i]);
        //}
        //log.add("------------------");
    }

}           
