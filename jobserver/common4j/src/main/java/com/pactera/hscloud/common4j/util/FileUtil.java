package com.pactera.hscloud.common4j.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author
 * 
 */
public class FileUtil {

	private static Log logger = LogFactory.getLog(FileUtil.class);
	private static String baseFilePath = null;
	public static String fileEncode = "GBK";
	static {
		URL url = FileUtil.class.getClassLoader().getResource("");
		baseFilePath = url.getPath();
		try {
			baseFilePath = URLDecoder.decode(baseFilePath, "UTF-8");
		} catch (UnsupportedEncodingException e) {

		}
	}

	public static InputStream openPropertiesInputStream(String name)
			throws FileNotFoundException {
		File file = new File(baseFilePath + name);

		return new FileInputStream(file);
	}

	public static OutputStream openPropertiesOutputStream(String name)
			throws FileNotFoundException {
		File file = new File(baseFilePath + name);
		return new FileOutputStream(file);
	}

	public static void println(Object obj) {
		logger.info(obj);
	}

	/*
 * 	
 */

	/*
	 * 检查是否已存在进程停止的文件存在
	 */

	public static boolean stopProcess() {
		String stopfile = "stop_jobserver";
		File f = new File(baseFilePath + "/" + stopfile);
		if (f.exists())
			return true;

		return false;
	}

	public static void printStackTrace(Exception e) {
		logger.error(e.getMessage(), e);
	}
}