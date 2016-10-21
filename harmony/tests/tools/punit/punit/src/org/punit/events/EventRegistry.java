/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.events;

import java.util.List;

/**
 * Interface for PUnit event registry. It can accept/remove a event
 * listener.
 */
public interface EventRegistry {

    /**
     * Adds a new event listener.
     * 
     * @param listener
     */
    public void addEventListener(EventListener listener);

    /**
     * Removes an event listener.
     * 
     * @param listener
     */
    public void removeEventListener(EventListener listener);

    /**
     * @return returns the registered event listeners.
     */
    public List<EventListener> eventListeners();
}
