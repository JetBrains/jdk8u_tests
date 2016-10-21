	/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.reporter.stream.file;

import java.io.PrintWriter;

import org.punit.reporter.stream.StreamLogger;
import org.punit.runner.Runner;
import org.punit.util.ReporterUtil;

public class FileLogger extends StreamLogger {

    private static final long serialVersionUID = -4086254621600007888L;

    private String _fileName;
    
    public FileLogger() {
    	this(null);
    }
    
    public FileLogger(String fileName) {
    	_fileName = fileName;
    }
    
    public void onRunnerStart(Class<?> clazz, Runner runner) {
    	if(_fileName == null) {
    		ReporterUtil.initResultFolder(runner);
    		_fileName = ReporterUtil.defaultFileName(clazz, runner) + ".txt"; //$NON-NLS-1$
    		_fileName = ReporterUtil.generateFileName(_fileName, runner);
    	}
 
        try {
            PrintWriter ps = new PrintWriter(_fileName);
            setWriter(ps);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        super.onRunnerStart(clazz, runner);
    }

}
