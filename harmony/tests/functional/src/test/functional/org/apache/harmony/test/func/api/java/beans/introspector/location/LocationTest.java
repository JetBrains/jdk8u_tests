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
package org.apache.harmony.test.func.api.java.beans.introspector.location;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import org.apache.harmony.test.func.api.java.beans.introspector.LocationException;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 * Under test: Introspector, PropertyDescriptor, SimpleBeanInfo.
 * <p>
 * This test verifies that Introspector finds real beanInfo, flushes beanInfo
 * from a cache. Beans and beanInfos store in subpackage beans. If name of
 * package contains "thesamepackage" then beanInfo stores in package of bean. If
 * name of package contains "differentlocation" then there is no beanInfo in
 * package of bean.
 * 
 * @see java.beans.Introspector
 */
public class LocationTest extends MultiCase {
    /**
     * <code>listOfBeanInfo</code> is list of all BeanInfo situated in
     * subpackages.
     */
    private BeanInfoStatus[]     listOfBeanInfo;
    /**
     * After Introspector has got BeanInfo, property descriptors have to place
     * to <code>propertyDescriptors</code> field.
     */
    private PropertyDescriptor[] propertyDescriptors;

    public static void main(String[] args) {
        System.exit(new LocationTest().test(args));
        // new LocationTest().testStopClassIsNotParentBean();
    }

    /**
     * Verify that Introspector finds BeanInfo in package of bean.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create a bean.
     * <li>Create beanInfo in package of the bean and redefine
     * getPropertyDescriptors() method.
     * <li>Get beanInfo using method Introspector.getBeanInfo(bean).
     * <li>Verify that getPropertyDescriptors() of returned beanInfo and
     * getPropertyDescriptors() of beanInfo created in item#2 return the same.
     */
    public Result testFindBeanInfoInPackageOfBean() {
        try {
            propertyDescriptors = Introspector
                .getBeanInfo(
                    org.apache.harmony.test.func.api.java.beans.introspector.location.beans.thesamepackage.Bean1.class)
                .getPropertyDescriptors();
            verifyProperties(new String[] { "property1" });
            verifyThatOnlyThisBeanInvoked(org.apache.harmony.test.func.api.java.beans.introspector.location.beans.thesamepackage.Bean1BeanInfo.class);
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.toString());
        } finally {
            clean();
        }
    }

    /**
     * Verify that Introspector finds beanInfo first in package of bean and then
     * in search path.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create a bean.
     * <li>Create beanInfo#1 for given bean in package of the bean and redefine
     * getPropertyDescriptors() for given bean method.
     * <li>Create beanInfo#2 in no-package of the bean and redefine
     * getPropertyDescriptors() method.
     * <li>Set search path to package of beanInfo#2.
     * <li>Get beanInfo using method Introspector.getBeanInfo(bean).
     * <li>Verify that getPropertyDescriptors()of returned beanInfo and
     * getPropertyDescriptors() of beanInfo#1 return the same.
     * <li>Verify that getPropertyDescriptors() of beanInfo#2 wasn't invoke.
     */
    public Result testSequenceOfSearch() {
        try {
            String packageName = org.apache.harmony.test.func.api.java.beans.introspector.location.beans.differentlocation.p2.Bean1BeanInfo.class
                .getPackage().getName();
            Introspector.setBeanInfoSearchPath(new String[] { packageName });
            propertyDescriptors = Introspector
                .getBeanInfo(
                    org.apache.harmony.test.func.api.java.beans.introspector.location.beans.thesamepackage.Bean1.class)
                .getPropertyDescriptors();
            verifyProperties(new String[] { "property1" });
            verifyThatOnlyThisBeanInvoked(org.apache.harmony.test.func.api.java.beans.introspector.location.beans.thesamepackage.Bean1BeanInfo.class);
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.toString());
        } finally {
            clean();
        }
    }

    /**
     * Verify that Introspector doesn't use beanInfo of parent bean, if there is
     * beanInfo of immediate bean.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create a bean#1.
     * <li>Create bean#2, which extends bean#1.
     * <li>Create beanInfo for two beans and redefine getPropertyDescriptors()
     * method. Bean and corresponding beanInfo are in one package.
     * <li>Get beanInfo using method Introspector.getBeanInfo(bean#2).
     * <li>Verify that getPropertyDescriptors() of returned beanInfo and
     * getPropertyDescriptors() of beanInfo#2 return the same.
     * <li>Verify that getPropertyDescriptors() of beanInfo#1 wasn't invoke.
     */
    public Result testUseOnlyImmediateBeanInfo() {
        try {
            propertyDescriptors = Introspector
                .getBeanInfo(
                    org.apache.harmony.test.func.api.java.beans.introspector.location.beans.subclasses.Bean2.class)
                .getPropertyDescriptors();
            verifyProperties(new String[] { "property2" });
            verifyThatOnlyThisBeanInvoked(org.apache.harmony.test.func.api.java.beans.introspector.location.beans.subclasses.Bean2BeanInfo.class);
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.toString());
        } finally {
            clean();
        }
    }

    /**
     * Verify that Introspector doesn't use immediate beanInfo and uses parent
     * beanInfo, if IGNORE_IMMEDIATE_BEANINFO flag was used to get beanInfo.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create a bean.
     * <li>Create bean#2, which extends bean#1.
     * <li>Create beanInfo for two beans and redefine getPropertyDescriptors()
     * method. Bean and corresponding beanInfo are in one package.
     * <li>Get beanInfo using method
     * Introspector.getBeanInfo(bean#2,IGNORE_IMMEDIATE_BEANINFO).
     * <li>Verify that getPropertyDescriptors()of returned beanInfo and
     * getPropertyDescriptors() of created beanInfo#1 return the same.
     * <li>Verify that getPropertyDescriptors() of created beanInfo#2 wasn't
     * invoke.
     */
    public Result testIgnoreImmidiateBeanInfo() {
        try {
            String packageName = org.apache.harmony.test.func.api.java.beans.introspector.location.beans.differentlocation.p2.Bean1BeanInfo.class
                .getPackage().getName();
            Introspector.setBeanInfoSearchPath(new String[] { packageName });
            propertyDescriptors = Introspector
                .getBeanInfo(
                    org.apache.harmony.test.func.api.java.beans.introspector.location.beans.subclasses.Bean2.class,
                    Introspector.IGNORE_IMMEDIATE_BEANINFO)
                .getPropertyDescriptors();
            verifyProperties(new String[] { "property1", "property8" });
            verifyThatOnlyThisBeanInvoked(org.apache.harmony.test.func.api.java.beans.introspector.location.beans.thesamepackage.Bean1BeanInfo.class);
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.toString());
        } finally {
            clean();
        }
    }

    /**
     * Verify that Introspector ignores all beanInfo, if IGNORE_ALL_BEANINFO
     * flag was used to get beanInfo.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create a bean. Create getter and setter methods of property in given
     * bean.
     * <li>Create beanInfo in package of bean and redefine
     * getPropertyDescriptors() method.
     * <li>Get beanInfo using method
     * Introspector.getBeanInfo(bean,IGNORE_ALL_BEANINFO).
     * <li>Verify that getPropertyDescriptors() of beanInfo created in item#2
     * wasn't invoke.
     * <li>Verify that getPropertyDescriptors() of returned beanInfo returns
     * introspected properties.
     */
    public Result testIgnoreAllBeanInfo() {
        try {
            propertyDescriptors = Introspector
                .getBeanInfo(
                    org.apache.harmony.test.func.api.java.beans.introspector.location.beans.thesamepackage.Bean1.class,
                    Introspector.IGNORE_ALL_BEANINFO).getPropertyDescriptors();
            verifyThatAllBeanInfoNotInvoked();
            verifyProperties(new String[] { "property8", "class" });
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.toString());
        } finally {
            clean();
        }
    }

    /**
     * Verify that Introspector caches BeanInfo.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create a bean.
     * <li>Create beanInfo in package of the bean and redefine
     * getPropertyDescriptors() method.
     * <li>Get beanInfo using method Introspector.getBeanInfo(bean) and invoke
     * getPropertyDescriptors() on returned beanInfo.
     * <li>Verify that getPropertyDescriptors() method of beanInfo was invoked.
     * <li>Get beanInfo using method Introspector.getBeanInfo(bean) and invoke
     * getPropertyDescriptors() on returned beanInfo.
     * <li>Verify that getPropertyDescriptors() method of beanInfo was not
     * invoked.
     * <li>Verify that references of two returned beanInfo are the same.
     */
    public Result testCacheBeanInfo() {
        try {
            BeanInfo beanInfo1 = Introspector
                .getBeanInfo(org.apache.harmony.test.func.api.java.beans.introspector.location.beans.thesamepackage.Bean1.class);
            beanInfo1.getPropertyDescriptors();
            assertTrue(new org.apache.harmony.test.func.api.java.beans.introspector.location.beans.thesamepackage.Bean1BeanInfo()
                .getIsInvokedAndChangeToNotInvoked());
            BeanInfo beanInfo2 = Introspector
                .getBeanInfo(org.apache.harmony.test.func.api.java.beans.introspector.location.beans.thesamepackage.Bean1.class);
            beanInfo2.getPropertyDescriptors();
            assertFalse(new org.apache.harmony.test.func.api.java.beans.introspector.location.beans.thesamepackage.Bean1BeanInfo()
                .getIsInvokedAndChangeToNotInvoked());
            assertEquals(beanInfo1, beanInfo2);
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.toString());
        } finally {
            clean();
        }
    }

    /**
     * Verify that flushCaches() method of Introspector flushes all caches.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create a bean.
     * <li>Create beanInfo in package of the bean and redefine
     * getPropertyDescriptors() method.
     * <li>Get beanInfo using method Introspector.getBeanInfo(bean) and invoke
     * getPropertyDescriptors() on returned beanInfo.
     * <li>Verify that getPropertyDescriptors() method of beanInfo was invoked.
     * <li>Flush cache.
     * <li>Repeat item#3.
     * <li>Verify that references of two beanInfo aren't the same.
     * <li>Verify that getPropertyDescriptors() method of beanInfo was invoked.
     */
    public Result testFlushCaches() {
        try {
            BeanInfo beanInfo1 = Introspector
                .getBeanInfo(org.apache.harmony.test.func.api.java.beans.introspector.location.beans.thesamepackage.Bean1.class);
            beanInfo1.getPropertyDescriptors();
            assertTrue(new org.apache.harmony.test.func.api.java.beans.introspector.location.beans.thesamepackage.Bean1BeanInfo()
                .getIsInvokedAndChangeToNotInvoked());
            Introspector.flushCaches();
            BeanInfo beanInfo2 = Introspector
                .getBeanInfo(org.apache.harmony.test.func.api.java.beans.introspector.location.beans.thesamepackage.Bean1.class);
            beanInfo2.getPropertyDescriptors();
            assertTrue(new org.apache.harmony.test.func.api.java.beans.introspector.location.beans.thesamepackage.Bean1BeanInfo()
                .getIsInvokedAndChangeToNotInvoked());
            assertTrue(beanInfo1 != beanInfo2);
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.toString());
        } finally {
            clean();
        }
    }

    /**
     * Verify that Introspector finds BeanInfo in no-package of bean, if there
     * isn't beanInfo in package of bean and search path was set to package of
     * beanInfo.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create a bean.
     * <li>Create beanInfo in no-package of the bean and redefine
     * getPropertyDescriptors() method.
     * <li>Create search path consisting of three packages: In first package
     * there is beanInfo for other bean. In second package there is beanInfo for
     * given bean. In third package there isn't any beanInfo at all.
     * <li>Set search path.
     * <li>Get beanInfo using method Introspector.getBeanInfo(bean).
     * <li>Verify that getPropertyDescriptors() of returned beanInfo and
     * getPropertyDescriptors() of beanInfo created in item#2 return the same.
     * <li>Verify that no other getPropertyDescriptors() methods of other
     * beanInfo were invoked.
     */
    public Result testSearchPath() {
        try {
            String packageName1 = org.apache.harmony.test.func.api.java.beans.introspector.location.beans.thesamepackage.subclass.Bean5BeanInfo.class
                .getPackage().getName();
            String packageName2 = org.apache.harmony.test.func.api.java.beans.introspector.location.beans.differentlocation.p2.Bean1BeanInfo.class
                .getPackage().getName();
            String packageName3 = "org.apache.harmony.test.func.api.java.beans";
            Introspector.setBeanInfoSearchPath(new String[] { packageName1,
                packageName2, packageName3 });
            propertyDescriptors = Introspector
                .getBeanInfo(
                    org.apache.harmony.test.func.api.java.beans.introspector.location.beans.differentlocation.p1.Bean1.class)
                .getPropertyDescriptors();
            verifyProperties(new String[] { "property1" });
            verifyThatOnlyThisBeanInvoked(org.apache.harmony.test.func.api.java.beans.introspector.location.beans.differentlocation.p2.Bean1BeanInfo.class);
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.toString());
        } finally {
            clean();
        }
    }

    /**
     * Verify that Introspector stops introspection of bean, if it encounters
     * stop class.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create a bean#1.
     * <li>Create beanInfo for bean#1 and redefine getPropertyDescriptors()
     * method. Beans and corresponding beanInfo are in one package.
     * <li>Create bean#2, which extends bean#1 and has getter and setter
     * methods of two properties.
     * <li>Get beanInfo using method Introspector.getBeanInfo(bean#2,bean#1),
     * bean#1 is stop class.
     * <li>Verify that getPropertyDescriptors() returns only introspected
     * properties on bean#1.
     * <li>Verify that getPropertyDescriptors() of beanInfo created for bean#1
     * wasn't invoke.
     */
    public Result testStopClassIsParentBean() {
        try {
            propertyDescriptors = Introspector
                .getBeanInfo(
                    org.apache.harmony.test.func.api.java.beans.introspector.location.beans.subclasses.p1.Bean3.class,
                    org.apache.harmony.test.func.api.java.beans.introspector.location.beans.thesamepackage.Bean1.class)
                .getPropertyDescriptors();
            verifyProperties(new String[] { "ii", "property8" });
            verifyThatAllBeanInfoNotInvoked();
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.toString());
        } finally {
            clean();
        }
    }

    /**
     * Verify that getBeanInfo(Class beanClass, Class stopClass) method
     * introspects a bean, as if getBeanInfo(Class beanClass) method introspects
     * the bean, if stopClass isn't parent, child and the same bean.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create a bean#1.
     * <li>Create another bean#2, which isn't parent, child of bean.
     * <li>Create beanInfo in package of the bean#1 and redefine
     * getPropertyDescriptors() method.
     * <li>Get beanInfo using method Introspector.getBeanInfo(bean#1,bean#2).
     * <li>Verify that getPropertyDescriptors() of returned beanInfo and
     * getPropertyDescriptors() of beanInfo created in item#3 return the same.
     */
    public Result testStopClassIsNotParentBean() {
        try {
            propertyDescriptors = Introspector
                .getBeanInfo(
                    org.apache.harmony.test.func.api.java.beans.introspector.location.beans.thesamepackage.Bean1.class,
                    org.apache.harmony.test.func.api.java.beans.introspector.location.beans.differentlocation.p1.Bean1.class)
                .getPropertyDescriptors();
            verifyProperties(new String[] { "property1" });
            verifyThatOnlyThisBeanInvoked(org.apache.harmony.test.func.api.java.beans.introspector.location.beans.thesamepackage.Bean1BeanInfo.class);
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.toString());
        } finally {
            clean();
        }
    }

    /**
     * Verify that flushFromCaches(bean1) method of Introspector deletes
     * beanInfo only for bean1 from cache.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Create a bean#1.
     * <li>Create beanInfo in package of the bean#1 and redefine
     * getPropertyDescriptors() method.
     * <li>Get beanInfo#1 using method Introspector.getBeanInfo(bean#1) and
     * invoke getPropertyDescriptors() on returned beanInfo.
     * <li>Create a bean#2.
     * <li>Create beanInfo in package of the bean#2 and redefine
     * getPropertyDescriptors() method.
     * <li>Get beanInfo#2 using method Introspector.getBeanInfo(bean#2) and
     * invoke getPropertyDescriptors() on returned beanInfo.
     * <li>Verify that getPropertyDescriptors() methods of beanInfo for bean#1
     * and for bean#2 were invoked.
     * <li>Invoke flushFromCaches(bean#1).
     * <li>Get beanInfo#3 using method Introspector.getBeanInfo(bean#1) and
     * invoke getPropertyDescriptors() on returned beanInfo.
     * <li>Verify that references of beanInfo#1 and beanInfo#3 aren't the same.
     * <li>Verify that getPropertyDescriptors() method of beanInfo for bean#1
     * was invoked.
     * <li>Get beanInfo#4 using method Introspector.getBeanInfo(bean#2) and
     * invoke getPropertyDescriptors() on returned beanInfo.
     * <li>Verify that references of beanInfo#2 and beanInfo#4 are the same.
     * <li>Verify that getPropertyDescriptors() method of beanInfo for bean#2
     * wasn't invoked.
     */
    public Result testFlushFromCaches() {
        try {
            BeanInfo beanInfo1 = Introspector
                .getBeanInfo(org.apache.harmony.test.func.api.java.beans.introspector.location.beans.thesamepackage.Bean1.class);
            beanInfo1.getPropertyDescriptors();
            new org.apache.harmony.test.func.api.java.beans.introspector.location.beans.thesamepackage.Bean1BeanInfo()
                .setNotInvoked();
            String packageName = org.apache.harmony.test.func.api.java.beans.introspector.location.beans.differentlocation.p2.Bean1BeanInfo.class
                .getPackage().getName();
            Introspector.setBeanInfoSearchPath(new String[] { packageName });
            BeanInfo beanInfo3 = Introspector
                .getBeanInfo(org.apache.harmony.test.func.api.java.beans.introspector.location.beans.differentlocation.p1.Bean1.class);
            beanInfo3.getPropertyDescriptors();
            new org.apache.harmony.test.func.api.java.beans.introspector.location.beans.differentlocation.p2.Bean1BeanInfo()
                .setNotInvoked();

            // Flush from cache beanInfo1
            Introspector
                .flushFromCaches(org.apache.harmony.test.func.api.java.beans.introspector.location.beans.thesamepackage.Bean1.class);

            BeanInfo beanInfo2 = Introspector
                .getBeanInfo(org.apache.harmony.test.func.api.java.beans.introspector.location.beans.thesamepackage.Bean1.class);
            beanInfo2.getPropertyDescriptors();
            assertTrue(new org.apache.harmony.test.func.api.java.beans.introspector.location.beans.thesamepackage.Bean1BeanInfo()
                .getIsInvokedAndChangeToNotInvoked());
            assertTrue(beanInfo1 != beanInfo2);
            BeanInfo beanInfo4 = Introspector
                .getBeanInfo(org.apache.harmony.test.func.api.java.beans.introspector.location.beans.differentlocation.p1.Bean1.class);
            beanInfo4.getPropertyDescriptors();
            assertFalse(new org.apache.harmony.test.func.api.java.beans.introspector.location.beans.differentlocation.p2.Bean1BeanInfo()
                .getIsInvokedAndChangeToNotInvoked());
            assertEquals(beanInfo3, beanInfo4);
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.toString());
        } finally {
            clean();
        }
    }

    /**
     * Verify that getPropertyDescriptors() method of <code>beanInfo</code>
     * was invoked.
     */
    private void verifyThatBeanInfoInvoked(Class beanInfo) throws Exception {
        BeanInfoStatus myBeanInfo = (BeanInfoStatus)beanInfo.newInstance();
        if (!myBeanInfo.getIsInvokedAndChangeToNotInvoked()) {
            throw new LocationException("wasn't invoked " + beanInfo);
        }
    }

    /**
     * Verify that Introspector uses only this BeanInfo.
     * 
     * @param beanInfo is full name of BeanInfo
     * @throws Exception
     */
    private void verifyThatOnlyThisBeanInvoked(Class beanInfo) throws Exception {
        verifyThatBeanInfoInvoked(beanInfo);
        verifyThatAllBeanInfoNotInvoked();
    }

    protected void setUp() throws Exception {
        if (listOfBeanInfo != null)
            return;
        listOfBeanInfo = new BeanInfoStatus[4];
        listOfBeanInfo[0] = new org.apache.harmony.test.func.api.java.beans.introspector.location.beans.differentlocation.p2.Bean1BeanInfo();
        listOfBeanInfo[1] = new org.apache.harmony.test.func.api.java.beans.introspector.location.beans.thesamepackage.Bean1BeanInfo();
        listOfBeanInfo[2] = new org.apache.harmony.test.func.api.java.beans.introspector.location.beans.thesamepackage.subclass.Bean5BeanInfo();
        listOfBeanInfo[3] = new org.apache.harmony.test.func.api.java.beans.introspector.location.beans.subclasses.Bean2BeanInfo();

    }

    /**
     * Verify that all BeanInfo are not used by Introspector.
     * 
     * @throws Exception
     */
    private void verifyThatAllBeanInfoNotInvoked() throws Exception {
        for (int i = 0; i < listOfBeanInfo.length; i++) {
            if (listOfBeanInfo[i].getIsInvokedAndChangeToNotInvoked()) {
                throw new LocationException("BeanInfo "
                    + listOfBeanInfo[i].getClass().getName()
                    + " must not be invoked.");
            }
        }
    }

    /**
     * Verify that <code>propertyDescriptors</code> contain only property with
     * specific names of properties.
     * 
     * @param propertyNames are names of properties
     * @throws LocationException
     */
    private void verifyProperties(String[] propertyNames)
        throws LocationException {
        if (propertyNames.length != propertyDescriptors.length) {
            throw new LocationException("Number of properties are not "
                + propertyNames.length);
        }
        int numberOfProperties = propertyNames.length;
        for (int k = 0; k < numberOfProperties; k++) {
            for (int i = 0; i < numberOfProperties; i++) {
                if (propertyDescriptors[i].getName().equals(propertyNames[k])) {
                    return;
                }
            }
            throw new LocationException("There is no property:"
                + propertyNames[k]);
        }
    }

    /**
     * Clean up.
     */
    private void clean() {
        for (int i = 0; i < listOfBeanInfo.length; i++) {
            listOfBeanInfo[i].setNotInvoked();
        }
        Introspector.flushCaches();
        propertyDescriptors = null;
    }
}