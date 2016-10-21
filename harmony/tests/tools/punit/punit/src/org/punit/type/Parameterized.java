/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.type;

/**
 * If a test implements <code>Parameterized</code> interface, the test method
 * with only a <code>Parameter</code> argument (i.e.
 * <code>testFoo(Parameter p))</code> will be executed.
 */
public interface Parameterized {

    /**
     * Is invoked before all watchers start.
     * 
     * @param param
     * @throws Exception
     */
    public void setUpBeforeWatchers(Parameter param) throws Exception;

    /**
     * Is invoked after all watchers start.
     * 
     * @param param
     * @throws Exception
     */
    public void setUpAfterWatchers(Parameter param) throws Exception;

    /**
     * Is invoked before all watchers stop.
     * 
     * @param param
     * @throws Exception
     */
    public void tearDownBeforeWatchers(Parameter param) throws Exception;

    /**
     * Is invoked after all watchers stop.
     * 
     * @param param
     * @throws Exception
     */
    public void tearDownAfterWatchers(Parameter param) throws Exception;

    /**
     * @return a parameter array for the test
     */
    Parameter[] parameters();
}
