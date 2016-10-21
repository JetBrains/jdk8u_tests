/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.runner;

import java.io.Serializable;

import org.punit.type.VM;

public class RunnerProperties implements Serializable {

    private static final long serialVersionUID = -4832579362013257784L;

    public String vmName;

    public boolean isIntermediate;
    
    public boolean isParent;
    
    public VM[] vms;

}
