/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.util;

import java.io.File;
import java.lang.reflect.Method;

import org.punit.runner.Runner;
import org.punit.runner.RunnerProperties;

public class ReporterUtil {
	
    public final static String LINE_SEPERATOR;
 
    private static boolean _isResultFolderCreated;
    
    static {
        LINE_SEPERATOR = System.getProperty("line.separator"); //$NON-NLS-1$
    }
    
    public static String simpleMethodName(Method method) {
        Class<?>[] params = method.getParameterTypes();
        String[] paramsNames = new String[params.length];
        for(int i = 0; i < params.length; ++i) {
            paramsNames[i] = params[i].getSimpleName();
        }
        return simpleMethodName(method, paramsNames);
    }
    
    public static String simpleMethodName(Method method, Object[] params) {
        StringBuffer sb = new StringBuffer();
        sb.append(method.getName());
        sb.append("("); //$NON-NLS-1$
        for(int i = 0; i < params.length - 1 ; ++i) {
            sb.append(params[i]);
            sb.append(", "); //$NON-NLS-1$
        }
        if(params.length > 0) {
            sb.append(params[params.length - 1]);
        }
        sb.append(")"); //$NON-NLS-1$
        return sb.toString();
    }

    public static String generateFileName(String fileName, Runner runner) {
        StringBuffer sb = new StringBuffer();
        sb.append(IOUtil.getCurrentPath());
        sb.append(File.separator);
        sb.append(runner.resultFolder());
        sb.append(File.separator);
        sb.append(fileName);
        return sb.toString();
    }

	public static String defaultFileName(Class<?> clazz, Runner runner) {
		StringBuffer sb = new StringBuffer();
		sb.append(clazz.getName());
        sb.append("."); //$NON-NLS-1$
        sb.append(runner.punitName());
        RunnerProperties properties = runner.properties();
        if(properties.vmName != null) {
            sb.append("."); //$NON-NLS-1$
            sb.append(properties.vmName);
        }
        return sb.toString();
	}
 
    public static void initResultFolder(Runner runner) {
    	if(_isResultFolderCreated) {
    		return;
    	}
    	_isResultFolderCreated = true;
		new File(runner.resultFolder()).mkdirs();
	}

}
