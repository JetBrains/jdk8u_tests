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
 * @version $Revision: 1.12 $
 */
package org.apache.harmony.harness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class TSCfgHandler extends CfgHandler {

    private final String classID       = "TSCfgHandler";

    private HashMap      runners       = new HashMap();
    private HashMap      plugins       = new HashMap();
    private ArrayList    plugin        = new ArrayList();
    private HashMap      parameters    = new HashMap();
    private HashMap      values        = new HashMap();
    private ArrayList    value         = new ArrayList();
    private ArrayList    selValues     = new ArrayList();
    private String       selValue      = "";
    private String       pluginName    = "";
    private String       parameterName = "";
    private boolean      repeat        = false;
    private Logging      log           = Main.getCurCore().getInternalLogger();

    public TSCfgHandler(ConfigIR curConfig) {
        super(curConfig);
    }

    public void startElement(String uri, String localName, String qName,
        Attributes attrs) throws SAXException {
        context.reset();
        if (localName.equals("runner-mapping")) {
            if (theConfig.getPlugins().get(
                DefaultConfigIRValues.EUNIT_PLUGIN_CLASS) == null) {
                HashMap tmpHM = theConfig.getPlugins();
                tmpHM.put(DefaultConfigIRValues.EUNIT_PLUGIN_CLASS,
                    new ArrayList());
                theConfig.setPlugins(tmpHM);
            }
            if (((ArrayList)theConfig.getPlugins().get(
                DefaultConfigIRValues.EUNIT_PLUGIN_CLASS)).size() <= 1) {
                ArrayList tmpAL = new ArrayList();
                tmpAL.add(DefaultConfigIRValues.EUNIT_CLASS_NAME);
                tmpAL.add(new HashMap());
                theConfig.setPluginProperties(
                    DefaultConfigIRValues.EUNIT_PLUGIN_CLASS, tmpAL);
            }
            runners = (HashMap)((ArrayList)theConfig.getPlugins().get(
                "ExecUnit")).get(1);
            if (runners.containsKey(attrs.getValue("name"))) {
                ((HashMap)runners.get(attrs.getValue("name"))).put("map-class",
                    attrs.getValue("class-name"));
            } else {
                HashMap tmp = new HashMap();
                tmp.put("map-class", attrs.getValue("class-name"));
                runners.put(attrs.getValue("name"), tmp);
            }
            //			runners.put(attrs.getValue("name"),
            // attrs.getValue("class-name"));
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
        if (localName.equals("property")) {
            keyName = attrs.getValue("name");
        }
    }

    public void endElement(String uri, String localName, String qName)
        throws SAXException {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tendElement(): ";
        if (localName.equals("property")) {
            keyValue = context.toString().trim();
            theConfig.setProperty(keyName, keyValue);
            log.add(Level.CONFIG, "TSCfg: setProperty('" + keyName + "', '"
                + keyValue + "') -> " + theConfig.getProperty(keyName));
        }
        if (localName.equals("mapping")) {
            ((ArrayList)theConfig.getPlugins().get("ExecUnit")).remove(1);
            ((ArrayList)theConfig.getPlugins().get("ExecUnit")).add(1, runners);
        }
        if (localName.equals("plugin-list")) {
            if (plugins.size() > 0) {
                HashMap conf_plugins = theConfig.getPlugins();
                for (Iterator e = plugins.keySet().iterator(); e.hasNext();) {
                    Object key = e.next();
                    if (conf_plugins.containsKey(key)) {
                        if (((ArrayList)plugins.get(key)).get(0) != null) {
                            ((ArrayList)conf_plugins.get(key)).set(0,
                                ((ArrayList)plugins.get(key)).get(0));
                        }
                        if (((ArrayList)plugins.get(key)).get(1) != null) {
                            parameters = (HashMap)((ArrayList)plugins.get(key))
                                .get(1);
                            for (Iterator p = parameters.keySet().iterator(); p
                                .hasNext();) {
                                Object keyp = p.next();
                                if (((HashMap)((ArrayList)conf_plugins.get(key))
                                    .get(1)).containsKey(keyp)) {
                                    values = (HashMap)parameters.get(keyp);
                                    for (Iterator v = values.keySet()
                                        .iterator(); v.hasNext();) {
                                        Object keyv = v.next();
                                        ((HashMap)((HashMap)((ArrayList)conf_plugins
                                            .get(key)).get(1)).get(keyp)).put(
                                            keyv, values.get(keyv));
                                    }
                                } else {
                                    ((HashMap)((ArrayList)conf_plugins.get(key))
                                        .get(1))
                                        .put(keyp, parameters.get(keyp));
                                }
                            }
                        }
                    } else {
                        conf_plugins.put(key, plugins.get(key));
                    }
                }
                theConfig.setPlugins(conf_plugins);
                log.add(Level.CONFIG, methodLogPrefix + "setPlugins(HashMap["
                    + plugins.size() + "]) ->");
                plugins = theConfig.getPlugins();
                for (Iterator e = plugins.keySet().iterator(); e.hasNext();) {
                    Object key = e.next();
                    plugin = (ArrayList)plugins.get(key);
                    log.add(Level.CONFIG, methodLogPrefix + "Plug-in:"
                        + key.toString() + " = " + plugin.get(0));
                    parameters = (HashMap)plugin.get(1);
                    for (Iterator p = parameters.keySet().iterator(); p
                        .hasNext();) {
                        Object keyp = p.next();
                        log.add(Level.CONFIG, methodLogPrefix + "\tPARAMETER:"
                            + keyp.toString());
                        values = (HashMap)parameters.get(keyp);
                        for (Iterator v = values.keySet().iterator(); v
                            .hasNext();) {
                            Object keyv = v.next();
                            if (values.get(keyv) instanceof ArrayList) {
                                value = (ArrayList)values.get(keyv);
                                String tmpStore = "\t\t- " + keyv.toString()
                                    + " = [";
                                for (int k = 0; k < value.size(); k++) {
                                    tmpStore = tmpStore
                                        + (String)value.get(k)
                                        + ((k == (value.size() - 1)) ? ""
                                            : "; ");
                                }
                                log.add(Level.CONFIG, tmpStore + "]");
                            } else {
                                log.add(Level.CONFIG, "\t\t- "
                                    + keyv.toString() + " = ["
                                    + values.get(keyv).toString() + "]");
                            }
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
    }
}