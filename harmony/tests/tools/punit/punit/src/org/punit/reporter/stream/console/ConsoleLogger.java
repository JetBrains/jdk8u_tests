/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.reporter.stream.console;

import java.io.PrintWriter;

import org.punit.reporter.stream.StreamLogger;

public class ConsoleLogger extends StreamLogger {

    private static final long serialVersionUID = 2691593175672418574L;

    private void readObject(java.io.ObjectInputStream in)
            throws java.io.IOException, ClassNotFoundException {
        _writer = new PrintWriter(System.err);
    }

    public ConsoleLogger() {
        super(new PrintWriter(System.err));
    }
    
    protected void closeStream() {
    	
    }
}
