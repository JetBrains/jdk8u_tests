/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.util;

import org.punit.message.Messages;
import org.punit.runner.Runner;
import org.punit.runner.RunnerProperties;

public class RunnerUtil {

    private static final int MAX_ARGS = 3;

    public static void run(Runner runner, String[] args) {
        int length = args.length;
        if (length == 0 || length > MAX_ARGS) {
            throw new IllegalArgumentException(length
                    + Messages.getString("runner.arguments")); //$NON-NLS-1$
        }
        Runner testRunner = runner;
        RunnerProperties properties = runner.properties();
        Class<?> testClass = ReflectionUtil.newClass(args[0]);
        if (length >= 2) {
            testRunner = (Runner) IOUtil.getSerialiableObject(args[1]);
        }
        if (length >= 3) {
            properties = (RunnerProperties) IOUtil
                    .getSerialiableObject(args[2]);
        }
        testRunner.run(testClass, properties);
    }
    
}
