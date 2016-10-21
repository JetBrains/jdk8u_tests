/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.watcher;

import java.io.Serializable;

import org.punit.type.Name;

public interface Watcher extends Name, Serializable, Cloneable {

    public void start();

    public void stop();

    public double value();

    public String stringValue();

    public String unit();

    public Watcher cloneSelf();

}
