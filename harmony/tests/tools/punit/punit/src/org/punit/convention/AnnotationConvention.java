/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.convention;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.punit.annotation.Test;
import org.punit.util.AnnotationUtil;
import org.punit.util.ReflectionUtil;

/**
 * The class merges annotation and name conventions with annotations taking
 * preference.
 */
public class AnnotationConvention extends AbstractConvention {

    private static final long serialVersionUID = -6800242731470821241L;

    protected NameConvention _delegate = new NameConvention();

    private Class<? extends Annotation> _after;
    private Class<? extends Annotation> _afterClass;
    private Class<? extends Annotation> _before;
    private Class<? extends Annotation> _beforeClass;
    private Class<? extends Annotation> _ignore;
    protected Class<? extends Annotation> _test;

    public AnnotationConvention() {
        this(org.punit.annotation.After.class,
                org.punit.annotation.AfterClass.class,
                org.punit.annotation.Before.class,
                org.punit.annotation.BeforeClass.class,
                org.punit.annotation.Ignore.class, Test.class);
    }

    public AnnotationConvention(Class<? extends Annotation> after,
            Class<? extends Annotation> afterClass,
            Class<? extends Annotation> before,
            Class<? extends Annotation> beforeClass,
            Class<? extends Annotation> ignore, Class<? extends Annotation> test) {
        _after = after;
        _afterClass = afterClass;
        _before = before;
        _beforeClass = beforeClass;
        _ignore = ignore;
        _test = test;
    }

    public Method getCheckMethod(Method method) {
        Test testAnnotation = (Test) method.getAnnotation(_test);
        String checkMethodName = null;
        if (testAnnotation != null) {
            checkMethodName = testAnnotation.checkMethod();
            if (checkMethodName != null && checkMethodName.length() > 0) {
                return ReflectionUtil.getMethod(method.getDeclaringClass(),
                        checkMethodName, method.getParameterTypes());
            }
        }
        return _delegate.getCheckMethod(method);
    }

    @SuppressWarnings("unchecked")
    public boolean isExcluded(Class clazz) {
        return clazz.isAnnotationPresent(_ignore);
    }

    public boolean isTestMethod(Method method) {
        if (method.isAnnotationPresent(_ignore)) {
            return false;
        }
        return method.isAnnotationPresent(_test) || _delegate.isTestMethod(method);
    }

    public Class<? extends Throwable> getExpectedException(Method method) {
        Test testAnnotation = (Test) method.getAnnotation(_test);
        if (testAnnotation == null) {
            return null;
        }
        Class<? extends Throwable> expected = testAnnotation.expected();
        if (expected == Test.NoException.class) {
            return null;
        }
        return expected;
    }

    public int getConcurrentCount(Object testInstance, Method method) {
        Test testAnnotation = (Test) method.getAnnotation(_test);
        if (testAnnotation == null) {
            testAnnotation = (Test) testInstance.getClass().getAnnotation(_test);
        }
        if (testAnnotation == null) {
            return _delegate.getConcurrentCount(testInstance, method);
        }
        return testAnnotation.concurrentCount();
    }

    @SuppressWarnings("unchecked")
    public Method getAfterClassMethod(Class test) {
        Method method = AnnotationUtil.getMethodByAnnotation(test, _afterClass);
        if (method == null) {
            method = _delegate.getAfterClassMethod(test);
        }
        return method;
    }

    @SuppressWarnings("unchecked")
    public Method getBeforeClassMethod(Class test) {
        Method method = AnnotationUtil
                .getMethodByAnnotation(test, _beforeClass);
        if (method == null) {
            method = _delegate.getBeforeClassMethod(test);
        }
        return method;
    }

    @SuppressWarnings("unchecked")
    public Method getSetUpMethod(Class test) {
        Method method = AnnotationUtil.getMethodByAnnotation(test, _before);
        if (method == null) {
            method = _delegate.getSetUpMethod(test);
        }
        return method;
    }

    @SuppressWarnings("unchecked")
    public Method getTearDownMethod(Class test) {
        Method method = AnnotationUtil.getMethodByAnnotation(test, _after);
        if (method == null) {
            method = _delegate.getTearDownMethod(test);
        }
        return method;
    }

}
