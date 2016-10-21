/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.type;

/**
 * Marks this test as a PUnit test. It has more setUp/tearDown control than
 * normal test class.
 * 
 */
public interface Test {

    /**
     * This method will be invoked before all watchers start.
     * 
     * @throws Exception
     */
    public void setUpBeforeWatchers() throws Exception;

    /**
     * This method will be invoked after all watchers start.
     * 
     * @throws Exception
     */
    public void setUpAfterWatchers() throws Exception;

    /**
     * This method will be invoked before all watchers stop.
     * 
     * @throws Exception
     */
    public void tearDownBeforeWatchers() throws Exception;

    /**
     * This method will be invoked after all watchers stop.
     * 
     * @throws Exception
     */
    public void tearDownAfterWatchers() throws Exception;

}
