/*
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
 * @author Aleksey Ignatenko
 * @version $Revision: 1.0 $
 */

package org.apache.harmony.test.reliability.vm.finalization;

import org.apache.harmony.test.reliability.share.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/*
 * Goal: check VM finalization subsystem 
 * The test relies that finalization is not garanteed by spec
 * Accepted parameters:
 * param[0] - number of threads to start
 * param[1] - path to folder where class files are located - to pack them in jar file
 * param[2] - path to folder where temporary jar file are to be located

 * The test consists of 5 parts executed consequently
 * 1 part - IOFinalizerActivities
 *   - the test case finds class files of reliability suit and transfers them 
 *   into 1 jar file in Tmp directory in finalizer. Execution is done in params[0] threads 
 * 2 part - RefFinalizerActivities
 *   - the test case builds list of entries with different reference types: weak, phantom, soft, solid
 *   - operations with ReferenceQueue are done for phantom refs in finilizer
 *   - Plus running through the list and creating additional not tracked list 
 * 3 part - SimplethreaddingActivities
 *   - the test starts threads in finalizer
 * 4 part - in the parts 1-3 finalize sections were instrumented to save reference to finalizer thread.
 *   The test gets some info about finalizer thread.   
 */

public class FinalizeResourceOrientedTest extends Test {
    volatile static boolean failed = false;
    public final static int NUMBER_OF_THREADS_OR_INSTANCES = 10;
    public final static int NUMBER_OF_CHAIN_OBJECTS = 100;
    public static int numberOfThreads = NUMBER_OF_THREADS_OR_INSTANCES;
    public static int numberOfObjects = NUMBER_OF_THREADS_OR_INSTANCES;
    public static int numberOfChainObjects = NUMBER_OF_CHAIN_OBJECTS;
    static Thread finThread = null;
    public static String classFolder = System.getProperty("java.class.path");
    public static String tmpFolder = System.getProperty("java.io.tmpdir");;
    
    synchronized public static void reportFinalizeThread(Thread t){
        if (finThread == null){
            finThread = t;
        }
    }
    
    public static void setFailed(){
        failed = true;
    }
    
    public static void commenterror(String msg){
        log.add(msg);
    }
    
    public static void main(String[] params){
        System.exit(new FinalizeResourceOrientedTest().test(params));
    }

    public int test(String []params){
        parseParams(params);
        
        // Stage 1: launch IOFinalizerActivities test
        try{
            for (int i=0; i<numberOfObjects; i++){
                IOFinalizerActivities act = new IOFinalizerActivities();
                act.Test();
            }            
        }catch(Throwable t){
            failed = true;
        }
        
        System.gc();
        System.runFinalization();
        
        if (failed){
            return fail("rootcause is in IOFinalizerActivities");
        }
        
        
        // Stage 2: launch RefFinalizerActivities test
        RefFinalizerActivities[] thrds = new RefFinalizerActivities[numberOfThreads];
        for (int i=0; i<thrds.length; i++ ){
            thrds[i] = new RefFinalizerActivities();
            thrds[i].start();
        }

        System.gc();
        System.runFinalization();
        
        for (int i=0; i<thrds.length; i++ ){
            try {
                thrds[i].join();
                thrds[i].hintnull();
                thrds[i] = null;
            } catch (InterruptedException e) {
                failed = true;
                log.add(e);
            }
            System.gc();
            System.runFinalization();
            
        }
        thrds = null;
        if (failed){
            return fail("rootcause is in RefFinalizerActivities");
        }

        // Stage 3: launch SimplethreaddingActivities test
        for (int i = 0; i < numberOfObjects; i++){
            SimplethreaddingLaunchActivities sta = new SimplethreaddingLaunchActivities();
            sta = null;
        }
        System.gc();
        System.runFinalization();
        

        // Stage 4: test finalizer thread if any
        while (finThread != null){
            try{
                finThread.checkAccess();
            } catch (SecurityException e){
                break;
            }
            finThread.getName();
            finThread.getThreadGroup();
            finThread.getState();
            break;
        }
        
        
        // Final stage: results
        if (failed){
            return fail("rootcause is RestoreObjectsFinalizerActivities");
        }
        
        
        return pass("OK");
    }
    
    public void parseParams(String[] params) {

        if (params.length >= 1) {
            numberOfThreads = Integer.parseInt(params[0]);
        }
        if (params.length >= 2) {
            classFolder = params[1];
        }
        if (params.length >= 3) {
            tmpFolder = params[2];
        }
    }
    
}

    /////////////////// Functional classes section /////////////////////////////

class IOFinalizerActivities {

    public void Test(){
        // scan class files in classFolder if any
        ArrayList<File> clFiles = new ArrayList<File>();
        String dir = FinalizeResourceOrientedTest.classFolder;
        scanClassFiles(dir, clFiles);
        clFiles = null;
    }
    
    protected void finalize(){
        FinalizeResourceOrientedTest.reportFinalizeThread(Thread.currentThread());
        ArrayList<File> classFiles = new ArrayList<File>();
        String workDir = FinalizeResourceOrientedTest.classFolder;
        scanClassFiles(workDir, classFiles);
        if (classFiles.size() == 0){
            FinalizeResourceOrientedTest.commenterror("failed to scan class files in " + workDir);
            FinalizeResourceOrientedTest.setFailed();
            return;
        }
        
        // Create temporary file for processing
        File tmpFileToProcess = null;;
        String tmpDir = FinalizeResourceOrientedTest.tmpFolder;
        if (tmpDir.length() == 0){
            tmpDir = FinalizeResourceOrientedTest.classFolder;
        }
        tmpDir += "goldenfiles";
        File tmpDirFolder = new File(tmpDir);
        // create this dir if there is no such
        tmpDirFolder.mkdir();
        
        // generate jar file name
        String postfix = "" + this.hashCode() + ".jar";
        ZipOutputStream zo = null;
        // create jar file
        try {
            tmpFileToProcess = File.createTempFile("JAR_", postfix, tmpDirFolder);
            //tmpFileToProcess.deleteOnExit();
            zo = new ZipOutputStream(new FileOutputStream(tmpFileToProcess));
        } catch (IOException e1) {
            FinalizeResourceOrientedTest.commenterror("failed to create jar file " + tmpFileToProcess.getAbsolutePath());
            FinalizeResourceOrientedTest.setFailed();
            return;
        }
    
        // flush class files into one jar file 
        byte buf[] = new byte[2048];
        for (int i = 0; i<classFiles.size();i++){
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(classFiles.get(i));
                try{
                    zo.putNextEntry(new ZipEntry(classFiles.get(i).getName()));
                } catch (java.util.zip.ZipException ze){
                    // Expected, e.g. equal names
                    fis.close();
                    continue;
                }
                int nBytes = 0;
                while((nBytes = fis.read(buf)) > 0){
                    zo.write(buf, 0, nBytes);
                }
            } catch (IOException e) {
                FinalizeResourceOrientedTest.commenterror("Unexpected error during conversion into jar file: " + e.getMessage());
                FinalizeResourceOrientedTest.setFailed();
                return;
            }finally{
                try {
                    zo.closeEntry();
                    fis.close();
                } catch (Throwable e) {
                    // Expected in case of unknown exception
                }
            }
        }
        try {
            zo.close();
            tmpFileToProcess.delete();
        } catch (IOException e) {
            FinalizeResourceOrientedTest.commenterror("Unexpected error when closing/deleting jar file: " + e.getMessage());
            FinalizeResourceOrientedTest.setFailed();
            return;
        }
    }
    
    // scan class files in test folder
    void scanClassFiles(String dir, ArrayList<File> classFiles){
        File root = new File(dir);
        File[] files = root.listFiles();
        for (int i=0; i< files.length; i++){
            if (files[i].getName().endsWith(".class")){
                synchronized(this){
                    classFiles.add(files[i]);
                }
            }
            if (files[i].isDirectory()){
                scanClassFiles(files[i].getAbsolutePath(), classFiles);
            }
        }
        files = null;
        root = null;
    }

}

    // Primitives to work with in finalization  

interface Entry{
    void nullRef();
    Object getRef();
    boolean isPhantom();
    ReferenceQueue getQue();
}

class WeakRefEntry implements Entry{
    WeakRefEntry(Entry obj){
        ref = new WeakReference(obj);
    }
    public void nullRef(){
        ref = null;
    }
    public boolean isPhantom(){
        return false;
    }
    public ReferenceQueue getQue(){
        return null;
    }
    public WeakReference ref = null;
    protected void fialize(){
        FinalizeResourceOrientedTest.reportFinalizeThread(Thread.currentThread());
        RefFinalizerActivities.weakCount++;
    }
    public Object getRef() {
        return ref;
    }
}

class SoftRefEntry implements Entry{
    SoftRefEntry(Entry obj){
        ref = new SoftReference(obj);
    }
    public void nullRef(){
        ref = null;
    }
    public Object getRef() {
        return ref;
    }
    public boolean isPhantom(){
        return false;
    }
    public ReferenceQueue getQue(){
        return null;
    }
    public SoftReference ref = null;
    protected void fialize(){
        FinalizeResourceOrientedTest.reportFinalizeThread(Thread.currentThread());
        RefFinalizerActivities.softCount++;
    }
}

class PhantomRefEntry implements Entry{
    PhantomRefEntry(Entry obj){
        ref = new PhantomReference(obj, refQue);
    }
    public void nullRef(){
        ref = null;
    }
    public Object getRef() {
        return ref;
    }
    public boolean isPhantom(){
        return true;
    }
    public ReferenceQueue getQue(){
        return refQue;
    }
    public PhantomReference ref = null;
    static ReferenceQueue refQue = new ReferenceQueue();
    protected void fialize(){
        FinalizeResourceOrientedTest.reportFinalizeThread(Thread.currentThread());
        RefFinalizerActivities.phantomCount++;
    }

}

class solidRefEntry implements Entry{
    solidRefEntry(Entry obj){
        ref = obj;
    }
    public void nullRef(){
        ref = null;
    }
    public Object getRef() {
        return ref;
    }
    public boolean isPhantom(){
        return false;
    }
    public ReferenceQueue getQue(){
        return null;
    }
    public Object ref = null;
    protected void fialize(){
        FinalizeResourceOrientedTest.reportFinalizeThread(Thread.currentThread());
        RefFinalizerActivities.solidCount++;
    }
}

    // building ref connections with freeing in finillize
class RefFinalizerActivities extends Thread{

    public static int weakCount = 0;
    public static int softCount = 0;
    public static int phantomCount = 0;
    public static int solidCount = 0;
    
    Random rnd = new Random();
    Entry first = null;// first element of the list
    Entry last = null; // last  element of the list    
    Object refs[] = new Object[FinalizeResourceOrientedTest.numberOfChainObjects];

    void hintnull(){
        first = null;
        last=null;
    }
    
    public void run(){
        // create list of mixed references
        for (int i = 0; i < FinalizeResourceOrientedTest.numberOfChainObjects; i++){
            last = newRandomEntry(last);
            if (i == 0){
                first = last;
            }
        }
    }
    
    protected void finalize(){
        FinalizeResourceOrientedTest.reportFinalizeThread(Thread.currentThread());
        // null all created refs
        Entry ce = first;
        while (ce != null){
            Entry e = null;
            synchronized(this){            
                e = (Entry)ce.getRef();
            }
            // process ReferenceQueue
            ReferenceQueue rq = null;
            if ((rq = ce.getQue()) != null){
                synchronized(ce){                
                    rq.poll();
                }
                try {
                    while (rq.remove(1) != null);
                } catch (InterruptedException e1) {
                }
            }
            ce.nullRef();
            ce = e;
        }
        first = last = null;

        int n_fin = weakCount + softCount + phantomCount + solidCount;
        if (n_fin > FinalizeResourceOrientedTest.numberOfChainObjects){
            FinalizeResourceOrientedTest.setFailed();
            FinalizeResourceOrientedTest.commenterror("Conflict: number of finilized objects " + n_fin + 
                " > than number of created objects " + FinalizeResourceOrientedTest.numberOfChainObjects);
        }
        
        // create new list - no refs nulling - GC is to check reachability for these objects
        synchronized (this){
            for (int i = 0; i < FinalizeResourceOrientedTest.numberOfChainObjects; i++){
                last = newRandomEntry(last);
                if (i == 0){
                    first = last;
                }
            }
        }
        
    }

    // creates Entry with different type of Ref
    Entry newRandomEntry(Entry obj){
        int cs = rnd.nextInt(4);
        Entry entry = null;
        switch (cs){
            case 0:
                entry = new WeakRefEntry(obj);
                break;
            case 1:
                entry = new SoftRefEntry(obj);
                break;
            case 2:
                entry = new PhantomRefEntry(obj);
                break;
            case 3:
                entry = new solidRefEntry(obj);
                break;
            default:
                entry = new WeakRefEntry(obj);
                break;
        }
        return entry;
    }
    
}


    // launching new threads in finalizer
class SimplethreaddingLaunchActivities {
    protected void finalize(){
        // starting threads from existing tests above
        new RefFinalizerActivities().start();
    }
}
