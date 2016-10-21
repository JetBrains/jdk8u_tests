/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.suite.builder;

import java.util.*;

import org.punit.convention.*;
import org.punit.type.*;
import org.punit.util.*;

public class TestSuiteBuilderImpl implements TestSuiteBuilder {
    
    private static final long serialVersionUID = -4468961881014123138L;
    
    private Convention _filter;

    public void setConvention(Convention filter) {
        _filter = filter;
    }
    
    /**
     * @see TestSuiteBuilder#buildTestClasses(Class)
     */
    public Object[] buildTestClasses(Class<?> clazz) {
        List<Object> testClasses = new ArrayList<Object>();
        buildTestClasses(testClasses, clazz);
        return testClasses.toArray();
    }
    
    private void buildTestClasses(List<Object> testClasses, Class<?> clazz) {
        if(isTestSuite(clazz)) {
            buildTestClassFromTestSuite(testClasses, clazz);
        } else {
            buildTestClassesFromClass(testClasses, clazz);
        }
    }

    private void buildTestClassFromTestSuite(List<Object> testClasses, Class<?> clazz) {
        if(_filter.isExcluded(clazz)) {
            return;
        }
        TestSuite testSuite = (TestSuite) ReflectionUtil.newInstance(clazz);
        testClasses.add(new TestSuiteLabelImpl(testSuite, true));
        Class<?>[] suite = testSuite.testSuite();
        for(int i = 0; i < suite.length; ++i) {
            buildTestClasses(testClasses, suite[i]);
        }
        testClasses.add(new TestSuiteLabelImpl(testSuite, false));
    }    

    private void buildTestClassesFromClass(List<Object> testClasses, Class<?> clazz) {
        if(_filter.isExcluded(clazz)) {
            return;
        }
        testClasses.add(clazz);
    }


    private boolean isTestSuite(Class<?> clazz) {
        return TestSuite.class.isAssignableFrom(clazz);
    }
    
}
