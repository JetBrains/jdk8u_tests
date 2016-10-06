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
 * Created on 03.06.2005
 */
package org.apache.harmony.test.func.api.javax.swing.BasicLookAndFeel;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.LookAndFeel;

import org.apache.harmony.test.func.api.javax.swing.share.LookAndFeels.BLAFImpl;
import org.apache.harmony.test.func.api.javax.swing.share.LookAndFeels.ConcreteLAFGenTest;
import org.apache.harmony.test.func.api.javax.swing.share.LookAndFeels.ConcreteLAFPublic;
import org.apache.harmony.share.Result;

/**
 */
public class BasicLookAndFeelTest extends ConcreteLAFGenTest{
    private BLAFImpl instance;

    protected ConcreteLAFPublic getInstance() {
        return new BLAFImpl();
    }
    
    public Result testInit() {
        instance = new BLAFImpl();
        return passed();
    }
    
    public Result testCreateAudioAction() {
        instance = new BLAFImpl();
        try {
            Action action = instance.createAudioAction( null );
        } catch (NullPointerException e) {}
          catch (Exception e) {
              return failed("Unexpected exception of " + e.getClass());
          }
          
        
        return passed();
    }
    
    public Result testGetAudioActionMap() {
        instance = new BLAFImpl();
        ActionMap map = instance.getAudioActionMap();
        if (instance.getAudioActionMap() != map ) {
            return failed("couldn't get active audio map");
        }
        return passed();
    }
    
    public Result testPlaySound() {
        instance = new BLAFImpl();
//        setWaitingTime( 100 );
//        JFrame frame = new JFrame();
//        final JButton button = new JButton();
//        Container pane = frame.getContentPane();
//        pane.setLayout(new FlowLayout());
//        pane.add(button);
//        button.setActionCommand("play");
//        button.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent arg0) {
//                if (arg0.getActionCommand().equals("play")) {
//                    Action action = instance.createAudioAction("auxilary" + File.separator + "sound.wav");
//                    instance.playSound( action );
//                }
//            }
//            
//        });
//        frame.show();
//        setDescription("A border has been installed on the button");
//        if (!Asserting()) {
//            return failed(getFailureReason());
//        }
        
        return passed();
    }
    
    public Result testInitClassDefaults() {
        return super.testInitClassDefaults();
    }

    public Result testInitComponentDefaults() {
        return super.testInitComponentDefaults();
    }

    public Result testInitSystemColorDefaults() {
        return super.testInitSystemColorDefaults();
    }
    
    public static void main(String[] args) {
        System.exit(new BasicLookAndFeelTest().test(args));
    }
}
