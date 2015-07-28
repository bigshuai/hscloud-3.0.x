/**
 * -----------------------------------------------------------------
 * Copyright (c) 2012 hisoft, All Rights Reserved.
 * This software is the proprietary information of hisoft.
 * Use is subject to strict licensing terms.
 * -----------------------------------------------------------------
 *
 * @author: Administrator 2013-4-18   下午3:56:07
 * @brief: 类功能描述
 * @log: Administrator
 */

package com.hisoft.hscloud.web.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.hisoft.hscloud.web.dao.Plans4RestDao;
import com.hisoft.hscloud.web.util.DBConstants;
import com.hisoft.hscloud.web.util.DBUtil;
import com.hisoft.hscloud.web.vo.PlansInfoVo;

@Repository
public class Plans4RestDaoImpl implements Plans4RestDao {
	/**
	 * @author: Administrator
	 * @brief: 类功能描述
	 * @log: 2013-4-18 下午3:56:07
	 **/
    @Resource
	private DataSource dataSource;
	private Connection conn;
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Override
	public List<PlansInfoVo> getPlans4RestByUser(String user_id, String vmId) {
		// 从数据库中查找
		logger.info("getting orderrelation list from db is starting......");
		PreparedStatement pst = null;
		ResultSet rs = null;
		PlansInfoVo pv = null;
		List<PlansInfoVo> oList = new ArrayList<PlansInfoVo>();
		boolean andFlag = true;
		
		try {
			conn = dataSource.getConnection();
			StringBuffer sb = new StringBuffer(DBUtil.getSql(DBConstants.GETALL_PLANS4INFO_SQL));
			sb.append(" and 1=1 ");
			if (vmId != null && !"".equals(vmId)) {
				if (andFlag) {
					sb.append(" ").append("AND").append(" ");
				}
				sb.append(" t.id=").append(vmId).append(" ");
				andFlag = true;
			}
			pst = conn.prepareStatement(sb.toString());
			pst.setString(1, user_id);
			rs = pst.executeQuery();
			while (rs.next()) {
				pv = new PlansInfoVo();
				pv.setId(rs.getInt("id"));
				pv.setScname(rs.getString("scname"));
				pv.setFeeType(rs.getString("price"));
				pv.setEffectiveon(rs.getString("effectiveon"));
				pv.setExpiredon(rs.getString("expiredon"));
				pv.setDescription(rs.getString("description"));
				pv.setOs_info(rs.getString("os_info"));
				pv.setDatacenter(rs.getString("zone_group"));
				oList.add(pv);
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				rs.close();
				pst.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		logger.info("getting orderrelation list from db is ending......");
		return oList;
	}
}
