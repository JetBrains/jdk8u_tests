// Compile command -  javac JavaDrawPC6.java
// Run command     -  java  JavaDrawPC

// Change version below for different javac
// And here rename class and file name e.g. JavaDrawPC to JavaDrawPi

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.awt.image.*;

public class JavaDrawPC extends JPanel implements ActionListener {

    private static final DecimalFormat decimalFormat =
            new DecimalFormat("0.00");

    String version = System.getProperty("java.version");
    String vendor = System.getProperty("java.vendor");
    String vendorPrefix = vendor.split(" ", 2)[0];
    int x, y;
    Timer timer;
    static int WIDTH = 1280;
    static int HEIGHT = 720;
    int gch = 1;
    int gcm = 1;
    int grn = 0;
    int circ = 120;
    int cir2 = 0;
    float ud2 = 1.0f;
    int pos = 20;
    float fpos = 20.0f;
    float fcir = 120.0f;
    float updown = 1.0f;
    float inc = 1.0f;
    Image image = null;
    Image image1 = null;
    Image image2 = null;
    int wi = 200;
    int hi = 200;
    int wj = 283;
    int hj = 283;
    float mv = 0.0f;
    String msg;
    String tests;
    double fps;
    double startTime;
    double startTest;
    double testTime;
    double runTime = 0;
    int frames = 0;
    int test = 0;

    Random randNum = new Random();
    private static PrintWriter prout;
    private static String fileName = "JavaDraw.txt";

    private static final String originVendor = "Oracle";
    private static Map<String, Map<String, Integer>> framesMap = new HashMap<String, Map<String, Integer>>(2);
    private static Map<String, Map<String, Float>> fpsMap = new HashMap<String, Map<String, Float>>(2);

    private static String renderingType;

    private JavaDrawPC() {
        image = Toolkit.getDefaultToolkit().getImage("bground.png");
        image1 = Toolkit.getDefaultToolkit().getImage("bground1.png");
        image2 = Toolkit.getDefaultToolkit().getImage("sweep.png");
        randNum.setSeed(999);
        timer = new Timer(0, this);
        startTime = (double) System.currentTimeMillis();
        startTest = startTime;
        try {
            // Create file
            prout = new PrintWriter(new FileWriter(new File(fileName), true));
        } catch (Exception e) {
            //Catch exception if any
            System.err.println("%n  Create File Error: %n%n" + e.getMessage());
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (runTime > 10.0) {
            prout.printf("%s:%s:%d:%.2f%n",  vendorPrefix + "_" + renderingType, tests.replace(' ','_'),
                    frames, fps);
            System.out.format("%35s %8d %8.2f%n", tests, frames, fps);
            startTime = (double) System.currentTimeMillis();
            test = test + 1;
            frames = 0;
            if (test == 6) {
                testTime = ((double) System.currentTimeMillis() - startTest) / 1000;
                System.out.format("%nTotal Elapsed Time %5.1f seconds%n", testTime);
                System.out.format("%nOperating System    "
                        + System.getProperty("os.name") + ", Arch. "
                        + System.getProperty("os.arch") + ", Version "
                        + System.getProperty("os.version") + "%n");
                System.out.format("Java Vendor         "
                        + vendor + ", Version "
                        + " " + version + "%n");
                System.out.format(System.getenv("PROCESSOR_IDENTIFIER")
                        + ", " + System.getenv("NUMBER_OF_PROCESSORS")
                        + " CPUs%n%n");
                prout.close();
                System.exit(0);
            }
        }
        repaint();
    }

    public void paintComponent(Graphics g) {
        int i;
        float fh;
        float fw;

        super.paintComponent(g);

        grn = grn + gch;
        if (grn > 255) {
            gch = -gcm;
            grn = 255;
        }
        if (grn < 1) {
            gch = gcm;
            grn = 0;
        }

        g.setColor(new Color(0, grn, 255));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        if (test > 2) {
            g.setColor(Color.BLACK);
            int c2 = 10;
            for (i = 0; i < 50; i++) {
                int r1 = (int) (randNum.nextFloat() * (float) HEIGHT / 4);
                int r2 = (int) (randNum.nextFloat() * (float) HEIGHT / 4);
                g.fillOval(WIDTH / 2 + r1, HEIGHT / 2 + r2, c2, c2);
                g.fillOval(WIDTH / 2 - r1, HEIGHT / 2 - r2, c2, c2);
                g.fillOval(WIDTH / 2 + r1, HEIGHT / 2 - r2, c2, c2);
                g.fillOval(WIDTH / 2 - r1, HEIGHT / 2 + r2, c2, c2);
            }
            if (test == 3) tests = "Plus 200 Random Small Circles";
        }

        if (test > 4) {
            g.setColor(Color.BLACK);
            int c22 = 10;
            for (i = 0; i < 1000; i++) {
                int r12 = (int) (randNum.nextFloat() * (float) WIDTH / 2);
                int r22 = (int) (randNum.nextFloat() * (float) HEIGHT / 2);
                g.fillOval(WIDTH / 2 + r12, HEIGHT / 2 + r22, c22, c22);
                g.fillOval(WIDTH / 2 - r12, HEIGHT / 2 - r22, c22, c22);
                g.fillOval(WIDTH / 2 + r12, HEIGHT / 2 - r22, c22, c22);
                g.fillOval(WIDTH / 2 - r12, HEIGHT / 2 + r22, c22, c22);
            }
            if (test == 5) tests = "Plus 4000 Random Small Circles";
        }


        if (test > 3) {
            for (i = 0; i < 80; i++) {
                fh = HEIGHT / 80 * (float) i;
                fw = WIDTH / 80 * (float) i;
                g.setColor(new Color(grn, 0, 0));
                g.drawLine(0, HEIGHT / 2, WIDTH, (int) fh);
                g.drawLine(WIDTH / 2, 0, (int) fw, HEIGHT);
                g.setColor(new Color(255, grn, 0));
                g.drawLine(WIDTH, HEIGHT / 2, 0, (int) fh);
                g.drawLine(WIDTH / 2, HEIGHT, (int) fw, 0);
            }
            if (test == 4) tests = "Plus 320 Long Lines";
        }

        int c1 = HEIGHT / 3;

        if (test > 1) {
            g.drawImage(image2, WIDTH - circ, HEIGHT - circ, wi, hi, null);
            g.drawImage(image2, cir2, HEIGHT - circ, wi, hi, null);

            fcir = fcir + ud2 * inc;
            circ = (int) fcir;
            if (circ > c1 * 3) ud2 = -1.0f;
            if (circ < c1) ud2 = 1.0f;
            cir2 = circ - 120;
            if (test == 2) tests = "Plus 2 SweepGradient Circles";
        }

        if (test > -1) {
            g.drawImage(image, pos / 2, 50, wi, hi, null);
            BufferedImage rotatedImg = new BufferedImage(wj, hj, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2e = (Graphics2D) rotatedImg.getGraphics();
            g2e.rotate(mv, image1.getWidth(this) / 2, image1.getHeight(this) / 2);
            g2e.drawImage(image1, 0, 0, this);
            g.drawImage(rotatedImg, WIDTH - wj * 5 / 4 - pos / 5, 50, wj, hj, null);
            if (test == 0) tests = "Display PNG Bitmap Twice Pass 1";
            if (test == 1) tests = "Display PNG Bitmap Twice Pass 2";
        }

        fpos = fpos + updown * inc;
        pos = (int) fpos;
        if (pos > WIDTH) updown = -1.0f;
        if (pos < 20) updown = 1.0f;
        mv = mv + 0.01f * inc;

        frames = frames + 1;

        Graphics2D g2 = (Graphics2D) g;
        Font font = new Font("MONOSPACE", Font.PLAIN, 24);
        g2.setFont(font);
        g.setColor(Color.WHITE);
        runTime = ((double) System.currentTimeMillis() - startTime) / 1000;
        fps = (double) frames / runTime;
        msg = String.format(" %6.2f FPS, %6d Frames, %6.2f Seconds", fps, frames, runTime);
        g2.drawString(msg, 10, 30);
        if (test == 0 && frames == 100) {
            inc = 200.0f / (float) fps;
        }
    }

    private static <V> void add(Map<String, Map <String, V>> map, String key, String innerKey, V value) {
        Map <String, V> innerMap;
        if (!map.containsKey(key)) {
            innerMap = new HashMap <String, V> (12);
            map.put(key, innerMap);
        } else {
            innerMap = map.get(key);
        }
        innerMap.put(innerKey, value);
    }

    private static void readValues() throws IOException {
        BufferedReader in;
        in = new BufferedReader(new FileReader(fileName));
        while (true) {
            String rsline = in.readLine();
            if (rsline == null) {
                break;
            }
            rsline = rsline.trim();
            String[] columns = rsline.split(":");
            String key = columns[0];
            JavaDrawPC.<Integer>add(framesMap, columns[0], columns[1], Integer.valueOf(columns[2]));
            JavaDrawPC.<Float>add(fpsMap, columns[0], columns[1], Float.valueOf(columns[3]));
        }
    }

    private static int compareResults() {
        int retValue = 0;

        try {
            readValues();
        } catch (IOException e) {
            System.out.println(e);
            return -1;
        }

        Map<String, Float> originFPS = fpsMap.get(originVendor);
        for (String test : originFPS.keySet()) {
            boolean failedCase = false;
            System.out.println("##teamcity[buildStatisticValue key='" + originVendor + "." + test
                    + "' value='" + decimalFormat.format(originFPS.get(test)) + "']");
            System.out.format("%45s = %5.2f%n", originVendor + "." + test, originFPS.get(test));
            for (String customVendor : fpsMap.keySet()) {
                if (customVendor.equals(originVendor)) continue;

                Map<String, Float> customFPS = fpsMap.get(customVendor);
                System.out.println("##teamcity[buildStatisticValue key='" + customVendor + "." + test
                        + "' value='" + decimalFormat.format(customFPS.get(test)) + "']");

                System.out.format("%45s = %5.2f", customVendor + "." + test, customFPS.get(test));
                if (customFPS.get(test) + originFPS.get(test) * 0.1 < originFPS.get(test)) {
                    retValue = -1;
                    failedCase = true;
                }
                System.out.println(failedCase ? "\n*** the value significantly less" : "");
            }
            System.out.println();
        }
        return retValue;
    }

    public static void main(String[] args) {

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-renderingType")) {
                renderingType = args[++i];
                renderingType = renderingType.substring(0, 1).toUpperCase() + renderingType.substring(1);;
            }
        }
        java.util.List<String> argValues = Arrays.<String>asList(args);

        if (argValues.contains("-compare")) {
            System.exit(compareResults());

        } else if (argValues.contains("-run")) {
            JFrame f = new JFrame(" JavaDrawPC");
            f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            JavaDrawPC m = new JavaDrawPC();
            f.add(m);
            f.setSize(WIDTH, HEIGHT);
            f.setVisible(true);
            m.timer.start();
        }
    }
}