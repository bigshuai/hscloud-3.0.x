/* 
* 文 件 名:  ResetCpPwdDaoImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-8-21 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.dao.impl; 

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.hisoft.hscloud.web.dao.ResetCpPwdDao;
import com.hisoft.hscloud.web.util.DBConstants;
import com.hisoft.hscloud.web.util.DBUtil;

/** 
 * <重置控制面板密码> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-8-21] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Repository
public class ResetCpPwdDaoImpl implements ResetCpPwdDao {
    
    private Logger logger = Logger.getLogger(this.getClass());
    
    @Resource
    private DataSource dataSource;

    private Connection conn;
    
    @Override
    public String queryCreateflagByVmId(String vmId) {
        // 从数据库中查找
        logger.info("getting user_id from db is starting......");
        PreparedStatement pst = null;
        ResultSet rs = null;

        String result = null;
        try {
            conn = dataSource.getConnection();
            String sql = DBUtil.getSql(DBConstants.QUERY_CREATEFLAG_BY_VMID);
            pst = conn.prepareStatement(sql);
            pst.setObject(1, vmId);
            rs = pst.executeQuery();
            
            if (rs.next()) {
                result = rs.getString("createflag");
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
