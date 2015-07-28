package com.pactera.hscloud.common4j.util;

import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.SecurityHelper;

public class DbConnectionManager {

	private DbConnectionPool pool;

	private static DbConnectionManager dbConnectionManager;

	public static Properties pro;
	
	public static int jdbcBatchSize=20;

	private static Logger log = Logger.getLogger(DbConnectionManager.class);

	static {
		try {
			pro = new Properties();
			pro.load(new InputStreamReader(FileUtil
					.openPropertiesInputStream("jdbc.properties"),
					FileUtil.fileEncode));
			String jbs = getProperty("jdbc.batchupdate");
			if (jbs != null)
				jdbcBatchSize = Integer.parseInt(jbs);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * 单实例模式
	 */

	private DbConnectionManager() {
		String securityKey=Constants.DEFAULT_SECURITY_KEY;
		String password=getProperty("jdbc.password");
		String decryptPasswd=SecurityHelper.DecryptData(password, securityKey);
		pool = new DbConnectionPool(getProperty("jdbc.driverClassName"),
				getProperty("jdbc.url"), getProperty("jdbc.username"),
				decryptPasswd, Integer.valueOf(getProperty("jdbc.minConns").trim()),
				Integer.valueOf(getProperty("jdbc.maxConns").trim()));

	}
   
	private static String getProperty(String key) {
		String ret = pro.getProperty(key, null);
		if (ret != null && ret.trim().equals(""))
			ret = null;
		return ret;
	}
	
	public static DbConnectionManager newInstance() {

		if (dbConnectionManager != null) {

			return dbConnectionManager;

		} else {

			return new DbConnectionManager();

		}

	}

	public Connection getConnection() {

		return pool.getConnection();

	}

	public void close(Connection conn) {

		pool.freeConnection(conn);

	}

	public void release() {

		pool.release();

	}

	protected class DbConnectionPool {

		private final Logger logger = Logger.getLogger(DbConnectionPool.class);

		private Vector<Connection> freeConnections = new Vector<Connection>();

		private String driverClass;

		private String url;

		private String userName;

		private String password;

		private int minConns;

		private int maxConns;

		private int isUsed = 0;

		private int timeout = 60 * 1000;

		public DbConnectionPool(String driverClass, String url,

		String userName, String password, int minConns, int maxConns) {

			this.driverClass = driverClass;

			this.url = url;

			this.userName = userName;

			this.password = password;

			this.minConns = minConns;

			this.maxConns = maxConns;

			initConnection();

		}

		private void initConnection() {

			try {

				Class.forName(driverClass); // 加载驱动

				for (int i = 0; i < minConns; i++) {

					Connection conn = DriverManager.getConnection(url,
							userName, password);

					freeConnections.add(conn);

					logger.info("初始化数据库连接放入连接池" + i);

				}

			} catch (ClassNotFoundException e) {

				logger.error("驱动无法加载，请检查驱动是否存在，driver: " + driverClass, e);

			} catch (SQLException e) {

				logger.error("数据库连接池初始化失败", e);

			}

		}

		/**
		 * 
		 * 获取连接
		 * 
		 * @return
		 */

		public synchronized Connection getConnection() {

			logger.info("已用 " + isUsed + " 个连接，空闲连接个数 "
					+ freeConnections.size());

			Connection conn = null;

			if (freeConnections.size() > 0) {

				conn = freeConnections.firstElement();

				try {

					if (conn.isClosed()) {

						conn = getConnection();

					}

				} catch (SQLException e) {

					conn = getConnection();

				}

				freeConnections.removeElementAt(0);

				isUsed++;

				return conn;

			}

			if (isUsed < maxConns) {

				conn = newConnection();

				isUsed++;

				return conn;

			}

			if (isUsed >= maxConns) {

				// 连接池已满

				long startTime = System.currentTimeMillis();

				try {

					wait(timeout);

				} catch (InterruptedException e) {

					logger.error(e.getMessage(),e);

				}

				if (new Date().getTime() - startTime > timeout) {

					logger.info("没有可获取的链接，正在重试...");

					conn = getConnection();

				}

			}

			return conn;

		}

		/**
		 * 
		 * 释放连接入连接池
		 * 
		 * @param conn
		 */

		public synchronized void freeConnection(Connection conn) {

			freeConnections.add(conn);

			if (isUsed > 0)
				isUsed--;

			notifyAll();

			logger.info("释放连接!");

		}

		private Connection newConnection() {

			Connection conn = null;

			try {

				conn = DriverManager.getConnection(url, userName, password);

				logger.info("创建了一个新的链接");

			} catch (SQLException e) {

				logger.error("获取数据库连接失败", e);

			}

			return conn;

		}

		public synchronized void release() {

			Enumeration<Connection> enums = freeConnections.elements();

			while (enums.hasMoreElements()) {

				try {

					enums.nextElement().close();

				} catch (SQLException e) {

					logger.warn("关闭链接失败", e);

				}

			}

			freeConnections.removeAllElements();

			logger.debug("释放了所有的连接");

		}

		public int getIsUsed() {

			return isUsed;

		}

	}
	
	public static void main(String[] args) {
		System.out.println("123");
	}

}