/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.reporter.chart;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import org.jfree.chart.*;
import org.jfree.data.category.*;
import org.punit.events.*;
import org.punit.exception.IOException;
import org.punit.message.*;
import org.punit.reporter.chart.image.*;
import org.punit.runner.*;
import org.punit.type.*;
import org.punit.util.*;
import org.punit.watcher.*;

public abstract class AbstractChartReporter extends VanillaEventListener {

    private Hashtable<DatasetKey, DefaultCategoryDataset> _datasets = new Hashtable<DatasetKey, DefaultCategoryDataset>();

    private LinkedList<TestSuite> _currentSuiteStack = new LinkedList<TestSuite>();

    protected ChartRender _render;

    protected transient RunnerProperties _runnerProperties;

    protected transient Object _testInstance;

    public AbstractChartReporter(ChartRender render) {
        _render = render;
    }

    protected abstract DatasetKey getKey(Watcher watcher);

    @SuppressWarnings("unchecked")
    public void onRunnerStart(final Class clazz, Runner runner) {
    	ReporterUtil.initResultFolder(runner);
        _runnerProperties = runner.properties();
        if (!isIntermediate()) {
            _render.onRunnerStart(clazz, runner);
        }
    }

    @SuppressWarnings("unchecked")
    public void onRunnerEnd(final Class clazz, final Runner runner) {
        for (DatasetKey key : datasets().keySet()) {
            DefaultCategoryDataset dataset = getDataset(runner, key);
            String fileName = generateResultFileNamePrefix(runner, key);
            storeDataset(dataset, fileName, key);
        }
        if (!isIntermediate()) {
            _render.onRunnerEnd(clazz, runner);
        }
    }

    private DefaultCategoryDataset getDataset(final Runner runner,
            DatasetKey key) {
        DefaultCategoryDataset dataset;
        if (isParentRunner()) {
            dataset = generateDatasetFromChildrenVMs(runner, key);
        } else {
            dataset = (DefaultCategoryDataset) getDatasetOrNew(key);
        }
        return dataset;
    }

    private DefaultCategoryDataset generateDatasetFromChildrenVMs(
            final Runner runner, DatasetKey key) {
        String childFileNamePrefix = generateResultFileNamePrefix(runner, key);
        final VM[] vms = _runnerProperties.vms;
        DefaultCategoryDataset dataset = getDatasetOrNew(key);
        for (int i = 0; i < vms.length; ++i) {
            String vmname = vms[i].punitName();
            String childFileName = generateIntermediateFileName(
                    childFileNamePrefix, vmname);
            DefaultCategoryDataset childDataset = consumeChildData(runner,
                    childFileName);
            dataset = ChartUtil.appendDataset(dataset, childDataset, vmname);
        }
        return dataset;
    }

    private DefaultCategoryDataset consumeChildData(final Runner runner,
            String childFileName) {
        ObjectInputStream ois = null;
        try {
            FileInputStream fis = new FileInputStream(childFileName);
            ois = new ObjectInputStream(fis);
            return (DefaultCategoryDataset) ois.readObject();
        } catch (Exception e) {
            throw new IOException(e);
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (java.io.IOException e) {
                    throw new IOException(e);
                }
            }
            IOUtil.deleteFile(childFileName);
        }
    }

    private void storeDataset(DefaultCategoryDataset dataset,
            String prefixFileName, DatasetKey key) {
        if (isIntermediate()) {
            String fileName = generateIntermediateFileName(prefixFileName,
                    _runnerProperties.vmName);
            ChartUtil.storeDatasetAsObject(dataset, new File(fileName));
        } else {
            String title = key.punitName();
            String yLegendText = watcherLegend(key.watcher());
            JFreeChart chart = ChartUtil.generateChartFromDataset(title,
                    yLegendText, dataset);
            _render.renderChart(chart, prefixFileName);
        }
    }

    private static String generateIntermediateFileName(String prefix,
            String vmName) {
        return prefix + vmName + ImageConstants.SER;
    }

    private final String generateResultFileNamePrefix(Runner runner,
            DatasetKey key) {
        StringBuffer sb = new StringBuffer();
        sb.append(Messages.getString("reporter.01")); //$NON-NLS-1$
        sb.append(File.separator);
        sb.append(key.punitName());
        sb.append("."); //$NON-NLS-1$
        sb.append(key.watcher().punitName());
        sb.append("."); //$NON-NLS-1$
        sb.append(runner.punitName());
        sb.append("."); //$NON-NLS-1$
        return sb.toString();
    }

    private static String watcherLegend(Watcher watcher) {
        return watcher.punitName() + " [" + watcher.unit() + "]"; //$NON-NLS-1$//$NON-NLS-2$
    }

    protected final boolean isIntermediate() {
        return _runnerProperties.isIntermediate;
    }

    protected final boolean isParentRunner() {
        return _runnerProperties.isParent;
    }

    private final DefaultCategoryDataset getDatasetOrNew(DatasetKey key) {
        DefaultCategoryDataset dataset = _datasets.get(key);
        if (dataset == null) {
            dataset = new DefaultCategoryDataset();
            putDataset(key, dataset);
        }
        return dataset;
    }

    protected final void putDataset(DatasetKey key,
            DefaultCategoryDataset dataset) {
        _datasets.put(key, dataset);
    }

    protected final Hashtable<DatasetKey, DefaultCategoryDataset> datasets() {
        return _datasets;
    }

    public boolean supportParentRunner() {
        return true;
    }

    public void onClassStart(Object testInstance) {
        _testInstance = testInstance;
    }

    public void onClassEnd(Object testInstance, Throwable t) {
        _testInstance = null;
    }

    public void onSuiteStart(TestSuite suite) {
        _currentSuiteStack.add(suite);
    }

    public void onSuiteEnd(TestSuite suite) {
        _currentSuiteStack.removeLast();
    }

    public TestSuite currentTestSuite() {
        return _currentSuiteStack.getLast();
    }

    public synchronized void onMethodEnd(final Method method,
			final Object testInstance, final Object[] params,
			final Throwable t, List<Watcher> watchers) {
		Iterator<Watcher> iter = watchers.iterator();
		while (iter.hasNext()) {
			Watcher watcher = iter.next();
			DatasetKey key = getKey(watcher);
			DefaultCategoryDataset dataset = getDatasetOrNew(key);
			if (isParentRunner()) {
				return;
			}
			double value = watcher.value();
			String yname = testInstance.getClass().getSimpleName();
			String xname = ReporterUtil.simpleMethodName(method, params);
			dataset.addValue(value, yname, xname); // value, y, x
		}
	}

}
