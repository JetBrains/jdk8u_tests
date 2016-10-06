/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
/*
 *
 */  
package org.apache.harmony.test.func.api.javax.naming.event;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.event.EventContext;
import javax.naming.event.EventDirContext;
import javax.naming.event.NamingEvent;
import javax.naming.event.NamingExceptionEvent;

import org.apache.harmony.test.func.api.javax.naming.share.MultiCaseUtil;
import org.apache.harmony.share.Result;

/**
 * Functional test for javax.naming.event package.
 * 
 */
public class EventTest extends MultiCaseUtil {

    /**
     * Initial context.
     */
    private DirContext                ctx;

    /**
     * Naming listener.
     */
    private NamingEventListenerSample l;

    /**
     * Context name.
     */
    private String                    ctxName = "Test";

    /**
     * Binding name.
     */
    private Object                    obj     = "Test Object";

    /**
     * Create InitialDirContext and NamingEventListener objects, register event
     * listener.
     */
    public void setUp() {
        try {
            Hashtable env = new Hashtable();
            env.put(Context.INITIAL_CONTEXT_FACTORY,
                "org.apache.harmony.test.func.api.javax."
                    + "naming.event.EventDirCtxFactory");
            ctx = new InitialDirContext(env);
            l = new NamingEventListenerSample();
            ((EventDirContext) ctx.lookup("")).addNamingListener(ctxName,
                EventContext.ONELEVEL_SCOPE, l);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(fail(e.toString()));
        }
    }

    /**
     * Bind new object: name="Test", obj="Test Object". <br/>Verify that:
     * <ul type="1">
     * <li>NamespaceChangeListener.objectAdded() method invoked</li>
     * <li>NamingEvent.getNewBinding().getObject().equals("Test Object")</li>
     * <li>NamingEvent.getNewBinding().getName().equals("Test")</li>
     * <li>NamingEvent.getOldBinding() == null</li>
     * </ul>
     * 
     * @return
     */
    public Result testObjectAdded() {
        try {
            bind();
            NamingEvent e = (NamingEvent) l.getEventObject();
            if (!(e.getType() == NamingEvent.OBJECT_ADDED
                && e.getNewBinding().getObject().equals(obj)
                && e.getNewBinding().getName().equals(ctxName) && e
                .getOldBinding() == null)) {
                return failed("FAILED");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.toString());
        }
        return passed();
    }

    /**
     * Bind new object: name="Test", obj="Test Object". <br/>unbind("Test").
     * <br/>Verify that:
     * <ul type="1">
     * <li>NamespaceChangeListener.objectRemoved() method invoked</li>
     * <li>NamingEvent.getNewBinding() == null</li>
     * <li>NamingEvent.getOldBinding().getObject().equals("Test Object")</li>
     * <li>NamingEvent.getOldBinding().getName().equals("Test")</li>
     * </ul>
     * 
     * @return
     */
    public Result testObjectRemoved() {
        try {
            bind();
            ctx.unbind(ctxName);
            NamingEvent e = (NamingEvent) l.getEventObject();
            if (!(e.getType() == NamingEvent.OBJECT_REMOVED
                && e.getNewBinding() == null
                && e.getOldBinding().getName().equals(ctxName) && e
                .getOldBinding().getObject().equals(obj))) {
                return failed("FAILED");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.toString());
        }
        return passed();
    }

    /**
     * Bind new object: name="Test", obj="Test Object". <br/>rename("Test",
     * "Test(renamed)"). <br/>Verify that:
     * <ul type="1">
     * <li>NamespaceChangeListener.objectRenamed() method invoked</li>
     * <li>NamingEvent.getNewBinding().getName().equals("Test(renamed)")</li>
     * <li>NamingEvent.getNewBinding().getObject().equals("Test Object")</li>
     * <li>NamingEvent.getOldBinding().getName().equals("Test")</li>
     * <li>NamingEvent.getOldBinding().getObject().equals("Test Object")</li>
     * </ul>
     * 
     * @return
     */
    public Result testObjectRenamed() {
        try {
            bind();
            String newName = ctxName + "(renamed)";
            ctx.rename(ctxName, newName);
            NamingEvent e = (NamingEvent) l.getEventObject();
            if (!(e.getType() == NamingEvent.OBJECT_RENAMED
                && e.getNewBinding().getName().equals(newName)
                && e.getNewBinding().getObject().equals(obj)
                && e.getOldBinding().getName().equals(ctxName) && e
                .getOldBinding().getObject().equals(obj))) {
                return failed("FAILED");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.toString());
        }
        return passed();
    }

    /**
     * Bind new object: name="Test", obj="Test Object". <br/>rebind("Test", "New
     * Test Object"). <br/>Verify that:
     * <ul type="1">
     * <li>ObjectChangeListener.objectChanged() method invoked</li>
     * <li>NamingEvent.getNewBinding().getName().equals("Test")</li>
     * <li>NamingEvent.getNewBinding().getObject().equals("New Test Object")
     * </li>
     * <li>NamingEvent.getOldBinding().getName().equals("Test")</li>
     * <li>NamingEvent.getOldBinding().getObject().equals("Test Object")</li>
     * </ul>
     * 
     * @return
     */
    public Result testObjectChanged() {
        try {
            bind();
            Object newObj = "New " + ctxName;
            ctx.rebind(ctxName, newObj);
            NamingEvent e = (NamingEvent) l.getEventObject();
            if (!(e.getType() == NamingEvent.OBJECT_CHANGED
                && e.getNewBinding().getName().equals(ctxName)
                && e.getNewBinding().getObject().equals(newObj)
                && e.getOldBinding().getName().equals(ctxName) && e
                .getOldBinding().getObject().equals(obj))) {
                return failed("FAILED");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.toString());
        }
        return passed();
    }

    /**
     * Invoke ctx.getSchema("Test"). <br/>Verify that:
     * <ul type="1">
     * <li>NamingExceptionEvent.namingExceptionThrown() method invoked</li>
     * <li>NamingExceptionEvent.getException().getMessage().equals("Ok")</li>
     * </ul>
     * 
     * @return
     */
    public Result testNamingExceptionThrown() {
        try {
            ctx.getSchema(ctxName);
            NamingExceptionEvent e = (NamingExceptionEvent) l.getEventObject();
            if (!(e.getException().getMessage().equals("Ok"))) {
                return failed(e.getException().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.toString());
        }
        return passed();
    }

    /**
     * Bind object: ctx.bind(ctxName, obj);
     * 
     * @throws NamingException
     */
    private void bind() throws NamingException {
        ctx.bind(ctxName, obj);
    }

    /**
     * Close InitialDirContext.
     */
    public void tearDown() {
        try {
            if (ctx != null) {
                ctx.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.exit(new EventTest().test(args));
    }
}