/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.reporter.chart.image;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.jfree.chart.JFreeChart;
import org.punit.reporter.chart.ChartRender;
import org.punit.runner.Runner;

/**
 * ImageRender renders a chart as a image file. Each dataset will be rendered as
 * a single image.
 * 
 */
public class ImageRender implements ChartRender {

    private static final long serialVersionUID = -1781926871099521773L;

    protected final int _height;

    protected final int _width;

    protected final String _format;

    /**
     * Constructor. Initialize a image render with default height 750, default
     * width 750, and default format png.
     */
    public ImageRender() {
        this(ImageConstants.DEFAULT_HEIGHT, ImageConstants.DEFAULT_WIDTH,
                ImageConstants.DEFAULT_FORMAT);
    }

    /**
     * Constructor. Initialize a image render with customized height, customized
     * width, and customized format.
     * 
     * @param height
     *            customized height
     * @param width
     *            customized width
     * @param format
     *            customized format
     */
    public ImageRender(int height, int width, String format) {
        _height = height;
        _width = width;
        _format = format;
    }

    /**
     * @see ChartRender#renderChart(JFreeChart, String)
     */
    public void renderChart(JFreeChart chart, String prefixFileName) {
        BufferedImage chartImage = chart.createBufferedImage(_height, _width);
        try {
            ImageIO.write(chartImage, _format, new File(prefixFileName
                    + _format));
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public void onRunnerStart(Class<?> clazz, Runner runner) {
        // nothing needs to be done
    }

    public void onRunnerEnd(Class<?> clazz, Runner runner) {
        // nothing needs to be done
    }

}
