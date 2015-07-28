/* 
 * 文 件 名:  BasicDaoImpl.java 
 * 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
 * 描    述:  <描述> 
 * 修 改 人:  lihonglei 
 * 修改时间:  2013-6-25 
 * 跟踪单号:  <跟踪单号> 
 * 修改单号:  <修改单号> 
 * 修改内容:  <修改内容> 
 */
package com.hisoft.hscloud.web.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.hisoft.hscloud.crm.usermanager.entity.PlatformRelation;
import com.hisoft.hscloud.web.dao.BasicDao;
import com.hisoft.hscloud.web.util.DBConstants;
import com.hisoft.hscloud.web.util.DBUtil;

/**
 * <基础查询> <功能详细描述>
 * 
 * @author lihonglei
 * @version [版本号, 2013-6-25]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Repository
public class BasicDaoImpl implements BasicDao {

    private Logger logger = Logger.getLogger(this.getClass());

    @Resource
    private DataSource dataSource;

    private Connection conn;

    @Override
    public Map<String, Object> queryAccessByAccessId(String accessId) {
        // 从数据库中查找
        logger.info("getting user_id from db is starting......");
        PreparedStatement pst = null;
        ResultSet rs = null;

        Map<String, Object> result = new HashMap<String, Object>();
        try {
            conn = dataSource.getConnection();
            String sql = DBUtil.getSql(DBConstants.QUERY_ACCESS);
            pst = conn.prepareStatement(sql);
            pst.setObject(1, accessId);
            rs = pst.executeQuery();

            while (rs.next()) {
                result.put("id", rs.getString("id"));
                result.put("accessId", rs.getString("accessId"));
                result.put("userId", rs.getString("userId"));
                result.put("emailaddr", rs.getString("emailaddr"));
                result.put("ip", rs.getString("ip"));
                result.put("accesskey", rs.getString("accesskey"));
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
        logger.info("getting factory_sequence from db is ending......");
        return result;
    }

    @Override
    public String queryReferenceIdByVmId(String vmId, String userId) {
        // 从数据库中查找
        logger.info("getting user_id from db is starting......");
        PreparedStatement pst = null;
        ResultSet rs = null;

        String result = null;
        try {
            conn = dataSource.getConnection();
            String sql = DBUtil.getSql(DBConstants.QUERY_REFERENCEID_BY_VMID);
            pst = conn.prepareStatement(sql);
            pst.setObject(1, vmId);
            pst.setObject(2, userId);
            rs = pst.executeQuery();

            if (rs.next()) {
                result = rs.getString("VpdcRefrenceId");
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
        logger.info("getting factory_sequence from db is ending......");
        return result;
    }

    /**
     * 查询ip
     * @param accessid
     * @return
     */
    @Override
    public List<String> getIpByAccesskey(String accessid) {
        logger.info("getting ip list from db is starting......");
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<String> ipList = new ArrayList<String>();
        try {
            conn = dataSource.getConnection();
            String sql = DBUtil.getSql(DBConstants.GET_ALL_IP_BY_ACCESSID);
            pst = conn.prepareStatement(sql);
            pst.setObject(1, accessid);
            rs = pst.executeQuery();
            while (rs.next()) {
                String ip = rs.getString("ip");
                ipList.add(ip);
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
        logger.info("getting ip list from db is ending......");
        return ipList;
    }

    @Override
    public long saveTask(Map<String, Object> taskMap) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        long result = 0l;
        try {
            conn = dataSource.getConnection();
            String sql = DBUtil.getSql(DBConstants.SAVE_TASK);
            pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            // pst.setObject(1, accessid);
            pst.setObject(1, taskMap.get("create_id"));
            pst.setObject(2, taskMap.get("status"));
            pst.setObject(3, taskMap.get("machine_no"));
            pst.setObject(4, taskMap.get("operating_type"));
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            if(rs.next()) {
                result = rs.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pst.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

	@Override
	public Map<String, Object> queryAccessByAccessId(String accessId,
			String emailaddr) {
		 // 从数据库中查找
        logger.info("getting user_id from db is starting......");
        PreparedStatement pst = null;
        ResultSet rs = null;

        Map<String, Object> result = new HashMap<String, Object>();
        try {
            conn = dataSource.getConnection();
            String sql = DBUtil.getSql(DBConstants.QUERY_ACCESS_KEY);
            pst = conn.prepareStatement(sql);
            pst.setObject(1, accessId);
           // pst.setObject(2, emailaddr);
            rs = pst.executeQuery();

            while (rs.next()) {
                result.put("id", rs.getString("id"));
                result.put("accessId", rs.getString("accessId"));
                result.put("userId", rs.getString("userId"));
                result.put("emailaddr", rs.getString("emailaddr"));
                result.put("ip", rs.getString("ip"));
                result.put("accesskey", rs.getString("accesskey"));
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
        logger.info("getting factory_sequence from db is ending......");
        return result;
	}

	@Override
	public PlatformRelation getPlatformRelationByExteranalUserId(
			String externalUserId) {
		// 从数据库中查找
        logger.info("getting user_id from db is starting......");
        PreparedStatement pst = null;
        ResultSet rs = null;

        PlatformRelation result = null;
        try {
            conn = dataSource.getConnection();
            String sql = DBUtil.getSql(DBConstants.QUERY_PLATFORM_RELATION_BY_EUSERID);
            pst = conn.prepareStatement(sql);
            pst.setObject(1, externalUserId);
            rs = pst.executeQuery();

            if (rs.next()) {
            	result = new PlatformRelation();
            	//result.setUserId(rs.getString(0));
                result.setExternalUserId(rs.getString(1));
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
        logger.info("getting factory_sequence from db is ending......");
        return result;
	}

	@Override
	public boolean addPlatformRelation(String externalUserId) {
		boolean resultFlag = false;
		PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            String sql = DBUtil.getSql(DBConstants.SAVE_PLATFORM_RELATION);
            pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            // pst.setObject(1, accessid);
            pst.setObject(1, null);
            pst.setObject(2, externalUserId);
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            if(rs.next()) {
            	resultFlag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pst.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return resultFlag;
	}
}
