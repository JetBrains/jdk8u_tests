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
package org.apache.harmony.test.share.dt;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 */
class RemoteRequest {

    /**
     * Shutdown remote system.
     * 
     * @param remoteSystemHost system host.
     * @param remoteSystemPort system port.
     * @return
     * @throws Exception
     */
    public static boolean shutDownSystem(final String remoteSystemHost,
        final int remoteSystemPort) throws Exception {
        try {
            return ((Boolean) sendRequest(new Shutdown(), remoteSystemHost,
                remoteSystemPort)).booleanValue();
        } catch (ConnectException ex) {
            return true;
        }
    }

    /**
     * Send request to the remote system.
     * 
     * @param request request object.
     * @param remoteSystemHost system host.
     * @param remoteSystemPort system port.
     * @return response.
     * @throws Exception
     */
    public static Object sendRequest(final Request request,
        final String remoteSystemHost, final int remoteSystemPort)
        throws Exception {
        final Socket socket = new Socket(remoteSystemHost, remoteSystemPort);
        final Object result;
        final ObjectInputStream in;
        final ObjectOutputStream out = new ObjectOutputStream(socket
            .getOutputStream());

        out.writeObject(request);
        out.flush();

        in = new ObjectInputStream(socket.getInputStream());

        result = in.readObject();

        out.close();
        in.close();

        if (result instanceof RemoteRequest.RemoteException) {
            throw new Exception(((RemoteRequest.RemoteException) result)
                .getThrowable());
        }

        return result;
    }

    public static interface Request extends Serializable {
        public Object perform(final RemoteSystem system) throws Exception;
    }

    static class RemoteException implements Serializable {
        private static final long serialVersionUID = -2641852897528206312L;
        private final Throwable   ex;

        RemoteException(final Throwable ex) {
            this.ex = ex;
        }

        Throwable getThrowable() {
            return ex;
        }
    }

    static class CheckConnection implements Request {
        private static final long serialVersionUID = -315474913453710039L;

        public Object perform(final RemoteSystem system) {
            return null;
        }
    }

    static class InvokeMethod implements Request {
        private static final long serialVersionUID = 2946538493595434022L;
        private final long        instanceId;
        private final int         methodId;
        private final Object[]    args;

        InvokeMethod(final long instanceId, final int methodId,
            final Object[] args) {
            this.instanceId = instanceId;
            this.methodId = methodId;
            this.args = args;
        }

        public Object perform(final RemoteSystem system) throws Exception {
            final ObjectWrapper wrapper = (ObjectWrapper) system.instances
                .get(new Long(instanceId));

            if (wrapper == null) {
                throw new Exception("Instance not found: " + instanceId);
            }

            return wrapper.invoke(methodId, args);
        }
    }

    static class Register implements Request {
        private static final long serialVersionUID = -4657180919145692868L;
        private final String      className;
        private final String[]    types;
        private final Object[]    params;

        Register(final String className, final Class[] types,
            final Object[] params) {
            this.className = className;
            this.params = params;

            if ((types != null) && (types.length > 0)) {
                this.types = new String[types.length];
                for (int i = 0; i < types.length; i++) {
                    this.types[i] = types[i].getName();
                }
            } else {
                this.types = null;
            }
        }

        public Object perform(final RemoteSystem system) throws Exception {
            final Class c = Class.forName(className);
            final Object instance;
            final Long id;

            if (this.types != null) {
                final Constructor constr;
                final Class[] types = new Class[this.types.length];
                for (int i = 0; i < types.length; i++) {
                    types[i] = Class.forName(this.types[i]);
                }

                constr = Class.forName(className).getConstructor(types);
                constr.setAccessible(true);
                instance = constr.newInstance(params);
            } else {
                instance = Class.forName(className).newInstance();
            }

            synchronized (system.lock) {
                id = new Long(system.counter);
                system.counter++;
            }

            system.instances.put(id, new ObjectWrapper(c, instance));

            return id;
        }
    }

    static class Unregister implements Request {
        private static final long serialVersionUID = 2836974302435053273L;
        private final long        instanceId;

        Unregister(final long instanceId) {
            this.instanceId = instanceId;
        }

        public Object perform(final RemoteSystem system) throws Exception {
            final ObjectWrapper wrapper = (ObjectWrapper) system.instances
                .remove(new Long(instanceId));

            if (wrapper == null) {
                throw new Exception("Instance not found: " + instanceId);
            }

            wrapper.release();

            return null;
        }
    }

    static class Shutdown implements Request {
        private static final long serialVersionUID = -211392202801463044L;

        public Object perform(final RemoteSystem system) throws Exception {
            return null;
        }
    }

    static class ObjectWrapper {
        private final Method[] methods;
        private final String[] methodSignatures;
        private final Object   instance;
        private final Method   finalize;

        ObjectWrapper(final Class c, final Object instance) {
            final Iterator it;
            final TreeMap map = new TreeMap();
            this.instance = instance;
            methods = c.getMethods();
            methodSignatures = new String[methods.length];

            for (int i = 0; i < methods.length; i++) {
                map.put(getSignature(methods[i]), methods[i]);
            }

            it = map.entrySet().iterator();
            for (int i = 0; it.hasNext(); i++) {
                final Map.Entry e = (Map.Entry) it.next();
                methodSignatures[i] = (String) e.getKey();
                methods[i] = (Method) e.getValue();
            }

            finalize = getFinalize(c);
        }

        int getMethodId(final Method method) throws NoSuchMethodException {
            final int ind = Arrays.binarySearch(methodSignatures,
                getSignature(method));

            if (ind < 0) {
                throw new NoSuchMethodException(method.toString());
            }

            return ind;
        }

        Object getInstance() {
            return instance;
        }

        Object invoke(final int methodId, final Object[] args) throws Exception {
            try {
                return methods[methodId].invoke(instance, args);
            } catch (InvocationTargetException ex) {
                final Throwable e = ex.getTargetException();

                if (e instanceof Exception) {
                    throw (Exception) e;
                }

                throw new Exception(e);
            }
        }

        void release() throws Exception {
            if (finalize != null && instance != null) {
                finalize.setAccessible(true);
                finalize.invoke(instance, (Object[])null);
            }
        }

        static String getSignature(final Method method) {
            final Class[] params = method.getParameterTypes();
            String m = method.getName() + "(";

            for (int i = 0; i < params.length; i++) {
                m += params[i].getName();

                if (i != (params.length - 1)) {
                    m += ", ";
                } else {
                    m += ")";
                }
            }

            return m;
        }

        private static Method getFinalize(final Class c) {
            try {
                return c.getDeclaredMethod("finalize", (Class[])null);
            } catch (Exception ex) {
                return null;
            }
        }
    }
}
