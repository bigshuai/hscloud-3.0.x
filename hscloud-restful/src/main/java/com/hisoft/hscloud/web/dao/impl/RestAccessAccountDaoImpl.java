/**
 * -----------------------------------------------------------------
 * Copyright (c) 2012 hisoft, All Rights Reserved.
 * This software is the proprietary information of hisoft.
 * Use is subject to strict licensing terms.
 * -----------------------------------------------------------------
 *
 * @author: hisoft 2013-5-10   下午2:38:27
 * @brief: 类功能描述
 * @log: hisoft
 */


package com.hisoft.hscloud.web.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import com.hisoft.hscloud.web.dao.RestAccessAccountDao;
import com.hisoft.hscloud.web.util.DBConstants;
import com.hisoft.hscloud.web.util.DBUtil;
import com.hisoft.hscloud.web.util.MD5Util;


@Repository
public class RestAccessAccountDaoImpl implements RestAccessAccountDao {
    
    
	/**
	 * @author: Administrator
	 * @brief: 类功能描述
	 * @log: 2013-4-18 下午3:56:07
	 **/
	@Resource
	private DataSource dataSource;

	/**
	 * @author: hisoft
	 * @brief: query access key
	 * @log: 2013-5-10 下午2:38:27
	 **/
	public String getAccessKey(String facSeq) {
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			con = dataSource.getConnection();
			pst = con.prepareStatement(DBUtil.getSql(DBConstants.QUERY_ACCESS_KEY));
			pst.setString(1, facSeq);
			rs = pst.executeQuery();
			if (rs.next()) {
				/*String str = "%s%s%s";
				String accessid = rs.getString("accessid");
				String email = rs.getString("emailaddr");*/
			    String accessKey = rs.getString("accesskey");
				accessKey = MD5Util.encode(accessKey);
				return accessKey;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}
}
