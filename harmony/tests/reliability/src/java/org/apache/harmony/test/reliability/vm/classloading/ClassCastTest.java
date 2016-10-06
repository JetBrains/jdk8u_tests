/*
 * Copyright 2007 The Apache Software Foundation or its licensors, as applicable
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
 * @author Aleksey Ignatenko
 * @version $Revision: 1.0 $
 */

/**
 *  GOAL: Test Class cast functionality
 *  see additional description in ClassMultiBas 
 *  
 *  The test does:
 *  a. finds all class files from JRE
 *  b. tries to load all these classes
 *  c. calls compileClass for every successfully loaded Class
 *  d. casting is done not more than RELEASE_CYCLE_TIME_LIMIT minutes (as it could take long on JRE)
 *  e. every loaded class is casted with Class.asSubclass to other loaded classes
 *  g. cast is checked with Class.isAssignableFrom function
 *  d. for every "java." class new Object is created by newInstance and
 *  checked that object has right Class with Class.isInstance()    
 *  
 *   No hangs, fails, crashes or hangs are expected.
 */

package org.apache.harmony.test.reliability.vm.classloading;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.harmony.test.reliability.share.ClassMultiTestBase;

public class ClassCastTest extends ClassMultiTestBase{
    static final int RELEASE_CYCLE_TIME_LIMIT = 1; //minutes
    Set<Class> syntMembers = null;
    long timeStart = 0;
    
    public static void main(String[] args){
        System.exit(new ClassCastTest().test(args));
    }
    
    public void initCycle() {
        syntMembers = new HashSet<Class>();
        timeStart = System.currentTimeMillis();
    }
    public void releaseCycle() {
        // do nothing
        Iterator it1 = syntMembers.iterator();
        while (it1.hasNext()){
            long timePassed = (System.currentTimeMillis() - timeStart) / 1000 / 60; // time in minutes
            if (timePassed > RELEASE_CYCLE_TIME_LIMIT){
                break;
            }
            Iterator it2 = syntMembers.iterator();
            Class c1 = (Class) it1.next();
            while (it2.hasNext()){
                Class c2 = (Class) it2.next();
                if (c1.equals(c2)){
                    continue;
                }
                try{
                    c1.asSubclass(c2);
                }catch (ClassCastException e){
                    // Expected
                    continue;
                }catch(Throwable e){
                    log.add("Unexpected cast exception: " + e.getMessage());
                    return;
                }
                // class was casted properly: trying to find custed method in 
                // parent classes via reflection
                if (!customCheckParent(c1,c2)){
                    log.add("Failed to check cast: " + c1.getName() + " failed to cast to " + c2.getName());
                }
                //System.out.println(c1.getName() + " casted to " + c2.getName());
            }

            // make newInstance only for "java." classes to avoid hangs, avoid "awt" and "swing" to avoid XServer up requirement
            if (c1.getName().startsWith("java.") && !c1.getName().contains("awt") && !c1.getName().contains("swing")){
                Object o = null;
                try {
                    o = c1.newInstance();
                } catch(IllegalAccessException e){
                    // Expectable
                    continue;
                } catch (InstantiationException e){
                    // Expectable
                    continue;
                } catch (ExceptionInInitializerError e){
                    // Expectable
                    continue;
                } catch (SecurityException e){
                    // Expectable
                    continue;
                }catch (Exception e){
                    // Expectable - propogated exception from class default constructor
                    continue;
                }
                if (!c1.isInstance(o)){
                    log.add("Failed to check instance of " + c1.getName());
                    return;
                }
                //System.out.println("OK for " + c1.getName());
            }       
            
        }
    }
    
    boolean customCheckParent(Class c, Class parent){
        return parent.isAssignableFrom(c);
    }
    
    public void testContent(Class cls) {
        if (cls.getName().contains("$")){
            return;
        }
        synchronized(syntMembers){
            syntMembers.add(cls);
        }
    }

}