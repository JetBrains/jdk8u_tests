/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.reporter.chart;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleInsets;
import org.punit.exception.IOException;

public class ChartUtil {

    public static DefaultCategoryDataset appendDataset(DefaultCategoryDataset ds1,
            CategoryDataset ds2, String prefix) {
        int rowCount = ds2.getRowCount();
        int columnCount = ds2.getColumnCount();
        for (int i = 0; i < rowCount; ++i) {
            for (int j = 0; j < columnCount; ++j) {
                String rowKey = (String) ds2.getRowKey(i);
                String columnKey = (String) ds2.getColumnKey(j);
                Number value = ds2.getValue(i, j);
                ds1.addValue(value, prefix + "_" + rowKey, columnKey); //$NON-NLS-1$
            }
        }
        return ds1;
    }

    public static JFreeChart generateChartFromDataset(String title, String yLegendText, CategoryDataset dataset) {
        CategoryAxis categoryAxis = new CategoryAxis(""); //$NON-NLS-1$
        categoryAxis.setLabelFont(ChartConstants.CATEGORY_LABEL_FONT);
        categoryAxis.setTickLabelFont(ChartConstants.CATEGORY_TICKLABEL_FONT);
    
        ValueAxis valueAxis = new NumberAxis(yLegendText);
        valueAxis.setLabelFont(ChartConstants.VALUE_LABEL_FONT);
        valueAxis.setTickLabelFont(ChartConstants.VALUE_TICKLABEL_FONT);
        LineAndShapeRenderer renderer = new LineAndShapeRenderer(true, false);
        CategoryPlot plot = new CategoryPlot(dataset, categoryAxis, valueAxis,
                renderer);
        plot.setOrientation(PlotOrientation.VERTICAL);
        JFreeChart chart = new JFreeChart(title, ChartConstants.TITLE_FONT, plot, false);
    
        LegendTitle legend = new LegendTitle(plot);
        legend.setItemFont(ChartConstants.LEGEND_FONT);
        legend.setMargin(new RectangleInsets(1.0, 1.0, 1.0, 1.0));
        legend.setBackgroundPaint(Color.white);
        chart.addLegend(legend);
    
        return chart;
    }

    public static void storeDatasetAsObject(DefaultCategoryDataset dataset,
            File file) {
        ObjectOutputStream oos = null;
        try {
            FileOutputStream fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(dataset);
            oos.close();
        } catch (java.io.IOException e) {
            throw new IOException(e);
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (java.io.IOException e) {
                    throw new IOException(e);
                }
            }
        }
    }

}
