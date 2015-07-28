package com.pactera.hscloud.common4j.util;

import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author
 * 
 */
public class DBUtil {

	private static Log logger = LogFactory.getLog(DBUtil.class);

	private static Properties ps;
	
	private static SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@SuppressWarnings({"rawtypes"})
	private static Map classMap = new HashMap();

	@SuppressWarnings({"rawtypes"})
	private static Map generatorMap = new HashMap();

	private static int jdbcBatchSize = 20;

	static {
		try {
			ps = new Properties();
			ps.load(new InputStreamReader(FileUtil
					.openPropertiesInputStream("jdbc.properties"),
					FileUtil.fileEncode));
			Class.forName(getProperty("jdbc.driverClassName"));
			String jbs = getProperty("jdbc.batchupdate");
			if (jbs != null)
				jdbcBatchSize = Integer.parseInt(jbs);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public static Connection getConnection() throws SQLException {
		//c3p0连接池获取connection
		return ConnectionManager.getInstance().getConnection();
		
		//jdbc普通链接获取connection
		/*DriverManager.setLoginTimeout(60);
		String securityKey=getProperty("jdbc.soWhat");
		if(StringUtils.isEmpty(securityKey)){
			securityKey=Constants.DEFAULT_SECURITY_KEY;
		}
		String decryptPasswd=SecurityHelper.DecryptData(getProperty("jdbc.password"), securityKey);
		return DriverManager.getConnection(getProperty("jdbc.url"),
				getProperty("jdbc.username"),decryptPasswd);*/
	}

	private static String getProperty(String key) {
		String ret = ps.getProperty(key, null);
		if (ret != null && ret.trim().equals(""))
			ret = null;
		return ret;
	}

	@SuppressWarnings({"rawtypes","unchecked"})
	private static List handlerResult(ResultSet rs, Class clazz)
			throws IllegalArgumentException, SQLException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {
		List list = new ArrayList();
		Object obj = null;
		Map map = handlerClass(clazz);
		Field[] fields = clazz.getDeclaredFields();

		while (rs.next()) {
			obj = clazz.newInstance();
			for (Field f : fields) {

				String name = f.getName();

				Method setMe = (Method) map.get(name + "_setMethod");

				String returnType = (String) map.get(name + "_returnType");
				
				logger.info(name+":"+rs.getString(name));
				
				if (setMe == null || returnType == null)
					continue;

				Object ret = null;

				try {
					if (returnType.equals("java.lang.String")) {
						ret = rs.getString(name);
					} else if (returnType.equals("java.lang.Long")) {
						ret = rs.getLong(name);
						if(rs.wasNull()){
							ret = null;
						}
					} else if (returnType.equals("java.lang.Integer")) {
						ret = rs.getInt(name);
						if(rs.wasNull()){
							ret = null;
						}
					} else if(returnType.equals("java.util.Date")) {
						ret = rs.getDate(name);
					}else if(returnType.equals("java.lang.Short")) {
						ret = rs.getShort(name);
					}else {
						throw new IllegalAccessException(returnType); // TODO
					}
				} catch (SQLException e) {
					logger.error(e.getMessage(), e);
				}

				setMe.invoke(obj, ret);
			}
			list.add(obj);
		}
		return list;
	}

	@SuppressWarnings({"rawtypes","unchecked"})
	private synchronized static Map handlerClass(Class clazz) {

		if (classMap.containsKey(clazz.getName())) {
			return (Map) classMap.get(clazz.getName());
		}

		Map retMap = new HashMap();
		Field[] fields = clazz.getDeclaredFields();
		for (Field f : fields) {
			String name = f.getName();

			try {
				Method getMe = clazz.getMethod("get"
						+ name.substring(0, 1).toUpperCase()
						+ name.substring(1));

				Method setMe = clazz.getMethod(
						"set" + name.substring(0, 1).toUpperCase()
								+ name.substring(1), getMe.getReturnType());

				retMap.put(name + "_setMethod", setMe);
				retMap.put(name + "_getMethod", getMe);
				retMap.put(name + "_returnType", getMe.getReturnType()
						.getName());
			} catch (SecurityException e) {
				logger.error(e.getMessage(), e);
			} catch (NoSuchMethodException e) {
				logger.error(e.getMessage(), e);
			}
		}
		classMap.put(clazz.getName(), retMap);

		return retMap;
	}

	public static void close(Connection con, Statement st, ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			// e.printStackTrace();
		}
		try {
			if (st != null)
				st.close();
		} catch (SQLException e) {
			// e.printStackTrace();
		}
		try {
			if (con != null)
				con.close();
		} catch (SQLException e) {
			// e.printStackTrace();
		}
	}

	@SuppressWarnings({"rawtypes"})
	public static void delete(List list, String keyName) throws Exception {
		if (list == null || list.size() == 0)
			return;

		String prefix = "jdbc.delete";

		String vsql = getProperty(prefix + "." + keyName + ".sql");
		String vcolumn = getProperty(prefix + "." + keyName + ".column");
		String vclass = getProperty(prefix + "." + keyName + ".class");

		if (vsql == null || vclass == null || vcolumn == null)
			throw new IllegalArgumentException("请检查“" + prefix + "." + keyName
					+ "”的相关配置！");
		logger.info("sql:"+vsql);
		Map map = handlerClass(Class.forName(vclass));
		String[] vcolumnArr = vcolumn.split(",");

		Connection con = null;
		PreparedStatement st = null;

		try {
			logger.info(" ---getConnection---");
			con = getConnection();
			logger.info(" --------- con:  -----------"+con);
			con.setAutoCommit(false);
			st = con.prepareStatement(vsql);
			int size = 0;
			for (Object obj : list) {
				for (int i = 0; i < vcolumnArr.length; i++) {
					String col = vcolumnArr[i];

					Method getMe = (Method) map.get(col + "_getMethod");
					String returnType = (String) map.get(col + "_returnType");

					if (getMe == null || returnType == null)
						continue;

					Object retObj = getMe.invoke(obj);

					if (returnType.equals("java.lang.String")) {
						if (retObj == null)
							st.setString(i + 1, null);
						else
							st.setString(i + 1, retObj.toString());
					} else if (returnType.equals("java.lang.Long")) {
						if (retObj == null)
							st.setLong(i + 1, 0L);
						else
							st.setLong(i + 1, Long.parseLong(retObj.toString()));
					} else if (returnType.equals("java.lang.Integer")) {
						if (retObj == null)
							st.setInt(i + 1, 0);
						else
							st.setInt(i + 1,
									Integer.parseInt(retObj.toString()));
					} else {
						throw new IllegalAccessException(returnType); // TODO
					}
				}

				st.addBatch();

				if (++size == jdbcBatchSize) {
					st.executeBatch();
					size = 0;
				}

			}

			if (size != 0) {
				st.executeBatch();
			}

			con.commit();
			logger.info("SQL语句“" + vsql + "”。删除“" + keyName + "”成功！共删除："
					+ list.size() + "条！");
		} catch (Exception e) {
			if (con != null)
				con.rollback();
			throw e;
		} finally {
			close(con, st, null);
		}
	}

	@SuppressWarnings({"rawtypes"})
	public static void Batchsave(List list, String keyName) throws Exception {
		if (list == null || list.size() == 0)
			return;

		String prefix = "jdbc.set";

		String vsql = getProperty(prefix + "." + keyName + ".sql");
		String vcolumn = getProperty(prefix + "." + keyName + ".column");
		String vclass = getProperty(prefix + "." + keyName + ".class");
		String tableName = getProperty(prefix + "." + keyName + ".table");

		if (vsql == null || vclass == null || vcolumn == null)
			throw new IllegalArgumentException("请检查“" + prefix + "." + keyName
					+ "”的相关配置！");
		logger.info("sql:"+vsql);
		Map map = handlerClass(Class.forName(vclass));
		String[] vcolumnArr = vcolumn.split(",");

		Connection con = null;
		PreparedStatement st = null;

		try {
			con = getConnection();
			logger.info(" --------- con:  -----------"+con);
			con.setAutoCommit(false);
			logger.info(" ---getConnection---");
			st = con.prepareStatement(vsql);
			int size = 0;
			for (Object obj : list) {
				for (int i = 0; i < vcolumnArr.length; i++) {
				    boolean isKey = false ;
					String col = vcolumnArr[i];
					 //主键特殊处理
				    if(col.indexOf("@")==0){
					    isKey = true ;
					    col = col.substring(1);
					}
					Method getMe = (Method) map.get(col + "_getMethod");
					String returnType = (String) map.get(col + "_returnType");

					if (getMe == null || returnType == null)
						continue;

					Object retObj = getMe.invoke(obj);
					if(isKey){
					    retObj = handlerGenerator(tableName).generate(con);
					}else{
					    retObj = getMe.invoke(obj) ;
					}

					if (returnType.equals("java.lang.String")) {
						if (retObj == null)
							st.setString(i + 1, null);
						else
							st.setString(i + 1, retObj.toString());
					} else if (returnType.equals("java.lang.Long")) {
						if (retObj == null)
							st.setLong(i + 1, 0L);
						else
							st.setLong(i + 1, Long.parseLong(retObj.toString()));
					} else if (returnType.equals("java.lang.Integer")) {
						if (retObj == null)
							st.setInt(i + 1, 0);
						else
							st.setInt(i + 1,
									Integer.parseInt(retObj.toString()));
					} else {
						throw new IllegalAccessException(returnType); // TODO
					}
				}

				st.addBatch();

				if (++size == jdbcBatchSize) {
					st.executeBatch();
					size = 0;
				}

			}

			if (size != 0) {
				st.executeBatch();
			}

			con.commit();
			logger.info("SQL语句“" + vsql + "”。保存“" + keyName + "”成功！共保存："
					+ list.size() + "条！");
		} catch (Exception e) {
			if (con != null)
				con.rollback();
			throw e;
		} finally {
			close(con, st, null);
		}
	}

	/*
	 * 单条记录保存,并返回@id
	 */
	@SuppressWarnings({"rawtypes"})
	public static Long save(Object obj, String keyName) throws Exception {
		Long id = 0L;
		if (obj == null)
			return id;
		String prefix = "jdbc.set";

		String vsql = getProperty(prefix + "." + keyName + ".sql");
		String vcolumn = getProperty(prefix + "." + keyName + ".column");
		String vclass = getProperty(prefix + "." + keyName + ".class");
		String tableName = getProperty(prefix + "." + keyName + ".table");

		if (vsql == null || vclass == null || vcolumn == null)
			throw new IllegalArgumentException("please check“" + prefix + "."
					+ keyName + "” config！");
		logger.info("sql:"+vsql);
		Map map = handlerClass(Class.forName(vclass));
		String[] vcolumnArr = vcolumn.split(",");

		Connection con = null;
		PreparedStatement ps = null;

		try {
			logger.info(" ---getConnection---");
			con = getConnection();
			logger.info(" --------- con:  -----------"+con);
			con.setAutoCommit(false);
			ps = con.prepareStatement(vsql);
			for (int i = 0; i < vcolumnArr.length; i++) {
				boolean isKey = false;
				String col = vcolumnArr[i];
				// 主键特殊处理
				if (col.indexOf("@") == 0) {
					isKey = true;
					col = col.substring(1);
				}
				Method getMe = (Method) map.get(col + "_getMethod");
				String returnType = (String) map.get(col + "_returnType");

				if (getMe == null || returnType == null)
					continue;

				Object retObj = getMe.invoke(obj);
				logger.info(col+":"+retObj);
				if (isKey) {
					retObj = handlerGenerator(tableName).generate(con);
					//id = Integer.parseInt(retObj.toString());
					  id=Long.parseLong(retObj.toString());
				} else {
					retObj = getMe.invoke(obj);
				}

				if (returnType.equals("java.lang.String")) {
					if (retObj == null)
						ps.setString(i + 1, null);
					else
						ps.setString(i + 1, retObj.toString());
				} else if (returnType.equals("java.lang.Long")) {
					if (retObj == null)
						ps.setLong(i + 1, 0L);
					else
						ps.setLong(i + 1, Long.parseLong(retObj.toString()));
				} else if (returnType.equals("java.lang.Integer")) {
					if (retObj == null)
						//ps.setInt(i + 1, null);
					    ps.setNull(i + 1, Types.NULL);
					else
						ps.setInt(i + 1, Integer.parseInt(retObj.toString()));
				} else if(returnType.equals("java.util.Date")) {
					if (retObj == null){
						ps.setTimestamp(i+1, null);
					}else{
						Date d=(Date)retObj;
						ps.setTimestamp(i+1,new Timestamp(d.getTime()));
					}
				} else if(returnType.equals("java.lang.Short")){
					if (retObj == null)
						ps.setInt(i + 1, 0);
					else
						ps.setInt(i + 1, Short.parseShort(retObj.toString()));
				}else{
					throw new IllegalAccessException(returnType); // TODO
				}
				
			}
			ps.executeUpdate();
			con.commit();
			logger.debug("SQL [" + vsql + "] save to [" + keyName
					+ "] success！");
		} catch (Exception e) {
			if (con != null)
				con.rollback();
			throw e;
		} finally {
			close(con, ps, null);
		}
		return id;
	}
	
	/*
	 * 单条记录保存,并返回@id
	 */
	@SuppressWarnings({"rawtypes"})
	public static Long save(Object obj, String keyName,String sqlAppend) throws Exception {
		Long id = 0L;
		if (obj == null)
			return id;
		String prefix = "jdbc.set";

		String vsql = getProperty(prefix + "." + keyName + ".sql");
		String vcolumn = getProperty(prefix + "." + keyName + ".column");
		String vclass = getProperty(prefix + "." + keyName + ".class");
		String tableName = getProperty(prefix + "." + keyName + ".table");

		if (vsql == null || vclass == null || vcolumn == null)
			throw new IllegalArgumentException("please check“" + prefix + "."
					+ keyName + "” config！");
		if (sqlAppend != null && !sqlAppend.trim().equals("")){
//			if(vsql.toLowerCase().contains(" where ")){
//				vsql += sqlAppend.toLowerCase().replace(" where ", "  and ");
//			}else if(sqlAppend.toLowerCase().contains(" where ")){
//				vsql += sqlAppend.replace(" and ", "  ");
//			}else{
//				sqlAppend = " where " + sqlAppend;
//				
//			}
			vsql += sqlAppend;
		}
		logger.info("sql:"+vsql);
		Map map = handlerClass(Class.forName(vclass));
		String[] vcolumnArr = vcolumn.split(",");

		Connection con = null;
		PreparedStatement ps = null;

		try {
			logger.info(" --------- getConnection:  -----------");
			con = getConnection();
			logger.info(" --------- con:  -----------"+con);
			con.setAutoCommit(false);
			ps = con.prepareStatement(vsql);
			for (int i = 0; i < vcolumnArr.length; i++) {
				boolean isKey = false;
				String col = vcolumnArr[i];
				// 主键特殊处理
				if (col.indexOf("@") == 0) {
					isKey = true;
					col = col.substring(1);
				}
				Method getMe = (Method) map.get(col + "_getMethod");
				String returnType = (String) map.get(col + "_returnType");

				if (getMe == null || returnType == null)
					continue;

				Object retObj = getMe.invoke(obj);
				logger.info(col+":"+retObj);
				if (isKey) {
					retObj = handlerGenerator(tableName).generate(con);
					//id = Integer.parseInt(retObj.toString());
					  id=Long.parseLong(retObj.toString());
				} else {
					retObj = getMe.invoke(obj);
				}

				if (returnType.equals("java.lang.String")) {
					if (retObj == null)
						ps.setString(i + 1, null);
					else
						ps.setString(i + 1, retObj.toString());
				} else if (returnType.equals("java.lang.Long")) {
					if (retObj == null)
						ps.setLong(i + 1, 0L);
					else
						ps.setLong(i + 1, Long.parseLong(retObj.toString()));
				} else if (returnType.equals("java.lang.Integer")) {
					if (retObj == null)
						ps.setInt(i + 1, 0);
					else
						ps.setInt(i + 1, Integer.parseInt(retObj.toString()));
				} else if(returnType.equals("java.util.Date")) {
					if (retObj == null){
						ps.setTimestamp(i+1, null);
					}else{
						Date d=(Date)retObj;
						ps.setTimestamp(i+1,new Timestamp(d.getTime()));
					}
				} else if(returnType.equals("java.lang.Short")){
					if (retObj == null)
						ps.setInt(i + 1, 0);
					else
						ps.setInt(i + 1, Short.parseShort(retObj.toString()));
				}else{
					throw new IllegalAccessException(returnType); // TODO
				}
				
			}
			ps.executeUpdate();
			con.commit();
			logger.debug("SQL [" + vsql + "] save to [" + keyName
					+ "] success！");
		} catch (Exception e) {
			if (con != null)
				con.rollback();
			throw e;
		} finally {
			close(con, ps, null);
		}
		return id;
	}

	/*
	 * 
	 */
	@SuppressWarnings({"unchecked"})
	private synchronized static NativeIdGenerator handlerGenerator(
			String tableName) {
		if (generatorMap.containsKey(tableName)) {
			return (NativeIdGenerator) generatorMap.get(tableName);
		} else {
			NativeIdGenerator ng = new NativeIdGenerator(tableName);
			generatorMap.put(tableName, ng);
			return ng;
		}
	}

	@SuppressWarnings({"rawtypes"})
	public static List getResult(String keyName) throws Exception {
		return getResult(keyName, null);
	}

	@SuppressWarnings({"rawtypes"})
	public static List getResult(String keyName, String sqlAppend)
			throws Exception {
		String prefix = "jdbc.get";

		String vsql = getProperty(prefix + "." + keyName + ".sql");
		String vclass = getProperty(prefix + "." + keyName + ".class");

		if (vsql == null || vclass == null)
			throw new IllegalArgumentException("请检查“" + prefix + "." + keyName
					+ "”的相关配置！");
		
		if (sqlAppend != null && !sqlAppend.trim().equals(""))
			vsql += sqlAppend;
		logger.info("sql:"+vsql);
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;

		try {
			logger.info(" --------- getConnection:  -----------");
			con = getConnection();
			logger.info("  --------- con:  -----------"+con);
			st = con.createStatement();
			rs = st.executeQuery(vsql);
			List retList = handlerResult(rs, Class.forName(vclass));
			logger.info("SQL语句“" + vsql + "”。查询“" + keyName + "”成功！共查询出："
					+ retList.size() + "条！");
			return retList;
		} finally {
			close(con, st, rs);
		}
	}
}
