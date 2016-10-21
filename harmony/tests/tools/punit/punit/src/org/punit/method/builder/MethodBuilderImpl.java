/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.method.builder;

import java.lang.reflect.*;
import java.util.*;

import org.punit.convention.*;
import org.punit.type.*;

/**
 * This class is the basic implementation for <code>TestMethodBuilder</code>
 * interface.
 * 
 */
public class MethodBuilderImpl implements TestMethodBuilder {

	private static final long serialVersionUID = -7839703149112306751L;
	
	private Convention _convention;

	public void setConvention(Convention convention) {
		_convention = convention;
	}

	/**
	 * Judges whether this method is a test method.
	 * 
	 * @param method
	 * @return
	 */
	protected boolean isTestMethod(Method method) {
		return _convention.isTestMethod(method);
	}

	/**
	 * @see TestMethodBuilder#extractTestMethods(Class)
	 */
	public Collection<Method> extractTestMethods(Class<?> testClass) {
		Collection<Method> testMethods = null;
		if (isAlphabetical(testClass)) {
			testMethods = new TreeSet<Method>(new AlphabeticalMethodNameComparator());
		} else {
			testMethods = new ArrayList<Method>();
		}
		Class<?> clazz = testClass;
		while (clazz != Object.class) {
			Method[] methods = clazz.getDeclaredMethods();
			for (int i = 0; i < methods.length; ++i) {
				Method method = methods[i];
				if (isTestMethod(method)) {
					testMethods.add(method);
				}
			}
			clazz = clazz.getSuperclass();
		}
		return testMethods;
	}

	private boolean isAlphabetical(Class<?> testClass) {
		return Alphabetical.class.isAssignableFrom(testClass);
	}

	private static class AlphabeticalMethodNameComparator implements Comparator<Method> {
		public int compare(Method m1, Method m2) {
			return m1.getName().compareTo(m2.getName());
		}
	}

}
