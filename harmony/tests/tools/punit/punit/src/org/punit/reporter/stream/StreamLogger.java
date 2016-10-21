/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.reporter.stream;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.logging.*;

import org.punit.events.*;
import org.punit.message.*;
import org.punit.reporter.*;
import org.punit.runner.*;
import org.punit.type.*;
import org.punit.util.*;
import org.punit.watcher.*;

public abstract class StreamLogger extends VanillaEventListener {

    protected transient PrintWriter _writer;

    protected transient TestResult _result;

    public Level _level = Level.INFO;
    
    private TimeWatcher _timeWatcher;

    public StreamLogger() {
    }

    public StreamLogger(PrintWriter writer) {
        _writer = writer;
    }

    public void onRunnerStart(Class<?> clazz, Runner runner) {
        startTimeWatcher();
        _result = runner.testResult();
        StringBuffer sb = new StringBuffer();
        sb.append("["); //$NON-NLS-1$
        sb.append(runner.punitName());
        sb.append("] "); //$NON-NLS-1$
        sb.append(Messages.getString("logger.01")); //$NON-NLS-1$
        sb.append(" "); //$NON-NLS-1$
        sb.append(clazz.getName());
        sb.append(ReporterUtil.LINE_SEPERATOR);
        log(sb.toString(), Level.INFO);
    }

    private void startTimeWatcher() {
        _timeWatcher = new TimeWatcher();
        _timeWatcher.start();
    }

    public void onRunnerEnd(Class<?> clazz, Runner runner) {
        logResult();
        closeStream();
    }

    protected void closeStream() {
    	_writer.close();
    }
    
    public void logResult() {
        stopTimeWatcher();
        logResultSummary();
        logFailures();
    }

	private void logResultSummary() {
		StringBuffer sb = new StringBuffer();
        sb.append(Messages.getString("logger.total")); //$NON-NLS-1$
        sb.append(_result.methodCount());
        sb.append(", "); //$NON-NLS-1$
        sb.append(Messages.getString("logger.failures")); //$NON-NLS-1$
        List<Throwable> failures = _result.failures();
        int failuresCount = failures.size();
        sb.append(failuresCount);
        sb.append(" ("); //$NON-NLS-1$
        if (failuresCount == 0) {
            sb.append(Messages.getString("logger.green")); //$NON-NLS-1$
        } else {
            sb.append(Messages.getString("logger.red")); //$NON-NLS-1$
        }
        sb.append(") - "); //$NON-NLS-1$
        sb.append(_timeWatcher.stringValue());
        sb.append(ReporterUtil.LINE_SEPERATOR);
        log(sb.toString(), Level.SEVERE);
	}

	private void logFailures() {
		List<Throwable> failures = _result.failures();
		Iterator<Throwable> iter = failures.iterator();
		int count = 0;
        while (iter.hasNext()) {
			log(++count + ")", Level.SEVERE); //$NON-NLS-1$
			if (shouldLog(Level.SEVERE)) {
				 iter.next().printStackTrace(_writer);
			}
		}
        _writer.flush();
	}

    private void stopTimeWatcher() {
        _timeWatcher.stop();
    }

    public void onSuiteStart(TestSuite suite) {
        StringBuffer sb = new StringBuffer();
        sb.append(Messages.getString("logger.06")); //$NON-NLS-1$
        sb.append(suite.getClass().getName());
        sb.append(ReporterUtil.LINE_SEPERATOR);
        log(sb.toString(), Level.INFO);
    }

    public void onClassStart(Object testInstance) {
         logln(testInstance.getClass().getName(), Level.INFO);
    }

    public void onMethodEnd(Method method, Object instance, Object[] params,
            Throwable t, List<Watcher> watchers) {
        logTestMethodResult(method, params, watchers, t);
    }

    private void logTestMethodResult(Method method, Object[] params, List<Watcher> watchers, Throwable t) {
        final StringBuffer sb = new StringBuffer();
        String simpleMethodName = ReporterUtil.simpleMethodName(method, params);
        sb.append(simpleMethodName);
        if (watchers.size() > 0) {
            final Iterator<Watcher> iter = watchers.iterator();
			Watcher watcher = iter.next();
			sb.append(" - ["); //$NON-NLS-1$
			sb.append(watcher.stringValue());
			while (iter.hasNext()) {
				watcher = iter.next();
				sb.append(","); //$NON-NLS-1$
				sb.append(watcher.stringValue());
			}
			sb.append("]"); //$NON-NLS-1$
        }
        
        sb.append(ReporterUtil.LINE_SEPERATOR);
        if(t != null) {
            sb.append(t.toString());
            sb.append(ReporterUtil.LINE_SEPERATOR);
        }
        log(sb.toString(), Level.INFO);
    }

    public void setWriter(PrintWriter writer) {
        _writer = writer;
    }

    
    public PrintWriter writer() {
        return _writer;
    }

    public void logln(Level level) {
        log(ReporterUtil.LINE_SEPERATOR, level);
    }

    public void logln(String message, Level level) {
        log(message + ReporterUtil.LINE_SEPERATOR, level);
    }

    public void log(String message, Level level) {
        log(_writer, message, level);
    }

    private void log(PrintWriter ps, String message, Level level) {
        if (ps == null) {
            return;
        }
        if (shouldLog(level)) {
            ps.print(message);
            ps.flush();
        }
    }

    private boolean shouldLog(Level level) {
        return level.intValue() >= _level.intValue();
    }

    public void setLogLevel(Level level) {
        _level = level;
    }

}