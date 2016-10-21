/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.events;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

import org.punit.runner.Runner;
import org.punit.type.TestSuite;
import org.punit.watcher.Watcher;

/**
 * Event system for PUnit. The event listener can be installed by
 * {@link EventRegistry#addEventListener(EventListener)}.
 * 
 * All PUnit runners implement {@link EventRegistry}.
 * 
 */
public interface EventListener extends Serializable {

    /**
     * Is triggered before running the class.
     * 
     * @param clazz
     *            the class to be run
     * @param runner
     *            the runner who runs this class
     */
    public void onRunnerStart(Class<?> clazz, Runner runner);

    /**
     * Is triggered after running the class.
     * 
     * @param clazz
     *            the class to be run
     * @param runner
     *            the runner who runs this class
     */
    public void onRunnerEnd(Class<?> clazz, Runner runner);

    /**
     * Is triggered when the test suite is to be executed.
     * 
     */
    public void onSuiteStart(TestSuite suite);

    /**
     * Is triggered after executing the test suite.
     */
    public void onSuiteEnd(TestSuite suite);

    /**
     * Is triggered when the test class is already instantiated and is ready to
     * be executed.
     * 
     * @param testInstance
     *            a test class instance
     */
    public void onClassStart(Object testInstance);

    /**
     * Is triggered after executing the test class.
     * 
     * @param testInstance
     *            the test class instance
     * @param t
     *            an error was triggered while tearing the test down
     */
    public void onClassEnd(Object testInstance, Throwable t);

    /**
     * Is triggered when the method to be executed.
     */
    public void onMethodStart(Method method, Object testInstance,
            Object[] params);

    /**
     * Is triggered after executing the method.
     */
    public void onMethodEnd(Method method, Object testInstance,
            Object[] params, Throwable t, List<Watcher> Watchers);

    /**
     * Is triggered before all watchers start.
     */
    public void onWatchersStart(List<Watcher> watchers);

    /**
     * Is triggered after all watchers stop.
     */
    public void onWatchersEnd(List<Watcher> watchers);

    /**
     * Is triggered before this watcher starts.
     */
    public void onWatcherStart(Watcher watcher);

    /**
     * Is triggered after this watcher stops.
     */
    public void onWatcherEnd(Watcher watcher);

    /**
     * PUnit may execute test suites against different virtual machines. The
     * runner who forks child virtual machine is called parent runner. Parent
     * runner only notifies the events to the event listener who support parent
     * runner. It is useful for logging and reporting system. For example, file
     * logging does not care about it since the children virtual machine has
     * given the output.
     * 
     * @return <code>true</code> if this listener supports parent runner
     */
    public boolean supportParentRunner();

}
