package org.punit.message;

import java.util.*;

public class Messages {
	private static final String BUNDLE_NAME = "punit"; //$NON-NLS-1$

	private static ResourceBundle RESOURCE_BUNDLE = null;

	private static Hashtable<String, String> MESSAGE_TABLE = null;
	
	private static boolean isAndroid;

	private Messages() {
	}

	static {
		// TODO: The code is a temp quick hack for Android, and will be removed
		// when Android supports i18n (which has been committed).
		isAndroid = isAndroid();
		if (isAndroid) {
			initMessageTable();
		} else {
			RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
		}
	}

	private static void initMessageTable() {
		MESSAGE_TABLE = new Hashtable<String, String>();
		for (int i = 0; i < MessageStrings.DATA.length; ++i) {
			MESSAGE_TABLE.put(MessageStrings.DATA[i][0],
					MessageStrings.DATA[i][1]);
		}
	}

	public static String getString(String key) {
		if(isAndroid) {
			return (String) MESSAGE_TABLE.get(key);
		}
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	public static boolean isAndroid() {
		return "Dalvik".equals(System.getProperty("java.vm.name"));
	}
}
