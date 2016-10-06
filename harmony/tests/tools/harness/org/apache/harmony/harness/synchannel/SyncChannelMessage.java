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
 * @author Kim, Khen Gi
 * @version $Revision: 1.7 $
 */
package org.apache.harmony.harness.synchannel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * SyncChannelMessage is an object to send/receive through the Synchronization
 * Channel. It contains fields Group name, Id, Command and Text. Group name is
 * Group name of the sender/receiver of message. Id is Id of the sender/receiver
 * of message. Command is internal field, used for internal messages (sending
 * mode or closing). Text is sent/ received string field.
 */
class SyncChannelMessage {
    public static final byte MESSAGE         = 1;
    public static final byte CLOSE           = 2;
    public static final byte GROUP_SEND      = 3;
    public static final byte INDIVIDUAL_SEND = 4;
    public static final byte GROUP_CAST      = 5;
    public static final byte INDIVIDUAL_CAST = 6;
    public static final byte SUPPLIED        = 7;
    public static final byte NOT_SUPPLIED    = 8;
    public static final byte CONNECTED       = 9;

    private byte[]           body;

    /**
     * Class constructor. Creates SyncChannelMessage. By default Command Field
     * is set to SyncChannelMessage.MESSAGE value.
     * 
     * @param group The Group name of the sender/ receiver of this message.
     * @param id The Id of the sender/ receiver of this message.
     * @param text The String to send/receive.
     */
    protected SyncChannelMessage(String group, int id, String text) {
        byte[] temp = text.getBytes();
        byte[] temp1 = group.getBytes();
        int temp_size = temp.length;
        int temp1_size = temp1.length;
        body = new byte[temp1_size + temp_size + 13];
        int i;

        body[0] = SyncChannelMessage.MESSAGE;
        for (i = 1; i < 5; i++) {
            body[i] = (byte)(id >> 8 * (4 - i));
        }
        for (i = 5; i < 9; i++) {
            body[i] = (byte)(temp1_size >> 8 * (8 - i));
        }
        System.arraycopy(temp1, 0, body, 9, temp1_size);
        for (i = 9 + temp1_size; i < 13 + temp1_size; i++) {
            body[i] = (byte)(temp_size >> 8 * (12 + temp1_size - i));
        }
        System.arraycopy(temp, 0, body, 13 + temp1_size, temp_size);
    }

    /**
     * Class constructor. Creates SyncChannelMessage from bytes array.
     * 
     * @param array The initial bytes array.
     */
    protected SyncChannelMessage(byte[] array) {
        body = new byte[array.length];
        System.arraycopy(array, 0, body, 0, array.length);
    }

    /**
     * Encodes the SyncChannelMessage into bytes array.
     * 
     * @return The representation of SyncChannelMessage as bytes array.
     */
    protected byte[] toBytesArray() {
        return body;
    }

    /**
     * Gets the Id field of the SyncChannelMessage.
     * 
     * @return The Id field of the SyncChannelMessage.
     */
    protected int getId() {
        int res = 0;
        for (int i = 1; i < 5; i++) {
            res = (res << 8) + ((int)(body[i] & 0xFF));
        }
        return res;
    }

    /**
     * Sets the Id field for the SyncChannelMessage.
     */
    protected void setId(int id) {
        for (int i = 1; i < 5; i++) {
            body[i] = (byte)(id >> 8 * (4 - i));
        }
    }

    /**
     * Gets the Group name of the sender/ receiver of the SyncChannelMessage.
     * 
     * @return The Group name of the sender/ receiver of the SyncChannelMessage.
     */
    protected String getGroupName() {
        String res;
        int group_name_size = 0;
        for (int i = 5; i < 9; i++) {
            group_name_size = (group_name_size << 8) + ((int)(body[i] & 0xFF));
        }
        res = new String(body, 9, group_name_size);
        return res;
    }

    /**
     * Gets the Text field of the SyncChannelMessage.
     * 
     * @return The Text field of the SyncChannelMessage.
     */
    protected String getText() {
        String res;
        int text_size = 0;
        int group_name_size = 0;
        for (int i = 5; i < 9; i++) {
            group_name_size = (group_name_size << 8) + ((int)(body[i] & 0xFF));
        }
        for (int i = 9 + group_name_size; i < 13 + group_name_size; i++) {
            text_size = (text_size << 8) + ((int)(body[i] & 0xFF));
        }
        res = new String(body, 13 + group_name_size, text_size);
        return res;
    }

    /**
     * Sets the Command field of the SyncChannelMessage.
     * 
     * @param c The Command to set for the SyncChannelMessage.
     */
    protected void setCommand(byte c) {
        body[0] = c;
    }

    /**
     * Gets the Command field of the SyncChannelMessage.
     * 
     * @return The Command field of the SyncChannelMessage.
     */
    protected byte getCommand() {
        return body[0];
    }

    /**
     * Gets the size of SyncChannelMessage in bytes.
     * 
     * @return The size of SyncChannelMessage in bytes.
     */
    protected int size() {
        return body.length;
    }

    /**
     * Writes SyncChannelMessage to the OutputStream
     * 
     * @param s The OutputStream, where message will be written.
     * @throws IOException
     */
    protected void send(OutputStream s) throws IOException {
        byte[] res = new byte[4 + body.length];
        for (int i = 0; i < 4; i++) {
            res[i] = (byte)(body.length >> 8 * (3 - i));
        }
        System.arraycopy(body, 0, res, 4, body.length);
        s.write(res);
    }

    /**
     * Reads the SyncChannelMessage from the InputStream.
     * 
     * @param input The InputStream, where message will be read from.
     * @return The SyncChannelMessage, which was read from InputStream
     * @throws IOException
     */
    protected static SyncChannelMessage receive(InputStream input)
        throws IOException {
        byte[] res = null;
        int message_size = 0;
        SyncChannelMessage message;
        int temp;
        for (int i = 0; i < 4; i++) {
            temp = input.read();
            if (temp == -1)
                throw new IOException(
                    "End of input stream reached. Cannot read SyncChannelMessage.");
            else
                message_size = (message_size << 8) + ((int)(temp & 0xFF));
        }
        res = new byte[message_size];
        for (int i = 0; i < message_size; i++) {
            temp = input.read();
            if (temp == -1)
                throw new IOException(
                    "End of input stream reached. Cannot read SyncChannelMessage.");
            else
                res[i] = (byte)(temp & 0xFF);
        }
        return new SyncChannelMessage(res);
    }

}