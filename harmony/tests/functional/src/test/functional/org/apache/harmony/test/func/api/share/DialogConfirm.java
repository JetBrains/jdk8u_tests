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

package org.apache.harmony.test.func.api.share;

import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.HashMap;

class DialogConfirm extends Frame implements ActionListener {

    private final String          YES_BTN             = "Yes";
    private final String          NO_BTN              = "No";
    private final String          SUBMIT_BTN          = "Submit";
    private final String          PAUSE_BTN           = "|| PAUSE";

    public static final int       RESULT_SUCCESS      = 0x0;
    public static final int       RESULT_FAILURE      = 0x2;
    public static final int       NO_IMAGE            = 0x4;

    private String[]              supportedExtensions = {".gif", ".jpg",
            ".png", ".GIF", ".JPG", ".PNG"            };
    private long                  waitingTime         = 150;
    private String                testedClass         = "";

    private Button                btnYes              = new Button(YES_BTN);
    private Button                btnNo               = new Button(NO_BTN);
    private Button                btnSubmit           = new Button(SUBMIT_BTN);
    private Button                btnPause            = new Button(PAUSE_BTN);
    ImageCanvas                   imgCanvas;
    private Image                 screenshot          = null;
    private Image                 defScreenshot;
    private final int             IMG_WIDTH           = 640;
    private final int             IMG_HEIGHT          = 480;
    private TextArea              txtDescription;
    private TextArea              txtReason           = new TextArea(5, 80);

    boolean                       assertingDone       = false;
    boolean                       assertStatus        = false;
    boolean                       firstStage          = true;
    protected static final int    MSGBOX_WIDTH        = 720;
    protected static final int    MSGBOX_HEIGHT       = 540;
    public static final Dimension msgBoxDimension     = new Dimension(
                                                              MSGBOX_WIDTH,
                                                              MSGBOX_HEIGHT);

    public static void main(String[] args) {
        HashMap allParams = new HashMap();
        for (int i = 0; i < args.length - 1; i += 2) {
            allParams.put(args[i], args[i + 1]);
        }

        long time = 0;
        int callcounter = 0;
        int alignment = 0;

        try {
            time = new Long((String) allParams.get("-time")).longValue();
            callcounter = (new Integer((String) allParams.get("-callcounter")))
                    .intValue();
            alignment = (new Integer((String) allParams.get("-alignment")))
                    .intValue();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        DialogConfirm dc = new DialogConfirm(
                (String) allParams.get("-caption"), (String) allParams
                        .get("-desc"), (String) allParams.get("-testedclass"),
                time, alignment);

        boolean screenShotExists = false;
        String ss = (String) allParams.get("-screenshot");
        
        if (!ss.equals(InteractiveTest.NO_SCREENSHOT)) {            
            dc.loadCustomImage(ss);
        }else if (dc.loadImage((String) allParams.get("-caller"), callcounter) == true) {
            screenShotExists = true;
        }

        /* Retrieve the excplanations about the result of the test */
        InteractiveTest.setFailureReason(dc.makeChoice());

        dc.dispose();

        /* Exit with the returned result 1 for success, 0 if failure */
        int exitCode = 0;
        if (dc.assertStatus) {
            exitCode = RESULT_SUCCESS;
        } else {
            exitCode = RESULT_FAILURE;
        }

        /* If no screenshot was found add additional flag */
        if (screenShotExists == false) {
            exitCode |= NO_IMAGE;
        }
        System.exit(exitCode);
    }
    public DialogConfirm(String caption, String testDesc, String testedCl,
            long time, int alignment) {
        super(caption);
        waitingTime = time;
        testedClass = testedCl;
        setSize(msgBoxDimension);
        setBackground(Color.WHITE);
        InteractiveTest.setIneractiveWindowAlignment(alignment);
        txtDescription = new TextArea(testDesc, 2, 80,
                TextArea.SCROLLBARS_VERTICAL_ONLY);
        txtDescription.setEditable(false);
        txtDescription.setBackground(Color.WHITE);
        /* Get the image resource */
        URL imagePath = DialogConfirm.class
                .getResource("../auxiliary/noImage.PNG");

        defScreenshot = Toolkit.getDefaultToolkit().createImage(imagePath);

        GridBagLayout gbLayout = new GridBagLayout();
        GridBagConstraints gbConstraints = new GridBagConstraints();

        setLayout(gbLayout);

        /* Let components fill the empty space */
        gbConstraints.fill = GridBagConstraints.HORIZONTAL;

        /* Set test description */
        txtDescription.setText(testDesc);
        gbConstraints.gridwidth = GridBagConstraints.REMAINDER;
        addComponent(gbConstraints, gbLayout, txtDescription);

        /* Load default image */
        imgCanvas = new ImageCanvas(defScreenshot, waitingTime);
        imgCanvas.setSize(IMG_WIDTH, IMG_HEIGHT);
        addComponent(gbConstraints, gbLayout, imgCanvas);

        /* Center components accordingly */
        gbConstraints.weightx = 1.0;

        /* Reset to default */
        gbConstraints.gridwidth = 1;

        /* add button "Yes" to panel */
        addComponent(gbConstraints, gbLayout, btnYes);

        /* Add button "No" to panel */
        gbLayout.setConstraints(btnNo, gbConstraints);
        addComponent(gbConstraints, gbLayout, btnNo);

        // btnPause.setFont(new Font("Arial", Font.BOLD, 12));

        /* Add action to button "Pause" */
        btnPause.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String action = e.getActionCommand();
                if (action.equals(PAUSE_BTN)) {
                    if (imgCanvas.getPause()) {
                        imgCanvas.setPause(false);
                    } else {
                        imgCanvas.setPause(true);
                    }
                }
            }
        });

        /* Add button "Pause" to panel */
        gbConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gbLayout.setConstraints(btnPause, gbConstraints);
        addComponent(gbConstraints, gbLayout, btnPause);

        /*
         * Add textarea (reason of failure) to panel. It is not showen until the
         * test has failed
         */
        txtReason.setText("Please indicate why the test has failed.");
        txtReason.addFocusListener(new FocusListener() {
            /* Reset default wording */
            public void focusGained(FocusEvent e) {
                if (txtReason.getText().equals(
                        "Please indicate why the test has failed.")) {
                    txtReason.setText("");
                }
            }

            public void focusLost(FocusEvent e) {

            }
        });

        addComponent(gbConstraints, gbLayout, txtReason);
        txtReason.setVisible(false);

        /*
         * "Submit" button, will submit comments indicated in the "reason of
         * failure" (txtReason) textarea
         */
        btnSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String action = e.getActionCommand();
                if (action.equals(SUBMIT_BTN)) {
                    assertingDone = true;
                }
            }
        });
        gbConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gbLayout.setConstraints(btnSubmit, gbConstraints);
        add(btnSubmit);
        btnSubmit.setVisible(false);

        setDialogConfirmVisible(true);
    }
    public long getWaitingTime() {
        return waitingTime;
    }

    protected void addComponent(GridBagConstraints constraints,
            GridBagLayout layout, Component addedComp) {
        layout.setConstraints(addedComp, constraints);
        add(addedComp);
    }

    public void setDialogConfirmVisible(boolean b) {
        if (b) {
            pack();
            setVisible(true);
            InteractiveTest.setWindowAlignment(this, InteractiveTest
                    .getIneractiveWindowAlignment());
            toFront();
        } else {
            dispose();
        }
    }

    public boolean loadImage(String testedMethod, int methodCallCounter) {
        String imagePath = "";
        boolean screenshotFound = false;
        for (int i = 0; i < supportedExtensions.length; ++i) {

            imagePath = "../" + testedClass + "/auxiliary/" + testedMethod
                    + methodCallCounter + supportedExtensions[i];

            URL imagePathURL = DialogConfirm.class.getResource(imagePath);

            /* if the screenshot is found load it and exit the loop */
            if (imagePathURL != null) {
                screenshot = Toolkit.getDefaultToolkit().createImage(
                        imagePathURL);
                screenshotFound = true;
                break;
            }
        }
        if (screenshotFound == false) {
            screenshot = defScreenshot;
        }

        /* Get the image component */
        ImageCanvas imgCanvas = (ImageCanvas) getComponents()[1];
        /* Load the new screenshot */
        imgCanvas.loadImage(screenshot);

        return screenshotFound;
    }

    public boolean loadCustomImage(String ss) {
        String imagePath = "";
        boolean screenshotFound = false;
        for (int i = 0; i < supportedExtensions.length; ++i) {

            imagePath = "../" + testedClass + "/auxiliary/" + ss
                    + supportedExtensions[i];

            URL imagePathURL = DialogConfirm.class.getResource(imagePath);

            /* if the screenshot is found load it and exit the loop */
            if (imagePathURL != null) {
                screenshot = Toolkit.getDefaultToolkit().createImage(
                        imagePathURL);
                screenshotFound = true;
                break;
            }
        }
        if (screenshotFound == false) {
            screenshot = defScreenshot;
        }

        /* Get the image component */
        ImageCanvas imgCanvas = (ImageCanvas) getComponents()[1];
        /* Load the new screenshot */
        imgCanvas.loadImage(screenshot);

        return screenshotFound;
    }

    public void actionPerformed(ActionEvent event) {
        String str = event.getActionCommand();
        if (str.equals(YES_BTN)) {
            assertStatus = true;
            assertingDone = true;
        } else if (str.equals(NO_BTN)) {
            assertStatus = false;
            assertingDone = false;
            btnYes.setVisible(false);
            btnNo.setVisible(false);
            /* Display the "reason of failure" textarea */
            txtReason.setVisible(true);
            btnSubmit.setVisible(true);
            validate();
            pack();
            InteractiveTest.setWindowAlignment(this, InteractiveTest
                    .getIneractiveWindowAlignment());
        }
    }

    public String makeChoice() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                assertStatus = false;
                assertingDone = true;
                dispose();
            }
        });

        setVisible(true);
        btnNo.addActionListener(this);
        btnYes.addActionListener(this);

        long i;
        for (i = 0; i < waitingTime; ++i) {
            /* if "Pause" state is enabled */
            if (imgCanvas.getPause()) {
                --i;
                continue;
            }
            if (assertingDone) {
                break;
            } else {
                try {
                    Thread.sleep(1000);
                    imgCanvas.decrementWaitingTime();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        /* if we have run out of time we need indicate this */
        if (i == waitingTime) {
            return "The test has timed out!";
        }
        return txtReason.getText();
    }
}