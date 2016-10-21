/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.runner;

import java.io.Serializable;

public interface ExecutorPool extends Serializable {
    public void execute(Runnable task);
    public void start();
    public void join();
}
