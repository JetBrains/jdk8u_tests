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

package org.apache.harmony.test.reliability.api.security;

import java.lang.reflect.Member;
import java.security.Principal;
import java.util.Iterator;
import java.util.Set;

import javax.security.auth.Subject;

import org.apache.harmony.test.reliability.share.ClassMultiTestBase;

/**
 *  GOAL: Test security related functionality on the basis of loaded classed 
 *  see additional description in ClassMultiBas 
 *  
 *  The test does:
 *  a. finds all class files from JRE
 *  b. tries to load all these classes
 *  c. eprforms sveral security related operations like checkMemberAccess on every
 *     loaded class
 *  
 *   No hangs, fails, crashes or hangs are expected.
 *   Note: testContent is able to be extended to any other operations avalaible
 *   with j.l.Class
 */

public class SecurityClassRelatedTest extends ClassMultiTestBase{
    SecurityManager sm = null;
    Subject sbj = null;    
    
    public static void main(String[] args){
        System.exit(new SecurityClassRelatedTest().test(args));
    }

    public void initCycle() {
        sm = new SecurityManager();
        sbj = new Subject();
    }
    public void releaseCycle() {
        // do nothing
    }
    
    public void testContent(Class cls) {
        sm.checkAccess(Thread.currentThread());
        
        try{
            sm.checkMemberAccess(cls, Member.PUBLIC);
        } catch(SecurityException e){
            // Expected
        } catch (Throwable e){
            log.add("Failed to checkMemberAccess on class " + cls.getName() + " Reason: " + e.getMessage());
        }

        try{
            sm.checkMemberAccess(cls, Member.DECLARED);
        } catch(SecurityException e){
            // Expected
        } catch (Throwable e){
            log.add("Failed to checkMemberAccess on class " + cls.getName() + " Reason: " + e.getMessage());
        }
        
        Set<Principal> prc = sbj.getPrincipals(cls);
        Iterator it = prc.iterator();
        while(it.hasNext()){
            Principal p = (Principal) it.next();
            p.getName();
        }
        
        Set pc = sbj.getPrivateCredentials(cls);
        it = pc.iterator();
        while(it.hasNext()){
            Object o = it.next();
            o.hashCode();
        }

        
        Set<Class> puc = sbj.getPublicCredentials(cls);
        it = puc.iterator();
        while(it.hasNext()){
            Class c = (Class) it.next();
            c.getName();
        }

    }

}