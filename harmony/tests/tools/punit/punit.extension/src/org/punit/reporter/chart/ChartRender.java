/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.reporter.chart;

import java.io.Serializable;

import org.jfree.chart.JFreeChart;
import org.punit.runner.Runner;

/**
 * Interface for rendering a chart. 
 */
public interface ChartRender extends Serializable {

    /**
     * It is triggered the beginning of the runner. Some render may initialize itself.
     * @param clazz 
     * @param runner
     */
    public void onRunnerStart(Class<?> clazz, Runner runner);
    
    /**
     * It is triggered the end of the runner. Some render may close itself.
     */
    public void onRunnerEnd(Class<?> clazz, Runner runner);

    /**
     * Renders a chart. prefixFileName is the suggested name of this chart.
     * @param chart
     * @param prefixFileName
     */
    public void renderChart(JFreeChart chart, String prefixFileName);
}
