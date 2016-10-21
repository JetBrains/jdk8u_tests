/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.type;

import java.io.Serializable;

/**
 * This classes defines a virtual machine properties, includes the path and the
 * name of the virtual machine. The format of the <code>path</code> is like
 * "/root/foo/bar/jre/bin/java -cp dir1/foo1.jar; dir2/bin". The
 * <code>name</code> will appear in the result files.
 * 
 */
public class VM implements Name, Serializable {

    private static final long serialVersionUID = 7903136094137840375L;

    private String _path;

    private String _name;

    /**
     * Constructor.
     * 
     * @param path
     *            the path of the virtual machine.
     * @param name
     *            the name of the virtual machine.
     */
    public VM(String path, String name) {
        _path = path;
        _name = name;
    }

    public String punitName() {
        return _name;
    }

    /**
     * @return returns the path of the virtual machine.
     */
    public String path() {
        return _path;
    }
}
