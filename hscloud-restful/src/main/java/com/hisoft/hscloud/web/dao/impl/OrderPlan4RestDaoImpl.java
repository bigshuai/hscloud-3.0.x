/* 
* 文 件 名:  OrderPlan4RestDao.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-5-29 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.dao.impl; 

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.hisoft.hscloud.web.dao.OrderPlan4RestDao;
import com.hisoft.hscloud.web.util.DBConstants;
import com.hisoft.hscloud.web.util.DBUtil;



/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-5-29] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Repository
public class OrderPlan4RestDaoImpl implements OrderPlan4RestDao {

    
    @Resource
    private DataSource dataSource;
    private Connection conn;
    private Logger logger = Logger.getLogger(this.getClass());

    @Override
    public String getFactorySeq(String accessid) {
        // 从数据库中查找
        logger.info("getting factory_sequence from db is starting......");
        PreparedStatement pst = null;
        ResultSet rs = null;
        String factory_seq=null;
        try {
            conn = dataSource.getConnection();
            String sql=DBUtil.getSql(DBConstants.QUERY_FACTORY_SENQUENCE);
            pst = conn.prepareStatement(sql);
            pst.setObject(1, accessid);
            rs = pst.executeQuery();
            while (rs.next()) {
                factory_seq=rs.getString("factory_senquence");
                break;
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
        return factory_seq;
    }
    @Override
    public String getUserId(String accessid) {
        // 从数据库中查找
                logger.info("getting user_id from db is starting......");
                PreparedStatement pst = null;
                ResultSet rs = null;
                String user_id=null;
                try {
                    conn = dataSource.getConnection();
                    String sql=DBUtil.getSql(DBConstants.QUERY_USER_ID);
                    pst = conn.prepareStatement(sql);
                    pst.setObject(1, accessid);
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        user_id=rs.getString("userid");
                        break;
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
                return user_id;
    }
    
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
}
