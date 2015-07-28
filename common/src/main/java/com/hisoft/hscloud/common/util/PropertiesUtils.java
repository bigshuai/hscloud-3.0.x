package com.hisoft.hscloud.common.util; 

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
	
	private static Map<String, String> properties = new HashMap<String, String>();
	
	private static Properties propertiesObject = new Properties();

	private static Logger logger = Logger.getLogger(PropertiesUtils.class);	
	
	public static Map<String, String> IMAGE_MAP = new HashMap<String, String>();
	
	public static Map<String, String> ICP_MAP = new HashMap<String, String>();

	public static Map<String, String> getProperties(String fileName) {
		if(properties.isEmpty()) {
			Properties propertiesTemp = new Properties();
			try {
				URL url = Thread.currentThread().getContextClassLoader().getResource("/");
				if(url == null){
					url = Thread.currentThread().getContextClassLoader().getResource(".");
				}
				propertiesTemp.load(new FileInputStream(url.getPath() + fileName ));
				for(Entry<Object, Object> entry : propertiesTemp.entrySet()) {
					properties.put((String)entry.getKey(), (String)entry.getValue());
				}
			} catch (FileNotFoundException e) {
				throw new HsCloudException("001", "getResourceTypeMap", logger, e);
			} catch (IOException e) {
				throw new HsCloudException("002", "getResourceTypeMap", logger, e);
			}
			
		}
		return properties;
	}
	
	/**
	 * 获取Properties对象
	* <功能详细描述> 
	* @param fileName
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public static Properties getPropertiesObject(String fileName) {
		if(propertiesObject.isEmpty()) {
			try {
				URL url = Thread.currentThread().getContextClassLoader().getResource("/");
				if(url == null){
					url = Thread.currentThread().getContextClassLoader().getResource(".");
				}
				propertiesObject.load(new FileInputStream(url.getPath() + fileName ));
				String cryptedPasswd=propertiesObject.getProperty("auth.password");
				String decrypedPasswd=SecurityHelper.DecryptData(cryptedPasswd,Constants.DEFAULT_SECURITY_KEY);
				propertiesObject.setProperty("auth.password",decrypedPasswd);
			} catch (FileNotFoundException e) {
				throw new HsCloudException("001", "getResourceTypeMap", logger, e);
			} catch (IOException e) {
				throw new HsCloudException("002", "getResourceTypeMap", logger, e);
			}
			
		}
		return propertiesObject;
	}
	
	public static Map<String, String> getPropertiesMap(String fileName, Map<String, String> map) {
		if(map.isEmpty()) {
			try {
				URL url = Thread.currentThread().getContextClassLoader().getResource("/");
				if(url == null){
					url = Thread.currentThread().getContextClassLoader().getResource(".");
				}
				Properties obj = new Properties();
				obj.load(new FileInputStream(url.getPath() + fileName ));
				for(Entry<Object, Object> entry : obj.entrySet()) {
					map.put((String)entry.getKey(), (String)entry.getValue());
				}
			} catch (FileNotFoundException e) {
				throw new HsCloudException("001", "getResourceTypeMap", logger, e);
			} catch (IOException e) {
				throw new HsCloudException("002", "getResourceTypeMap", logger, e);
			}
			
		}
		return map;
	}
}
