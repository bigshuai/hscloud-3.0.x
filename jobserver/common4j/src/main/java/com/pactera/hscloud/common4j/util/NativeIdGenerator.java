package com.pactera.hscloud.common4j.util;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * cacheSize 每次做为缓冲ID的数量。当导入大数据量时，值越大ID消耗性能越小。默认为20。 tableName
 * hc_id_generator表中TABLE_NAME列的值，默认为 实体数据库表名加“_ID” 。
 * 
 * @author
 * 
 */
public class NativeIdGenerator {

	private String tableName;

	private int cacheSize = 100;

	private int remainder = 0;

	private Long base = null;

	public synchronized Serializable generate(Connection con)
			throws SQLException {
		if (remainder == 0) {// 库存用完
			Statement stmt = null;
			ResultSet rs = null;
			try {
				stmt = con.createStatement();

				rs = stmt
						.executeQuery("SELECT current_max_id FROM hc_id_generator WHERE table_name = '"
								+ tableName + "' ");

				long cur = 0L;

				try {
					rs.next();
					cur = rs.getLong(1);
				} catch (SQLException e) {
					// 出现异常说明表中无此记录。开启新事务创建记录。
					throw new SQLException("“hc_id_generator”表中无“" + tableName
							+ "”记录，请先手动添加。");
				}

				stmt.executeUpdate("UPDATE hc_id_generator SET current_max_id = "
						+ (cur + cacheSize)
						+ " WHERE table_name = '"
						+ tableName + "' ");

				base = cur + 1;
			} finally {
				DBUtil.close(null, stmt, rs);
			}

			remainder = cacheSize;
		}

		Long retValue = base + (cacheSize - remainder--);

		return retValue;
	}

	public NativeIdGenerator(String tableName) {
		this.tableName = tableName.toUpperCase() + "_ID";
	}
}