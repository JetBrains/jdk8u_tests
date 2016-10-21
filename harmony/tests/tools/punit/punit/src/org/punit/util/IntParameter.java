/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.util;

import org.punit.type.Parameter;

public class IntParameter implements Parameter {

    private int _intValue;

    public IntParameter(int value) {
        _intValue = value;
    }

    public int intValue() {
        return _intValue;
    }

    public String toString() {
        return String.valueOf(_intValue);
    }

}
