/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.runner;

import java.io.Serializable;

import org.punit.events.EventRegistry;
import org.punit.method.builder.TestMethodBuilder;
import org.punit.method.runner.MethodRunner;
import org.punit.reporter.TestResult;
import org.punit.suite.builder.TestSuiteBuilder;
import org.punit.type.Name;

public interface Runner extends EventRegistry, Name, Serializable, Cloneable {
    
    public int run(Class<?> clazz);
    
    public int run(Class<?> clazz, RunnerProperties properties);
    
    public RunnerProperties properties();
    
    public TestMethodBuilder methodBuilder();
        
    public MethodRunner methodRunner();

    public TestSuiteBuilder suiteBuilder();

    public TestResult testResult();
    
    public void resultFolder(String folder);
    
    public String resultFolder();
    
}
