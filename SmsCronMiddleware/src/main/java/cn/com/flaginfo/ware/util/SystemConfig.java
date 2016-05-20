package cn.com.flaginfo.ware.util;

import java.util.ResourceBundle;
import java.util.MissingResourceException;

public class SystemConfig {
	private static final String BUNDLE_NAME = "middleware"; 

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private SystemConfig() {
	}

	/**
	 * get the value from the properties file
	 * 
	 * @param key
	 *            the key in the properties file
	 * @return
	 */
	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}

