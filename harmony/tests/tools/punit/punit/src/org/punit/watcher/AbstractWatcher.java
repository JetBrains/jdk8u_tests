/* (C) Copyright 2007 */

package org.punit.watcher;

import org.punit.exception.ReflectionException;

public abstract class AbstractWatcher implements Watcher {
    
    public String stringValue() {
        return value() + unit();
    }

    public Watcher cloneSelf() {
        try {
            return (Watcher) clone();
        } catch (CloneNotSupportedException e) {
            throw new ReflectionException(e);
        }
    }

    public int hashCode() {
        return getClass().hashCode();
    }

    public boolean equals(Object obj) {
        return getClass() == obj.getClass();
    }

}
