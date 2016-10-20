/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */    
/**
 * @author Anton V. Karnachuk
 * @version $Revision: 1.2 $
 */

/**
 * Created on 17.03.2005
 */
package org.apache.harmony.share.framework.jpda.jdwp;

import org.apache.harmony.share.framework.jpda.TestErrorException;

/**
 * This class represent parsed EventPacket with received event set data.
 */
public class ParsedEvent {
    
    private byte suspendPolicy;
    private int requestID;
    private byte eventKind;
    
    /**
     * Create new instance with specified data.
     */
    protected ParsedEvent(byte suspendPolicy, Packet packet, byte eventKind) {
        this.suspendPolicy = suspendPolicy;
        this.requestID = packet.getNextValueAsInt();
        this.eventKind = eventKind;
    }
    
    /**
     * Returns RequestID of this event set.
     * @return RequestID of this event set
     */
    public int getRequestID() {
        return requestID;
    }
    
    /**
     * Returns suspend policy of this event set.
     * @return suspend policy of this event set
     */
    public byte getSuspendPolicy() {
        return suspendPolicy;
    }
    
    /**
     * @return Returns the eventKind.
     */
    public byte getEventKind() {
        return eventKind;
    }
    
    public static class EventThread extends ParsedEvent {
        private long threadID;
        
            /**
             * @param suspendPolicy
             * @param packet
             */
        protected EventThread(byte suspendPolicy, Packet packet, byte eventKind) {
            super(suspendPolicy, packet, eventKind);
            this.threadID = packet.getNextValueAsThreadID();
        }
        
        /**
         * @return Returns the threadID.
         */
        public long getThreadID() {
            return threadID;
        }
}
    
    private static class EventThreadLocation extends EventThread {
        private Location location;
        
        /**
         * @param suspendPolicy
         * @param packet
         */
        protected EventThreadLocation(byte suspendPolicy, Packet packet, byte eventKind) {
            super(suspendPolicy, packet, eventKind);
            this.location = packet.getNextValueAsLocation();
        }
        
            /**
             * @return Returns the location.
             */
        public Location getLocation() {
            return location;
        }
}
    
    public static final class Event_VM_START extends EventThread {
        /**
         * @param suspendPolicy
         * @param packet
         */
        private Event_VM_START(byte suspendPolicy, Packet packet) {
            super(suspendPolicy, packet, JDWPConstants.EventKind.VM_START);
        }
    };
    
    public static final class Event_SINGLE_STEP extends EventThreadLocation {

        /**
         * @param suspendPolicy
         * @param packet
         */
        private Event_SINGLE_STEP(byte suspendPolicy, Packet packet) {
            super(suspendPolicy, packet, JDWPConstants.EventKind.SINGLE_STEP);
        }
        
    }

    public static final class Event_BREAKPOINT extends EventThreadLocation {

        /**
         * @param suspendPolicy
         * @param packet
         */
        private Event_BREAKPOINT(byte suspendPolicy, Packet packet) {
            super(suspendPolicy, packet, JDWPConstants.EventKind.BREAKPOINT);
        }
    }

    public static final class Event_METHOD_ENTRY extends EventThreadLocation {

        /**
         * @param suspendPolicy
         * @param packet
         */
        private Event_METHOD_ENTRY(byte suspendPolicy, Packet packet) {
            super(suspendPolicy, packet, JDWPConstants.EventKind.METHOD_ENTRY);
        }
    }

    public static final class Event_METHOD_EXIT extends EventThreadLocation {

        /**
         * @param suspendPolicy
         * @param packet
         */
        private Event_METHOD_EXIT(byte suspendPolicy, Packet packet) {
            super(suspendPolicy, packet, JDWPConstants.EventKind.METHOD_EXIT);
        }
    }

    public static final class Event_EXCEPTION extends EventThreadLocation {
        private TaggedObject exception; 
        private Location catchLocation;
        
        /**
         * @param suspendPolicy
         * @param packet
         */
        private Event_EXCEPTION(byte suspendPolicy, Packet packet) {
            super(suspendPolicy, packet, JDWPConstants.EventKind.EXCEPTION);
            exception = packet.getNextValueAsTaggedObject();
            catchLocation = packet.getNextValueAsLocation();
        }
        
        
            /**
             * @return Returns the catchLocation.
             */
        public Location getCatchLocation() {
            return catchLocation;
        }
        /**
         * @return Returns the exception.
         */
        public TaggedObject getException() {
            return exception;
        }
}

    public static final class Event_THREAD_START extends EventThread {
        /**
         * @param suspendPolicy
         * @param packet
         */
        private Event_THREAD_START(byte suspendPolicy, Packet packet) {
            super(suspendPolicy, packet, JDWPConstants.EventKind.THREAD_START);
        }
    };
    
    public static final class Event_THREAD_DEATH extends EventThread {
        /**
         * @param suspendPolicy
         * @param packet
         */
        private Event_THREAD_DEATH(byte suspendPolicy, Packet packet) {
            super(suspendPolicy, packet, JDWPConstants.EventKind.THREAD_DEATH);
        }
    };
    
    public static final class Event_CLASS_PREPARE extends EventThread {
        private byte refTypeTag;
        private long typeID;
        private String signature;
        private int status;
        /**
         * @param suspendPolicy
         * @param packet
         */
        protected Event_CLASS_PREPARE(byte suspendPolicy, Packet packet) {
            super(suspendPolicy, packet, JDWPConstants.EventKind.CLASS_PREPARE);
            refTypeTag = packet.getNextValueAsByte();
            typeID = packet.getNextValueAsReferenceTypeID();
            signature = packet.getNextValueAsString();
            status = packet.getNextValueAsInt(); 
        }
        
        
            /**
             * @return Returns the refTypeTag.
             */
        public byte getRefTypeTag() {
            return refTypeTag;
        }
        /**
         * @return Returns the signature.
         */
        public String getSignature() {
            return signature;
        }
        /**
         * @return Returns the status.
         */
        public int getStatus() {
            return status;
        }
        /**
         * @return Returns the typeID.
         */
        public long getTypeID() {
            return typeID;
        }
    };
    
    public static final class Event_CLASS_UNLOAD extends ParsedEvent {
        private String signature;
    
        /**
         * @param suspendPolicy
         * @param packet
         */
        private Event_CLASS_UNLOAD(byte suspendPolicy, Packet packet) {
            super(suspendPolicy, packet, JDWPConstants.EventKind.CLASS_UNLOAD);
            signature = packet.getNextValueAsString();
        }
        
        
            /**
         * @return Returns the signature.
         */
        public String getSignature() {
            return signature;
        }
};

    public static final class Event_FIELD_ACCESS extends EventThreadLocation {
        private byte refTypeTag;
        private long typeID;
        private long fieldID;
        private TaggedObject object;
    
        /**
         * @param suspendPolicy
         * @param packet
         */
        private Event_FIELD_ACCESS(byte suspendPolicy, Packet packet) {
            super(suspendPolicy, packet, JDWPConstants.EventKind.FIELD_ACCESS);
            refTypeTag = packet.getNextValueAsByte();
            typeID = packet.getNextValueAsReferenceTypeID();
            fieldID = packet.getNextValueAsFieldID();
            object = packet.getNextValueAsTaggedObject(); 
        }
        
        
            /**
         * @return Returns the fieldID.
         */
        public long getFieldID() {
            return fieldID;
        }
        /**
         * @return Returns the object.
         */
        public TaggedObject getObject() {
            return object;
        }
        /**
         * @return Returns the refTypeTag.
         */
        public byte getRefTypeTag() {
            return refTypeTag;
        }
        /**
         * @return Returns the typeID.
         */
        public long getTypeID() {
            return typeID;
        }
    };

    public static final class Event_FIELD_MODIFICATION extends EventThreadLocation {
        private byte refTypeTag;
        private long typeID;
        private long fieldID;
        private TaggedObject object;
        private Value valueToBe;
    
        /**
         * @param suspendPolicy
         * @param packet
         */
        private Event_FIELD_MODIFICATION(byte suspendPolicy, Packet packet) {
            super(suspendPolicy, packet, JDWPConstants.EventKind.FIELD_MODIFICATION);
            refTypeTag = packet.getNextValueAsByte();
            typeID = packet.getNextValueAsReferenceTypeID();
            fieldID = packet.getNextValueAsFieldID();
            object = packet.getNextValueAsTaggedObject();
            valueToBe = packet.getNextValueAsValue();
        }
        
        
        /**
         * @return Returns the fieldID.
         */
        public long getFieldID() {
            return fieldID;
        }
        /**
         * @return Returns the object.
         */
        public TaggedObject getObject() {
            return object;
        }
        /**
         * @return Returns the refTypeTag.
         */
        public byte getRefTypeTag() {
            return refTypeTag;
        }
        /**
         * @return Returns the typeID.
         */
        public long getTypeID() {
            return typeID;
        }
        
        /**
         * @return Returns the valueToBe.
         */
        public Value getValueToBe() {
            return valueToBe;
        }
    };

    public static final class Event_VM_DEATH extends ParsedEvent {
        /**
         * @param suspendPolicy
         * @param packet
         */
        private Event_VM_DEATH(byte suspendPolicy, Packet packet) {
            super(suspendPolicy, packet, JDWPConstants.EventKind.VM_DEATH);
        }
    };
    
    /**
     * Returns array of ParsedEvent extracted from given EventPacket.
     * 
     * @param packet EventPacket to parse events
     * @return array of extracted ParsedEvents
     */
    public static ParsedEvent[] parseEventPacket(Packet packet) {
        
        Packet packetCopy = new Packet(packet.toBytesArray());

        // Suspend Policy field
        byte suspendPolicy = packetCopy.getNextValueAsByte();

        // Number of events
        int eventCount = packetCopy.getNextValueAsInt();
        
        ParsedEvent[] events = new ParsedEvent[eventCount];
        
        // For all events in packet
        for (int i = 0; i < eventCount; i++) {
            byte eventKind = packetCopy.getNextValueAsByte();
            switch (eventKind) {
                case JDWPConstants.EventKind.VM_START: {
                    events[i] = new Event_VM_START(suspendPolicy, packetCopy);
                    break;
                }
                case JDWPConstants.EventKind.SINGLE_STEP: {
                    events[i] = new Event_SINGLE_STEP(suspendPolicy, packetCopy);
                    break;
                }
                case JDWPConstants.EventKind.BREAKPOINT: {
                    events[i] = new Event_BREAKPOINT(suspendPolicy, packetCopy);
                    break;
                }
                case JDWPConstants.EventKind.METHOD_ENTRY: {
                    events[i] = new Event_METHOD_ENTRY(suspendPolicy, packetCopy);
                    break;
                }
                case JDWPConstants.EventKind.METHOD_EXIT: {
                    events[i] = new Event_METHOD_EXIT(suspendPolicy, packetCopy);
                    break;
                }
                case JDWPConstants.EventKind.EXCEPTION: {
                    events[i] = new Event_EXCEPTION(suspendPolicy, packetCopy);
                    break;
                }
                case JDWPConstants.EventKind.THREAD_START: {
                    events[i] = new Event_THREAD_START(suspendPolicy, packetCopy);
                    break;
                }
                case JDWPConstants.EventKind.THREAD_DEATH: {
                    events[i] = new Event_THREAD_DEATH(suspendPolicy, packetCopy);
                    break;
                }
                case JDWPConstants.EventKind.CLASS_PREPARE: {
                    events[i] = new Event_CLASS_PREPARE(suspendPolicy, packetCopy);
                    break;
                }
                case JDWPConstants.EventKind.CLASS_UNLOAD: {
                    events[i] = new Event_CLASS_UNLOAD(suspendPolicy, packetCopy);
                    break;
                }
                case JDWPConstants.EventKind.FIELD_ACCESS: {
                    events[i] = new Event_FIELD_ACCESS(suspendPolicy, packetCopy);
                    break;
                }
                case JDWPConstants.EventKind.FIELD_MODIFICATION: {
                    events[i] = new Event_FIELD_MODIFICATION(suspendPolicy, packetCopy);
                    break;
                }
                case JDWPConstants.EventKind.VM_DEATH: {
                    events[i] = new Event_VM_DEATH(suspendPolicy, packetCopy);
                    break;
                }
                default: {
                    throw new TestErrorException("Unexpected kind of event: "+eventKind);  
                }
            }
        }
        return events;
    }
    
}
