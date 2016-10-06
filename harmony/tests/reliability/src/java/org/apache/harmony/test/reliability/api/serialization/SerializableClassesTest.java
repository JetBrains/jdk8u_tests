/*
 * Copyright 2007 The Apache Software Foundation or its licensors, as applicable
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author Alexey Ignatenko
 * @version $Revision: 1.1 $
 */

package org.apache.harmony.test.reliability.api.serialization;

import org.apache.harmony.test.reliability.share.JarFilesScanner;
import org.apache.harmony.test.reliability.share.Test;
import java.io.File;
import java.io.NotSerializableException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Hashtable;

/*
 * Goal: check that serialization of instances of J2SE serializable classes 
 *       works as expected (ho crashes, hangs, etc.) in multi-threaded environment.
 *
 * The test does:
 *
 *   1. Reads parameters:
 *        param[1] - number of serialization threads
 *        param[2] - number of deserialization threads
 *        param[3] - directory where serizliation files will be created
 *      
 *   2. Starts param[1] "controlling" threads, each thread:
 *
 *       1. Starts single serialization thread, which:
 *
 *          a. Instantiates serializable classes. If instance of a class can not be created, 
 *             then, it is ignored, otherwise,
 *
 *          b. Writes created object into .ser file.
 *
 *       2. Waits for completion of the serializing thread. If serialization passed ok, then:
 *      
 *       3. Starts param[2] deserialization threads, each of the threads:
 *
 *           a. Reads an object from file.
 *
 *           b. Checks that its class is the same as the class of the original (serialized) object.
 *
 *       4. Finally, removes created .ser file.
 *   
 */

public class SerializableClassesTest extends Test {

    int NUMBER_OF_THREADS = 3;
    int NUMBER_OF_SUBTHREADS = 10;
    String OUT_DIR_NAME = ".";
    Class jlSerializableClass = null;

    volatile boolean failed = false;

    public static void main(String[] args) {
        System.exit(new SerializableClassesTest().test(args));
    }

    public int test(String[] params) {

        parseParams(params);
        try {
            jlSerializableClass = Class.forName("java.io.Serializable");
        } catch (ClassNotFoundException e1) {
            log.add("Failed to load java.io.Serializable class");
            e1.printStackTrace();
            return fail("");
        }
        failed = false;
        
        Thread thrds[] = new Thread[NUMBER_OF_THREADS];

        // run NUMBER_OF_THREADS of threads, each of the RunSerializationThread thread 
        // does serialization of objects of serializable classes and then starts 
        // NUMBER_OF_SUBTHREADS of threads, each deserializing the objects

        for (int i = 0; i < thrds.length; i++) {
            thrds[i] = new RunSerializationThread(this);
            thrds[i].start();
        }

        for (int j = 0; j < thrds.length; j++) {
            try {
                thrds[j].join();
            } catch (InterruptedException e) {
                log.add("" + e.getMessage() + " exception while joining " + j + "-th thread");
                failed = true;
            }
        }

        if (failed) {
            return fail("FAILED");
        }
        return pass("OK");
    }

    
    public void parseParams(String[] params) {
        if (params.length >= 1) {
            NUMBER_OF_THREADS = Integer.parseInt(params[0]);
        }
        if (params.length >= 2) {
            NUMBER_OF_SUBTHREADS = Integer.parseInt(params[1]);
        }
        if (params.length >= 3) {
            OUT_DIR_NAME = params[2];
        }
    }
}

class RunSerializationThread extends Thread {
    
    SerializableClassesTest base;
    
    RunSerializationThread(SerializableClassesTest base){
        this.base = base;
    }
   
    public void run(){
        Hashtable<Object, Object> ht = new Hashtable<Object, Object>();
        
        // First, do serialization into a file in a single SerializerThread thread

        SerializerThread serializerThread = new SerializerThread(base, base.OUT_DIR_NAME);
        
        serializerThread.start();
        try {
            serializerThread.join();
        } catch (InterruptedException ie){
        }
        
        // If serialization passed with no problems, then, Second step - start
        // NUMBER_OF_SUBTHREADS of threads which do deserialization from single file 
        // in parallel

        if (serializerThread.failed == false){
       
            SerThread[] t = new SerThread[base.NUMBER_OF_SUBTHREADS];
        
            for (int i = 0; i < t.length; ++i){
                t[i] = new DeSerializerThread(base, serializerThread.getSerFileName());
                t[i].start();
            }
        
            for (int i = 0; i < t.length; ++i){
                try {
                    t[i].join();
                } catch (InterruptedException ie){
                }
            }
            
        } else {
            base.log.add("Thread " + this.getId() + ": no deserialization due to failure in serialization");
        }

        // Finally, remove created by the serizliation thread (SerializerThread) files

        serializerThread.deleteSerFile();
    }
}

abstract class SerThread extends Thread {

    SerializableClassesTest base;

    ArrayList<String> classNames;

    volatile boolean failed = false;
    
    SerThread(SerializableClassesTest base){
        this.base = base;
    }
    
    // Receives a list of classes whose instances to be created and serialized
    void getClassNames() {
        classNames = ClassContainer.getSerializableClassNames();
    }

    
    public void run(){
        getClassNames();
        try {
            createStreams();
            int instantiated = 0, noninstantiated = 0;
            for (int i = 0; i < classNames.size(); ++i){
                Object o;
                try {
                    o = instantiate(classNames.get(i));
                    if (o == null){
                        continue;
                    }
                    ++instantiated;
                } catch (Throwable ex) {
                    // ex.printStackTrace();
                    // System.out.println(" Failed to instantiate: " + classNames.get(i));
                    ++noninstantiated;
                    continue;
                }
                process(classNames.get(i), o); //   <-- this is where serialization and deserialization runs
            }
            // System.out.println("Instantiated " + instantiated + 
            //                    ", failed to create instance " + noninstantiated + " classes");
       } catch (Throwable t){
           // t.printStackTrace();
           base.log.add("Thread " + this.getId() + ", unexpected exception: " + t + ", with message: " + t.getMessage());
           t.printStackTrace();
           base.failed = true;
           failed = true;
       } finally {
           closeStreams();
       }
    }
    
    abstract void process(String className, Object object) throws Throwable;

    Object instantiate(String className) throws Throwable {
        Class c = Class.forName(className);
        if (!checkSerializableAndNotAbstract(c)){
            return null;
        }
        Object o = null;
        try{
            o = c.newInstance();
        }catch (Throwable e){
        // Ignore
            return null;
        }
        //System.out.println(className);
        return o;
    }
    
    // Functions checks that class has Serializable interface and not abstract
    boolean checkSerializableAndNotAbstract(Class c){
        
        Class []intfs = c.getInterfaces();
        // try to find jlSerializableClass in interfaces
        for (int i = 0; i < intfs.length; i++){
            if (intfs[i].equals(base.jlSerializableClass)){
                int md = c.getModifiers(); 
                if (Modifier.isAbstract(md)){
                    return false;
                }
                return true;
            }
        }

        // jlSerializableClass not found - try to do find it in base class
        Class baseCls = c.getSuperclass();
        if (baseCls != null){
            return checkSerializableAndNotAbstract(baseCls);
        }
        
        return false;
    }
    
    abstract void createStreams() throws Exception;
    abstract void closeStreams();
}


class SerializerThread extends SerThread {
    ObjectOutputStream oos;
    OutputStream os;
    String fileName;
    String outDir;
    
    SerializerThread(SerializableClassesTest base, String outDir){
        super(base);
        this.outDir = outDir;
    }
        
    void process(String className, Object object) throws Throwable {
        try {
            oos.writeObject(object);
            // store class and its serialized object in order to compare class of deserialized 
            // object - later, in DeSerializerThread-s
            //System.out.println(object.getClass().getName() + " : " + fileName);
        } catch (NullPointerException e){
            // skip
        } catch (NotSerializableException e){
            // skip - the test is not intendent to check NotSerializableException 
        } catch (Throwable t) {
            base.log.add("Thread " + this.getId() + ": serialization of " + className + ": unexpected exception " + t);
            throw t;
        } 
    }

    void createStreams() throws Exception {
        this.oos = new ObjectOutputStream(createOutputStream());
    }
    
    OutputStream createOutputStream() throws Exception {
        this.fileName = createSerFile().getAbsolutePath();
        return (this.os = new FileOutputStream(fileName));
    }
   
    File createSerFile(){
        File f = null;
        synchronized (base){
            f = new File(new File(outDir).getAbsolutePath() + File.separator + 
                         "SerializableClassesTest" + File.separator + this.getId() + "_" + this.hashCode() + "_file.ser");
            if (f.exists()){
                f.delete();
            } else {
                File f2 = new File(f.getParent());
                f2.mkdirs();
            }
        }
        return f;
    }

    String getSerFileName(){
        return this.fileName;
    }

    void deleteSerFile(){
        File f = new File(fileName);
        synchronized(base){
            f.delete();
        }
    }
    
    void closeStreams(){
        if (os != null){
            try {
                this.os.close();
            } catch (IOException ioe){
            }
        }
        if (oos != null) {
            try {
                this.oos.close();
            } catch (IOException ioe){
            }
        }
    }
}

class DeSerializerThread extends SerThread {

    ObjectInputStream ois;
    InputStream is;
    String fileName; 

    DeSerializerThread(SerializableClassesTest base, String fileName){
        super(base);
        this.fileName = fileName;
    }

    void process(String className, Object object) throws Throwable {
        try {
            Object deserializedObject = null;
            try{
                deserializedObject = ois.readObject();
            } catch(IOException  e){
                // skip
                return;                
            }
            if (deserializedObject == null){
                return;
            }
        
            synchronized(deserializedObject){     // just for additional testing, no special meaning
                Thread.yield();
                if (className == deserializedObject.getClass().getName()){
                    base.log.add("Thread " + this.getId() + ", deserialization of: " + className + 
                                 ": readObject() returned instance of " + deserializedObject.getClass() + 
                                 "instead of expected " + className + " : " + fileName);
                    base.failed = true;
                    failed = true;
                }
                //deserializedObject.notifyAll();
            }
        } catch (Throwable t) {
            base.log.add("Deserialization of " + className + ": unexpected exception " + t);
            throw t;
        }
    }

    void createStreams() throws Exception {
        this.ois = new ObjectInputStream(createInputStream());
    }
    
    InputStream createInputStream() throws Exception {
        return (this.is = new FileInputStream(this.fileName));
    }
    
    void closeStreams(){
        if (ois != null){
            try {
                this.ois.close();
            } catch (IOException ioe){
            }
        }
        if (is != null){
            try {
                this.is.close();
            } catch (IOException ioe){
            }
        }
    }
}


class ClassContainer {

    static ArrayList<String> serializableClassNamesAL = null; 

    static synchronized ArrayList<String> getSerializableClassNames() {
        if (serializableClassNamesAL == null){
            serializableClassNamesAL = initAL();
        }
        return serializableClassNamesAL;
    }
    
    static ArrayList<String> initAL() {
        ArrayList<String> al = JarFilesScanner.getClassFilesInJRE();
        for (int i = 0; i< al.size(); i++){
            String nm = al.get(i);
            nm = nm.replace("/", ".");
            nm = nm.replace("\\", ".");
            nm = nm.replace(".class", "");
            al.set(i, nm);
        }
        return al; 
    }
    
}
