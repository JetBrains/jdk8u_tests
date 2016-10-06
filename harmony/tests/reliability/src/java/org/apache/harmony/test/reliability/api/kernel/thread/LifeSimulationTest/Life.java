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
 * @author Igor A. Pyankov
 * @version $Revision: 1.2 $
 */

package org.apache.harmony.test.reliability.api.kernel.thread.LifeSimulationTest;

import org.apache.harmony.test.reliability.share.Test;
import java.util.Random;

/**
 *  The test emulate life of primary creatures ("Life" game by John Horton Conway).
 *  (http://en.wikipedia.org/wiki/Conway's_Game_of_Life)
 *
 *  The laws of life are pretty simple:
 *      Every "creature" has 2 properties "age" and "health"
 *      and has ability "to feed".
 *      The "creature" passes away if
 *          - age is achieved age limit,
 *          - ate few of "food".
 *      The "creature" can generate new "creature" 
 *      if it is "adult" (age > 1/3 age limit) and has enough "health".
 *
 *  Also the more "creatures" means the less food.                
 *  All "creatures" have to die due to end of "food"
 */

public class Life extends Test {
    
    static int total = 0;
    static int maxAge = 100;
    static Random rand = new Random ();
    static int maxTotal = 200;
    static volatile int food = 100000;
    static int inithealth = 100;
    static Life base;    
    Object lock = new Object();
    
    public static void main(String[] args) {
        base = new Life();
        System.exit(base.test(args));
    }

    public int test(String[] params) {
        boolean br_started = false;
        parseParams(params);
        // log.add("maxTotal creatures = " + maxTotal);
        // log.add("maxAge of creature = " + maxAge);
        // log.add("initial health of creature = " + inithealth);
        // log.add("total amount of  food = " + food);

        total = 0;
        BigBrother br = new BigBrother();               
        for (int i = 0; i < maxTotal; i++) {
            new Creature(Integer.toString(i), lock).start();
            if (total > 2 && !br_started) {
                br.start();
                br_started = true;
            }             
        }
       
        try {
            br.join();
        } catch (InterruptedException e) {            
            e.printStackTrace();
        }
        return pass("OK");
    }

    public void parseParams(String[] params) {
        if (params.length >= 1) {
            maxTotal  = Integer.parseInt(params[0]);
            if (params.length >= 2) {            
                maxAge    = Integer.parseInt(params[1]);
                if (params.length >= 3) {
                    inithealth= Integer.parseInt(params[2]);
                    if (params.length >= 4) {
                        food = Integer.parseInt(params[3]);
                    }
                }
            }
        }        
    } 

}  

class BigBrother extends Thread { 
   
    public void run () {               
        while (Life.total > 0){
            // Life.base.log.add(Life.total + " food:"+ Life.food);
            Thread.yield();            
        }        
        return; 
    }   
}

class Creature extends Thread {
    private String name;
    private int age;
    private int health;  
    private Life base;
    private int child; 
    private Object lock;
    
    Creature (String name, Object lock) {
        age = 0;
        child = 0;
        health = Life.inithealth;
        this.name = name;
        base = Life.base;
        this.lock = lock;        
    }
    
    public void run () {
        incTotal();     
        //       base.log.add(name + " was born");
        while (age < Life.maxAge && health > 0){
            age++; 
            synchronized (lock) {
                feed();                        
                fission();     
            }            
            health--;
            //            base.log.add(name + " alive " + age + " h:" + health);
            Thread.yield();            
        }
        decTotal();
        //        base.log.add(name + " died");        
        return; 
    }
    
    
    private void fission() {
        if (age > Life.maxAge/3 
            && health > Life.inithealth/5
            && Life.total < Life.maxTotal) {            
            new Creature(name + "+" + (++child), lock).start();
            health -= 20;
            if (age > 4*Life.maxAge/5) {  //pretty old 
                health = 0;               // kill creature - no chance to live 
            }
        }        
    }

    private void feed() {
        int morsel = 0;
        int creature_share = 0;
        if (Life.total < Life.maxTotal && 0 < Life.food) {
            creature_share = Life.food/Life.total;               
            morsel = creature_share / (Life.rand.nextInt(99) + 1);   
            if (morsel == 0) {
                morsel = 2;
            }
            Life.food -= morsel;
            health += morsel;   
        } else {
            health -=50;
        }
        //       base.log.add(name + "/" + health + " feeds " + morsel + " of " + creature_share);
    }
    
    private void incTotal() {
        synchronized (lock) {
            Life.total++;    
        }            
    }
    
    private void decTotal() {
        synchronized (lock) {
            Life.total--;    
        }            
    }
    
}