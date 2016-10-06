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
 * @author A.Tyuryushkin
 * @version $Revision: 1.14 $
 */
package org.apache.harmony.harness;

import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;

public class TECfgHandler extends CfgHandler {

    private static final String classID       = "TECfgHandler";

    private HashMap             plugins       = new HashMap();
    private ArrayList           plugin        = new ArrayList();
    private HashMap             parameters    = new HashMap();
    private HashMap             values        = new HashMap();
    private ArrayList           value         = new ArrayList();
    private ArrayList           remoteRunner  = new ArrayList();
    private ArrayList           host          = new ArrayList();
    private ArrayList           subSuites     = new ArrayList();
    private ArrayList           resources     = new ArrayList();
    private HashMap             resource      = new HashMap();
    private String              resourceName  = "";
    private String              propertyName  = "";
    private String              pluginName    = "";
    private String              parameterName = "";
    private boolean             repeat        = false;
    private Logging             log           = Main.getCurCore()
                                                  .getInternalLogger();

    public TECfgHandler(ConfigIR curConfig) {
        super(curConfig);
    }

    public void startElement(String uri, String localName, String qName,
        Attributes attrs) throws SAXException {
        context.reset();
        if (localName.equals("exec-mode")) {
            keyName = attrs.getValue("name");
        }
        if (localName.equals("host")) {
            host.add(attrs.getValue("name"));
        }
        if (localName.equals("resource")) {
            resourceName = attrs.getValue("name");
            resource = new HashMap();
        }
        if (localName.equals("property")) {
            propertyName = attrs.getValue("name");
        }
        if (localName.equals("plugin")) {
            pluginName = attrs.getValue("name");
            plugin.add(attrs.getValue("class-name"));
        }
        if (localName.equals("parameter")) {
            parameterName = attrs.getValue("name");
        }
        if (localName.equals("value")) {
            keyName = attrs.getValue("name");
            if (values.containsKey(keyName)) {
                repeat = true;
            }
        }
    }

    public void endElement(String uri, String localName, String qName)
        throws SAXException {

        String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tendElement(): ";

        if (localName.equals("property")) {
            java.lang.reflect.Method[] methods = theConfig.getClass()
                .getMethods();
            int methodsCount = methods.length;
            boolean stop = false;
            for (int i = 0; i < methodsCount && !stop; i++) {
                if (methods[i].getName().equalsIgnoreCase("set" + propertyName)) {
                    log.add(Level.FINE, methods[i].getName());
                    Class[] args = methods[i].getParameterTypes();
                    if (args.length == 1) {
                        Object[] objects = { context.toString().trim() };
                        if (args[0].toString().equals("class java.lang.String")) {
                            // String
                            log.add(Level.FINE, methodLogPrefix + "\t('"
                                + objects[0] + "')");
                        }
                        if (args[0].toString().equals("int")) {
                            // Integer
                            objects[0] = Integer.valueOf((String)objects[0]);
                            log.add(Level.FINE, methodLogPrefix + "\t("
                                + objects[0] + ")");
                        }
                        //
                        try {
                            methods[i].invoke(theConfig, objects);
                        } catch (InvocationTargetException e) {
                            log
                                .add(methodLogPrefix
                                    + "Error in ENV configuration: setter 'set"
                                    + propertyName
                                    + "' have been return exception");
                        } catch (IllegalAccessException e) {
                            log.add(methodLogPrefix
                                + "Error in ENV configuration: setter 'set"
                                + propertyName + "' is inaccessible in class");
                        }
                    }
                    stop = true;
                }
            }
        }

        if (localName.equals("exec-mode")) {
            keyValue = context.toString().trim();
            if (keyName.length() > 0 && keyValue != null) {
                if (keyName.equalsIgnoreCase(Constants.GEN_VM_OPT)
                    || keyName.equalsIgnoreCase(Constants.GEN_ENV)) {
                    Object obj = theConfig.getProperty(keyName);
                    String value = keyValue;
                    if (obj != null) {
                        try {
                            if (((String)obj).length() > 0) {
                                value = (String)obj + " " + keyValue;
                            }
                        } catch (Exception e) {
                            log.add(Level.FINE, methodLogPrefix
                                + "setProperty('" + keyName + "', '" + keyValue
                                + "') -> " + "can't set option second time");
                        }
                    }
                    theConfig.setProperty(keyName, value);
                } else {
                    theConfig.setProperty(keyName, keyValue);
                }
                log.add(Level.FINE, methodLogPrefix + "setProperty('" + keyName
                    + "', '" + keyValue + "') -> "
                    + theConfig.getProperty(keyName));
            }
        }

        if (localName.equals("run-remote")) {
            theConfig.setRemoteRunner(remoteRunner);
            log.add(Level.FINE, methodLogPrefix + "setRemoteRunner(ArrayList["
                + remoteRunner.size() + "])");
            remoteRunner = (ArrayList)theConfig.getRemoteRunner();
            for (int i = 0; i < remoteRunner.size(); i++) {
                host = (ArrayList)remoteRunner.get(i);
                String key = "";
                for (int j = 0; j < host.size(); j++) {
                    if (j == 0)
                        key = "host";
                    if (j == 1)
                        key = "port";
                    if (j == 2)
                        key = "mode";
                    if (j == 3)
                        key = "run";
                    log.add(Level.CONFIG, "\t " + key + " - ('"
                        + host.get(j).toString() + "') -> ");
                }
            }
        }
        if (localName.equals("host")) {
            remoteRunner.add(host);
            host = new ArrayList();
        }
        if (localName.equals("port")) {
            host.add(context.toString().trim());
        }
        if (localName.equals("mode")) {
            host.add(context.toString().trim());
        }
        if (localName.equals("run")) {
            host.add(context.toString().trim());
        }

        if (localName.equals("totest")) {
            theConfig.setTestedSubSuites(subSuites);
            log
                .add(Level.CONFIG, methodLogPrefix
                    + "setTestedSubSuites(ArrayList[" + subSuites.size()
                    + "]) -> ");
        }
        if (localName.equals("subsuite")) {
            keyValue = context.toString().trim();
            subSuites.add(keyValue);
        }

        if (localName.equals("plugin-list")) {
            if (plugins.size() > 0) {
                theConfig.setPlugins(plugins);
                log.add(Level.CONFIG, methodLogPrefix + "setPlugins(HashMap["
                    + plugins.size() + "]) ->");
                plugins = theConfig.getPlugins();
                for (Iterator i = plugins.keySet().iterator(); i.hasNext();) {
                    Object key = i.next();
                    plugin = (ArrayList)plugins.get(key);
                    log.add(Level.CONFIG, methodLogPrefix + "Plug-in:"
                        + key.toString() + " = " + plugin.get(0).toString());
                    for (Iterator j = ((HashMap)plugin.get(1)).keySet()
                        .iterator(); j.hasNext();) {
                        Object keyp = j.next();
                        parameters = (HashMap)((HashMap)plugin.get(1))
                            .get(keyp);
                        log.add(Level.FINE, methodLogPrefix + "\tPARAMETER:"
                            + keyp.toString());
                        for (Iterator v = parameters.keySet().iterator(); v
                            .hasNext();) {
                            Object keyv = v.next();
                            value = (ArrayList)parameters.get(keyv);
                            String tmpStore = "\t\t- " + keyv.toString()
                                + " = [";
                            for (int k = 0; k < value.size(); k++) {
                                tmpStore = tmpStore + (String)value.get(k)
                                    + ((k == (value.size() - 1)) ? "" : "; ");
                            }
                            log.add(Level.FINE, tmpStore + "]");
                        }
                    }
                }
            }
        }
        if (localName.equals("plugin")) {
            plugin.add(parameters);
            plugins.put(pluginName, plugin);
            parameters = new HashMap();
            plugin = new ArrayList();
        }
        if (localName.equals("parameter")) {
            parameters.put(parameterName, values);
            values = new HashMap();
        }
        if (localName.equals("value")) {
            keyValue = context.toString().trim();
            if (repeat) {
                value = (ArrayList)values.get(keyName);
            } else {
                value = new ArrayList();
            }
            value.add(keyValue);
            values.put(keyName, value);
            repeat = false;
        }

        if (localName.equals("resource-list")) {
            theConfig.setResources(resources);
            log.add(Level.FINE, methodLogPrefix + "setResources(ArrayList["
                + resources.size() + "])");
            resources = (ArrayList)theConfig.getResources();
            for (int i = 0; i < resources.size(); i++) {
                resource = (HashMap)resources.get(i);
                log.add(Level.CONFIG, "\t " + "resource" + " - ('"
                    + resource.get("resource").toString() + "') -> ");
                log.add(Level.CONFIG, "\t " + "concurrency" + " - ('"
                    + resource.get("concurrency").toString() + "') -> ");
                log.add(Level.CONFIG, "\t " + "runit" + " - ('"
                    + resource.get("runit").toString() + "') -> ");
            }
        }
        if (localName.equals("resource")) {
            if (!resource.containsKey("resource")) {
                resource.put("resource", resourceName);
            }
            if (!resource.containsKey("concurrency")) {
                resource.put("concurrency", "1");
            }
            if (!resource.containsKey("runit")) {
                resource.put("runit", "false");
            }
            resources.add(resource);
            resourceName = "";
        }
        if (localName.equals("concurrency")) {
            resource.put("concurrency", context.toString().trim());
        }
        if (localName.equals("runit")) {
            resource.put("runit", context.toString().trim());
        }
        if (localName.equals("value")) {
            if (resourceName.length() > 1) {
                resource.put(keyName, context.toString().trim());
            }
        }
    }
}