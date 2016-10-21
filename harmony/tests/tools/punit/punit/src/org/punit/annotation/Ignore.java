/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
public @interface Ignore {
	String value() default "";
}
