/* (C) Copyright 2007, by Andrew Zhang */
package org.punit.runner;

import org.punit.message.Messages;
import org.punit.method.builder.MethodBuilderImpl;
import org.punit.method.runner.ConcurrentMethodRunner;
import org.punit.suite.builder.TestSuiteBuilderImpl;
import org.punit.util.RunnerUtil;

public class ConcurrentRunner extends AbstractRunner {

    private static final long serialVersionUID = -7193902024861434576L;
    
    private static final int DEFAULT_CONCURRENT_COUNT = 10;

    public static void main(String[] args) {
        RunnerUtil.run(new ConcurrentRunner(), args);
    }
    
    public ConcurrentRunner() {
        this(ConcurrentRunner.DEFAULT_CONCURRENT_COUNT);
    }
    
    public ConcurrentRunner(int concurrentCount) {
        super(new TestSuiteBuilderImpl(), new MethodBuilderImpl(), new ConcurrentMethodRunner(concurrentCount));
    }

    public String punitName() {
        return Messages.getString("runner.03"); //$NON-NLS-1$
    }

}
