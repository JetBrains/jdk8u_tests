/* (C) Copyright 2007, by Andrew Zhang */
package org.punit.listener;

import java.lang.reflect.*;
import java.util.*;

import org.punit.events.*;
import org.punit.util.*;

/**
 * The listener sets class public fields using properties.
 */
public class FieldSetter extends VanillaEventListener {

    private static final long serialVersionUID = 5194809928409791777L;

    private final Properties _properties;

    public FieldSetter(Properties properties) {
        _properties = properties;
    }

    public void onClassStart(Object testInstance) {
        setClassFields(testInstance, _properties);
    }

    private final String PACKAGE_SEPARATOR = "."; //$NON-NLS-1$

    /**
     * Set public class fields from java properties.
     * 
     * @param clazz
     *            a class to set fields
     * @param properites
     *            a set of properties
     */
    private void setClassFields(Object testInstance, Properties properties) {
        for (Field field : testInstance.getClass().getFields()) {
            String fieldName = PACKAGE_SEPARATOR
                    + field.getDeclaringClass().getName() + PACKAGE_SEPARATOR
                    + field.getName();
            for (Object key : properties.keySet()) {
                final String keyName = key.toString();
                if (fieldName.indexOf(PACKAGE_SEPARATOR + keyName) >= 0) {
                    ReflectionUtil.setField(testInstance, field, properties
                            .getProperty(keyName));
                }
            }
        }
    }
}
