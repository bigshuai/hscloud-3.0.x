package com.hisoft.hscloud.crm.usermanager.util; 

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.hisoft.hscloud.common.util.HsCloudException;

public class PropertiesUtils {
	
	private static Map<String, String> resourceTypeMap = new HashMap<String, String>();
	private static Map<String, String> menuNameMapZH = new HashMap<String, String>();
	private static Map<String, String> menuNameMapEN = new HashMap<String, String>();
	private static Logger logger = Logger.getLogger(PropertiesUtils.class);
	private static final String FILE_NAME = "resourceType.properties";
	private static final String MENU_FILE_NAME_ZH = "menuName_ZH.properties";
	private static final String MENU_FILE_NAME_EN = "menuName_EN.properties";
	private static final String CHINESE = "zh_CN";
	private static final String ENGLISH = "en_US";
	

	public static Map<String, String> getResourceTypeMap() {
		if(resourceTypeMap.isEmpty()) {
			Properties properties = new Properties();
			try {
				URL url = Thread.currentThread().getContextClassLoader().getResource("/");
				properties.load(new FileInputStream(url.getPath() + FILE_NAME));
				for(Entry<Object, Object> entry : properties.entrySet()) {
					resourceTypeMap.put((String)entry.getKey(), (String)entry.getValue());
				}
			} catch (FileNotFoundException e) {
				throw new HsCloudException("001", "getResourceTypeMap", logger, e);
			} catch (IOException e) {
				throw new HsCloudException("002", "getResourceTypeMap", logger, e);
			}
			
		}
		return resourceTypeMap;
	}
	
	public static Map<String, String> getMenuNameMapZH() {
		if(menuNameMapZH.isEmpty()) {
			Properties properties = new Properties();
			try {
				URL url = Thread.currentThread().getContextClassLoader().getResource("/");
				properties.load(new FileInputStream(url.getPath() + MENU_FILE_NAME_ZH));
				for(Entry<Object, Object> entry : properties.entrySet()) {
					menuNameMapZH.put((String)entry.getKey(), (String)entry.getValue());
				}
			} catch (FileNotFoundException e) {
				throw new HsCloudException("001", "getMenuNameMap", logger, e);
			} catch (IOException e) {
				throw new HsCloudException("002", "getMenuNameMap", logger, e);
			}
			
		}
		return menuNameMapZH;
	}
	
	public static Map<String, String> getMenuNameMapEN() {
		if(menuNameMapEN.isEmpty()) {
			Properties properties = new Properties();
			try {
				URL url = Thread.currentThread().getContextClassLoader().getResource("/");
				properties.load(new FileInputStream(url.getPath() + MENU_FILE_NAME_EN));
				for(Entry<Object, Object> entry : properties.entrySet()) {
					menuNameMapEN.put((String)entry.getKey(), (String)entry.getValue());
				}
			} catch (FileNotFoundException e) {
				throw new HsCloudException("001", "getMenuNameMap", logger, e);
			} catch (IOException e) {
				throw new HsCloudException("002", "getMenuNameMap", logger, e);
			}
			
		}
		return menuNameMapEN;
	}
	
	public static Map<String, String> getMenuNameByLanguage(String language) {
		if(CHINESE.equalsIgnoreCase(language)) {
			return getMenuNameMapZH();
		}
		if(ENGLISH.equalsIgnoreCase(language)) {
			return getMenuNameMapEN();
		}
		return new HashMap<String, String>();
	}
}
