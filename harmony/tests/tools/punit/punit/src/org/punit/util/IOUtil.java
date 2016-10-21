/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.punit.exception.IOException;

public class IOUtil {
    public static String getCurrentPath() {
        try {
            return new File(".").getCanonicalPath(); //$NON-NLS-1$
        } catch (java.io.IOException e) {
            throw new IOException(e);
        }
    }

    public static Process exec(String command) {
        try {
            return Runtime.getRuntime().exec(command);
        } catch (java.io.IOException e) {
            throw new IOException(e);
        }
    }

    public static void serialize(Object obj, String fileName) {
        ObjectOutputStream oos = null;
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
            oos.close();
        } catch (java.io.IOException e) {
            throw new IOException(e);
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (java.io.IOException e) {
                    throw new IOException(e);
                }
            }
        }
    }

    public static Object getSerialiableObject(String fileName) {
        ObjectInputStream ois = null;
        try {
            FileInputStream fis = new FileInputStream(fileName);
            ois = new ObjectInputStream(fis);
            return ois.readObject();
        } catch (Exception e) {
            throw new IOException(e);
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (java.io.IOException e) {
                    throw new IOException(e);
                }
            }
        }
    }

    public static void deleteFile(String string) {
        new File(string).delete();
    }
}
