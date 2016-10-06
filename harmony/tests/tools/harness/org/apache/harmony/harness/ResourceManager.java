/*
    Copyright 2005-2006 The Apache Software Foundation or its licensors, as applicable

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

    See the License for the specific language governing permissions and
    limitations under the License.
*/
/**
 * @author Vladimir A. Ivanov
 * @version $Revision: 1.15 $
 */
package org.apache.harmony.harness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.apache.harmony.harness.Resource;
import org.apache.harmony.harness.ExecThread;

class ResourceManager extends Thread {

    private final String       classID       = "ResourceManager";

    public static final String EU_RESOURCE   = "execThread";
    public static final String MC_RESOURCE   = "mcore";

    private Main               mMod          = Main.getCurCore();
    private ArrayList          freeResource  = new ArrayList();
    private ArrayList          lockResource  = new ArrayList();
    private ArrayList          totalResource = new ArrayList();

    private Logging            log           = mMod.getInternalLogger();

    //special kind of resources
    ExecThread[]               units;

    synchronized boolean lockResourceIsEmpty() {
        if (lockResource.size() > 0) {
            return false;
        }
        return true;
    }

    synchronized boolean freeResourceIsEmpty() {
        if (freeResource.size() > 0) {
            return false;
        }
        return true;
    }

    synchronized boolean totalResourceIsEmpty() {
        if (totalResource.size() > 0) {
            return false;
        }
        return true;
    }

    //check: it is possible to allocate needed resource for the
    //test run. It is a copy of allocResources() but work with totalResource
    //and not modified any resource information
    boolean allocResourcePossible(String name, String className, int groupNumber) {
        Resource resource;
        boolean findOK = false;
        boolean findCl = false;
        boolean findName = false;
        int resCntCl = 0;
        int resCntNm = 0;
        //selection by className (remote and same/local)
        if (name.length() < 1) {
            for (int i = totalResource.size() - 1; i >= 0; i--) {
                resource = (Resource)totalResource.get(i);
                if (className.equalsIgnoreCase(resource.className)) {
                    findOK = true;
                    resCntNm++;
                    if (groupNumber <= 1 || resCntNm == groupNumber) {
                        break;
                    }
                }
            }
            if (groupNumber > 1 && resCntNm != groupNumber) {
                findOK = false;
            }
            // selection by name and class
        } else {
            for (int i = totalResource.size() - 1; i >= 0; i--) {
                resource = (Resource)totalResource.get(i);
                if (className.equalsIgnoreCase(resource.className)) {
                    findCl = true;
                    resCntCl++;
                    if (groupNumber <= 1 || resCntCl == groupNumber) {
                        break;
                    }
                }
            }
            if (groupNumber > 1 && resCntCl != groupNumber) {
                findCl = false;
            }
            for (int i = totalResource.size() - 1; i >= 0; i--) {
                resource = (Resource)totalResource.get(i);
                if (name.equalsIgnoreCase(resource.name)) {
                    findName = true;
                    resCntNm++;
                    if (groupNumber <= 1 || resCntNm == groupNumber) {
                        break;
                    }
                }
            }
            if (groupNumber > 1 && resCntNm != groupNumber) {
                findCl = false;
            }
            if (findCl && findName) {
                findOK = true;
            }
        }
        return findOK;
    }

    //general resources like printers etc
    boolean allocSpecialResourcePossible(String[] resourceNames) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tallocSpecialResourcePossible(): ";
        if (resourceNames == null || resourceNames.length < 1) {
            return true;
        }
        boolean[] findOK = new boolean[resourceNames.length];
        Resource resource;
        for (int i = 0; i < resourceNames.length; i++) {
            if (resourceNames[i] != null) {
                for (int y = 0; y < totalResource.size(); y++) {
                    resource = (Resource)totalResource.get(y);
                    //external resource id is name. For internal resource it
                    // may be null
                    if (resource.name != null
                        && resourceNames[i].equalsIgnoreCase(resource.name)) {
                        findOK[i] = true;
                        break;
                    }
                }
            }
        }
        for (int i = 0; i < findOK.length; i++) {
            if (findOK[i] != true) {
                log.add(Level.FINE, methodLogPrefix
                    + "Impossible to allocate external resources");
                return false;
            }
        }
        return true;
    }

    synchronized boolean allocResources(String name, String className,
        int repNumber, int groupNumber) {

        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tallocResources(): ";
        Resource resource;
        boolean findOK = false;
        boolean findCl = false;
        boolean findName = false;
        int resCntCl = 0;
        int resCntNm = 0;
        //selection by className only (remote and same/local)
        if (className != null && (name == null || name.length() < 1)) {
            for (int i = freeResource.size() - 1; i >= 0; i--) {
                resource = (Resource)freeResource.get(i);
                if (className.equalsIgnoreCase(resource.className)) {
                    findOK = true;
                    resource.lockedBy = repNumber;
                    freeResource.remove(i);
                    lockResource.add(resource);
                    resCntNm++;
                    if (groupNumber <= 1 || resCntNm == groupNumber) {
                        break;
                    }
                }
            }
            if (groupNumber > 1 && resCntNm != groupNumber) {
                freeResources(repNumber);
                findOK = false;
                log
                    .add(
                        Level.FINE,
                        methodLogPrefix
                            + "Type selection: Can't allocate resource of pointed type: "
                            + className);
            }
            //selection by name only
        } else if (name != null
            && (className == null || className.length() < 1)) {
            for (int i = freeResource.size() - 1; i >= 0; i--) {
                resource = (Resource)freeResource.get(i);
                if (name.equalsIgnoreCase(resource.name)) {
                    findOK = true;
                    resource.lockedBy = repNumber;
                    freeResource.remove(i);
                    lockResource.add(resource);
                    resCntNm++;
                    if (groupNumber <= 1 || resCntNm == groupNumber) {
                        break;
                    }
                }
            }
            if (groupNumber > 1 && resCntNm != groupNumber) {
                freeResources(repNumber);
                findOK = false;
                log
                    .add(
                        Level.FINE,
                        methodLogPrefix
                            + "Name selection: Can't allocate resource with pointed name: "
                            + name);
            }
            // selection by name and class
        } else {
            if (name == null || className == null)
                for (int i = freeResource.size() - 1; i >= 0; i--) {
                    resource = (Resource)freeResource.get(i);
                    if (className.equalsIgnoreCase(resource.className)) {
                        findCl = true;
                        resource.lockedBy = repNumber;
                        freeResource.remove(i);
                        lockResource.add(resource);
                        resCntCl++;
                        if (groupNumber <= 1 || resCntCl == groupNumber) {
                            break;
                        }
                    }
                }
            if (groupNumber > 1 && resCntCl != groupNumber) {
                freeResources(repNumber);
                findCl = false;
            }
            for (int i = freeResource.size() - 1; i >= 0; i--) {
                resource = (Resource)freeResource.get(i);
                if (name.equalsIgnoreCase(resource.name)) {
                    findName = true;
                    resource.lockedBy = repNumber;
                    freeResource.remove(i);
                    lockResource.add(resource);
                    resCntNm++;
                    if (groupNumber <= 1 || resCntNm == groupNumber) {
                        break;
                    }
                }
            }
            if (groupNumber > 1 && resCntNm != groupNumber) {
                freeResources(repNumber);
                findCl = false;
            }
            if (findCl && findName) {
                findOK = true;
            } else {
                if (!findCl) {
                    log.add(Level.FINE, methodLogPrefix
                        + "Can't allocate resource of pointed type: "
                        + className);
                }
                if (!findName) {
                    log.add(Level.FINE, methodLogPrefix
                        + "Can't allocate resource with pointed name: " + name);
                }
                freeResources(repNumber);
            }
        }
        log.add(Level.FINEST, methodLogPrefix + "Resource " + name + " type "
            + className + "\tResult of allocation: " + findOK);
        return findOK;
    }

    synchronized boolean allocSpecialResource(String[] resourceNames,
        int allocNumber) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tallocSpecialResource(): ";
        if (resourceNames == null || resourceNames.length < 1) {
            return true;
        }
        boolean[] findOK = new boolean[resourceNames.length];
        Resource resource;
        for (int i = 0; i < resourceNames.length; i++) {
            if (resourceNames[i] != null) {
                for (int y = 0; y < freeResource.size(); y++) {
                    resource = (Resource)freeResource.get(y);
                    if (resource.name != null
                        && resourceNames[i].equalsIgnoreCase(resource.name)) {
                        resource.lockedBy = allocNumber;
                        freeResource.remove(y);
                        lockResource.add(resource);
                        findOK[i] = true;
                    }
                }
            }
        }
        for (int i = 0; i < findOK.length; i++) {
            if (findOK[i] != true) {
                freeResources(allocNumber);
                log.add(Level.FINE, methodLogPrefix
                    + "Can't allocate external resources for test");
                return false;
            }
        }
        return true;
    }

    Resource getAllocResource(String name, String className, int num) {
        Resource resource;
        for (int i = 0; i < lockResource.size(); i++) {
            resource = (Resource)lockResource.get(i);
            if (name.length() < 1) {
                if (resource.lockedBy == num
                    && resource.className.equalsIgnoreCase(className)) {
                    return resource;
                }
            } else if (resource.lockedBy == num
                && resource.name.equalsIgnoreCase(name)) {
                return resource;
            }
        }
        return null;
    }

    ArrayList getReservedResourceList(int reservNumber) {
        ArrayList retVal = new ArrayList();
        Resource resource;
        for (int i = 0; i < lockResource.size(); i++) {
            resource = (Resource)lockResource.get(i);
            if (resource.lockedBy == reservNumber) {
                retVal.add(resource);
            }
        }
        return retVal;
    }

    synchronized void freeResources(int repNumber) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tfreeResources(I): ";
        Resource resource;
        for (int i = lockResource.size() - 1; i >= 0; i--) {
            resource = (Resource)lockResource.get(i);
            if (resource.lockedBy == repNumber) {
                resource.lockedBy = -1;
                lockResource.remove(i);
                freeResource.add(resource);
                log.add(Level.FINE, methodLogPrefix
                    + Util.getCurrentTimeToLog() + " Free resources: "
                    + resource.getResourceTypeName() + " locked by "
                    + repNumber);
            }
        }
    }

    synchronized void freeResources(int repNumber, String type) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tfreeResources(IS): ";
        Resource resource;
        for (int i = lockResource.size() - 1; i >= 0; i--) {
            resource = (Resource)lockResource.get(i);
            if (resource.lockedBy == repNumber
                && resource.getResourceTypeName() == type) {
                resource.lockedBy = -1;
                lockResource.remove(i);
                freeResource.add(resource);
                log.add(Level.FINE, methodLogPrefix
                    + Util.getCurrentTimeToLog() + " Free resources type "
                    + type + " locked by " + repNumber);
            }
        }
    }

    
    ArrayList getReservedResourceList(int reservNumber, String name,
        String className) {
        ArrayList retVal = new ArrayList();
        Resource resource;
        if (name != null && name.length() > 0) {
            for (int i = 0; i < lockResource.size(); i++) {
                resource = (Resource)lockResource.get(i);
                if (resource.lockedBy == reservNumber
                    && resource.name.equalsIgnoreCase(name)) {
                    retVal.add(resource);
                }
            }
        } else {
            for (int i = 0; i < lockResource.size(); i++) {
                resource = (Resource)lockResource.get(i);
                if (resource.lockedBy == reservNumber
                    && resource.className.equalsIgnoreCase(className)) {
                    retVal.add(resource);
                }
            }
        }
        return retVal;
    }

    Resource getResource(String name, String className, int cnt) {
        Resource resource;
        if (allocResources(name, className, cnt, 0)) {
            resource = getAllocResource(name, className, cnt);
            resource.lockedBy = cnt;
            return resource;
        }
        return null;
    }

    void addResource(Resource rs) {
        freeResource.add(rs);
    }

    void addMCoreResource(MCoreIR mc) {
        freeResource.add(new Resource("", null, MC_RESOURCE, mc));
    }

    void updateMCoreResources() {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tupdateMCoreResources(): ";
        List mcs = mMod.getMCPoll().getAll();
        for (int i = 0; i < mcs.size(); i++) {
            boolean registered = false;
            for (int cnt = 0; cnt < totalResource.size(); cnt++) {
                if (mcs.get(i).equals(
                    ((Resource)totalResource.get(cnt)).getResourceTypeClass())) {
                    registered = true;
                }
            }
            if (!registered) {
                Resource rs = new Resource("", null, MC_RESOURCE, mcs.get(i));
                freeResource.add(rs);
                totalResource.add(rs);
                log.add(Level.FINE, methodLogPrefix
                    + Util.getCurrentTimeToLog()
                    + " add to resource new MCore OK: " + mcs.get(i));
            }
        }
    }

    void createResourceList(int concurency, Dispatcher reportToObj) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tcreateResourceList(): ";
        Resource resource;
        MCPool pool = mMod.getMCPoll();
        ExecUnitPool eup = mMod.getExecUnitPool();
        String[] euNames = eup.getNames();
        //in the case of remote run need one execution thread and one
        //or more MCore for each test
        ExecThread[] units = new ExecThread[concurency];
        for (int i = 0; i < units.length; i++) {
            units[i] = new ExecThread(reportToObj);
            units[i].start();
        }

        if (mMod.getConfigIR().getLocalM() == Main.LOCAL) {
            for (int i = 0; i < euNames.length; i++) {
                ExecUnit[] eu = eup.getSet(euNames[i]);
                //note, in the case Local we always have
                //execunits = concurency and units = concurency
                //all execution units are resources
                for (int y = 0; y < eu.length; y++) {
                    resource = new Resource(euNames[i], eu[y], "", units[y]);
                    freeResource.add(resource);
                    log.add(Level.FINE, methodLogPrefix
                        + "add to resource OK: " + units[y]);
                }
            }
            //execution threads are also resource
            for (int cnt = 0; cnt < units.length; cnt++) {
                resource = new Resource("", null, EU_RESOURCE, units[cnt]);
                freeResource.add(resource);
                log.add(Level.FINE, methodLogPrefix + "add to resource OK: "
                    + units[cnt]);
            }
        } else { //for remote configuration management by MC
            for (int cnt = 0; cnt < units.length; cnt++) {
                resource = new Resource("", null, EU_RESOURCE, units[cnt]);
                freeResource.add(resource);
                log.add(Level.FINE, methodLogPrefix + "add to resource OK: "
                    + units[cnt]);
            }
        }
        if (pool != null) {
            for (int i = 0; i < pool.size(); i++) {
                addMCoreResource(pool.get(i));
                log.add(Level.CONFIG, methodLogPrefix
                    + "add to resource MCore OK: " + pool.get(i));
            }
        }
        ArrayList extResources = mMod.getConfigIR().getResources();
        for (int i = 0; i < extResources.size(); i++) {
            HashMap extResource = (HashMap)extResources.get(i);
            if (extResource.containsKey("resource")) {
                int cnt = 1;
                try {
                    cnt = Integer.parseInt((String)extResource
                        .get("concurrency"));
                } catch (Throwable th) {
                    //do nothing - use default value
                }
                for (int y = 0; y < cnt; y++) {
                    resource = new Resource(
                        (String)extResource.get("resource"), null, null, null);
                    freeResource.add(resource);
                }
                if (extResource.containsKey("runit")) {
                    String cmd = ((String)extResource.get("runit")).trim();
                    if (!cmd.equalsIgnoreCase("false")) {
                        Process proc;
                        try {
                            proc = java.lang.Runtime.getRuntime().exec(cmd);
                            Main.getCurCore().addExtProcs(proc);
                        } catch (Throwable th) {
                            log.add(Level.INFO, methodLogPrefix
                                + "Can't execute command for resource:'" + cmd
                                + "'", th);
                        }
                    }
                }
            }
        }
        totalResource = (ArrayList)freeResource.clone();
    }

    public void waitToResourceRelease(int timeout) {
        if (units != null) {
            for (int i = 0; i < units.length; i++) {
                units[i].stopIt();
                for (int y = timeout; y != 0; y--) {
                    try {
                        units[i].join(Constants.INTERNAL_TIMEOUT);
                        if (!units[i].isAlive()) {
                            break;
                        }
                    } catch (InterruptedException e) {
                        //do nothing
                    }
                }
            }
        }
    }
}

