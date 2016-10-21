package org.punit.method.runner;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

import org.punit.convention.Convention;
import org.punit.events.*;
import org.punit.runner.RunnerProperties;
import org.punit.watcher.Watcher;

public interface MethodRunner extends Serializable, Cloneable {
    /**
     * Runs the test method.
     * @param testInstance
     * @param method
     * @param params
     * @throw the exception if there is any during the execution
     */
    public void run(Object testInstance, Method method, Object[] params);
    
    /**
     * 
     * @param eventListeners List <EventListener>
     */
    public void setEventListeners(List<EventListener> eventListeners);

    public void setRunnerProperties(RunnerProperties props);
    
    /**
     * @return watchers associated with this method runner
     */
    public List<Watcher> watchers();
    
    public void addWatcher(Watcher watcher);
        
    public void removeWatcher(Watcher watcher);
    
    public Object clone();
    
    public void setConvention(Convention convention);
    
    public boolean shouldStop();
}
