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
/** 
 */  
/*
 * Created on 06.04.2005
 *
 */
package org.apache.harmony.test.func.api.javax.swing.AbstractButton;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Robot;
import java.awt.Window;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;

import org.apache.harmony.test.func.api.javax.swing.share.RobotRunner;
import org.apache.harmony.share.Test;

/**
 *  
 */
public class AbstractButtonInteractiveTest extends Test {
    private Robot robot = null;
    private static final int FRAME_SIZE = 200;
    private static int START_X = 0;
    private static int START_Y = 0;
    static final int BORDER_WIDTH = 10;
    static JFrame testFrame = null;

    public static void main(String[] args) {
        System.exit(new AbstractButtonInteractiveTest().test(args));
    }

    public int test() {
        try {
//            JFrame surveyFrame = new JFrame("survey");
//            surveyFrame.move(FRAME_SIZE + 10, 0);

            JButton yesButton = new JButton("passed");
            yesButton.addMouseListener(new CloseListener(true));
            JButton noButton = new JButton("failed");
            noButton.addMouseListener(new CloseListener(false));

//            surveyFrame.getContentPane().add(yesButton, BorderLayout.SOUTH);
//            surveyFrame.getContentPane().add(noButton, BorderLayout.NORTH);
//            surveyFrame.getContentPane().add(
//                    new JLabel("Please check that mouse click makes border green, "
//                            + "mouse enter - magenta, mouse exit - blue, mouse press - yellow, mouse release - red"),
//                    BorderLayout.CENTER);
//            surveyFrame.pack();
//            surveyFrame.setVisible(true);

            testFrame = new JFrame("test field");
            AbstractButton ab = new AB();
            ab.setPreferredSize(new Dimension(FRAME_SIZE, FRAME_SIZE));
            ab.setBorder(BorderFactory.createLineBorder(Color.CYAN, BORDER_WIDTH));
            ab.setBorderPainted(true);

            testFrame.getContentPane().add(ab);
            testFrame.pack();
            testFrame.setVisible(true);
            
            START_X = (testFrame.getWidth() - FRAME_SIZE) / 2;
            START_Y = testFrame.getHeight() - START_X - FRAME_SIZE;   
            
            robot = new Robot();
            robot.setAutoDelay(1000);

//            surveyFrame.requestFocus();

            ab.setBorderPainted(true);
            ab.addMouseListener(new ColorBorderMouseListener());
            

            CloseAdapter.register(testFrame);
//            CloseAdapter.register(surveyFrame);
            
//            synchronized (testFrame) {
//                testFrame.wait();
//            }
            
            
        } catch (Throwable e) {
            System.err.println("me here");
            e.printStackTrace();
        }

//        boolean result = CloseAdapter.isSuccess();

        boolean result = RobotRunner.run(this, new String[] {"isBorderCyan", "moveInside", "isBorderMagenta", 
                "moveOutside",
                "isBorderBlue", "moveInside", "isBorderMagenta", "mousePressLeft", "isBorderYellow", 
                "mouseReleaseLeft", "isBorderGreen", "mousePressLeft", "isBorderYellow", "moveOutside", "mouseReleaseLeft", 
                "isBorderRed" 
                }) ;
        
        CloseAdapter.close(result);
        
        return result ? pass() : fail("test failed");
        //return CloseAdapter.isSuccess() ? pass() : fail("test failed");
    }
    
    public boolean isBorderCyan() {
        return isBorderColor(Color.CYAN);
    }
    
    public boolean isBorderGreen() {
        return isBorderColor(Color.GREEN);
    }

    public boolean isBorderMagenta() {
        return isBorderColor(Color.MAGENTA);
    }

    public boolean isBorderBlue() {
        return isBorderColor(Color.BLUE);
    }
    
    public boolean isBorderYellow() {
        return isBorderColor(Color.YELLOW);
    }

    public boolean isBorderRed() {
        return isBorderColor(Color.RED);
    }
    
    public void moveInside() {
        robot.mouseMove(START_X + FRAME_SIZE / 2, START_Y + FRAME_SIZE / 2);
    }

    public void moveOutside() {
        robot.mouseMove(START_X + FRAME_SIZE * 2, START_Y + FRAME_SIZE * 2);
    }
    
    public void mousePressLeft() {
        robot.mousePress(InputEvent.BUTTON1_MASK);
    }

    public void mouseReleaseLeft() {
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }

    
    private boolean isBorderColor(Color c) {
        testFrame.requestFocus();
        System.err.println("isBorderColor : color is " + robot.getPixelColor(START_X, START_Y));
        final int[]  XCoordsInBorder = new int[] {START_X, START_X + BORDER_WIDTH - 1, 
                START_X + FRAME_SIZE - 1, START_X + FRAME_SIZE - BORDER_WIDTH };
        final int[]  YCoordsInBorder = new int[] {START_Y, START_Y + BORDER_WIDTH - 1, 
                START_Y + FRAME_SIZE - 1 , START_Y + FRAME_SIZE - BORDER_WIDTH };
        final int[]  XCoordsNotInBorder = new int[] {START_X - 1, START_X + BORDER_WIDTH, 
                START_X + FRAME_SIZE, START_X + FRAME_SIZE - BORDER_WIDTH - 1};
        final int[]  YCoordsNotInBorder = new int[] {START_Y - 1, START_Y + BORDER_WIDTH, 
                START_Y + FRAME_SIZE, START_Y + FRAME_SIZE - BORDER_WIDTH - 1};

        
        for(int i =0; i < XCoordsInBorder.length; ++i) {
            for(int j =0; j < YCoordsInBorder.length; ++j) {
                //System.err.println("testing (" + XCoordsInBorder[i] + ", " + YCoordsInBorder[j] + ")");
                if(!robot.getPixelColor(XCoordsInBorder[i], YCoordsInBorder[j]).equals(c)) {
                    System.err.println("color of point (" + XCoordsInBorder[i] + ", " + YCoordsInBorder[j] + ") is wrong (should be in border)");
                    return false;
                }
            }
        }
        
        for(int i =0; i < XCoordsNotInBorder.length; ++i) {
            for(int j =0; j < YCoordsNotInBorder.length; ++j) {
                if(robot.getPixelColor(XCoordsNotInBorder[i], YCoordsNotInBorder[j]).equals(c)) {
                    System.err.println("color of point (" + XCoordsNotInBorder[i] + ", " + YCoordsNotInBorder[j] + ") is wrong (should be out of border)");
                    return false;
                }
            }
        }

        return true;
    }
}

class CloseAdapter extends WindowAdapter {
    private static boolean success = false;

    private static List toBeClosed = Collections
            .synchronizedList(new ArrayList());

    private CloseAdapter() {
    };

    public static boolean isSuccess() {
        return success;
    }

    public static void register(Window w) {
        w.addWindowListener(new CloseAdapter());
        toBeClosed.add(w);
    }

    public void windowClosing(WindowEvent e) {
        close(success);
    }

    static private void close(Window w) {
        w.setVisible(false);
        w.dispose();
        synchronized (w) {
            w.notifyAll();
        }
    }

    static public void close(boolean bln) {
        success = bln;
        for (int i = 0; i < toBeClosed.size(); ++i) {
            close((Window) toBeClosed.get(i));
        }
    }
}

class CloseListener implements MouseListener {
    private boolean result;

    CloseListener(boolean result) {
        this.result = result;
    }

    public void mouseClicked(MouseEvent e) {
        CloseAdapter.close(result);
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }
}

class AB extends AbstractButton {
    protected void paintComponent(Graphics arg0) {
        arg0.setColor(Color.BLACK);
        //arg0.drawString((isFocusOwner() ? "i'm focused" : "i'm not focused") + System.currentTimeMillis(), 0, 50);
        arg0.drawString("" + System.currentTimeMillis(), 30, 50);
    }
}

class ColorBorderMouseListener implements  MouseListener {
    public void mouseClicked(MouseEvent e) {
        AbstractButton ab = (AbstractButton) e.getComponent();
        ab.setBorder(BorderFactory.createLineBorder(Color.GREEN, AbstractButtonInteractiveTest.BORDER_WIDTH));
    }

    public void mouseEntered(MouseEvent e) {
        AbstractButton ab = (AbstractButton) e.getComponent();
        ab.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, AbstractButtonInteractiveTest.BORDER_WIDTH));
    }

    public void mouseExited(MouseEvent e) {
        AbstractButton ab = (AbstractButton) e.getComponent();
        ab.setBorder(BorderFactory.createLineBorder(Color.BLUE, AbstractButtonInteractiveTest.BORDER_WIDTH));
    }

    public void mousePressed(MouseEvent e) {
        AbstractButton ab = (AbstractButton) e.getComponent();
        ab.setBorder(BorderFactory.createLineBorder(Color.YELLOW, AbstractButtonInteractiveTest.BORDER_WIDTH));
    }

    public void mouseReleased(MouseEvent e) {
        AbstractButton ab = (AbstractButton) e.getComponent();
        ab.setBorder(BorderFactory.createLineBorder(Color.RED, AbstractButtonInteractiveTest.BORDER_WIDTH));
    }
}
