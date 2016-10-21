/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Test {

    Class<? extends Throwable> expected() default NoException.class;

    String checkMethod() default "";

    int concurrentCount() default 0;

    public class NoException extends Throwable {
        private static final long serialVersionUID = 3987745685001380514L;
    }
}
